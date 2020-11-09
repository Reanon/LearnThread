/**
 * FileName: TestLambda1
 * Description: 推导lambda表达式
 *
 * @create: 2020/11/9 19:04
 * @author Reanon
 * @version 1.0.0
 * @since 1.8
 */

package Lambda;


import javax.sound.midi.Soundbank;

// 1.定义一个函数式接口
interface ILike {
    void lambda();
}

public class TestLambda1 {
    public static void main(String[] args) {
        // 4.局部内部类
        class Like3 implements ILike {

            @Override
            public void lambda() {
                System.out.println("i like lambda3 !");
            }
        }
        // 5.匿名内部类,没有类名称，必须借助接口或父类
        ILike like = new ILike(){
            @Override
            public void lambda() {
                System.out.println("i like lambda4 !");
            }
        };
        like.lambda();
        // 6.用lambda简化
        like = () ->{
            System.out.println("i like lambda 5 !");
        };
        like.lambda();


    }

    //3.静态内部类
    static class Like2 implements ILike {

        @Override
        public void lambda() {
            System.out.println("i like lambda2 !");
        }
    }
}

// 2.实现类
class Like implements ILike {
    @Override
    public void lambda() {
        System.out.println("i like lambda!");
    }

}