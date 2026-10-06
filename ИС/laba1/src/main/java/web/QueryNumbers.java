package web;

import service.exception.BadRequestException;

public final class QueryNumbers {
    private QueryNumbers() {
    }

    public static int requiredInt(String value, String name) {
        Integer result = optionalInt(value, name);
        if (result == null) {
            throw new BadRequestException(name + " is required");
        }
        return result;
    }

    public static Integer optionalInt(String value, String name) {
        if (value == null) {
            return null;
        }
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException exception) {
            throw new BadRequestException(name + " must be a 32-bit integer");
        }
    }

    public static Long optionalLong(String value, String name) {
        if (value == null) {
            return null;
        }
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException exception) {
            throw new BadRequestException(name + " must be a 64-bit integer");
        }
    }

    public static Float optionalFloat(String value, String name) {
        if (value == null) {
            return null;
        }
        try {
            return Float.valueOf(value);
        } catch (NumberFormatException exception) {
            throw new BadRequestException(name + " must be a number");
        }
    }
}
