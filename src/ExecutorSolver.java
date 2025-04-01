
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorSolver extends Solver{

    @Override
    public void precompute() {
        precomputedFactors = initFactorList(); // Init factor list
        int numThreads = Runtime.getRuntime().availableProcessors(); // Get # of threads for machine
        ExecutorService executor = Executors.newFixedThreadPool(numThreads); // Start executor with numThreads
        List<Future<?>> futures = new ArrayList<>(); 

        // Calculate chunksize for each thread 
        int totalNumbers = UPPER - LOWER + 1;
        int chunkSize = totalNumbers / numThreads;

        // Start timing
        long start = System.nanoTime();

        for (int i = 0; i < numThreads; i++){
            int startNum = LOWER + i * chunkSize;
            int endNum;
            if (i == numThreads - 1) {
                endNum = UPPER;
            } else {
                endNum = startNum + chunkSize - 1;
            }

            // For each chunk of numbers run this based on the start and end num, keeps task amount to cores 1:1
            Future<?> future = executor.submit(() -> {
                for (int n = startNum; n<= endNum; n++){
                    for (int j = 1; j <= Math.sqrt(n); j++) {
                        if (n % j == 0) {
                            precomputedFactors.get(n).add(j);
                            int pair = n / j;
                            if (pair != j) {
                                precomputedFactors.get(n).add(pair);
                            }
                        }
                    }
                }
            });

            futures.add(future);
        }
        
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
            return computeFactors(n);
        }
    }
    
}
