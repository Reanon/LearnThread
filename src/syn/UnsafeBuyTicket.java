/**
 * FileName: UnsafeBuyTicket
 * Description: 不安全的买票
 *
 * @create: 2020/11/10 14:56
 * @author Reanon
 * @version 1.0.0
 * @since 1.8
 */

package syn;
// 线程不安全 有负数
class BuyTicket implements Runnable {
    // 设置标志位来外部停止方法
    boolean flag = true;
    private int ticketNums = 10;

    @Override
    public void run() {
        while (flag) {
            buy();
        }
    }

    //判断是否有票
    private void buy() {
        if (ticketNums <= 0) {
            flag = false;
            return;
        }
        // 模拟延时
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "拿到" + ticketNums--);
    }
}

public class UnsafeBuyTicket {
    public static void main(String[] args) {
        BuyTicket station = new BuyTicket();
        new Thread(station, "小明").start();
        new Thread(station, "老师").start();
        new Thread(station, "黄牛").start();
    }
}
