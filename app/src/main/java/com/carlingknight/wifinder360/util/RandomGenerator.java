package com.carlingknight.wifinder360.util;

import java.util.Random;

/**
 * Created by Carling Knight on 29/03/2017.
 *
 * Adapted from the excellent method available here
 * http://stackoverflow.com/questions/12116092/android-random-string-generator
 * by bennettaur
 */

public class RandomGenerator {

    public static String String(Integer length) {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(length);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
