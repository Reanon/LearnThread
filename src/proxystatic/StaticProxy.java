/**
 * FileName: StaticProxy
 * Description: 静态代理
 *
 * @create: 2020/11/9 18:44
 * @author Reanon
 * @version 1.0.0
 * @since 1.8
 */

package proxystatic;

interface Marry {
    void happyMarry();
}

public class StaticProxy {
    public static void main(String[] args) {
        WeddingCompany company = new WeddingCompany(new You());
        company.happyMarry();
    }

}

class You implements Marry {
    @Override
    public void happyMarry() {
        System.out.println("结婚了真开心！");
    }
}

//代理角色，帮助你结婚
class WeddingCompany implements Marry {

    // 结婚对象
    private Marry target;

    public WeddingCompany(Marry target) {
        this.target = target;
    }

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