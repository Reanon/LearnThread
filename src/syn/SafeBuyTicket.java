/**
 * FileName: SafeBuyTicket
 * Description:
 *
 * @create: 2020/11/10 16:10
 * @author Reanon
 * @version 1.0.0
 * @since 1.8
 */

package syn;

class BuyTicket1 implements Runnable {
    // 设置标志位来外部停止方法
    boolean flag = true;
    private int ticketNums = 10;

    @Override
    public void run() {
        while (flag) {
            buy();
        }
    }

    // synchronized 同步方法
    private synchronized void buy() {
        //判断是否有票
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

public class SafeBuyTicket {
    public static void main(String[] args) {
        BuyTicket1 station = new BuyTicket1();
        new Thread(station, "小明").start();
        new Thread(station, "老师").start();
        new Thread(station, "黄牛").start();
    }
}
