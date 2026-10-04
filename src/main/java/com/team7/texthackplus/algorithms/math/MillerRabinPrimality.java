package com.team7.texthackplus.algorithms.math;

/**
 * Miller-Rabin Primality Test.
 * A probabilistic algorithm to determine if a given number is prime.
 * Used for hashing or cryptographic analysis of documents.
 */
public class MillerRabinPrimality {

    public static class SearchResult {
        private final boolean isPrime;
        private final int comparisons;
        private final long timeNs;
        private final String algorithm = "Miller-Rabin Primality Test";
        private final String complexity = "O(k log^3 n)";

        public SearchResult(boolean isPrime, int comparisons, long timeNs) {
            this.isPrime = isPrime;
            this.comparisons = comparisons;
            this.timeNs = timeNs;
        }

        public boolean isPrime() { return isPrime; }
        public int getComparisons() { return comparisons; }
        public long getTimeNs() { return timeNs; }
        public String getAlgorithm() { return algorithm; }
        public String getComplexity() { return complexity; }
    }

    /**
     * Checks whether a number is probably prime.
     *
     * @param n          The number to check.
     * @param iterations The number of iterations (k) to determine accuracy.
     * @return a {@link SearchResult} indicating primality.
     */
    public static SearchResult isPrime(long n, int iterations) {
        long start = System.nanoTime();
        int comparisons = 0;

        if (n <= 1) {
            return new SearchResult(false, comparisons, System.nanoTime() - start);
        }
        if (n <= 3) {
            return new SearchResult(true, comparisons, System.nanoTime() - start);
        }
        if (n % 2 == 0) {
            return new SearchResult(false, comparisons, System.nanoTime() - start);
        }

        long d = n - 1;
        while (d % 2 == 0) {
            d /= 2;
        }

        boolean prime = true;
        for (int i = 0; i < iterations; i++) {
            comparisons++;
            // Generate a pseudo-random base 'a' in the range [2, n - 2]
            // We use a simple linear congruential generator to avoid java.util.Random
            long a = 2 + (Math.abs(pseudoRandom(i)) % (n - 3));

            if (!millerTest(d, n, a)) {
                prime = false;
                break;
            }
        }

        long end = System.nanoTime();
        return new SearchResult(prime, comparisons, end - start);
    }

    private static boolean millerTest(long d, long n, long a) {
        long x = power(a, d, n);
        if (x == 1 || x == n - 1) {
            return true;
        }

        while (d != n - 1) {
            x = mulMod(x, x, n);
            d *= 2;

            if (x == 1) return false;
            if (x == n - 1) return true;
        }
        return false;
    }

    private static long power(long base, long exp, long mod) {
        long res = 1;
        base = base % mod;
        while (exp > 0) {
            if ((exp & 1) == 1) {
                res = mulMod(res, base, mod);
            }
            exp = exp >> 1;
            base = mulMod(base, base, mod);
        }
        return res;
    }
    
    // To prevent overflow when multiplying two long values
    private static long mulMod(long a, long b, long mod) {
        long res = 0;
        a %= mod;
        while (b > 0) {
            if ((b & 1) == 1) {
                res = (res + a) % mod;
            }
            a = (a * 2) % mod;
            b >>= 1;
        }
        return res;
    }

    private static long pseudoRandom(int seed) {
        long x = System.nanoTime() + seed;
        x ^= (x << 21);
        x ^= (x >>> 35);
        x ^= (x << 4);
        return x;
    }
}
