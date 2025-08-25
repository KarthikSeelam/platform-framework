package com.ican.cortex.platform.utilities.general.utils;


import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@Component
public class GeneralUtils {

    public static UUID generateUUID() {
        return UUID.randomUUID();
    }

    public static int generateRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String repeatString(String str, int count) {
        return str.repeat(count);
    }
}
