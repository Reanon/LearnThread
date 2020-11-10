/**
 * FileName: UnsafeBank
 * Description: 不安全的银行
 *
 * @create: 2020/11/10 15:11
 * @author Reanon
 * @version 1.0.0
 * @since 1.8
 */

package syn;


public class UnsafeBank {
    public static void main(String[] args) {
        SafeAccount account = new SafeAccount(100,"基金");
        SafeDrawing you = new SafeDrawing(account,50,"you");
        SafeDrawing wo = new SafeDrawing(account,100,"wo");

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
    SafeAccount account;
    int drawingMoney;
    int nowMoney;

    public Drawing(SafeAccount account, int drawingMoney, String name) {
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