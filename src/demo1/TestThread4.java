/**
 * FileName: TestThread4
 * Description:
 *
 * @create: 2020/11/9 17:28
 * @author Reanon
 * @version 1.0.0
 * @since 1.8
 */

package demo1;

// 多线程操作同一个对象
// 发现问题:多个线程操作同一个资源的情况下,线程不安全，数据紊乱。

public class TestThread4 implements Runnable {

    //票数
    private int ticketNums = 10;
    @Override
    public void run() {
        while (true) {
            if (ticketNums <= 0) {
                break;
            }
            // 模拟延时
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + "--> get the  " + ticketNums-- + " ticket");
        }
    }

    public static void main(String[] args) {
        TestThread4 ticket = new TestThread4();
        new Thread(ticket, "  小明 ").start();
        new Thread(ticket, "  老师 ").start();
        new Thread(ticket, " 黄牛党 ").start();
    }
}
