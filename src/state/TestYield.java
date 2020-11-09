/**
 * FileName: TestYield
 * Description:
 *
 * @create: 2020/11/9 20:36
 * @author Reanon
 * @version 1.0.0
 * @since 1.8
 */

package state;


public class TestYield {
    public static void main(String[] args) {
        MyYeild myYeild = new MyYeild();
        new Thread(myYeild, "a").start();
        new Thread(myYeild, "b").start();
    }
}

class MyYeild implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "线程正在执行");
        //礼让
        Thread.yield();
        System.out.println(Thread.currentThread().getName() + "线程停止执行");
    }
}