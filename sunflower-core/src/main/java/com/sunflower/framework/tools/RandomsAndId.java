package com.sunflower.framework.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

public class RandomsAndId {
    public RandomsAndId() {
    }

    public static String generate() {
        StringBuilder sb = new StringBuilder();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        sb.append(sdf.format(cal.getTime()));
        sb.append(String.valueOf(cal.getTimeInMillis()).substring(7));
        sb.append(String.valueOf(Math.round(Math.random() * 90.0D + 10.0D)));
        return sb.toString();
    }

    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24);
    }

    public static String getRandomString(int length) {
        if (length <= 0) {
            return "";
        } else {
            char[] randomChar = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm'};
            Random random = new Random();
            StringBuffer stringBuffer = new StringBuffer();

            for(int i = 0; i < length; ++i) {
                stringBuffer.append(randomChar[random.nextInt(10)]);
            }

            return stringBuffer.toString();
        }
    }

    public static String getRandomNum(int length) {
        if (length <= 0) {
            return "";
        } else {
            char[] randomChar = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
            Random random = new Random();
            StringBuffer stringBuffer = new StringBuffer();

            for(int i = 0; i < length; ++i) {
                stringBuffer.append(randomChar[random.nextInt(10)]);
            }

            return stringBuffer.toString();
        }
    }
}
