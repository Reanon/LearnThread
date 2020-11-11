
## Commit图例

| 序号 | emoji              | 在本项目中的含义                                             | 简写标记             |
| ---- | ------------------ | ------------------------------------------------------------ | -------------------- |
| (0)  | :tada:             | 初始化项目::                                                 | `:tada:`             |
| (1)  | :memo:             | 更新文档，包括但不限于README                                 | `:memo:`             |
| (2)  | :bulb:             | 发布新的阅读笔记 <sub>**(注1)**</sub>                        | `:bulb:`             |
| (3)  | :sparkles:         | 增量更新阅读笔记                                             | `:sparkles:`         |
| (4)  | :recycle:          | 重构，主要指修改已有的阅读笔记，极少情形下会修改源码 <sub>**(注2)**</sub> | `:recycle:`          |
| (5)  | :pencil2:          | 校对，主要指更正错别字、调整源码分组、修改源码排版等         | `:pencil2:`          |
| (6)  | :white_check _mark: | 发布测试文件                                                 | `:white_check_mark:` |

# JAVA 多线程笔记



参考狂神bilibli视频地址：https://www.bilibili.com/video/BV1V4411p7EF/

![image-20201109164159992](https://aliyun-typora-img.oss-cn-beijing.aliyuncs.com/imgs/20201109164200.png)

## 一、基本概念

Process和Thread

1. 程序：指令和数据的有序集合，其本身没有任何运行的含义，是一个静态的概念。
2. 进程：是执行程序的一次执行过程，是一个动态的概念。是系统资源分配的单位。
3. 线程：CPU调度和执行的单位。

 ==一个进程可以包含有多个线程==（如视频中同时听到声音、看到图像，还可以看弹幕）；一个进程**至少**有一个线程，否则无存在的意义。

注意：很多多线程是模拟出来，真正的多线程是指有多个CPU，即多核，如服务器。如果是模拟出来的多线程，即在一个CPU的情况下，在同一个时间点，cpu只能执行一个代码，因为切换很快，所以就有同时执行的错觉。

## 二 、核心概念

- 线程就是独立的执行路径；
- 在程序运行时，即使没有自己创建线程，后台也会有多个线程，如==主线程，gc线程==；
- ==main()称为主线程，为系统的入口，用于执行整个程序==；
- 在一个进程中，如果开辟了多个线程，线程的运行由调度器安排调度，调度器是与操作系统紧密相关的，先后顺序是不能认为干预的；
- 对同一份资源操作时，会存在资源抢夺问题，需要加入并发控制；
- 线程会带来额外的开销，如CPU调度时间，并发控制开销；
- 每个线程在自己的工作内存交互，内存控制不当会造成数据不一致

## 三、线程创建

![image-20201109164910599](https://aliyun-typora-img.oss-cn-beijing.aliyuncs.com/imgs/20201109164910.png)

### 1、继承Thread类,重写run方法

查看[Thread类](https://www.matools.com/api/java8)的API文档

a.创建==Thread类==

b.重写==run()==方法

c.调用==start()==开启线程

总结：**线程开启不一定立即执行，由CPU调度执行**

```java
//创建线程方式一：继承Thread类，重写run()方法，调用start()开启线程
public class TestThread1 extends Thread {
    @Override
    public void run() {
        // run方法线程体
        for (int i = 0; i < 20; i++) {
            System.out.println("我在看代码--" + i);
        }
    }
    
    public static void main(String[] args) {
        //main线程，主线程

        //创建一个Thread子类线程对象
        TestThread1 testThread1 = new TestThread1();
        //调用start()方法开启线程, 执行run()方法
        testThread1.start();
        // 执行main主线程的print方法
        for (int i = 0; i < 20; i++) {
            System.out.println("我在学习多线程--" + i);
        }
    }
}
```

练习：网图下载

### 2、实现Runnable接口

a.实现==Runnable接口==

b.重写==run(）==方法

c.执行线程需要创建**runnable接口实现类**，调用==start()==方法

```java
// 创建线程方式﹔实现runnable接口,重写run方法，执行线程需要丢入runnable接口实现类。调用start方法。
public class TestThread3 implements Runnable {
    // 重写run方法
    @Override
    public void run() {
        // run方法线程体
        for (int i = 0; i < 20; i++) {
            System.out.println("我在看代码--" + i);
        }
    }
    
    public static void main(String[] args) {
        // 创建runnable接口的实现类对象
        TestThread3 testThread3 = new TestThread3();
        // 创建线程对象，通过线程对象来开启线程，代理
        // 可以简写为：new Thread(testThread3).start() 
        Thread thread = new Thread(testThread3);
        thread.start();
        
        for (int i = 0; i < 200; i++) {
            System.out.println("我在学习多线程--" + i);
        }
    }
}
```

### 小结：以上两者区别

- 继承Thread类：
  - 子类继承Thread类具备多线程能力
  - 启动线程：子类对象.start()
  - **不建议使用：避免OOP单继承局限性**
- ==实现Runnable接口==
  - 实现Runnable接口具备多线程能力
  - 启动线程：==new Thread(子类对象).start()==
  - ==推荐使用==：避免单继承局限性，方便同一个对象被多个线程使用

#### 练习：买火车票

```java
// 多线程操作同一个对象
// 发现问题:多个线程操作同一个资源的情况下,线程不安全，数据紊乱。

public class TestThread4 implements Runnable {
    // 1、定义实现Runnable 接口的类    
    //票数
    private int ticketNums = 10;
    // 2、重写run()方法
    @Override
    public void run() {
        while (true) {
            if (ticketNums <= 0) {
                break;
            }
            // 模拟延时
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + "--> get the  " + ticketNums-- + " ticket");
        }
    }

    public static void main(String[] args) {
        // 3、创建实现了Runnable的类
        TestThread4 ticket = new TestThread4();
        // 4、新建Thread接口对象：Thread(实现Runnable接口的实例)
        // 5、启动线程 .start()
        new Thread(ticket, "  小明 ").start();
        new Thread(ticket, "  老师 ").start();
        new Thread(ticket, " 黄牛党 ").start();
    }
}
```

#### 练习：龟兔赛跑

- 获得线程名：==Thread.currentThread().getName()== 

1. 首先来个赛道距离，然后要离终点越来越近
2. 判断比赛是否结束
3. 打印出胜利者
4. 龟兔赛跑开始
5. 故事中是乌龟赢的，兔子需要睡觉，所以我们来模拟兔子睡觉
6. 终于，乌龟赢得比赛

```Java
// 模拟龟兔赛跑
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
            // Thread.currentThread().getName() 获得线程名
            if (Thread.currentThread().getName().equals("兔子") && i % 10 == 0) {
                // Thread.sleep()会抛出异常，需要进行捕获
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
	// 判断是否完成比赛
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
```



### 3、实现Callable接口

- 实现Calleble接口，需要返回值类型

- 重写call()方法，需要抛出异常

- 创建执行服务

  > ExecutorService ser = Executors.newFixedThreadPool(线程数);

- 提交执行

  > Future<返回值类型> r = ser.submit(线程名)；

- 获取结果

  > 类型 res=r.get();

- 关闭服务

  > ser.shutdownNow();

总结：优点：1.可以定义返回值；2.能抛出异常

 缺点：相比而言较为麻烦

```java

```



## 四、静态代理

——多线程底层实现原理

结合线程来看：

> new Thread(t).start();
>
> new WeddingCompany(new You()).happyMarry();

- Runnable ——> Marry 接口
- Thread ---->WeddingCompany 即代理
- t ——> new You() 目标对象(真实角色)

**总结**：真实对象和代理对象都要实现同一个接口，代理对象要代理真实角色

 ==优点==：代理对象可以做很多真实对象不能做的事情；真实对象专注自己的事情

```java
//例子： 真实角色：你； 代理角色：婚庆公司； 结婚：实现结婚接口
interface Marry {
    // 定义接口
    void happyMarry();
}

public class StaticProxy {
    // 主方法
    public static void main(String[] args) {
        // 新建一个代理对象类实例，传入真实对象
        WeddingCompany company = new WeddingCompany(new You());
        company.happyMarry();
    }

}
// 真实结婚对象
// 实现Marry接口
class You implements Marry {
    // 重写接口方法
    @Override
    public void happyMarry() {
        System.out.println("结婚了真开心！");
    }
}

//代理角色，帮助你结婚
// 也需要实现Marry接口
class WeddingCompany implements Marry {

    // 结婚对象
    // 定义接口对象
    private Marry target;

    public WeddingCompany(Marry target) {
        this.target = target;
    }
	// 重写接口方法
    @Override
    public void happyMarry() {
        before();
        this.target.happyMarry();
        after();
    }

    public void after() {
        System.out.println("结婚之前，布置现场");
    }

    public void before() {
        System.out.println("结婚之后，收尾款");
    }
}
```



## 五、Lambda表达式

参考文章：[Java Lambda表达式看这一篇就够了](https://objcoding.com/2019/03/04/lambda/)

- 实质属于函数式编程的概念

>(params) ->expression[表达式]
>
>(params) ->statement[语句]
>
>(params) -> { statements }
>
>例如：new Thread (==()->System.out.printIn("多线程学习")==) .start();

### 1、作用

- 避免匿名内部类定义过多
- 使代码看起来简洁
- 简化代码，只留下核心逻辑

### 2、函数式接口

1. 理解Functional Interface(函数式接口）是学习Java8 lambda表达式的关键所在。
2. 定义：==任何接口==，如果==只包含唯一一个抽象方法==，那么它就是一个函数式接口
3. 对于函数式接口，我们可以通过lambda表达式来创建该接口的对象

```java
// 接口中只包含一个抽象方法
public interface Runnable{
	public abstract void run();
}
```

### 3、lambda简单推导

3.1 调用==实现类==的接口方法

```java
// 1.定义一个函数式接口
// 函数式接口只有一个抽象方法
interface ILike {
    void lambda();
}
// 2.实现类
class Like implements ILike {
    @Override
    public void lambda() {
        System.out.println("i like lambda!");
    }

}

public class TestLambda1 {
    // 使用接口实现类来调用接口方法
    public static void main(String[] args) {
        ILike like = new Like();
        like.lambda();
    }

}
```

3.2 使用==静态内部类==实现接口方法

```java
public class TestLambda1 {
    //3.静态内部类，在main方法外
    static class Like2 implements ILike{
        @Override
        public void lambda() {
            System.out.println("i like lambda2 !");
        }
    }
    public static void main(String[] args) {
        // 使用静态内部类调用接口
        ILike like = new Like2();
        like.lambda();
    }
}
```

3.3 调用==局部内部类==实现

```Java
public class TestLambda1 {
    public static void main(String[] args) {
        // 4.局部内部类，在main方法里
        class Like3 implements ILike {
            // 内部类实现接口
            @Override
            public void lambda() {
                System.out.println("i like lambda3 !");
            }
        }
        ILike like = new Like3();
        like.lambda();
    }
}
```

3.4 匿名内部类

- ==匿名内部类==：没有类名称，必须借助接口或父类

```Java
public class TestLambda1 {
    public static void main(String[] args) {
        // 5.匿名内部类,没有类名称，必须借助接口或父类
        // ILike就是类名
        ILike like = new ILike(){
            @Override
            public void lambda() {
                System.out.println("i like lambda4 !");
            }
        }; // 这里有个分号
        like.lambda();
    }
}

```

3.5 lambda简化

```java
public class TestLambda1 {
    public static void main(String[] args) {
        // 6.用lambda简化
        ILike like = () ->{
            System.out.println("i like lambda 5 !");
        }; // 这里有分号
        like.lambda();
    }
}
```

### Lambda 总结

- lambda表达式只能有一行代码的情况下才能简化成为一行，如果==有多行，那么就用代码块包裹==。
- lambda表达式的前提是==接口为函数式接口==。
  - 函数式接口：只包含唯一一个抽象方法
- 多个参数也可以去掉参数类型，要去掉就都去掉，==必须加上括号==。

#### 新建一个线程

下面述代码给`Tread`类传递了一个匿名的`Runnable`接口对象，重载`Runnable`接口的`run()`方法来实现相应逻辑。

```Java
// JDK7 匿名内部类写法
new Thread(new Runnable(){// 接口名
	@Override
	public void run(){// 方法名
		System.out.println("Thread run()");
	}
}).start();
```

上在Java 8中可以简化为如下形式：

```java
// JDK8 Lambda表达式写法
new Thread(
		() -> System.out.println("Thread run()")// 省略接口名和方法名
).start();

// JDK8 Lambda表达式代码块写法
new Thread(
        () -> {
            System.out.print("Hello");
            System.out.println(" Hoolee");
        }
).start();
```

#### 练习lambda

```Java
public class TestLambda2 {
    public static void main(String[] args) {
        // 接口对象
        ILove  love = null;
        // 1.lambda表示简化，传递一个接口方法
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
```



## 六、线程停止

### 1、线程状态

![image-20201109193757527](https://aliyun-typora-img.oss-cn-beijing.aliyuncs.com/imgs/20201109193757.png)

![image-20201109194214279](https://aliyun-typora-img.oss-cn-beijing.aliyuncs.com/imgs/20201109194214.png)

### 2、线程方法

>setPriority(int néwPriority)：更改线程的优先级
>
>static void sleep(long millis)：在指定的毫秒数内让当前正在执行的线程休眠
>
>void join()：等待该线程终止
>
>static void yield()：暂停当前正在执行的线程对象，并执行其他线程
>
>void interrupt()：中断线程，别用这个方式
>
>boolean isAlive()：测试线程是否处于活动状态

### 3、线程停止的方法

- 建议线程正常停止——>利用次数，不建议死循环
- ==建议使用标志位==——>设置一个标志位，当flag = false，则终止线程运行
- 不要使用stop或者destroy等过时或者JDK不建议使用的方法

```java
// 实现Runnable接口的类
public class TestStop implements Runnable {
    // 1.设置一个标志位
    private boolean flag = true;

    //2.设置一个公开的方法停止线程，转换标志位
    public void stop() {
        this.flag = false;
    }
    // 实现Runnable接口方法
    @Override
    public void run() {
        int i = 0;
        while (flag) {
            System.out.println("run....Thread" + i);
        }
    }
    public static void main(String[] args) {
        TestStop testStop = new TestStop();
        // 使用实现Runnable接口的类实例初始化Thread,然后启动线程
        new Thread(testStop).start();
        for (int i = 0; i < 1000; i++) {
            System.out.println("main" + i);
            if (i == 900) {
                // 调用stop方法切换标志位，让线程停止
                testStop.stop();
                System.out.println("线程停止了");
            }
        }
    }
}
```

> 打印结果：
>
> ...
> run....Thread0
> run....Thread0
> 线程停止了 // 子线程停止了
> main901  // 主线程还在运行
> main902
> ...

## 七、线程休眠 - sleep()

- sleep(时间)指定当前线程阻塞的毫秒数；
- sleep存在异常InterruptedException；
- sleep时间达到后线程进入就绪状态；
- sleep可以模拟网络延时，倒计时等；
- 每一个对象都有一个锁，==sleep不会释放锁==。

#### 练习1：模拟延时

模拟网络延时的作用：放大问题的发生性

```java 
// 例子1：模拟延时：TestThread
// 模拟网络延时的作用：放大问题的发生性

public class TestSleep implements Runnable{
    //票数
    private int ticketNums = 10;
    @Override
    public void run() {
        while (true) {
            if (ticketNums <= 0) {
                break;
            }
            // 模拟延时
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "-->拿到了第" + ticketNums-- + "张票");
        }
    }
    public static void main(String[] args) {
        TestSleep ticket = new TestSleep();
        new Thread(ticket, " 小明 ").start();
        new Thread(ticket, " 老师 ").start();
        new Thread(ticket, "黄牛党").start();
    }
}
```

#### 练习2： 模拟倒计时

```java
public class TestSleep2 {
    public static void main(String[] args) {
        // 在这里捕获Thread.sleep()抛出的异常
        try {
            tenDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    // throws抛出异常
    public static void tenDown() throws InterruptedException {
        int num = 10;
        while(true){
            // 对可能的异常不做处理，直接抛出
            Thread.sleep(1000);
            System.out.println(num--);
            if(num<=0){
                break;
            }
        }

    }
}
```

#### 练习3：打印系统时间

- ==new SimpleDateFormat("HH:mm:ss").format(startTime)==：格式化系统时间

```java
public class TestSleep2 {
    public static void main(String[] args) {
        //打印系统当前时间
        Date startTime = new Date(System.currentTimeMillis());
        while (true) {
            try {
                Thread.sleep(1000);
                // 使用SimpleDateFormat格式化输出时间
                System.out.println(new SimpleDateFormat("HH:mm:ss").format(startTime));
                //更新当前时间
                startTime = new Date(System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
```

## 八、线程礼让 - yield()

- 礼让线程，让当前正在执行的线程暂停，但不阻塞；
- 将线程从运行状态转为就绪状态;
- 让CPU重新调度，==礼让不一定成功==，看CPU心情。

```java
public class TestYield {
    public static void main(String[] args) {
        MyYeild myYeild = new MyYeild();
        new Thread(myYeild, "a").start();
        new Thread(myYeild, "b").start();
    }
}
class MyYeild implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "线程正在执行");
        //礼让
        Thread.yield();
        System.out.println(Thread.currentThread().getName() + "线程停止执行");
    }
}
```

>无礼让情况：
>a线程正在执行
>a线程停止执行
>b线程正在执行
>b线程停止执行
>
>礼让成功情况下：
>a线程正在执行
>b线程正在执行
>b线程停止执行
>a线程停止执行
>
>

## 九、线程强制执行 - join()

- join合并线程，待此线程执行完后，再执行其它线程，其它线程阻塞；
- 可以想象成插队

```java
public class TestJoin implements Runnable {
    public static void main(String[] args) throws InterruptedException {
        //创建runnable接口实现类
        TestJoin testJoin = new TestJoin();
        // 通过线程对象来开启线程代理
        Thread thread = new Thread(testJoin);
        // 调用start方法
        thread.start();

        // 主线程：main
        for (int i = 0; i < 500; i++) {
            if (i == 200) {
                // 调用线程插队.join()
                thread.join();
            }
            System.out.println("main" + i);
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println("线程Vip来了" + i);
        }
    }
}
```

## 十、观测线程状态

查看Java API ：[Thread.State](https://www.matools.com/api/java8)

- /观察状态：==Thread.State== state = ==thread.getState()==

 线程可以处于以下状态之一：

- NEW：尚未启动的线程处于此状态。
- RUNNABLE：在Java虚拟机中执行的线程处于此状态。
- BLOCKED：被阻塞等待监视器锁定的线程处于此状态。
- WAITING：正在等待另一个线程执行特定动作的线程处于此状态。
- TIMED_WAITING：正在等待另一个线程执行动作达到指定等待时间的线程处于此状态。
- TERMINATED：已退出的线程处于此状态。

![image-20201109194214279](https://aliyun-typora-img.oss-cn-beijing.aliyuncs.com/imgs/20201109212458.png)

一个线程可以在给定时间点处于一个状态。 这些状态是不反映任何操作系统线程状态的虚拟机状态。

```java
public class TestState {
    public static void main(String[] args) throws InterruptedException {
        // 使用lambda表达式实现Runnable接口
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
```

> NEW
> TIMED_WAITING
> TIMED_WAITING
> TIMED_WAITING
> TIMED_WAITING
> RUNNABLE
> ///////
> TERMINATED
>
> //再次启动会报错
> Exception in thread "main" java.lang.IllegalThreadStateException
> 	at java.lang.Thread.start(Thread.java:708)
> 	at state.TestState.main(TestState.java:42)
>
> Process finished with exit code 1

## 十一、线程的优先级

- Java提供一个线程调度器来监控程序中启动后进入就绪状态的所有线程，线程调度器按照优先级来决定应该调度哪个线程来执行。
- 线程的优先级用数字表示，范围从1~10.
  - Thread.MIN_PRIORITY = 1;
  - Thread.MAX_PRIORITY = 10;
  - Thread.NORM_PRIORITY = 5;
- 使用一下方法改变或获取优先级
  -  ==getPriority()==
  -  ==setPriority(int xxx)==

```java
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
```

**总结**：==优先级低只是意味着获得调度的概率低==，并不是优先级低就不会被调用了，有可能优先级低会被先执行；这都看CPU的调度。

## 十二、守护（daemon）线程

- 线程分为**用户线程**和**守护线程**
- 虚拟机必须确保==用户线程==执行完毕 （如，main）
- 虚拟机不用等待==守护线程==执行完毕 （如，后台记录操作日志，监控内存，垃圾回收……）
- 设置为守护进程：`thread.setDaemon(true)`

```java
public class TestDaemon {
    public static void main(String[] args) {
        God god = new God();
        You you = new You();
        Thread thread = new Thread(god);
        // 默认false表示是用户线程，正常的线程都是用户线程
        // true就是设置为守护进程
        thread.setDaemon(true);

        // 守护线程启动: 上帝
        thread.start();
        // 用户线程启动:你，传入实现Runnable接口的类实例
        new Thread(you).start();

    }
}
//上帝
class God implements Runnable {
    @Override
    public void run() {
        while (true) {
            System.out.println("上帝保佑着你");
        }
    }
}
//你
class You implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 36500; i++) {
            System.out.println("你一生都开心的活着");
        }
        System.out.println("======good Bye======");
    }
}
```

## 十三、线程同步机制

==并发==：**同一个对象**被**多个线程**同时操作 （如：上万人同时抢票）

> 处理多线程问题时，多线程访问同一个对象(并发)，并且某些线程还想修改这个对象。这时则需要线程同步。
>
> ==线程同步其实是一种等待机制==，多个需要同时访问此对象的线程进入这个==对象的等待池==形成队列，等待前面的线程使用完毕，下一个线程再使用。

> 线程同步的形成条件：==队列+锁==

### 队列与锁

>由于同一进程的多个线程共享同一块存储空间，在带来方便的同时，也带来了访问冲突问题，为了保证数据在方法中被访问时的正确性，在访问时加入==锁机制synchronized==，当一个线程获得对象的排它锁，独占资源，其他线程必须等待，使用后释放锁即可。
>
>存在以下问题:
>
>1. 一个线程持有锁会导致其他所有需要此锁的线程挂起；
>2. 在多线程竞争下，加锁、释放锁会导致比较多的上下文切换和调度延时，引起性能问题;
>3. 如果一个优先级高的线程等待一个优先级低的线程释放锁会导致优先级倒置，引起性能问题。

## 十四、三大不安全案例

### 1、不安全的买票

```java
// 线程不安全 有负数
class BuyTicket implements Runnable {
    // 设置标志位来外部停止方法
    boolean flag = true;
    private int ticketNums = 10;

    @Override
    public void run() {
        while (flag) {
            buy();
        }
    }

    //判断是否有票
    private void buy() {
        if (ticketNums <= 0) {
            flag = false;
            return;
        }
        // 模拟延时
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "拿到" + ticketNums--);
    }
}

public class UnsafeBuyTicket {
    public static void main(String[] args) {
        BuyTicket station = new BuyTicket();
        new Thread(station, "小明").start();
        new Thread(station, "老师").start();
        new Thread(station, "黄牛").start();
    }
}
```

> ...
> 老师拿到1
> 小明拿到0
> 黄牛拿到-1  //有负数

### 2、不安全的银行

```java
// 不安全的银行
public class UnsafeBank {
    public static void main(String[] args) {
        Account account = new Account(100,"基金");
        Drawing you = new Drawing(account,50,"you");
        Drawing wo = new Drawing(account,100,"wo");

        you.start();
        wo.start();
    }
}

//账户
class Account {
    // 余额
    int money;
    // 卡名
    String name;

    public Account(int money, String name) {
        this.money = money;
        this.name = name;
    }
}

//银行：模拟取款
class Drawing extends Thread {
    Account account;
    int drawingMoney;
    int nowMoney;

    public Drawing(Account account, int drawingMoney, String name) {
        // 线程名
        super(name);
        this.account = account;
        this.drawingMoney = drawingMoney;
    }
    //取钱
    @Override
    public void run() {
        // 判断有没有钱
        if (account.money - drawingMoney < 0) {
            System.out.println(Thread.currentThread().getName() + "钱不够，取不了");
        return;
        }
        // sleep放大问题发生性
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 卡内余额 = 余额 - 取的钱
        account.money = account.money - drawingMoney;
        // 手里的钱
        nowMoney = nowMoney + drawingMoney;
        System.out.println(account.name + "余额为：" + account.money);
        // Thread.currentThread().getName() = this.getName()
        System.out.println(this.getName() + "手里的钱为：" + nowMoney);
    }
}
```

> 基金余额为：-50
> 基金余额为：-50
> you手里的钱为：50
> wo手里的钱为：100

### 3、不安全的集合

```java
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
```

> 9997  // 说明有三个同时操作List,导致冲突

## 十五、同步方法和同步方法块

> ◆由于我们可以通过private关键字来保证数据对象只能被方法访问，所以我们只需要针对方法提出一套机制，这套机制就是==synchronized关键字==,它包括两种用法：synchronized方法和synchronized块

### 同步方法

- 同步方法: public ==synchronized== void method(int args) }

> ◆==synchronized方法==控制对“对象”的访问，每个对象对应一 把锁，每个synchronized方法都必须获得调用该方法的对象的锁才能执行，否则线程会阻塞，方法一旦执行，就独占该锁，直到该方法返回才释放锁，后面被阻塞的线程才能获得这个锁，继续执行。
> **缺陷**：若将一个大的方法申明为synchronized将会影响效率

> 方法里需要修改的内容才需要锁，否则浪费资源

### 同步块

- 同步块: synchronized ==(Obj){}==

> ◆==Obj== 称之为同步监视器
> ◆Obj 可以是任何对象，但是推荐使用共享资源作为同步监视器
> ◆同步方法中无需指定同步监视器，因为==同步方法的同步监视器就是this==，就是这个对象本身，或者是class [反射中讲解]
> ◆同步监视器的执行过程
>
> 1. 第一个线程访问，锁定同步监视器，执行其中代码。
> 2. 第二个线程访问，发现同步监视器被锁定，无法访问。
> 3. 第一个线程访问完毕，解锁同步监视器。
> 4. 第二个线程访问， 发现同步监视器没有锁,然后锁定并访问。

### 修改三个不安全案例为安全：

#### 1.买票

其他部分保持不变，只需要修改buy方法为==同步方法==

```Java
// 加上synchronized关键字，即变为同步方法，默认锁的是this
private synchronized void buy() {
    //判断是否有票
    if (ticketNums <= 0) {
        flag = false;
        return;
    }
    // 模拟延时
    try {
        Thread.sleep(200);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    System.out.println(Thread.currentThread().getName() + "拿到" + ticketNums--);
}
```

> 结果：
>
> 小明拿到10
> 小明拿到9
> 小明拿到8
> 小明拿到7
> 小明拿到6
> 小明拿到5
> 小明拿到4
> 小明拿到3
> 黄牛拿到2
> 黄牛拿到1



#### 2.银行

使用synchronized同步块，锁住account，即同步监视器是account。

- ==锁的对象就是变化的量==，比如需要增删改查的量

```Java
//取钱
@Override
public void run() {
    // 锁的对象就是变化的量，比如需要增删改查的量
    // 使用synchronized 同步块，同步监视器是account
    synchronized (account){
        // 判断有没有钱
        if (account.money - drawingMoney < 0) {
            System.out.println(Thread.currentThread().getName() + "钱不够，取不了");
            return;
        }
        // sleep放大问题发生性
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 卡内余额 = 余额 - 取的钱
        account.money = account.money - drawingMoney;
        // 手里的钱
        nowMoney = nowMoney + drawingMoney;
        System.out.println(account.name + "余额为：" + account.money);
        // Thread.currentThread().getName() = this.getName()
        System.out.println(this.getName() + "手里的钱为：" + nowMoney);
    }
}
```



#### 3.list

synchronized同步块，锁住list，即同步监视器是list。

```java
public class SafeList {
    public static void main(String[] args) throws InterruptedException {
        List<String> list = new ArrayList<String>();
        // 循环开启10000个线程
        for (int i = 0; i < 10000; i++) {
            new Thread(()->{
                // 给list加上同步锁
                synchronized (list){
                    list.add(Thread.currentThread().getName());
                }
            }).start();
        }
        Thread.sleep(3000);
        System.out.println(list.size());
    }
}
```

### 测试JUC安全集合

- JUC：==java.util.concurrent==

```java
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

//输出：10000
```

## 十六、死锁

### 1.定义

> 多个线程各自占有一些共享资源，并且互相等待其他线程占有的资源才能运行，而导致两个或者多个线程都在等待对方释放资源，都停止执行的情形。 某一个同步块同时拥有“==两个以上对象的锁==”时，就可能会发生“死锁”的问题。

```java
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
class Lipstick {}
//镜子
class Mirror {}
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
                //一秒钟后想获得镜子的锁
                synchronized (mirror) {
                    System.out.println(this.getName() + "获得镜子的锁");
                }
            }
        } else {
            //获得镜子的锁
            synchronized (mirror) {
                System.out.println(this.name + "获得口红的锁");
                Thread.sleep(1000);
                //两秒钟后想获得口红的锁
                synchronized (lipstick) {
                    System.out.println(this.getName() + "获得镜子的锁");
                    System.out.println(this.getName() + "获得镜子的锁");
                }
            }
        }
    }
}
```

>白雪公主获得口红的锁
>灰姑娘获得口红的锁
>...  // 程序无法停止

#### 解决办法

把同步代码块拿出来，不两个人同时抱一把锁。

```java
private  void makeup() throws InterruptedException {
    if(choice ==0){
        synchronized (lipstick){  //获得口红的锁
            System.out.println(this.name+"获得口红的锁");
            Thread.sleep(1000);
        }
        //从同步代码块拿出来
        synchronized (mirror){  //一秒钟后想获得镜子的锁
            System.out.println(this.name+"获得镜子的锁");
        }
    }else{
        synchronized (mirror){  //获得镜子的锁
            System.out.println(this.name+"获得镜子的锁");
            Thread.sleep(2000);
        }
        //从同步代码块拿出来
        synchronized (lipstick) {  //两秒钟后想获得口红的锁
            System.out.println(this.name + "获得口红的锁");
        }
    }
}
```

>灰姑娘获得口红的锁
>白雪公主获得镜子的锁
>灰姑娘获得镜子的锁
>白雪公主获得口红的锁
>// 可以正常结束

### **2.死锁避免方法**

- 产生死锁的四个必要条件：
  - 互斥条件：一个资源每次只能被一个进程使用。
  - ==请求与保持条件==：一个进程因请求资源而阻塞时，对已获得的资源保持不放。
  - 不剥夺条件：进程已获得的资源，在未使用完之前不能强行剥夺。
  - 循环等待条件：若干进程之间形成一种头尾相接的循环等待资源关系。

只要破坏其中一个。就可以避免死锁。

## 十七、LOCK

> ◆ 从JDK 5.0开始，Java提供了更强大的线程同步机制——通过==显式定义同步锁对象来实现同步==。同步锁使用==Lock对象==充当;
>
> ◆  ==java.util.concurrent.locks.Lock接口==是控制多个线程对共享资源进行访问的工具。锁提供了对共享资源的独占访问，每次只能有一个线程对Lock对象加锁，线程开始访问共享资源之前应先获得Lock对象 
>
> ◆ ==ReentrantLock类==实现了Lock ，它拥有与synchronized相同的并发性和内存语义，在实现线程安全的控制中，比较常用的是ReentrantLock，可以显式加锁、释放锁。

![image-20201111155650867](https://aliyun-typora-img.oss-cn-beijing.aliyuncs.com/imgs/20201111155650.png)

不安全的买票例子说明：

```java
// 测试Lock 锁
public class TestLock {
    public static void main(String[] args) {
        Lock testLock = new Lock();
        new Thread(testLock).start();
        new Thread(testLock).start();
        new Thread(testLock).start();

    }
}

class Lock implements Runnable {
    // 定义lock锁
    private final ReentrantLock lock = new ReentrantLock();
    int ticketNums = 10;

    @Override
    public void run() {
        while (true) {
            // 加锁
            lock.lock();
            try {
                if (ticketNums > 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "-->拿到了第" + ticketNums-- + "票");
                } else {
                    break;
                }
            } finally {
                // 解锁
                lock.unlock();
            }
        }
    }
}
```

注意：如果需要抛出异常，==unlock()需要写在finally{}块中==



### synchronized与Lock对比

> ◆ ==Lock是显式锁==(手动开启和关闭锁，别忘记关闭锁) ==synchronized是隐式锁==， 出了作用域自动释放
>
> ◆ Lock只有代码块锁，synchronized有代码块锁和方法锁
>
> ◆ 使用Lock锁，JVM将花费较少的时间来调度线程，性能更好。并且具有更好的扩展性(提供更多的子类)
>
> ◆ 优先使用顺序:
> Lock > 同步代码块(已经进入了方法体，分配了相应资源) > 同步方法(在方法体之外)

## 十八、线程协作 - 生产者消费者模式

### 1、线程通信

Java提供了几个方法解决线程之间的通信问题。

- ==wait()==：表示线程一直**等待**，直到其他线程通知，与sleep不同，**会释放锁**
- wait(long timeout)：指定等待的毫秒数
- ==notify()==：**唤醒**一个处于等待状态的线程
- ==notifyAll()==：唤醒同一个对象上所有调用wait()方法的线程，优先级别高的线程优先调度

注意：均是Object类的方法，都==只能在同步方法或者同步代码块==中使用，否则会抛出异常IlegalMonitorStateException

#### 1. 应用场景:生产者和消费者问题

![Java并发-- 生产者-消费者模式| 点滴积累](https://aliyun-typora-img.oss-cn-beijing.aliyuncs.com/imgs/20201111160212.png)

◆ 假设仓库中只能存放一件产品，生产者将生产出来的产品放入仓库,消费者将仓库中产品取走消费。

◆ 如果仓库中没有产品，则生产者将产品放入仓库，否则停止生产并等待，直到仓库中的产品被消费者取走为止。

◆ 如果仓库中放有产品，则消费者可以将产品取走消费，否则停止消费并等待，直到仓库中再次放入产品为止。

#### 2. 分析

这是一个线程同步问题，生产者和消费者共享同一个资源，并且==生产者和消费者之间相互依赖，互为条件==。

- 对于生产者，没有生产产品之前，要通知消费者等待。而生产了产品之后又需要马上通知消费者消费
- 对于消费者，在消费之后，要通知生产者已经结束消费，需要生产新的产品以供消费。
- 在生产者消费者问题中，仅有synchronized是不够的：
  - synchronized可阻止并发更新同一个共享资源，实现了同步
  - synchronized不能用来实现不同线程之间的消息传递(通信)



### 2、管程法

**解决方式**：==并发协作模型==“生产者/消费者模式”–>管程法

◆ 生产者：负责生产数据的模块(可能是方法，对象，线程，进程);
◆ 消费者：负责处理数据的模块(可能是方法，对象，线程，进程);
◆ 缓冲区：消费者不能直接使用生产者的数据，他们之间有个“缓冲区”

**生产者将生产好的数据放入缓冲区，消费者从缓冲区拿出数据**

![Java实现生产者消费者问题的多种方式| Harries Blog™](https://lh3.googleusercontent.com/proxy/H3t16F_UouoYoUAiC7cr6VKhH2HsePvOCaL6ftAOTg9XZJAEX66DVCkAY8mwBsnXdDO0Rz6ezMu16CVC34iSFtotoqFvi9_er68QS_-4henNJLz80F4)

#### 生产者

```java
// 测试生产者消费者模型 --> 利用缓冲区解决：管程法
// 生产者
class Producer extends Thread {
    SynContainer container;

    public Producer(SynContainer container) {
        this.container = container;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            container.push(new Chicken(i));
            System.out.println("生产了第" + i + "只鸡");
        }
    }
}
```

#### 消费者

```java
//消费者
class Consumer extends Thread {
    SynContainer container;

    public Consumer(SynContainer container) {
        this.container = container;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println("消费了----第" + container.pop().id + "只鸡");
        }
    }
}
```

#### 容器

```java
//缓冲区
class SynContainer {
    //需要一个容器大小
    Chicken[] chickens = new Chicken[10];
    //容器计数器
    int count = 0;

    // 生产者放入产品
    public synchronized void push(Chicken chicken) {
        // 如果容器满了，就需要等待消费者消费
        if (count == chickens.length) {
            // 通知消费者消费，生产等待
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 如果没有满，我们就需要丢入产品
        chickens[count] = chicken;
        count++;
        //可以通知消费者消费了
        this.notifyAll();
    }

    //消费者消费产品
    public synchronized Chicken pop() {
        // 判断能否消费
        if (count == 0) {
            //通等待生产者生产
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 如果可以消费。就取出产品
        count--;
        Chicken chicken = chickens[count];

        //可以通知生产者消费了
        this.notifyAll();
        return chicken;
    }
}
```

#### 测试一下：

```java
// 测试生产者消费者模型 --> 利用缓冲区解决：管程法
public class TestPC {
    public static void main(String[] args) {
        SynContainer container = new SynContainer();

        new Producer(container).start();
        new Consumer(container).start();
    }

}

// 生产者
class Producer extends Thread {
    SynContainer container;

    public Producer(SynContainer container) {
        this.container = container;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            container.push(new Chicken(i));
            System.out.println("生产了第" + i + "只鸡");
        }
    }
}

//消费者
class Consumer extends Thread {
    SynContainer container;

    public Consumer(SynContainer container) {
        this.container = container;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println("消费了----第" + container.pop().id + "只鸡");
        }
    }
}

// 产品
class Chicken {
    int id; //产品编号

    public Chicken(int id) {
        this.id = id;
    }
}

//缓冲区
class SynContainer {
    //需要一个容器大小
    Chicken[] chickens = new Chicken[10];
    //容器计数器
    int count = 0;

    // 生产者放入产品
    public synchronized void push(Chicken chicken) {
        // 如果容器满了，就需要等待消费者消费
        if (count == chickens.length) {
            // 通知消费者消费，生产等待
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 如果没有满，我们就需要丢入产品
        chickens[count] = chicken;
        count++;
        //可以通知消费者消费了
        this.notifyAll();
    }

    //消费者消费产品
    public synchronized Chicken pop() {
        // 判断能否消费
        if (count == 0) {
            //通等待生产者生产
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 如果可以消费。就取出产品
        count--;
        Chicken chicken = chickens[count];

        //可以通知生产者消费了
        this.notifyAll();
        return chicken;
    }
}
```

> 生产了第0只鸡
> 生产了第1只鸡
> ...
> 消费了----第97只鸡
> 生产了第98只鸡
> 消费了----第98只鸡
> 生产了第99只鸡
> 消费了----第99只鸡

### 3、信号灯法

**解决方式**：==并发协作模型==“生产者/消费者模式”–>信号灯法

通过设置标志位解决

```java
//并发协作模型“生产者/消费者模式”–>信号灯法
public class TestPC2 {
    public static void main(String[] args) {
        TV tv = new TV();
        new Player(tv).start();
        new Watcher(tv).start();
    }
}

//生产者--演员
class Player extends Thread {
    TV tv;

    public Player(TV tv) {
        this.tv = tv;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                this.tv.play("快乐大本营");
            } else {
                this.tv.play("天天向上");
            }
        }
    }
}

//消费者--观众
class Watcher extends Thread {
    TV tv;

    public Watcher(TV tv) {
        this.tv = tv;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            tv.watch();
        }
    }

}

//产品--节目
class TV {
    //演员表演，观众等待  T
    //观众观看，演员等待  F
    String video;   //表演节目
    boolean flag = true;

    //表演
    // 涉及并发, 需要添加synchronized
    public synchronized void play(String video) {
        if (!flag) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("演员表演了：" + video);
        //通知观众观看
        this.video = video;
        this.notifyAll();
        this.flag = !this.flag;
    }

    //观看
    public synchronized void watch() {
        if (flag) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("观看了：" + this.video);
        //通知演员表演
        this.notifyAll();
        this.flag = !this.flag;
    }
}
```

## 十九、线程池

### 线程池

> ◆ 背景：经常创建和销毁、使用量特别大的资源，比如并发情况下的线程，对性能影响很大。
>
> ◆ 思路：提前创建好多个线程，放入线程池中，使用时直接获取，使用完放回池中。可以避免频繁创建销毁、实现重复利用。类似生活中的公共交通工具。 
>
> ◆ 好处：
>
> 1. 提高响应速度(减少了创建新线程的时间)
>
> 2. 降低资源消耗(重复利用线程池中线程，不需要每次都创建)
>
> 3. 便于==线程管理==…
>
>    corePoolSize： 核心池的大小
>
>    maximumPoolSize：最大线程数
>
>    keepAliveTime： 线程没有任务时最多保持多长时间后会终止

JDK 5.0 起提供了==线程池相关API==： ExecutorService 和Executors

> ◆ ==ExecutorService==： 真正的线程池**接口**。常见子类ThreadPoolExecutor
>
> 1. void execute(Runnable command) ：执行任务/命令，没有返回值，一般用来执Runnable
> 2. <T> Future <T> submit(Callable<T> task)：执行任务,有返回值，一般用来执行Callable
> 3. void shutdown() ：关闭连接池
>
> ◆ ==Executors==： 工具类、线程池的工厂类，用于创建并返回不同类型的线程池

### 使用线程池

```java
// 测试线程池
public class TestPool {
    public static void main(String[] args) {
        //1.创建服务，创建线程池
        //参数为线程池大小
        ExecutorService service = Executors.newFixedThreadPool(10);
        //执行
        service.execute(new MyThread());
        service.execute(new MyThread());
        service.execute(new MyThread());
        service.execute(new MyThread());
        service.execute(new MyThread());

        //2.关闭连接
        service.shutdown();
    }
}
class MyThread implements Runnable{
    @Override
    public void run() {
            System.out.println(Thread.currentThread().getName());
    }
}
```

## 二十、多线程总结

==多线程创建==方法：

1. 继承Thread类
2. 实现Runnable接口
3. 实现Callable接口

```Java
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
```

>继承Thread类
>实现Runnable接口
>实现Callable接口
>100