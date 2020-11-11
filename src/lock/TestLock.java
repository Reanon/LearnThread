/**
 * FileName: TestLock
 * Description: 测试Lock锁
 *
 * @create: 2020/11/11 15:42
 * @author Reanon
 * @version 1.0.0
 * @since 1.8
 */

package lock;

import java.util.concurrent.locks.ReentrantLock;

// 测试Lock 锁
public class TestLock {
    public static void main(String[] args) {
        Lock testLock2 = new Lock();
        new Thread(testLock2).start();
        new Thread(testLock2).start();
        new Thread(testLock2).start();

    }
}

class Lock implements Runnable {
    // 定义lock锁
    private final ReentrantLock lock = new ReentrantLock();
    int ticketNums = 10;

    @Override
    public void run() {
        while (true) {
            // 加锁
            lock.lock();
            try {
                if (ticketNums > 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "-->拿到了第" + ticketNums-- + "票");
                } else {
                    break;
                }
            } finally {
                lock.unlock();
                // 解锁
            }
        }
    }
}