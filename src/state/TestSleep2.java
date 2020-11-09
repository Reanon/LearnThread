/**
 * FileName: TestSleep2
 * Description:
 *
 * @create: 2020/11/9 20:24
 * @author Reanon
 * @version 1.0.0
 * @since 1.8
 */

package state;


import java.text.SimpleDateFormat;
import java.util.Date;

public class TestSleep2 {
    public static void main(String[] args) {
        //打印系统当前时间
        Date startTime = new Date(System.currentTimeMillis());
        while (true) {
            try {
                Thread.sleep(1000);
                System.out.println(new SimpleDateFormat("HH:mm:ss").format(startTime));
                //更新当前时间
                startTime = new Date(System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void tenDown() throws InterruptedException {
        int num = 10;
        while (true) {
            Thread.sleep(1000);
            System.out.println(num--);
            if (num <= 0) {
                break;
            }
        }

    }
}
