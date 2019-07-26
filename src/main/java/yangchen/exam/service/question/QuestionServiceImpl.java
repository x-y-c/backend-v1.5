package yangchen.exam.service.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import yangchen.exam.Enum.StageEnum;
import yangchen.exam.entity.ExamPaper;
import yangchen.exam.entity.QuestionNew;
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
    public QuestionNew createQuestion(QuestionNew question) {
        QuestionNew save = questionRepo.save(question);
        return save;
    }

    @Override
    public QuestionNew updateQuestion(QuestionNew question) {
        QuestionNew save = questionRepo.save(question);
        return save;
    }


    //如果删除成功，则该id对应的对象不存在
    @Override
    public Boolean deleteQuestion(Integer id) {
        questionRepo.deleteById(id);
        return findQuestionById(id) == null;
    }

    @Override
    public QuestionNew findQuestionById(Integer id) {
        Optional<QuestionNew> result = questionRepo.findById(id);
        return result.get();


    }

    @Override
    public List<QuestionNew> findQuestionAll() {
        return questionRepo.findAll();
    }

    @Override
    public QuestionNew getQuestionBy(Integer examinationId, Integer index) {
        ExamPaper examinationById = examinationService.getExaminationById(examinationId);

        String[] split = examinationById.getTitleId().split(",");
        Optional<QuestionNew> byId = questionRepo.findById((Integer.valueOf(split[index])));
        return byId.get();
    }

    @Override
    public List<QuestionInfo> getQuestionNamesByExamPage(Integer examPageId) {
        ExamPaper exampaperByExamPaper = examinationService.getExampaperByExamPaper(examPageId);
        List<QuestionInfo> examNameList = new ArrayList<>();
        String titleId = exampaperByExamPaper.getTitleId();
        String[] split = titleId.split(",");
        for (int i = 0; i < split.length; i++) {
            QuestionNew question = questionBaseService.getQuestionById(Integer.valueOf(split[i]));
            QuestionInfo questionInfo = new QuestionInfo();
            questionInfo.setLabel("题目" + (i + 1));
            questionInfo.setValue(question.getQuestionName());
            examNameList.add(questionInfo);
        }
        return examNameList;
    }

    @Override
    public Page<QuestionNew> getPageQuestion(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        Page<QuestionNew> all = questionRepo.findAll(pageable);
       all.forEach(questionNew -> {
           questionNew.setStage(StageEnum.getStageName(questionNew.getStage()));
       });

        return all;
    }

    @Override
    public QuestionNew findByQuestionBh(String questionBh) {
        QuestionNew questionResult = questionRepo.findByQuestionBh(questionBh);
        return questionResult;
    }
}
