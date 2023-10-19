import librosa
from fastdtw import fastdtw
from scipy.spatial.distance import euclidean
import os
import sys

# Get the parameter passed from Java
regNum = sys.argv[1] if len(sys.argv) > 1 else None
#print("Reg Number is: ", regNum)

def compareAudioSamples():
    testFilePartialPath = "\\src\\main\\res\\temp\\test.wav"
    refFilePartialPath = "\\src\\main\\res\\audioSamples\\" + regNum + ".wav"

    currentPath = os.getcwd()

    testFileFullPath = currentPath + testFilePartialPath
    refFileFullPath = currentPath + refFilePartialPath

    #print(testFileFullPath)
    #print(refFileFullPath)

    # Load the reference voice sample
    referenceFile = refFileFullPath

    # Load file and get reference audio and sample rate
    refAudio, refSampleRate = librosa.load(referenceFile, sr=None)

    # Load the test voice sample
    testFile = testFileFullPath

    # Load file and get test audio and sample rate
    testAudio, testSampleRate = librosa.load(testFile, sr=None)

    # Voice Activity Detection (VAD) to identify speech segments
    refSpeechSegment = librosa.effects.split(refAudio, top_db=20)[0]
    testSpeechSegment = librosa.effects.split(testAudio, top_db=20)[0]

    # Extract MFCCs from the speech segments
    refMFCCs = librosa.feature.mfcc(y=refAudio[refSpeechSegment[0]:refSpeechSegment[1]], sr=refSampleRate, n_mfcc=13)
    testMFCCs = librosa.feature.mfcc(y=testAudio[testSpeechSegment[0]:testSpeechSegment[1]], sr=testSampleRate, n_mfcc=13)

    # Dynamic Time Warping (DTW) to align the MFCC sequences
    distance, path = fastdtw(refMFCCs.T, testMFCCs.T, dist=euclidean)

    # Set a threshold (adjust as needed)
    threshold = 30000

    # Compare the DTW distance to the threshold
    
    if distance < threshold:
        print(1)
    else:
        print(0)

    print("DTW Distance:", distance)
    
compareAudioSamples()
