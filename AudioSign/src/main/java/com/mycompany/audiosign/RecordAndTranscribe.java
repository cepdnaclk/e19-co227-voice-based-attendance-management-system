package com.mycompany.audiosign;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RecordAndTranscribe {
    private String line = "";
    public String runCode(String path) {
        Thread thread = new Thread(() -> {
            try {
                String workingDir = System.getProperty("user.dir"); // Working directory path
                String pathToPy = workingDir + "/src/main/py/"+path+".py"; // Absolute path for the file
                //System.out.println(pathToPy);
                ProcessBuilder processBuilder = new ProcessBuilder("python", pathToPy); // Used to run python files from java

                // Run python code
                Process process = processBuilder.start();

                // If python output something, to display that
                InputStream inputStream = process.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                line = reader.readLine(); // Output of the python is taken to line variable
                //System.out.println(line + " startSTT");

                //fetchData();

                // Get the exit code. 0 -> Normal termination. 1 -> Abnormal termination
                int exitCode = process.waitFor();
                // Exit code is not checked here because it is checked using python output
                if (exitCode == 0) {
                    // Recording is successful
                } else {
                    // Recording is not successful
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join(); // This waits for thread to finish
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return line;
    }
}
