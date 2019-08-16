package com.findme.util;

import com.findme.exception.BadRequestException;
import com.findme.models.FriendRelationshipStatus;

public class UtilString {

    public static Long stringToLong(String string) throws BadRequestException {
        try {
            return Long.parseLong(string);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException("Can't convert it to Long. Please, check you request and try again.");
        }
    }
    public static Integer stringToInteger(String string) throws BadRequestException {
        try {
            return Integer.parseInt(string);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException("Can't convert it to Integer. Please, check you request and try again.");
        }
    }


    public static boolean isEmail(String emailAddress) {
        return emailAddress != null
                && emailAddress.contains("@")
                && emailAddress.contains(".")
                && emailAddress.trim().length() > 6;
    }


    public static String phoneChecker(String phoneNumber) {
        StringBuilder realPhoneNumber = new StringBuilder();
        if (phoneNumber != null && phoneNumber.trim().length() > 6) {
            char[] chars = phoneNumber.trim().toCharArray();
            if (Character.isDigit(chars[0]) || '+' == chars[0]) {
                realPhoneNumber.append(chars[0]);
            }
            for (int i = 1; i < chars.length; i++) {
                if (Character.isDigit(chars[i])) {
                    realPhoneNumber.append(chars[i]);
                }
            }
            if (realPhoneNumber.length() < 7) {
                return null;
            }
            return realPhoneNumber.toString();
        } else {
            return null;
        }
    }

}
