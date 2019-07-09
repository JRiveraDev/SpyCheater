package org.runnerer.spycheater.common.utils;

import java.math.BigInteger;
import java.security.SecureRandom;

public class ID
{

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generate()
    {
        return new BigInteger(24, RANDOM).toString(32);
    }
}

