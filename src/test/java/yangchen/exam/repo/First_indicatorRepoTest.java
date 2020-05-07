package yangchen.exam.repo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import yangchen.exam.entity.First_indicator;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class First_indicatorRepoTest {

    @Autowired
    private First_indicatorRepo first_indicatorRepo;

    @Test
    public void findAll(){
        System.out.println(first_indicatorRepo.findAll());
    }

    @Test
    public void save(){
        First_indicator first_indicator = new First_indicator();
        first_indicator.setFirstid(15);
        first_indicator.setContent("test");
        First_indicator first_indicator1 = first_indicatorRepo.save(first_indicator);
        System.out.println(first_indicator1);
    }
}