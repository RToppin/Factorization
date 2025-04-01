import java.util.List;
import java.util.stream.IntStream;

public class StreamSolver extends Solver {
    /**
     * Precompute() uses a single thread to find the factors of n, up to sqrt(n), and adds the factor pair where it is not itself.
     */
    @Override
    public void precompute(){
        precomputedFactors = initFactorList(); // Init factor list
        
        // Start timing
        long start = System.nanoTime();

        IntStream.range(LOWER, UPPER).forEach(n->addFactors(n));

        // Store elapsed time
        precomputeTimeNs = System.nanoTime() - start;
    }

    /**
     * For each value in the stream of integers, factorize n and add it's factors to precomputedFactors
     * @param n
     */
    public void addFactors(int n){
        for (int i = 1; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                precomputedFactors.get(n).add(i);
                int pair = n / i;
                if (pair != i) {
                    precomputedFactors.get(n).add(pair);
                }
            }
        }
    }


    /**
     * Gets the factors of an int n from the premade list if it is below the UPPER and computes if it is beyond UPPER
     */
    @Override
    public List<Integer> getFactors(int n) {
        if (n <= UPPER) {
            return precomputedFactors.get(n);
        } else {
            return computeFactors(n);
        }
    }
}
