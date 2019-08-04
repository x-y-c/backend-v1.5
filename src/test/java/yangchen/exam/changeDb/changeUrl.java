//package yangchen.exam.changeDb;
//
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import yangchen.exam.entity.QuestionNew;
//import yangchen.exam.repo.QuestionRepo;
//import yangchen.exam.util.UrlImageUrl;
//
//import java.util.List;
//
//@SpringBootTest
//@RunWith(SpringJUnit4ClassRunner.class)
//public class changeUrl {
//    @Autowired
//    private QuestionRepo questionRepo;
//
//    @Test
//    public void test() {
//        List<QuestionNew> all = questionRepo.findAll();
//        for (QuestionNew questionNew : all) {
//            String url = questionNew.getQuestionDescription();
//            String s = UrlImageUrl.setImagesDomain(url);
//            questionNew.setQuestionDetails(s);
//            questionRepo.save(questionNew);
//            System.out.println(s);
//
//        }
//    }
//}
