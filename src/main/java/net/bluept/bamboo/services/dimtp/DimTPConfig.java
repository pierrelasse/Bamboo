package net.bluept.bamboo.services.dimtp;

import java.util.Random;

public class DimTPConfig {
    public static int INTERVAL;
    public static final int INTERVAL_MIN = 60 * 5;
    public static final int INTERVAL_MAX = 60 * 10;

    public static final int X_MIN = 5000;
    public static final int X_MAX = 10000000;
    public static final int Z_MIN = 5000;
    public static final int Z_MAX = 10000000;

    public static final int MAX_TRIES = 6;

    public static final String BLACKSCREEN_CHAR = "\u7004";
    public static final String EMPTY_STR = "";

    public static final Random random = new Random();
}
