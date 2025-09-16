package utils;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public final class InitLimiter {
    private static final int LIMIT = Integer.parseInt(System.getProperty("INIT_LIMIT", "2"));
    private static final Semaphore SEM = new Semaphore(LIMIT);
    private InitLimiter(){}

    public static void beforeInit() {
        try {
            SEM.acquire();                             // aynı anda en fazla LIMIT başlatma
            // küçük jitter: hepsi aynı anda DLL/IO yapmasın
            Thread.sleep(ThreadLocalRandom.current().nextInt(120, 350));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
    public static void afterInit() { SEM.release(); }
}
