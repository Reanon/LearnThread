/**
 * FileName: DeadLock
 * Description: 死锁
 *
 * @create: 2020/11/11 14:40
 * @author Reanon
 * @version 1.0.0
 * @since 1.8
 */

package lock;

// 死锁：多个线程互相抱着对方需要的资源
public class DeadLock {
    public static void main(String[] args) {
        MakeUp m1 = new MakeUp(0, "灰姑娘");
        MakeUp m2 = new MakeUp(1, "白雪公主");

        m1.start();
        m2.start();
    }
}

//口红
class Lipstick {

}

//镜子
class Mirror {

}

//化妆
class MakeUp extends Thread {
    static Lipstick lipstick = new Lipstick();
    static Mirror mirror = new Mirror();

    int choice;
    String name;

    public MakeUp(int choice, String name) {
        this.choice = choice;
        this.name = name;
    }


    @Override
    public void run() {
        try {
            makeup();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 化妆，互相持有对方的锁，就是需要拿到对方的资源
    private void makeup() throws InterruptedException {
        if (choice == 0) {
            synchronized (lipstick) {
                //获得口红的锁
                System.out.println(this.name + "获得口红的锁");
                Thread.sleep(1000);
            }
            //从同步代码块拿出来
            synchronized (mirror) {
                //一秒钟后想获得镜子的锁
                System.out.println(this.name + "获得镜子的锁");
            }
        } else {
            synchronized (mirror) {
                //获得镜子的锁
                System.out.println(this.name + "获得镜子的锁");
                Thread.sleep(2000);
            }
            //从同步代码块拿出来
            synchronized (lipstick) {
                //两秒钟后想获得口红的锁
                System.out.println(this.name + "获得口红的锁");
            }
        }
    }
}