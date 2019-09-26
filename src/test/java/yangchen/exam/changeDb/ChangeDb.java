//package yangchen.exam.changeDb;
//
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import yangchen.exam.entity.QuestionNew;
//import yangchen.exam.entity.Questions;
//import yangchen.exam.repo.QuestionNewRepo;
//import yangchen.exam.repo.QuestionRepo;
//import yangchen.exam.repo.QuestionsRepo;
//
//import java.util.List;
//
//@SpringBootTest
//@RunWith(SpringJUnit4ClassRunner.class)
//public class ChangeDb {
//    @Autowired
//    private QuestionsRepo questionsRepo;
//    @Autowired
//    private QuestionNewRepo questionNewRepo;
//
//    @Autowired
//    private QuestionRepo questionRepo;
//
//    @Test
//    public void test() {
//
//        List<Questions> all = questionsRepo.findAll();
//        for (Questions questions : all) {
//            if (!questions.getChecked().equals(String.valueOf(100002))) {
//                QuestionNew questionNew = new QuestionNew();
//                questionNew.setAddTime(questions.getAddTime());
//                questionNew.setAnswer(questions.getAnswer());
//                questionNew.setCustomBh(questions.getCustomBh());
//                questionNew.setDifficulty(questions.getDifficulty().toString());
//                questionNew.setEndTag(questions.getEndTag());
//                questionNew.setQuestionBh(questions.getQuestionBh());
//                questionNew.setQuestionDetails(questions.getDescription());
//                questionNew.setQuestionName(questions.getName());
//                questionNew.setQuestionType(questions.getQuestionType());
//                questionNew.setSourceCode(questions.getSourceCode());
//                questionNew.setStage(questions.getStage().toString());
//                questionNew.setStartTag(questions.getStartTag());
//                questionNew.setIsProgramBlank(questions.getIsProgramBlank());
//                questionNew.setMemo(questions.getMemo());
//                if (questions.getChecked().equals("100001")){
//                    questionNew.setActived(Boolean.TRUE);
//                }
//                else {
//                    questionNew.setActived(Boolean.FALSE);
//                }
//                questionNewRepo.save(questionNew);
//            }
//        }
//
//    }
//
//
////    @Test
////    public void updateMemo() {
////        List<Questions> all = questionsRepo.findAll();
////        for (Questions questions : all) {
////            if ("100001".equals(questions.getIsProgramBlank())) {
////
////                QuestionNew byQuestionBh = questionRepo.findByQuestionBh(questions.getQuestionBh());
////                if (byQuestionBh != null) {
////                    byQuestionBh.setMemo(questions.getMemo());
////                    questionRepo.save(byQuestionBh);
////                }
////            }
////        }
////
////    }
//}
