from scipy.spatial.distance import euclidean
from fastdtw import fastdtw
import pyaudio
import wave
import os


# Recording parameters
FORMAT = pyaudio.paInt16
CHANNELS = 1
RATE = 44100
CHUNK = 1024
RECORD_SECONDS = 5
OUTPUT_FILE_NAME = "\\src\\main\\res\\temp\\ref.wav" # To set the path

OUTPUT_FILE_PATH = os.getcwd() + OUTPUT_FILE_NAME # Absolute path


audio = pyaudio.PyAudio() # Setting up

stream = audio.open(format=FORMAT, channels=CHANNELS,
                    rate=RATE, input=True,
                    frames_per_buffer=CHUNK)

frames = [] # To store audio frames

for _ in range(int(RATE / CHUNK * RECORD_SECONDS)): # Determines how many times this has to loop 
        data = stream.read(CHUNK) # Read data
        frames.append(data) # Append it to frames array


stream.stop_stream() # Stop audio stream
stream.close() # Close audio stream
audio.terminate() # Terminate PyAydio

with wave.open(OUTPUT_FILE_PATH, 'wb') as wf: # Save the output file as WAV file
    wf.setnchannels(CHANNELS)
    wf.setsampwidth(audio.get_sample_size(FORMAT))
    wf.setframerate(RATE)
    wf.writeframes(b''.join(frames))
    wf.close()

#print(f"Saved recorded audio as '{OUTPUT_FILE_PATH}'")
print(1)
