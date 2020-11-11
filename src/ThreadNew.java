import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * FileName: ThreadNew
 * Description:
 *
 * @author Reanon
 * @version 1.0.0
 * @create: 2020/11/11 17:30
 * @since 1.8
 */

// 回顾线程的创建
public class ThreadNew {
    public static void main(String[] args) {
        // 1. 继承Thread类
        new MyThread1().start();
        // 2. 实现Runnable接口
        new Thread(new MyThread2()).start();

        // 3.实现Callable接口
        FutureTask<Integer> futureTask = new FutureTask<>(new MyThread3());
        new Thread(futureTask).start();

        try {
            // 获取返回值，捕获异常
            int integer = futureTask.get();
            System.out.println(integer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

// 1. 继承Thread类
class MyThread1 extends Thread {
    @Override
    public void run() {
        System.out.println("继承Thread类");
    }
}

// 2. 实现Runnable接口
class MyThread2 implements Runnable {
    @Override
    public void run() {
        System.out.println("实现Runnable接口");
    }
}

// 3.实现Callable接口
class MyThread3 implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println("实现Callable接口");
        return 100;
    }

}