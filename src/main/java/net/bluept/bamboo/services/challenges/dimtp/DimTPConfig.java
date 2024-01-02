package net.bluept.bamboo.services.challenges.dimtp;

public class DimTPConfig {
    public static int INTERVAL;
    public static final int INTERVAL_MIN = 60 * 5;
    public static final int INTERVAL_MAX = 60 * 10;

    public static final int X_MIN = 200;
    public static final int X_MAX = 15000;
    public static final int Z_MIN = X_MIN;
    public static final int Z_MAX = X_MAX;

    public static final int MAX_TRIES = 6;

    public static final String BLACKSCREEN_CHAR = "\u7004";
    public static final String EMPTY_STR = "";

    public static boolean enabled = true;
}
