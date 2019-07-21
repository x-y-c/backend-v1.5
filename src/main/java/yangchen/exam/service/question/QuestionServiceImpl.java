package yangchen.exam.service.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.ExamPaper;
import yangchen.exam.entity.Question;
import yangchen.exam.model.QuestionInfo;
import yangchen.exam.repo.questionRepo;
import yangchen.exam.service.examination.ExaminationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author yc
 */
@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private questionRepo questionRepo;


    @Autowired
    private QuestionBaseService questionBaseService;
    @Autowired
    private ExaminationService examinationService;


    @Override
    public Question createQuestion(Question question) {
        Question save = questionRepo.save(question);
        return save;
    }

    @Override
    public Question updateQuestion(Question question) {
        Question save = questionRepo.save(question);
        return save;
    }


    //如果删除成功，则该id对应的对象不存在
    @Override
    public Boolean deleteQuestion(Integer id) {
        questionRepo.deleteById(id);
        return findQuestionById(id) == null;
    }

    @Override
    public Question findQuestionById(Integer id) {
        Optional<Question> result = questionRepo.findById(id);
        return result.get();


    }

    @Override
    public List<Question> findQuestionByCategory(String category) {
        return questionRepo.findByCategory(category);
    }

    @Override
    public List<Question> findQuestionAll() {
        return questionRepo.findAll();
    }

    @Override
    public Question getQuestionBy(Integer examinationId, Integer index) {
        ExamPaper examinationById = examinationService.getExaminationById(examinationId);

        String[] split = examinationById.getTitleId().split(",");
        Optional<Question> byId = questionRepo.findById((Integer.valueOf(split[index])));
        return byId.get();
    }

    @Override
    public List<QuestionInfo> getQuestionNamesByExamPage(Integer examPageId) {
        ExamPaper exampaperByExamPaper = examinationService.getExampaperByExamPaper(examPageId);
        List<QuestionInfo> examNameList = new ArrayList<>();
        String titleId = exampaperByExamPaper.getTitleId();
        String[] split = titleId.split(",");
        for (int i=0;i<split.length;i++) {
            Question question = questionBaseService.getQuestionById(Integer.valueOf(split[i]));
            QuestionInfo questionInfo = new QuestionInfo();
            questionInfo.setLabel("题目"+(i+1));
            questionInfo.setValue(question.getQuestionName());
            examNameList.add(questionInfo);
        }
        return examNameList;
    }
}
