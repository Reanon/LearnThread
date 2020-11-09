/**
 * FileName: TestState
 * Description: 查看线程状态
 *
 * @create: 2020/11/9 21:01
 * @author Reanon
 * @version 1.0.0
 * @since 1.8
 */

package state;


public class TestState {
    public static void main(String[] args) throws InterruptedException {
        // 使用lambda表达式
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("///////");
        });
        //观察状态
        Thread.State state = thread.getState();
        System.out.println(state);
        //观察后启动
        //启动线程
        thread.start();
        thread.getState(); //Run
        while (state != Thread.State.TERMINATED){
            //只要线程不停止，就一直输出状态
            Thread.sleep(100);
            //更新线程状态
            state = thread.getState();
            //输出状态
            System.out.println(state);
        }
        // 报错，一旦进入死亡状态的线程不能再启动
        thread.start();
    }
}
