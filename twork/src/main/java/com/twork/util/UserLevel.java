package com.twork.util;

public class UserLevel {
    public static final Integer UNAVAILABLE_USER = -1;
    public static final Integer UNCHECKED_USER = 1;
    public static final Integer ORDINARY_USER = 2;
    public static final Integer POWER_USER = 3;
    public static final Integer MANAGER_USER = 4;

    public static boolean check(Integer level) {
        if (UNAVAILABLE_USER.equals(level)
                || UNCHECKED_USER.equals(level)
                || ORDINARY_USER.equals(level)
                || POWER_USER.equals(level)
                || MANAGER_USER.equals(level)) {
            return true;
        }
        return false;
    }
}
