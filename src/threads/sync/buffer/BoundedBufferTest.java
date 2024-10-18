package threads.sync.buffer;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class BoundedBufferTest {
    public static void main(String[] args) throws InterruptedException {
        BoundedBuffer<Integer> buffer = new BoundedBuffer<>(2);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        int threadsCount = 100;
        CountDownLatch countDownLatchFinish = new CountDownLatch(threadsCount);
        Thread[] threads = new Thread[threadsCount];
        for (int i = 0; i < threadsCount; i++) {
            final int counter = i;
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        countDownLatch.await();
                        System.out.printf("%d started %s\n", counter, LocalDateTime.now());
                        Random random = new Random();
                        int rand = random.nextInt(1, 1000);
                        if (counter % 2 == 0) {
//                            System.out.printf("thread %d putting %d\n", counter, rand);
                            buffer.put(rand);
                        } else {
                            System.out.printf("thread %d took %d\n", counter, buffer.take());
                        }
                        countDownLatchFinish.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            threads[i].start();
        }
        countDownLatch.countDown();

        countDownLatchFinish.await();

    }
}
