package com.bartoleo.gflappyasciibird.util


class MathUtils {

    private static Random random

    public static synchronized init() {
        if (!random)
            random = new Random()
    }

/**
 * @param low inclusive
 * @param high inclusive
 * @return
 */
    public static int getIntInRange(int low, int high) {
        if (low == high) return low
        if (!random) init()
        return random.nextInt(high + 1 - low) + low
    }

}
