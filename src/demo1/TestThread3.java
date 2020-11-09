/**
 * FileName: TestThread3
 * Description:
 *
 * @create: 2020/11/9 17:16
 * @author Reanon
 * @version 1.0.0
 * @since 1.8
 */

package demo1;

// 创建线程方式﹔实现runnable接口,重写run方法，执行线程需要丢入runnable接口实现类。调用start方法。

public class TestThread3 implements Runnable {
    public static void main(String[] args) {
        // 创建runnable接口的实现类对象
        TestThread3 testThread3 = new TestThread3();
        // 创建线程对象，通过线程对象来开启线程，代理
        Thread thread = new Thread(testThread3);

        thread.start();
        for (int i = 0; i < 200; i++) {
            System.out.println("我在学习多线程--" + i);
        }
    }

    @Override
    public void run() {
        // run方法线程体
        for (int i = 0; i < 20; i++) {
            System.out.println("我在看代码--" + i);
        }
    }
}