package io.github.brendoncurmi.fusionpixelmon.impl;

public class TimeUtil {

    /**
     * Asynchronously executes the specified runnable a time 'millis' milliseconds later.
     *
     * @param runnable the runnable.
     * @param millis   the delay in milliseconds before executing.
     */
    public static void setTimeout(Runnable runnable, int millis) {
        new Thread(() -> {
            try {
                Thread.sleep(millis);
                runnable.run();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();
    }
}
