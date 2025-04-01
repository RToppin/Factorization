/**
 * Abstract class used for variable and funciton reusabilitly. 
 * https://stackoverflow.com/questions/3344816/when-and-why-to-use-abstract-classes-methods (why I chose this)
 *
 */


import java.util.ArrayList;
import java.util.List;

public abstract class Solver {
    protected final int LOWER = 2;
    protected final int UPPER = 100_000;
    protected long precomputeTimeNs = -1;

    protected List<List<Integer>> precomputedFactors = null;

    /**
     * Precomute all factors from 2 - 100_000
     * This will be timed and returned to the user
     */
    public abstract void precompute();

    /**
     * Returns a list of factors for a given number
     * either from the precomputed factors or dynamically 
     * proccessed
     */
    public abstract List<Integer> getFactors(int n);

    // Utility: Init empty list for factors
    protected List<List<Integer>> initFactorList(){
        List<List<Integer>> factorLists = new ArrayList<>(UPPER + 1);
        for (int i = 0; i <= UPPER; i++) {
            factorLists.add(new ArrayList<>());
        }
        return factorLists;
    }

    // Utility: on the spot factorization
    protected List<Integer> computeFactors(int n) {
        List<Integer> factors = new ArrayList<>();
        for (int i = 1; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                factors.add(i);
                int pair = n / i;
                if (pair != i) {
                    factors.add(pair);
                }
            }
        }
        return factors;
    }

    /**
     * Gets the time it took the last run solver to precompute factors
     * @return
     */
    public long getPrecomputeTimeNs() {
        return precomputeTimeNs;
    }
}
