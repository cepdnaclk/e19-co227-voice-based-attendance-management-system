from pydub import AudioSegment

# Load wav audio files
reference_sample_path = "ref.wav"
test_sample_path = "test.wav"

try:
    reference_sample = AudioSegment.from_file(reference_sample_path, format="wav")
    test_sample = AudioSegment.from_file(test_sample_path, format="wav")
except Exception as e:
    print(f"Error loading audio files: {e}")
    exit(1)

# Normalize volume (optional)
reference_sample = reference_sample.normalize()
test_sample = test_sample.normalize()


# Save the preprocessed samples (optional)
preprocessed_reference_path = "preprocessed_reference.wav"
preprocessed_test_path = "preprocessed_test.wav"

reference_sample.export(preprocessed_reference_path, format="wav")
test_sample.export(preprocessed_test_path, format="wav")

print("wav voice samples loaded and preprocessed successfully.")

import librosa
import librosa.display
import matplotlib.pyplot as plt
import numpy as np

# Load the audio file
audio_file = preprocessed_reference_path  # Replace with the path to your audio file

# Load the audio file using librosa
audio, sample_rate = librosa.load(audio_file, sr=None)

# Extract MFCCs
mfccs = librosa.feature.mfcc(y=audio, sr=sample_rate, n_mfcc=13)

# Extract pitch and fundamental frequency (F0)
f0, voiced_flag, voiced_probs = librosa.pyin(audio, fmin=librosa.note_to_hz('C2'), fmax=librosa.note_to_hz('C7'))

# Compute the spectrogram
spectrogram = librosa.feature.melspectrogram(y=audio, sr=sample_rate)

# Display the MFCCs
plt.figure(figsize=(10, 4))
librosa.display.specshow(mfccs, x_axis='time')
plt.colorbar()
plt.title('MFCC')
plt.tight_layout()

# Display the pitch/F0
plt.figure(figsize=(10, 4))
plt.subplot(2, 1, 1)
plt.semilogy(f0, label='F0 (fundamental frequency)')
plt.ylabel('Hz')
plt.xticks([])
plt.xlim([0, len(f0)])
plt.legend()

# Display the waveform
plt.subplot(2, 1, 2)
plt.plot(np.arange(len(audio)) / sample_rate, audio, alpha=0.5)
plt.xlabel('Time (s)')
plt.ylabel('Amplitude')
plt.title('Waveform')
plt.tight_layout()

# Display the spectrogram
plt.figure(figsize=(10, 4))
librosa.display.specshow(librosa.power_to_db(spectrogram, ref=np.max), y_axis='mel', x_axis='time')
plt.colorbar(format='%+2.0f dB')
plt.title('Mel Spectrogram')
plt.tight_layout()

plt.show()
