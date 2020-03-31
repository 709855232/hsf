import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 @author mht
 @date 2019/9/17 15:02 */
public class test {

    ApplicationContext ac;

    @Before
    public void init(){
        ac = new ClassPathXmlApplicationContext("spring.xml");
    }

    @Test
    public void test(){
        //Object bean = ac.getBean("teacher");
        Object bean = ac.getBean("animal");
        System.out.println(bean);

        System.out.println(ac);
    }
}
