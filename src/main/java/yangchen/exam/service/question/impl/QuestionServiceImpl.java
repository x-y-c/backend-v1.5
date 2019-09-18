package yangchen.exam.service.question.impl;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import yangchen.exam.Enum.DifficultEnum;
import yangchen.exam.Enum.QuestionTypeEnum;
import yangchen.exam.Enum.StageEnum;
import yangchen.exam.entity.ExamPaper;
import yangchen.exam.entity.QuestionLog;
import yangchen.exam.entity.QuestionNew;
import yangchen.exam.entity.SubmitPractice;
import yangchen.exam.model.*;
import yangchen.exam.repo.*;
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

    @Autowired
    private QuestionLogRepo questionLogRepo;

    @Autowired
    private SubmitPracticeRepo submitPracticeRepo;

    public static final Logger LOGGER = LoggerFactory.getLogger(QuestionServiceImpl.class);


    @Value("${image.base64.path}")
    private String imgPath;

    @Override
    public QuestionNew createQuestion(QuestionNew question) {
        String questionBh = UUID.randomUUID().toString().replace("-", "");
        question.setQuestionBh(questionBh);
        question.setActived(Boolean.TRUE);
        QuestionNew questionNew = questionRepo.save(question);
        return questionNew;
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

        QuestionNew question = questionRepo.findByQuestionBh(questionBh);
        question.setActived(Boolean.FALSE);
        questionRepo.save(question);


    }

    @Override
    public QuestionNew saveQuestionWithImgDecode(QuestionNew questionNew) throws IOException {

        //preQuestionDetails是 前端富文本编辑器中返回的数据；
        String preQuestionDetails = questionNew.getPreQuestionDetails();
        //取出富文本编辑器中的<img>标签信息；(base64编码的字符串)
        String imgLabelContent = UrlImageUrl.getImgLabel(preQuestionDetails);
        questionNew.setStage(StageEnum.getStageCode(questionNew.getStage()));
        questionNew.setDifficulty(DifficultEnum.getDifficultCode(questionNew.getDifficulty()));
        questionNew.setQuestionType(QuestionTypeEnum.getQuestionTypeCode(questionNew.getQuestionType()));
        if (imgLabelContent != null) {
            String randomName = UUID.randomUUID().toString().replace("-", "") + ".jpg";
            String imagePath = imgPath + randomName;
            Base64Util.saveImgByte(imgLabelContent, imagePath);
            String urlImgInfo = UrlImageUrl.updateImageDomain(preQuestionDetails, randomName);
            questionNew.setQuestionDetails(urlImgInfo);
            questionNew.setActived(Boolean.TRUE);
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
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<QuestionNew> all = questionRepo.findAll(pageable);
        all.forEach(questionNew -> {
            questionNew.setStage(StageEnum.getStageName(questionNew.getStage()));
            questionNew.setDifficulty(DifficultEnum.getDifficultName(questionNew.getDifficulty()));
            questionNew.setQuestionType(QuestionTypeEnum.getQuestionTypeName(questionNew.getQuestionType()));
        });
        return all;
    }

    @Override
    public Page<QuestionNew> getStageQuestionPage(String stage, Integer pageNo, Integer pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<QuestionNew> all = questionRepo.findByStage(StageEnum.getStageCode(stage), pageable);
        all.forEach(questionNew -> {
            questionNew.setStage(StageEnum.getStageName(questionNew.getStage()));
            questionNew.setDifficulty(DifficultEnum.getDifficultName(questionNew.getDifficulty()));
            questionNew.setQuestionType(QuestionTypeEnum.getQuestionTypeName(questionNew.getQuestionType()));
        });
        return all;
    }

    @Override
    public Page<QuestionNew> getIdQuestionPage(String value, Integer pageNo, Integer pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<QuestionNew> all = questionRepo.findById(Integer.valueOf(value), pageable);
        all.forEach(questionNew -> {
            questionNew.setStage(StageEnum.getStageName(questionNew.getStage()));
            questionNew.setDifficulty(DifficultEnum.getDifficultName(questionNew.getDifficulty()));
            questionNew.setQuestionType(QuestionTypeEnum.getQuestionTypeName(questionNew.getQuestionType()));
        });
        return all;
    }

    @Override
    public Page<QuestionNew> getTitleQuestionPage(String value, Integer pageNo, Integer pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<QuestionNew> all = questionRepo.findByQuestionNameLike("%" + value + "%", pageable);
        all.forEach(questionNew -> {
            questionNew.setStage(StageEnum.getStageName(questionNew.getStage()));
            questionNew.setDifficulty(DifficultEnum.getDifficultName(questionNew.getDifficulty()));
            questionNew.setQuestionType(QuestionTypeEnum.getQuestionTypeName(questionNew.getQuestionType()));
        });
        return all;
    }

    @Override
    public Page<QuestionNew> getCustomBhQuestionPage(String value, Integer pageNo, Integer pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<QuestionNew> all = questionRepo.findByCustomBhLike("%" + value + "%", pageable);
        all.forEach(questionNew -> {
            questionNew.setStage(StageEnum.getStageName(questionNew.getStage()));
            questionNew.setDifficulty(DifficultEnum.getDifficultName(questionNew.getDifficulty()));
            questionNew.setQuestionType(QuestionTypeEnum.getQuestionTypeName(questionNew.getQuestionType()));
        });
        return all;
    }

    @Override
    public List<QuestionNew> searchStage(String stage) {
        return questionRepo.findByStageAndActivedIsTrue(StageEnum.getStageCode(stage));
    }

    /*
    QuestionPractice
        label--练习
        List<QuestionType>
                label
                List<QuestionStage>
                    label
                    List<QuestionLastChildren>
                        label
                        questionBh

//                QuestionLastChildren questionLastChildren = new QuestionLastChildren();
//                questionLastChildren.setLabel();
     */
    //todo 对于这里的代码，改成方法套方法的形式，清晰明了
//    @Override
//    public QuestionPractice getQuestionPracticeInfo() {
//        QuestionPractice questionPractice = new QuestionPractice();
//        questionPractice.setLabel("练习题");
//        List<QuestionTypeChildren> questionTypeChildrenList = new ArrayList<QuestionTypeChildren>();
//        //1、按题型查询题目
//        for (QuestionTypeEnum questionType : QuestionTypeEnum.values()) {
//            QuestionTypeChildren questionTypeChildren = new QuestionTypeChildren();
//            questionTypeChildren.setLabel(questionType.getQuestionTypeName());
//            List<QuestionNew> questionNewList = questionRepo.findByQuestionTypeAndActivedIsTrue(questionType.getQuestionTypeCode());
//            List<QuestionStageChildren> questionStageChildrenList = new ArrayList<QuestionStageChildren>();
//            //2、按阶段查询
//            for (StageEnum stageEnum : StageEnum.values()) {
//                QuestionStageChildren questionStageChildren = new QuestionStageChildren();
//                questionStageChildren.setLabel(stageEnum.getStageName());
//                List<QuestionLastChildren> questionLastChildrenList = new ArrayList<QuestionLastChildren>();
//                for (QuestionNew questionNew : questionNewList) {
//                    if (questionNew.getStage().equals(stageEnum.getStageCode())) {
//                        QuestionLastChildren questionLastChildren = new QuestionLastChildren();
//                        questionLastChildren.setLabel(questionNew.getQuestionName());
//                        questionLastChildren.setQuestionBh(questionNew.getQuestionBh());
//                        questionLastChildrenList.add(questionLastChildren);
//                    }
//                }
//                questionStageChildren.setChildren(questionLastChildrenList);
//                questionStageChildrenList.add(questionStageChildren);
//            }
//
//            questionTypeChildren.setChildren(questionStageChildrenList);
//            questionTypeChildrenList.add(questionTypeChildren);
//        }
//        questionTypeChildrenList.remove(questionTypeChildrenList.size()-1);
//        questionPractice.setChildren(questionTypeChildrenList);
//        return questionPractice;
//    }

    //获取树状的练习题的数据格式
    @Override
    public QuestionPractice getQuestionPracticeInfo() {
        QuestionPractice questionPractice = new QuestionPractice();
        questionPractice.setLabel("练习题");

        List<QuestionTypeChildren> questionTypeChildrenList = getPracticeByQuestionType();
        questionPractice.setChildren(questionTypeChildrenList);

        return questionPractice;
    }

    //1、按题型查询题目
    public List<QuestionTypeChildren> getPracticeByQuestionType() {

        List<QuestionTypeChildren> questionTypeChildrenList = new ArrayList<QuestionTypeChildren>();
        for (QuestionTypeEnum questionType : QuestionTypeEnum.values()) {
            QuestionTypeChildren questionTypeChildren = new QuestionTypeChildren();
            questionTypeChildren.setLabel(questionType.getQuestionTypeName());
            List<QuestionNew> questionNewList = questionRepo.findByQuestionTypeAndActivedIsTrue(questionType.getQuestionTypeCode());

            List<QuestionStageChildren> questionStageChildrenList = getPracticeByStage(questionNewList);

            questionTypeChildren.setChildren(questionStageChildrenList);
            questionTypeChildrenList.add(questionTypeChildren);
        }
        questionTypeChildrenList.remove(questionTypeChildrenList.size() - 1);

        return questionTypeChildrenList;
    }

    //2、按阶段查询题目
    public List<QuestionStageChildren> getPracticeByStage(List<QuestionNew> questionNewList) {
        List<QuestionStageChildren> questionStageChildrenList = new ArrayList<QuestionStageChildren>();

        for (StageEnum stageEnum : StageEnum.values()) {
            QuestionStageChildren questionStageChildren = new QuestionStageChildren();
            questionStageChildren.setLabel(stageEnum.getStageName());
            List<QuestionLastChildren> questionLastChildrenList = new ArrayList<QuestionLastChildren>();
            for (QuestionNew questionNew : questionNewList) {
                if (questionNew.getStage().equals(stageEnum.getStageCode())) {
                    QuestionLastChildren questionLastChildren = new QuestionLastChildren();
                    questionLastChildren.setLabel("（"+questionNew.getId()+"）\t"+questionNew.getQuestionName());
                    questionLastChildren.setQuestionBh(questionNew.getQuestionBh());
                    questionLastChildrenList.add(questionLastChildren);
                }
            }
            questionStageChildren.setChildren(questionLastChildrenList);
            questionStageChildrenList.add(questionStageChildren);
        }
        return questionStageChildrenList;
    }

    @Override
    public QuestionDetail getPracticeItem(String questionBh, Integer studentId) {
        QuestionNew questionNew = questionRepo.findByQuestionBh(questionBh);
        QuestionDetail questionDetail = new QuestionDetail();
        questionDetail.setTitle(questionNew.getQuestionName());
        questionDetail.setId(questionNew.getId().toString());
        questionDetail.setCustomBh(questionNew.getCustomBh());
        questionDetail.setIsProgramBlank(questionNew.getIsProgramBlank());
        questionDetail.setQuestion(questionNew.getQuestionDetails());
//        questionDetail.setSrc();
        if ("100001".equals(questionNew.getIsProgramBlank())) {
            Gson gson = new Gson();
            SourceCode sourceCode = gson.fromJson(questionNew.getSourceCode(), SourceCode.class);
            questionDetail.setSrc(sourceCode.getKey().get(0).getCode());
        } else {
            questionDetail.setSrc("");
        }
        SubmitPractice submitLast = submitPracticeRepo.getSubmitLast(String.valueOf(questionNew.getId()), studentId);
        if (submitLast == null) {
            questionDetail.setSrc("");
            questionDetail.setScore(0);
        } else {
            questionDetail.setSrc(submitLast.getSrc());
            questionDetail.setScore(submitLast.getScore());
        }

        return questionDetail;
    }


    //todo
    @Override
    public String getPracticeFront(String questionBh) {
        QuestionNew questionNew = questionRepo.findByQuestionBh(questionBh);
        String questionType = QuestionTypeEnum.getQuestionTypeName(questionNew.getQuestionType());
        String stage = StageEnum.getStageName(questionNew.getStage());

        List<QuestionTypeChildren> questionTypeChildrenList = getPracticeByQuestionType();
        String questionBhCurrent = "";
        String questionBhFront = "";
        String questionBhIWant = "";
        for (QuestionTypeChildren questionTypeChildren : questionTypeChildrenList) {
            if (questionTypeChildren.getLabel().equals(questionType)) {
                List<QuestionStageChildren> questionStageChildrenList = questionTypeChildren.getChildren();
                for (QuestionStageChildren questionStageChildren : questionStageChildrenList) {
                    if (questionStageChildren.getLabel().equals(stage)) {
                        List<QuestionLastChildren> questionLastChildren = questionStageChildren.getChildren();
                        for (int i = 0; i < questionLastChildren.size(); i++) {
                            questionBhCurrent = questionLastChildren.get(i).getQuestionBh();
                            if (questionBhCurrent.equals(questionBh)) {
                                questionBhIWant = questionBhFront;
                                break;
                            } else {
                                questionBhFront = questionBhCurrent;
                            }
                        }
                    }
                }
            }
        }

        return questionBhIWant;
    }


    //todo
    @Override
    public String getPracticeNext(String questionBh) {
        QuestionNew questionNew = questionRepo.findByQuestionBh(questionBh);
        String questionType = QuestionTypeEnum.getQuestionTypeName(questionNew.getQuestionType());
        String stage = StageEnum.getStageName(questionNew.getStage());

        List<QuestionTypeChildren> questionTypeChildrenList = getPracticeByQuestionType();
        String questionBhCurrent = "";
        String questionBhIWant = "";
        for (QuestionTypeChildren questionTypeChildren : questionTypeChildrenList) {
            if (questionTypeChildren.getLabel().equals(questionType)) {
                List<QuestionStageChildren> questionStageChildrenList = questionTypeChildren.getChildren();
                for (QuestionStageChildren questionStageChildren : questionStageChildrenList) {
                    if (questionStageChildren.getLabel().equals(stage)) {
                        List<QuestionLastChildren> questionLastChildren = questionStageChildren.getChildren();
                        for (int i = 0; i < questionLastChildren.size(); i++) {
                            questionBhCurrent = questionLastChildren.get(i).getQuestionBh();
                            if (questionBhCurrent.equals(questionBh)) {
                                questionBhIWant = questionLastChildren.get(i + 1).getQuestionBh();
                                break;
                            }
                        }
                    }
                }
            }
        }

        return questionBhIWant;
    }

    @Override
    public QuestionNew findByQuestionBh(String questionBh) {
        QuestionNew questionResult = questionRepo.findByQuestionBh(questionBh);
        return questionResult;
    }


    @Override
    public QuestionLog addQuestionLog(QuestionNew questionNew, String flag) {
        QuestionLog questionLog = new QuestionLog();
        questionLog.setEditCustomBh(questionNew.getCustomBh());
        questionLog.setOptionDo(flag);
        questionLog.setQuestionBh(questionNew.getQuestionBh());
        questionLogRepo.save(questionLog);
        return questionLog;
    }

    @Override
    public List<QuestionLogModel> getQuestionLog() {
        List<QuestionLog> questionLogList = questionLogRepo.findAll();
        List<QuestionLogModel> questionLogModelList = new ArrayList<>();
        questionLogList.parallelStream().forEach(questionLog -> {
            QuestionLogModel questionLogModel = new QuestionLogModel();
            questionLogModel.setOptionDo(questionLog.getOptionDo());
            questionLogModel.setName(questionLog.getEditCustomBh());
            questionLogModel.setEditTime(questionLog.getEditTime());
            QuestionNew questionNew = questionRepo.findByQuestionBh(questionLog.getQuestionBh());
            questionLogModel.setQuestionId(questionNew.getId().toString());
            questionLogModel.setQuestionName(questionNew.getQuestionName());
            questionLogModelList.add(questionLogModel);
        });
        return questionLogModelList;
    }

    @Override
    public String getAnswer(String questionBh) {
        String answer = "";
        Gson gson = new Gson();
        QuestionNew questionNew = questionRepo.findByQuestionBh(questionBh);
        SourceCode sourceCode = gson.fromJson(questionNew.getSourceCode(), SourceCode.class);
        String code = sourceCode.getKey().get(0).getCode();
        if ("100001".equals(questionNew.getIsProgramBlank())) {
            answer = add(code,questionNew.getMemo());
        } else {
            answer = code;
        }
        return answer;
    }

    public String add(String line,String memo){
        String result = "";
        String start = "/******start******/";
        String end = "/******end******/";
        int length1 = start.length();
        int index = line.indexOf(start);
        int index2 = line.indexOf(end);
        for(int i=0;i<index+length1;i++){
            result +=line.charAt(i);
        }
        String blank= memo;
        for(int i=0;i<blank.length();i++){
            result +=blank.charAt(i);
        }
        //index2=1
//        line.length()=3
        for(int i=index2;i<line.length();i++){
            result +=line.charAt(i);
        }
        System.out.println(result);
        return result;
    }


}
