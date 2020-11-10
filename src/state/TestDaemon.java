/**
 * FileName: TestDaemon
 * Description: 测试守护线程
 *
 * @create: 2020/11/9 21:48
 * @author Reanon
 * @version 1.0.0
 * @since 1.8
 */

package state;


import org.omg.PortableServer.THREAD_POLICY_ID;

public class TestDaemon {
    public static void main(String[] args) {
        God god = new God();
        You you = new You();
        Thread thread = new Thread(god);
        // 默认false表示是用户线程，正常的线程都是用户线程
        thread.setDaemon(true);

        // 守护线程启动: 上帝
        thread.start();
        // 用户线程启动:你
        new Thread(you).start();

    }
}
//上帝
class God implements Runnable {
    @Override
    public void run() {
        while (true) {
            System.out.println("上帝保佑着你");
        }
    }
}
//你
class You implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 36500; i++) {
            System.out.println("你一生都开心的活着");
        }
        System.out.println("======good Bye======");
    }
}