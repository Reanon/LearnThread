/**
 * FileName: TestJoin
 * Description:
 *
 * @create: 2020/11/9 20:44
 * @author Reanon
 * @version 1.0.0
 * @since 1.8
 */

package state;


public class TestJoin implements Runnable {
    public static void main(String[] args) throws InterruptedException {
        //创建runnable接口实现类
        TestJoin testJoin = new TestJoin();
        // 通过线程对象来开启线程代理
        Thread thread = new Thread(testJoin);
        // 调用start方法
        thread.start();

        // 主线程
        for (int i = 0; i < 500; i++) {
            if (i == 200) {
                // 插队
                thread.join();
            }
            System.out.println("main" + i);
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println("线程Vip来了" + i);
        }
    }
}
