from threading import Thread
from queue import Queue
import pyaudio
from vosk import Model, KaldiRecognizer
import json
import wave
import time
import os
import librosa
from scipy.spatial.distance import euclidean
from fastdtw import fastdtw

CHANNELS = 1 # Audio channel number. Mono recording is used
FRAME_RATE = 44100 # Number of frames per secong. Change this to 16000 if something goes wrong
RECORD_SECONDS = 5  # Duration of the recording
AUDIO_FORMAT = pyaudio.paInt16
SAMPLE_SIZE = 2 # 2byte samples ( 16 bit )

messages = Queue()
recordings = Queue()

# If test.wav exists already, then delete it
if os.path.exists("test.wav"):
    os.remove("test.wav")
    print("Deleted existing test.wav")

model = Model(model_name="vosk-model-small-en-in-0.4") # Vosk indian english model is used here
recognizer = KaldiRecognizer(model, FRAME_RATE) # Setup the recognizer
recognizer.SetWords(True)

#p = pyaudio.PyAudio()

# for i in range(p.get_device_count()):
    #print(p.get_device_info_by_index(i)) # Device info

#p.terminate()

def recordAndTranscribe(chunk=1024):
    p = pyaudio.PyAudio()

    stream = p.open(format=AUDIO_FORMAT,
                    channels=CHANNELS,
                    rate=FRAME_RATE, # Sample rate ( Number of samples / second)
                    input=True, # Stream is used for inputting
                    input_device_index=1, # Change the input device index if needed
                    frames_per_buffer=chunk) 

    frames = []

    print("Recording...")
    
    for _ in range(int(FRAME_RATE / chunk * RECORD_SECONDS)): # Determines how many times this has to loop 
        data = stream.read(chunk) # Read data
        frames.append(data) # Append it to frames array

    print("Recording complete.")
    stream.stop_stream() # Stop audio stream
    stream.close() # Close audio stream
    p.terminate() # Terminate PyAydio

    # Save the recorded frames as a WAV file
    saveWAV(frames)

    # Perform speech recognition
    speechRecognition(frames)
      
def speechRecognition(frames):
    recognizer.AcceptWaveform(b''.join(frames)) # Takes the audio signal. Uses VOSK API
    result = recognizer.Result() # Take the result of the recognisor
    resultText = json.loads(result)["text"] # Take the text part of the result
    print("Raw Text:", resultText)

    # Try to convert text result to numbers
    try:
        number = convertToNumbers(resultText) 
        print("Converted Number:", number)
    except ValueError:
        print("Unable to convert to a number.")

def convertToNumbers(text):
    # Possible words to numbers
    wordsToNum = {
        'one': '1',
        'on': '1',
        'oh': '1',
        'than': '1',
        'done': '1',
        'run': '1',
        'stun': '1',
        'bun': '1',
        'nun': '1',
        'fun': '1',
        'gun': '1',
        'sun': '1',
        
        'two': '2',
        'to': '2',
        'you': '2',
        'who': '2',
        'new': '2',
        'do': '2',
        'due': '2',
        'true': '2',
        'u': '2',
        'too': '2',
        
        'three': '3',
        'tree': '3',
        'he': '3',
        'we': '3',
        'be': '3',
        'she': '3',
        'see': '3',
        'the': '3',
        'free': '3',
        'sea': '3',
        'key': '3',
        
        'four': '4',
        'for': '4',
        'more': '4',
        'war': '4',
        'or': '4',
        'door': '4',
        'floor': '4',
        'pour': '4',
        
        'five': '5',
        'live': '5',
        'arrive': '5',
        'thrive': '5',
        'hive': '5',
        'rive': '5',
        
        'six': '6',
        'fix': '6',
        'seeks': '6',
        'tricks': '6',
        'bricks': '6',
        'chicks': '6',

        'seven': '7',
        'heaven': '7',
        'eleven': '7',
        'evan': '7',
        'bevan': '7',
        'levan': '7',
        'leavan': '7',

        'eight': '8',
        'stage': '8',
        'great': '8',
        'rate': '8',
        'late': '8',
        'weight': '8',
        'date': '8',
        'plate': '8',
        'wait': '8',
        'ate': '8',
        'gate': '8',
        'freight': '8',
        'bait': '8',
        'trait': '8',
        'states': '8',
        
        'nine': '9',
        'line': '9',
        'fine': '9',
        'mine': '9',
        'wine': '9',
        'pine': '9',
        'spine': '9',
        'spine': '9',
        'dine': '9',
        'dine': '9',
        
        'zero': '0',
        'seero': '0',
        'hero': '0',
        'sera': '0',
        'nero': '0',
        'c o': '0',
        'si no': '0'
        
    }

    # Splitting the text
    tokens = text.split()
    
    # Converting word to a number
    numbers = [wordsToNum.get(token.lower(), token) for token in tokens] # Takes each token from tokens, check in the dictionary for matching numbers. If exists the number is retruned. If not the original token is returned.
    
    # Convert the numbers to a string
    resultStr = ''.join(numbers)

    # Return the result 
    return resultStr

def saveWAV(frames):
    # Create a new wave file
    wf = wave.open("test.wav", 'wb') # Create a new WAV file
    wf.setnchannels(CHANNELS) # Set the number of channels
    wf.setsampwidth(pyaudio.PyAudio().get_sample_size(AUDIO_FORMAT)) # Set the sample width using get_sample_size method providing the audio format
    wf.setframerate(FRAME_RATE) # Set the frame rate
    wf.writeframes(b''.join(frames)) # Write to WAV file
    wf.close() # Close the WAV file

def start():
    messages.put(True) # Start a new thread
    print("Starting recording...")
    recAndTranscribe = Thread(target=recordAndTranscribe) # Create a new thread which runs recordAndTranscribe function
    recAndTranscribe.start() # Start the thread

def stop():
    print("Stopping")
    messages.get() # Dequeue the message

start()

def compareAudioSamples():
    # Load the reference voice sample
    referenceFile = "ref.wav"
    
    #Load file and get reference audio and sample rate
    refAudio, refSampleRate = librosa.load(referenceFile, sr=None) # sr = None is used so sampling rate will be determined from the file

    # Load the test voice sample
    testFile = "test.wav"
    
    #Load file and get test audio and sample rate
    testAudio, testSampleRate = librosa.load(testFile, sr=None) # sr = None is used so sampling rate will be determined from the file

    # Mel-Frequency Cepstral Coefficients (MFCCs) are features commonly used in audio processing.
    # Extract MFCCs from the reference voice sample
    refMFCCs = librosa.feature.mfcc(y=refAudio, sr=refSampleRate, n_mfcc=13)

    # Extract MFCCs from the test voice sample
    testMFCCs = librosa.feature.mfcc(y=testAudio, sr=testSampleRate, n_mfcc=13)

    # Dynamic Time Warping (DTW) is a way to compare two sequences that do not sync up perfectly. 
    # It is a method to calculate the optimal matching between two sequences. 
    # Apply DTW to align the MFCC sequences
    distance, path = fastdtw(refMFCCs.T, testMFCCs.T, dist=euclidean)

    # Set a threshold (adjust as needed)
    threshold = 20000  # Change for precission. Higher the number -> Low accuracy, lower the number -> high accuracy

    # Compare the DTW distance to the threshold
    print("DTW Distance:", distance)
    if distance < threshold:
        print("Voice samples match.")
    else:
        print("Voice samples do not match.")
        
time.sleep(6) # Since it takes five seconds to record the audio, program will wait 5+ seconds and compare samples/
compareAudioSamples() # Compare two samples
