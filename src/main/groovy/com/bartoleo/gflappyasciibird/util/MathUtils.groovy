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

    public static boolean checkCollide(Integer x, Integer y, Integer oWidth, Integer oHeight, Integer xTwo, Integer yTwo, Integer oTwoWidth, Integer oTwoHeight) {
        // AABB 1
        int x1Min = x;
        int x1Max = x + oWidth;
        int y1Max = y + oHeight;
        int y1Min = y;

        // AABB 2
        int x2Min = xTwo;
        int x2Max = xTwo + oTwoWidth;
        int y2Max = yTwo + oTwoHeight;
        int y2Min = yTwo;

        // Collision tests
        if (x1Max < x2Min || x1Min > x2Max) return false;
        if (y1Max < y2Min || y1Min > y2Max) return false;

        return true;
    }
}
