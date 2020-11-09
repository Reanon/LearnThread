/**
 * FileName: TestStop
 * Description:
 *
 * @create: 2020/11/9 19:46
 * @author Reanon
 * @version 1.0.0
 * @since 1.8
 */

package state;


public class TestStop implements Runnable {
    // 1.设置一个标志位
    private boolean flag = true;

    //2.设置一个公开的方法停止线程，转换标志位
    public void stop() {
        this.flag = false;
    }
    @Override
    public void run() {
        int i = 0;
        while (flag) {
            System.out.println("run....Thread" + i);
        }
    }
    public static void main(String[] args) {
        TestStop testStop = new TestStop();
        new Thread(testStop).start();
        for (int i = 0; i < 1000; i++) {
            System.out.println("main" + i);
            if (i == 900) {
                // 调用stop方法切换标志位，让线程停止
                testStop.stop();
                System.out.println("线程停止了");
            }
        }
    }
}

