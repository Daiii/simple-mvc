import cn.self.zhangbo.service.IndexService;
import cn.self.zhangbo.service.impl.IndexServiceImpl;

public class MainTest {

    public static void main(String[] args) {
        System.out.println(IndexService.class.isInstance(new IndexServiceImpl()));
    }
}
