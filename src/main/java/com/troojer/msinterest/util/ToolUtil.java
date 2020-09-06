package com.troojer.msinterest.util;

import java.util.Random;

import static java.util.ResourceBundle.getBundle;
import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

public class ToolUtil {
    public static String getMessage(String messageName) {
        return getBundle("messages", getLocale()).getString(messageName);
    }

    public static String getRandomCode(int digitCount) {
        if (digitCount > 9 || digitCount < 1) throw new IllegalArgumentException("digit must be between 1 and 9");
        Random rnd = new Random();
        int number = rnd.nextInt((int)Math.pow(10, digitCount) - 1);
        return String.format("%0" + digitCount + "d", number);
    }
}
