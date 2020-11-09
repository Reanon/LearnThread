/**
 * FileName: Rece
 * Description: 模拟龟兔赛跑
 *
 * @create: 2020/11/9 17:39
 * @author Reanon
 * @version 1.0.0
 * @since 1.8
 */

package demo1;


public class Race implements Runnable {
    // winner
    private static String winner;

    public static void main(String[] args) {
        Race race = new Race();
        new Thread(race, "兔子").start();
        new Thread(race, "乌龟").start();
    }

    @Override
    public void run() {

        for (int i = 0; i <= 100; i++) {
            // 模拟兔子休息
            if (Thread.currentThread().getName().equals("兔子") && i % 10 == 0) {
                //
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 判断比赛是否结束
            boolean flag = gameOver(i);

            if (flag) {
                break;
            }
            System.out.println(Thread.currentThread().getName() + " -->run " + i + " steps");
        }
    }

    /**
     * 判断是否完成比赛
     *
     * @param steps
     * @return boolean
     */
    private boolean gameOver(int steps) {
        if (winner != null) {
            return true;
        } else {
            if (steps >= 100) {
                winner = Thread.currentThread().getName();
                System.out.println("winner is " + winner);
            }
        }
        return false;
    }
}
