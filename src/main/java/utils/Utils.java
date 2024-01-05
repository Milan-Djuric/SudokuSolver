package utils;

public class Utils {
    public static void sleep (int miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
        }
    }
}
