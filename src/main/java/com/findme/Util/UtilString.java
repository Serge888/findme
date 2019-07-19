package com.findme.Util;

import com.findme.exception.BadRequestException;

public class UtilString {

    public static Long stringToLong(String string) throws BadRequestException {
        try {
            return Long.parseLong(string);
        } catch (Exception e) {
            throw new BadRequestException("Can't convert it to Long. Please, check you request and try again.");
        }
    }
}
