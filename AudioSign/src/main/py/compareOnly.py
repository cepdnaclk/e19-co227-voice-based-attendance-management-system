import librosa
from fastdtw import fastdtw
from scipy.spatial.distance import euclidean
import os
import sys

# Get the parameter passed from Java
regNum = sys.argv[1] if len(sys.argv) > 1 else None
print("Reg Number is: ", regNum)

def compareAudioSamples():
    testFilePartialPath = "\\src\\main\\res\\temp\\test.wav"
    refFilePartialPath = "\\src\\main\\res\\audioSamples\\"+regNum+".wav"
    
    currentPath = os.getcwd()
    
    testFileFullPath = currentPath + testFilePartialPath
    refFileFullPath = currentPath + refFilePartialPath
    
    
    print(testFileFullPath)
    print(refFileFullPath)
    
    # Load the reference voice sample
    referenceFile = refFileFullPath
    
    #Load file and get reference audio and sample rate
    refAudio, refSampleRate = librosa.load(referenceFile, sr=None) # sr = None is used so sampling rate will be determined from the file

    # Load the test voice sample
    testFile = testFileFullPath
    
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
    threshold = 30000  # Change for precission. Higher the number -> Low accuracy, lower the number -> high accuracy

    # Compare the DTW distance to the threshold
    if distance < threshold:
        print(1)
    else:
        print(0)
    print("DTW Distance:", distance)    
        
compareAudioSamples()