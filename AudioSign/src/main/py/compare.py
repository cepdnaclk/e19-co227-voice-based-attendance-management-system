import librosa
from scipy.spatial.distance import euclidean
from fastdtw import fastdtw
import pyaudio
import wave

# Parameters for recording
FORMAT = pyaudio.paInt16
CHANNELS = 1
RATE = 44100
CHUNK = 1024
RECORD_SECONDS = 5
OUTPUT_FILE = "ref.wav"

# Initialize PyAudio
audio = pyaudio.PyAudio()

# Configure and open the microphone stream
stream = audio.open(format=FORMAT, channels=CHANNELS,
                    rate=RATE, input=True,
                    frames_per_buffer=CHUNK)

print("Recording...")

frames = []

# Record audio from the microphone
for _ in range(0, int(RATE / CHUNK * RECORD_SECONDS)):
    data = stream.read(CHUNK)
    frames.append(data)

print("Recording complete.")

# Close the microphone stream
stream.stop_stream()
stream.close()
audio.terminate()

# Save the recorded audio as a WAV file
with wave.open(OUTPUT_FILE, 'wb') as wf:
    wf.setnchannels(CHANNELS)
    wf.setsampwidth(audio.get_sample_size(FORMAT))
    wf.setframerate(RATE)
    wf.writeframes(b''.join(frames))

print(f"Saved recorded audio as '{OUTPUT_FILE}'")


# Load the reference voice sample
reference_file = "ref.wav"
reference_audio, reference_sample_rate = librosa.load(reference_file, sr=None)

# Load the test voice sample
test_file = "test.wav"
test_audio, test_sample_rate = librosa.load(test_file, sr=None)

# Extract MFCCs from the reference voice sample
reference_mfccs = librosa.feature.mfcc(y=reference_audio, sr=reference_sample_rate, n_mfcc=13)

# Extract MFCCs from the test voice sample
test_mfccs = librosa.feature.mfcc(y=test_audio, sr=test_sample_rate, n_mfcc=13)

# Apply DTW to align the MFCC sequences
distance, path = fastdtw(reference_mfccs.T, test_mfccs.T, dist=euclidean)

# Set a threshold (adjust as needed)
threshold = 20000  # You may need to experiment with different threshold values

# Compare the DTW distance to the threshold
print("DTW Distance:", distance)
if distance < threshold:
    print("Voice samples match.")
else:
    print("Voice samples do not match.")


