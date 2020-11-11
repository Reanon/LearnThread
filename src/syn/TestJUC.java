/**
 * FileName: TestJUC
 * Description: 测试JUC安全类型集合
 *
 * @create: 2020/11/10 16:42
 * @author Reanon
 * @version 1.0.0
 * @since 1.8
 */

package syn;

import java.util.concurrent.CopyOnWriteArrayList;

// 测试JUC安全类型集合
public class TestJUC {
    public static void main(String[] args) {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                list.add(Thread.currentThread().getName());
            }).start();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(list.size());
    }
}
