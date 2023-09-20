from threading import Thread
from queue import Queue
import pyaudio
from vosk import Model, KaldiRecognizer
import json

CHANNELS = 1 # Number of chanels
FRAME_RATE = 16000 # Quality of recording
RECORD_SECONDS = 3 # Delay
AUDIO_FORMAT = pyaudio.paInt16
SAMPLE_SIZE = 2


messages = Queue()
recordings = Queue()


model = Model(model_name="vosk-model-small-en-in-0.4") # Model
recognizer = KaldiRecognizer(model, FRAME_RATE) # Initialize recognizer
recognizer.SetWords(True)

p = pyaudio.PyAudio() #Connect pyAudio to system sound devices
for i in range(p.get_device_count()):
    print(p.get_device_info_by_index(i))
    
p.terminate() 


def speechRecognition():
    while not messages.empty(): # To see if we are still recording
        frames = recordings.get() 
        
        recognizer.AcceptWaveform(b''.join(frames)) # Joining frames. 1024 frames at a time. They will be joined together
        result = recognizer.Result()
        resultText = json.loads(result)["text"]
        print(resultText)
        

def recordAudio(chunk = 1024):
    p = pyaudio.PyAudio()
    
    stream = p.open(format=AUDIO_FORMAT, 
                    channels=CHANNELS, 
                    rate=FRAME_RATE, 
                    input= True,
                    input_device_index=1,
                    frames_per_buffer=chunk) # Connect to mic and record. Chunk - How often we read from our mic
    
    frames = []
    
    while not messages.empty(): # We put true in messages. When we call stop function, we take that message, and the recording will stop
        data = stream.read(chunk) # Read from mic
        frames.append(data) # Add to frames
        
        if len(frames) >= (FRAME_RATE* RECORD_SECONDS) / chunk: # If we have more than 20 seconds of recording, then the audio is added to the recordings queue.
            # Basically this will transer recordings every 20 seconds for processing
            recordings.put(frames.copy())
            frames = []
        
    
    stream.start_stream()
    stream.close()
    p.terminate()



def start():
    messages.put(True)
    print("Starting")    
    record = Thread(target = recordAudio)
    record.start()
    
    transcribe = Thread(target=speechRecognition)
    transcribe.start()

def stop():
    print("Stopping")
    messages.get()

start()