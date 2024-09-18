

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class PingWebsite {

    public static void main(String[] args) {
        // Example usage: pinging google.com 10 times
        pingWebsite("google.com", 10);
    }

    public static void pingWebsite(String website, int numberOfPings) {
        // Create a new ProcessBuilder instance to build and start processes
        ProcessBuilder processBuilder = new ProcessBuilder();

        // Set the command to run in the process
        // On Windows, use "cmd.exe" with "/c" to execute the ping command
        // -n is used to specify the number of pings
        processBuilder.command("cmd.exe", "/c", "ping", "-n", Integer.toString(numberOfPings), website);

        // Create a single-threaded ExecutorService for managing threads
        ExecutorService servicePool = Executors.newSingleThreadExecutor();

        try {
            // Start the process
            Process process = processBuilder.start();

            // Create a Callable to read the output of the process
            Callable<List<String>> outputReader = () -> {
                // List to store the output lines
                List<String> lines = new ArrayList<>();
                // Get the input stream of the process
                InputStream inputStream = process.getInputStream();
                // Wrap the input stream with a BufferedReader for efficient reading
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                // Read each line of the output and add it to the list
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
                return lines;
            };

            // Submit the Callable to the ExecutorService for execution
            Future<List<String>> future = servicePool.submit(outputReader);

            // Wait for the result and print it to the console
            List<String> output = future.get();
            for (String line : output) {
                System.out.println(line);
            }

            // Wait for the process to complete and get its exit code
            int exitCode = process.waitFor();
            System.out.println("Exited with error code " + exitCode);

        } catch (IOException | InterruptedException | ExecutionException e) {
            // Handle any exceptions that occur during execution
            e.printStackTrace();
        } finally {
            // Shutdown the ExecutorService to release its resources
            servicePool.shutdown();
        }
    }
}