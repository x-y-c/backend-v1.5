//package yangchen.exam.repo;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@SpringBootTest
//@RunWith(SpringJUnit4ClassRunner.class)
//public class StudentRepoTest {
//
//    @Autowired
//    private StudentRepo studentRepo;
//
//    @Test
//    public void test() {
//        List<String> grades = new ArrayList<>();
//        grades.add("计科1601");
//        grades.add("网工1501");
//        System.out.println(studentRepo.getStudentIdByGrade(grades).toString());
//    }
//
//}