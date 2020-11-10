/**
 * FileName: UnsafeList
 * Description: 不安全的集合
 *
 * @create: 2020/11/10 15:29
 * @author Reanon
 * @version 1.0.0
 * @since 1.8
 */

package syn;


import java.util.ArrayList;
import java.util.List;

public class UnsafeList {
    public static void main(String[] args) throws InterruptedException {
        List<String> list = new ArrayList<String>();
        // 循环开启10000个线程
        for (int i = 0; i < 10000; i++) {
            new Thread(()->{
                list.add(Thread.currentThread().getName());
            }).start();
        }
        Thread.sleep(3000);
        System.out.println(list.size());
    }
}