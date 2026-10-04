package texthack.algorithms.primality;

/**
 * Miller-Rabin probabilistic primality test, from scratch (no java.util,
 * no java.math). Error probability is at most 4^-k after k independent
 * rounds. Used to validate/generate large IDs (e.g. hashing/document IDs)
 * quickly without deterministic factorization.
 */
public class MillerRabin {

    /** Minimal linear-congruential PRNG, seeded from System.nanoTime(), to avoid java.util.Random. */
    private static class SimpleRandom {
        private long state;
        SimpleRandom() { this.state = System.nanoTime() ^ 0x5DEECE66DL; }
        long nextLong() {
            state = (state * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
            long high = state;
            state = (state * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
            return (high << 16) ^ state;
        }
    }

    /** Modular multiplication that avoids overflow via Russian-peasant multiplication (no BigInteger). */
    private static long mulMod(long a, long b, long mod) {
        long result = 0;
        a %= mod;
        if (a < 0) a += mod;
        b %= mod;
        if (b < 0) b += mod;
        while (b > 0) {
            if ((b & 1) == 1) {
                result += a;
                if (result >= mod) result -= mod;
            }
            a += a;
            if (a >= mod) a -= mod;
            b >>= 1;
        }
        return result;
    }

    private static long powMod(long base, long exp, long mod) {
        long result = 1;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) result = mulMod(result, base, mod);
            base = mulMod(base, base, mod);
            exp >>= 1;
        }
        return result;
    }

    public static boolean isProbablePrime(long n, int rounds) {
        if (n < 2) return false;
        for (long p : new long[]{2, 3, 5, 7, 11, 13, 17, 19, 23}) {
            if (n == p) return true;
            if (n % p == 0) return false;
        }

        long d = n - 1;
        int r = 0;
        while (d % 2 == 0) { d /= 2; r++; }

        SimpleRandom rand = new SimpleRandom();
        for (int i = 0; i < rounds; i++) {
            long a = 2 + (Math.abs(rand.nextLong()) % (n - 3));
            long x = powMod(a, d, n);
            if (x == 1 || x == n - 1) continue;

            boolean composite = true;
            for (int j = 0; j < r - 1; j++) {
                x = mulMod(x, x, n);
                if (x == n - 1) { composite = false; break; }
            }
            if (composite) return false;
        }
        return true;
    }

    public static boolean isProbablePrime(long n) {
        return isProbablePrime(n, 20);
    }
}
