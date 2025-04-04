import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class DistributedSolver extends Solver {

    private static final String SERVER_ADDRESS = "localhost"; // Address of the server
    private static final int SERVER_PORT = 12345; // Port where the server listens


    /**
     * Precomputes the factors of numbers in a given range using multiple threads.
     * This method divides the number range into chunks and assigns each chunk to a separate thread for
     * parallel processing. It uses an ExecutorService to manage the threads. Once all threads complete their tasks,
     * the factors are stored in `precomputedFactors` for future retrieval.
     *
     * @throws InterruptedException if the thread is interrupted while waiting for the completion of tasks
     * @throws ExecutionException if an exception occurs during task execution
     */
    @Override
    public void precompute() {
        precomputedFactors = initFactorList();

        int numThreads = Runtime.getRuntime().availableProcessors(); // Get the number of threads for the machine
        ExecutorService executor = Executors.newFixedThreadPool(numThreads); // Start executor with numThreads
        List<Future<?>> futures = new ArrayList<>();

        // Calculate chunk size for each thread
        int totalNumbers = UPPER - LOWER + 1;
        int chunkSize = totalNumbers / numThreads;

        // Start timing
        long start = System.nanoTime();

        for (int i = 0; i < numThreads; i++) {
            int startNum = LOWER + i * chunkSize;
            int endNum;
            if (i == numThreads - 1) {
                endNum = UPPER;
            } else {
                endNum = startNum + chunkSize - 1;
            }

            // Submit task to executor for each chunk
            Future<?> future = executor.submit(() -> {
                for (int n = startNum; n <= endNum; n++) {
                    List<Integer> factors = getFactorsFromServer(n);
                    if (factors != null) {
                        // Store the factors at index `n` in the precomputedFactors list
                        precomputedFactors.set(n, factors); // Set factors for the number `n`
                    }
                }
            });

            futures.add(future);
        }

        // Wait for all tasks to complete
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();

        // Store elapsed time
        precomputeTimeNs = System.nanoTime() - start;
    }

    @Override
    public List<Integer> getFactors(int n) {
        if (n <= UPPER) {
            return precomputedFactors.get(n);
        } else {
            return computeFactors(n); // If n > UPPER, compute factors locally (not via server)
        }
    }

    // Method to get factors from the server via socket communication
    private List<Integer> getFactorsFromServer(int n) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            // Send the number to be factored to the server
            out.writeObject(n);

            // Receive the list of factors from the server
            List<Integer> factors = (List<Integer>) in.readObject();
            return factors;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null; // Return null if an error occurs
    }
}