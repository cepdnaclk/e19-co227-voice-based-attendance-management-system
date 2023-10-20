from threading import Thread
from queue import Queue
import pyaudio
from vosk import Model, KaldiRecognizer
import json
import wave
import os


CHANNELS = 1 # Audio channel number. Mono recording is used
FRAME_RATE = 44100 # Number of frames per secong. Change this to 16000 if something goes wrong
RECORD_SECONDS = 5  # Duration of the recording
AUDIO_FORMAT = pyaudio.paInt16
SAMPLE_SIZE = 2 # 2byte samples ( 16 bit )

messages = Queue()

model = Model(model_name="vosk-model-small-en-in-0.4") # Vosk indian english model is used here
recognizer = KaldiRecognizer(model, FRAME_RATE) # Setup the recognizer
recognizer.SetWords(True)

# In case microphone configuration is needed
#p = pyaudio.PyAudio()

# for i in range(p.get_device_count()):
    #print(p.get_device_info_by_index(i)) # Device info

#p.terminate()

def recordAndTranscribe(chunk=1024):
    frames = [] # To hold frames

    #print("Reading audio from test.wav...")
    fileLocation = "\\src\\main\\res\\temp\\test.wav"
    filePath = os.getcwd() + fileLocation 
    
    
    try:
        # Open the test.wav file
        wf = wave.open(filePath, 'rb')

        # Read frames from the WAV file
        data = wf.readframes(chunk)
        while data:
            frames.append(data)
            data = wf.readframes(chunk)
    
    finally:
    # Close the file 
        if 'wf' in locals() and wf:
            wf.close()
    

    #print("Audio read complete.")
    wf.close() 

    # Perform speech recognition
    speechRecognition(frames)
      
def speechRecognition(frames):
    recognizer.AcceptWaveform(b''.join(frames)) # Takes the audio signal. Uses VOSK API
    result = recognizer.Result() # Take the result of the recognisor
    resultText = json.loads(result)["text"] # Take the text part of the result
    #print("Raw Text:", resultText)

    # Try to convert text result to numbers
    try:
        number = int(convertToNumbers(resultText) )
        print(number)
    except ValueError:
        print("-1") # If the recording is not convertable
        return 1

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

def start():
    messages.put(True) # Start a new thread
    #print("Starting recording...")
    recAndTranscribe = Thread(target=recordAndTranscribe) # Create a new thread which runs recordAndTranscribe function
    recAndTranscribe.start() # Start the thread

def stop():
    #print("Stopping")
    messages.get() # Dequeue the message

start()

