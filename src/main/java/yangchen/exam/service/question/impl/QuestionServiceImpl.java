package yangchen.exam.service.question.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import yangchen.exam.Enum.StageEnum;
import yangchen.exam.entity.ExamPaper;
import yangchen.exam.entity.QuestionNew;
import yangchen.exam.model.QuestionInfo;
import yangchen.exam.repo.ExamPaperRepo;
import yangchen.exam.repo.QuestionRepo;
import yangchen.exam.repo.TestCaseRepo;
import yangchen.exam.service.examination.ExaminationService;
import yangchen.exam.service.question.QuestionBaseService;
import yangchen.exam.service.question.QuestionService;
import yangchen.exam.util.Base64Util;
import yangchen.exam.util.UrlImageUrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author yc
 */
@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepo questionRepo;
    @Autowired
    private QuestionBaseService questionBaseService;
    @Autowired
    private ExaminationService examinationService;
    @Autowired
    private TestCaseRepo TestCaseRepo;

    @Autowired
    private ExamPaperRepo examPaperRepo;

    @Value("${image.base64.path}")
    private String imgPath;

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
    //删除题目的同时删除测试用例
    //1.通过questionBH 定位 question 删掉
    //2.通过testcaseBH == questionBH 定位 testcase 删掉
    @Override
    public void deleteQuestion(String questionBh) {

        questionRepo.deleteQuestionNewByQuestionBh(questionBh);
        TestCaseRepo.deleteTestCaseByQuestionId(questionBh);

    }

    @Override
    public QuestionNew saveQuestionWithImgDecode(QuestionNew questionNew) throws IOException {

        //preQuestionDetails是 前端富文本编辑器中返回的数据；
        String preQuestionDetails = questionNew.getPreQuestionDetails();
        //取出富文本编辑器中的<img>标签信息；(base64编码的字符串)
        String imgLabelContent = UrlImageUrl.getImgLabel(preQuestionDetails);
        if (imgLabelContent != null) {
            String randomName = UUID.randomUUID().toString().replace("-", "") + ".jpg";
            String imagePath = imgPath + randomName;
            Base64Util.saveImgByte(imgLabelContent, imagePath);
            String urlImgInfo = UrlImageUrl.updateImageDomain(preQuestionDetails, randomName);
            questionNew.setQuestionDetails(urlImgInfo);
            return questionRepo.save(questionNew);
        } else {
            questionNew.setQuestionDetails(preQuestionDetails);
            return questionRepo.save(questionNew);

        }


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
    public List<String> getQuestionBhList(Integer examPaperId) {
        List<String> questionBhList = new ArrayList<>();
        ExamPaper examPaper = examPaperRepo.findById(examPaperId).get();
        String titleId = examPaper.getTitleId();
        String[] titleArray = titleId.split(",");
        for (String title : titleArray) {
            QuestionNew questionNew = questionRepo.findById(Integer.valueOf(title)).get();
            questionBhList.add(questionNew.getQuestionBh());
        }
        return questionBhList;
    }

    @Override
    public Page<QuestionNew> getPageQuestion(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
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
