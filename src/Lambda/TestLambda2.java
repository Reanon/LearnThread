/**
 * FileName: TestLambda2
 * Description:
 *
 * @create: 2020/11/9 19:26
 * @author Reanon
 * @version 1.0.0
 * @since 1.8
 */

package Lambda;

public class TestLambda2 {
    public static void main(String[] args) {
        // 接口对象
        ILove  love = null;
        // 1.lambda表示简化
        love = (int a) ->{
            System.out.println("i love you -->" + a);
        };

        //简化1：参数类型
        love = (a) ->{
            System.out.println("i love you -->" + a);
        };
        //简化2：简化括号
        love = a ->{
            System.out.println("i love you -->" + a);
        };
        //简化3：简化花括号
        // lambda表达式只能有一行代码的情况下才能简化成为一行，如果有多行，那么就用代码块包裹。
        love = a -> System.out.println("i love you -->" + a);

        love.love(521);
    }
}

// 函数式接口：只有一个抽象方法
interface ILove{
    void love(int a);
}

// 实现类
class Love implements ILove{

    @Override
    public void love(int a) {
        System.out.println("i love you --> " + a);
    }
}