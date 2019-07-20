package yangchen.exam.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import yangchen.exam.entity.ExamGroupNew;
import yangchen.exam.model.*;
import yangchen.exam.service.examInfo.ExamInfoService;
import yangchen.exam.service.examination.ExamGroupService;
import yangchen.exam.service.examination.ExaminationService;

import java.util.List;

/**
 * @author YC
 * @date 2019/5/7 11:35
 * O(∩_∩)O)
 */
@RestController
@RequestMapping(value = "/examInfo", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExamController {

    @Autowired
    private ExaminationService examinationService;

    @Autowired
    private ExamGroupService examGroupService;

    @Autowired
    private ExamInfoService examInfoService;

    public static final Logger LOGGER = LoggerFactory.getLogger(ExamController.class);

    /**
     * 通过学号查询考试信息；
     * 包括考试题目，时间，等等信息；
     *
     *
     * @param studentId
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public JsonResult getExamInfoByStudentId(@RequestParam Integer studentId) {
        List<ExaminationDetail> examinationDetails = examinationService.examInfoDetail(studentId);
        return JsonResult.succResult(examinationDetails);
    }

    @RequestMapping(value = "/finished", method = RequestMethod.GET)
    public JsonResult getFinishedExam(@RequestParam Integer studentId) {
        List<ExaminationDetail> finishedExamination = examinationService.getFinishedExamination(studentId);
        return JsonResult.succResult(finishedExamination);
    }

    @RequestMapping(value = "/unstarted", method = RequestMethod.GET)
    public JsonResult getUnstartExam(@RequestParam Integer studentId) {
        List<ExaminationDetail> unStartedExamination = examinationService.getUnstartedExamination(studentId);
        return JsonResult.succResult(unStartedExamination);
    }

    @RequestMapping(value = "/ended", method = RequestMethod.GET)
    public JsonResult getEndedExam(@RequestParam Integer studentId) {
        List<ExaminationDetail> endedExamination = examinationService.getEndedExamination(studentId);
        return JsonResult.succResult(endedExamination);
    }

    @RequestMapping(value = "/ing", method = RequestMethod.GET)
    public JsonResult getIngExam(@RequestParam Integer studentId) {
        List<ExaminationDetail> ingExamination = examinationService.getIngExamination(studentId);
        return JsonResult.succResult(ingExamination);
    }




    @RequestMapping(value = "/complex", method = RequestMethod.POST)
    public JsonResult createExam(@RequestBody ExamParam examParam) {
        ExamGroupNew exam = examinationService.createExam(examParam);
        LOGGER.info(exam.toString());
        return JsonResult.succResult(exam.toString());
    }

    @RequestMapping(value = "/unUsed", method = RequestMethod.GET)
    public JsonResult queryExamUnused() {
        return JsonResult.succResult(examinationService.getUnUsedExamination());
    }

    @RequestMapping(value = "/used", method = RequestMethod.GET)
    public JsonResult queryExamUsed() {
        return JsonResult.succResult(examinationService.getUsedExamination());
    }

    /**
     * 通过试卷id获取
     *
     * @param id
     * @return
     */

    /**
     * 这里再包装一层，判断试卷是否已经进行作答了，如果作答了，返回一个boolean的值；
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/question", method = RequestMethod.GET)
    public JsonResult getQuestionInfo(Integer id) {
        QuestionResult questionInfoResult = examinationService.getQuestionInfoResult(id);
        return JsonResult.succResult(questionInfoResult);
    }

    @RequestMapping(value = "/examination", method = RequestMethod.GET)
    public JsonResult getExamGroup(Integer id) {
        List<ExamGroupNew> allExamGroup = examGroupService.getAllExamGroup(id);
        return JsonResult.succResult(allExamGroup);
    }

    @RequestMapping(value = "/submit", method = RequestMethod.GET)
    public JsonResult submitTest(@RequestParam Integer id, @RequestParam Integer studentId) {
        Boolean aBoolean = examinationService.submitTest(id, studentId);
        return JsonResult.succResult(aBoolean);
    }

    @RequestMapping(value = "/ttl", method = RequestMethod.GET)
    public JsonResult getTtl(@RequestParam Integer examinationId) {
        Integer ttl = examInfoService.getTtl(examinationId);
        return JsonResult.succResult(ttl);
    }

}
