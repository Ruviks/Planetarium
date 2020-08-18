package com.mygdx.game;

import java.util.Random;

public class RuvikUtil {
    /**
     * @param min the minimum bound
     * @param max the maximum bound
     * @return random int from range(min,max)
     */
    public static  int range(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
