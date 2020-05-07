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
import yangchen.exam.entity.*;
import yangchen.exam.model.*;
import yangchen.exam.repo.*;
import yangchen.exam.service.examination.ExaminationService;
import yangchen.exam.service.question.QuestionBaseService;
import yangchen.exam.service.question.QuestionService;
import yangchen.exam.util.Base64Util;
import yangchen.exam.util.DecodeQuestionDetails;
import yangchen.exam.util.UrlImageUtil;

import java.io.IOException;
import java.util.*;

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
    private TestCaseRepo testCaseRepo;

    @Autowired
    private ExamPaperRepo examPaperRepo;

    @Autowired
    private QuestionLogRepo questionLogRepo;

    @Autowired
    private SubmitPracticeRepo submitPracticeRepo;

    public static final Logger LOGGER = LoggerFactory.getLogger(QuestionServiceImpl.class);


    @Value("${image.base64.path}")
    private String imgPath;

    @Value("${image.nginx.path}")
    /*   /ckupload/   */
    private String imgNginx;

    @Value("${image.nginx.url.path}")
    /*  http://211.68.35.79:2048  */
    private String imgNginxUrl;


    @Override
    public QuestionNew createQuestion(QuestionNew question) {
        String questionBh = UUID.randomUUID().toString().replace("-", "");
        question.setQuestionBh(questionBh);
        question.setActived(Boolean.FALSE);
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
    public QuestionNew saveQuestionWithImgDecodeNew(QuestionUpdate questionUpdate) throws IOException {

        //preQuestionDetails是 前端富文本编辑器中返回的数据；
        String preQuestionDetails = questionUpdate.getPreQuestionDetails();
        //取出富文本编辑器中的<img>标签信息；(base64编码的字符串)
        List<String> imgLabelContentList = UrlImageUtil.getImgLabels(preQuestionDetails);
        //阶段，难度，题型，通过枚举把文字改成对应的编码
        questionUpdate.setStage(StageEnum.getStageCode(questionUpdate.getStage()));
        questionUpdate.setDifficulty(DifficultEnum.getDifficultCode(questionUpdate.getDifficulty()));
        questionUpdate.setQuestionType(QuestionTypeEnum.getQuestionTypeCode(questionUpdate.getQuestionType()));

        if (imgLabelContentList.size() > 0) {
            List<String> randomNameList = new ArrayList<>();
            for (String imgLabelContent : imgLabelContentList) {
                if (imgLabelContent.indexOf(imgNginx) != -1) {
                    //这里不应该取前面的网址
                    imgLabelContent = imgLabelContent.replace(imgNginxUrl,"");
                    randomNameList.add(imgLabelContent);
                } else {
                    String randomName = UUID.randomUUID().toString().replace("-", "") + ".jpg";
                    String imagePath = imgPath + randomName;
                    String imageUrl = imgNginx + randomName;
                    Base64Util.saveImgByte(imgLabelContent, imagePath);
                    randomNameList.add(imageUrl);
                }
            }
            String urlImgInfo = UrlImageUtil.updateImageDomainNew(preQuestionDetails, randomNameList);
            questionUpdate.setQuestionDetails(urlImgInfo);
            questionUpdate.setActived(Boolean.FALSE);
            return questionRepo.save(new QuestionNew(questionUpdate));
        } else {
            questionUpdate.setQuestionDetails(preQuestionDetails);
            return questionRepo.save(new QuestionNew(questionUpdate));
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
            // questionInfo.setValue(question.getQuestionName());
            questionInfo.setValue('['+question.getId().toString()+"]"+question.getQuestionName());
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
                    questionLastChildren.setLabel("（" + questionNew.getId() + "）\t"+"["+DifficultEnum.getDifficultName(questionNew.getDifficulty())+"]\t" + questionNew.getQuestionName());
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

        if ("100001".equals(questionNew.getIsProgramBlank())) {
            Gson gson = new Gson();
            SourceCode sourceCode = gson.fromJson(questionNew.getSourceCode(), SourceCode.class);
            questionDetail.setSrc(sourceCode.getKey().get(0).getCode());
        } else {
            questionDetail.setSrc("");
        }

        questionDetail.setQuestion(DecodeQuestionDetails.getRightImage(imgNginxUrl, questionNew.getQuestionDetails()));

        //todo 暂时把代码保存的部分删了，这部分逻辑有问题
//        SubmitPractice submitLast = submitPracticeRepo.getSubmitLast(String.valueOf(questionNew.getId()), studentId);
//        if (submitLast != null) {
//            questionDetail.setCodeHistory(submitLast.getSrc());
//            questionDetail.setScore(submitLast.getScore());
//        }

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
    public QuestionLog addQuestionLog(QuestionUpdate questionUpdate, String flag) {
        QuestionLog questionLog = new QuestionLog();
        questionLog.setEditCustomBh(questionUpdate.getCustomBh());
        questionLog.setOptionDo(flag);
        questionLog.setQuestionBh(questionUpdate.getQuestionBh());
        questionLogRepo.save(questionLog);
        return questionLog;
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
        //questionLogList.parallelStream().forEach(questionLog -> {
        questionLogList.forEach(questionLog -> {
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
//            answer = add(code, questionNew.getMemo());
            answer = questionNew.getMemo();
        } else {
            answer = code;
        }
        return answer;
    }

    @Override
    public List<QuestionNew> getQuestion(String questionType) {
        List<QuestionNew> questionList = questionRepo.findByQuestionTypeAndActivedIsTrue(questionType);
        return questionList;
    }

    @Override
    public HashMap<String,String> getQuestionInput2Output(String questionBh) {
        HashMap<String,String> input2output = new HashMap<>();
        List<TestCase> testCaseList = testCaseRepo.findByQuestionId(questionBh);
        testCaseList.forEach(testCase -> {
            input2output.put(testCase.getTestCaseInput(),testCase.getTestCaseOutput());
        });
        return input2output;
    }

    @Override
    public List<String> getQuestionInput(String questionBh) {
        List<TestCase> testCaseList = testCaseRepo.findByQuestionId(questionBh);
        List<String> input = new ArrayList<>();
        testCaseList.forEach(testCase -> {
            input.add(testCase.getTestCaseInput());
        });
        return input;
    }

    @Override
    public List<String> getQuestionOutput(String questionBh) {
        List<TestCase> testCaseList = testCaseRepo.findByQuestionId(questionBh);
        List<String> output = new ArrayList<>();
        testCaseList.forEach(testCase -> {
            output.add(testCase.getTestCaseOutput());
        });
        return output;
    }

    @Override
    public Boolean isChangeSrc(QuestionNew question,String src) {
        Gson gson = new Gson();
        SourceCode sourceCode = gson.fromJson(question.getSourceCode(), SourceCode.class);
        String codeInDB = sourceCode.getKey().get(0).getCode();

        src = washData(src);
        codeInDB = washData(codeInDB);
        System.out.println("result\n"+src.equals(codeInDB));
        if(src.equals(codeInDB)){
            return false;
        }
        return true;
    }



    public String washData(String src){
        int pos = 0;
        int lengthStart = "/******start******/".length();
        int lengthEnd = "/******end******/".length();
        StringBuilder s = new StringBuilder(src);
        int num1 = 0;
        int num2 = 0;
        while(true){

            num1=src.indexOf("/******start******/",pos);
            if(num1!=-1){
                num1 = src.indexOf("/******start******/",pos) + lengthStart;
            }
            num2 = src.indexOf("/******end******/",pos);

            if(num1==-1||num2==-1){
                break;
            }
            src = s.replace(num1,num2,"").toString();
            num2 = src.indexOf("/******end******/",pos);
            pos = num2 + lengthEnd;
        }
        System.out.println("================\n"+src);
        return src;
    }

//    public String add(String line, String memo) {
//        String result = "";
//        String start = "/******start******/";
//        String end = "/******end******/";
//        int length1 = start.length();
//        int index = line.indexOf(start);
//        int index2 = line.indexOf(end);
//        for (int i = 0; i < index + length1; i++) {
//            result += line.charAt(i);
//        }
//        String blank = memo;
//        for (int i = 0; i < blank.length(); i++) {
//            result += blank.charAt(i);
//        }
//        //index2=1
////        line.length()=3
//        for (int i = index2; i < line.length(); i++) {
//            result += line.charAt(i);
//        }
//        System.out.println(result);
//        return result;
//    }

}
