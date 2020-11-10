/**
 * FileName: TestPriority
 * Description: 测试线程优先级
 *
 * @create: 2020/11/9 21:27
 * @author Reanon
 * @version 1.0.0
 * @since 1.8
 */

package state;

// 测试线程优先级别
public class TestPriority extends Thread{
    public static void main(String[] args) {
        //主线程默认优先级
        System.out.println(Thread.currentThread().getName() + "-->" + Thread.currentThread().getPriority());
        MyPriority myPriority = new MyPriority();

        Thread t1 = new Thread(myPriority,"1");
        Thread t2 = new Thread(myPriority,"2");
        Thread t3 = new Thread(myPriority,"3");
        Thread t4 = new Thread(myPriority,"4");
        //先设置优先级，再启动
        t1.start();

        t2.setPriority(1);
        t2.start();

        t3.setPriority(4);
        t3.start();
        // MAX_PRIORITY = 10
        t4.setPriority(Thread.MAX_PRIORITY);
        t4.start();
    }

}
class MyPriority implements Runnable{

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "-->" + Thread.currentThread().getPriority());

    }
}