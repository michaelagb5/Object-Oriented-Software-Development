import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PingWebsite {

    public static void main(String[] args) {
        // Example usage: pinging google.com 10 times
        pingWebsite("google.com", 10);
    }

    public static void pingWebsite(String website, int numberOfPings) {
        // Create a new ProcessBuilder instance
        ProcessBuilder processBuilder = new ProcessBuilder();

        // Set the command to run in the process
        // On Windows, use "cmd.exe" with "/c" to execute the ping command
        // -n is used to specify the number of pings
        processBuilder.command("cmd.exe", "/c", "ping", "-n", Integer.toString(numberOfPings), website);

        ExecutorService servicePool = Executors.newSingleThreadExecutor();

        try {
            // Start the process
            Process process = processBuilder.start();

            // Create a Callable to read the output
            Callable<List<String>> outputReader = () -> {
                List<String> lines = new ArrayList<>();
                InputStream inputStream = process.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
                return lines;
            };

            // Submit the Callable to the ExecutorService
            Future<List<String>> future = servicePool.submit(outputReader);

            // Wait for the result and print it to the console
            List<String> output = future.get();
            for (String line : output) {
                System.out.println(line);
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();
            System.out.println("Exited with error code " + exitCode);

        } catch (IOException | InterruptedException | ExecutionException e) {
            // Handle any exceptions that occur during execution
            e.printStackTrace();
        } finally {
            // Shutdown the ExecutorService
            servicePool.shutdown();
        }
    }
}