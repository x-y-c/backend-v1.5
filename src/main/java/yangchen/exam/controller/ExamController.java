package yangchen.exam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import yangchen.exam.entity.ExamGroup;
import yangchen.exam.model.*;
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

    /**
     * 通过学号查询考试信息；
     * 包括考试题目，时间，等等信息；
     * d
     *
     * @param studentId
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public JsonResult getExamInfoByStudentId(@RequestParam Long studentId) {
        List<ExaminationDetail> examinationDetails = examinationService.examInfoDetail(studentId);
        return JsonResult.succResult(examinationDetails);
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public JsonResult createExam(@RequestBody ExamCreatedParam examCreatedParam) {
        if (examCreatedParam.getNumber() == null) {
            //如果题目没有指定，就是5道题
            examCreatedParam.setNumber(5);
        }
        if (examCreatedParam.getNumber() == null ||
                examCreatedParam.getCategory() == null ||
                examCreatedParam.getDesc() == null ||
                examCreatedParam.getStart() == null ||
                examCreatedParam.getEnd() == null ||
                examCreatedParam.getTtl() == null
        ) {
            return JsonResult.errorResult(ResultCode.WRONG_PARAMS, "参数不能为空", null);
        }
        examinationService.createExam(examCreatedParam);
        return JsonResult.succResult(null);
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

    @RequestMapping(value = "/question", method = RequestMethod.GET)
    public JsonResult getQuestionInfo(Integer id) {
        List<QuestionDetail> questionInfo = examinationService.getQuestionInfo(id);
        return JsonResult.succResult(questionInfo);
    }

    @RequestMapping(value = "/examination", method = RequestMethod.GET)
    public JsonResult getExamGroup(Integer id ) {
        List<ExamGroup> allExamGroup = examGroupService.getAllExamGroup(id);
        return JsonResult.succResult(allExamGroup);


    }
}