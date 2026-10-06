package com.texthackplus.algorithms.math;

public class MillerRabinPrimalityTest {

    public static void main(String[] args) {
        testPrimes();
        testComposites();
        testSmallNumbers();
        System.out.println("MillerRabinPrimality tests passed!");
    }

    private static void testPrimes() {
        assert MillerRabinPrimality.isPrime(17, 5).isPrime() : "17 should be prime";
        assert MillerRabinPrimality.isPrime(31, 5).isPrime() : "31 should be prime";
        assert MillerRabinPrimality.isPrime(104729, 10).isPrime() : "104729 should be prime";
    }

    private static void testComposites() {
        assert !MillerRabinPrimality.isPrime(15, 5).isPrime() : "15 should not be prime";
        assert !MillerRabinPrimality.isPrime(100, 5).isPrime() : "100 should not be prime";
        assert !MillerRabinPrimality.isPrime(104727, 10).isPrime() : "104727 should not be prime";
    }

    private static void testSmallNumbers() {
        assert !MillerRabinPrimality.isPrime(-5, 5).isPrime() : "-5 should not be prime";
        assert !MillerRabinPrimality.isPrime(0, 5).isPrime() : "0 should not be prime";
        assert !MillerRabinPrimality.isPrime(1, 5).isPrime() : "1 should not be prime";
        assert MillerRabinPrimality.isPrime(2, 5).isPrime() : "2 should be prime";
        assert MillerRabinPrimality.isPrime(3, 5).isPrime() : "3 should be prime";
    }
}
