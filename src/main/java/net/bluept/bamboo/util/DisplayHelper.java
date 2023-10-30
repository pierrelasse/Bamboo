package net.bluept.bamboo.util;

public class DisplayHelper {
    public static String timerAnimation(int animationTick, boolean right) {
        if (right) {
            return " " + switch (animationTick) {
                case 1 -> "&d<<<";
                case 2 -> "&d<<&5<";
                case 3 -> "&d<&5<<";
                case 4 -> "&5<<<";
                case 5 -> "&5<<&d<";
                default -> "&5<&d<<";
            };
        } else {
            return switch (animationTick) {
                case 1 -> "&d>>>";
                case 2 -> "&5>&d>>";
                case 3 -> "&5>>&d>";
                case 4 -> "&5>>>";
                case 5 -> "&d>&5>>";
                default -> "&d>>&5>";
            } + " ";
        }
    }

    public static String convertSecondsToDuration(long seconds) {
        long days = seconds / (24 * 3600);
        long hours = (seconds % (24 * 3600)) / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;

        StringBuilder sb = new StringBuilder();

        if (days > 0) {
            sb.append(days).append("d ");
        }
        if (hours > 0 || days > 0) {
            sb.append(hours).append("h ");
        }
        if (minutes > 0 || hours > 0 || days > 0) {
            sb.append(minutes).append("m ");
        }

        return sb.append(remainingSeconds).append("s").toString();
    }
}
