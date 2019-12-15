package yangchen.exam.controller;


import cn.hutool.http.useragent.UserAgentUtil;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import yangchen.exam.entity.ExamGroupNew;
import yangchen.exam.entity.ExamInfo;
import yangchen.exam.entity.ExamPaper;
import yangchen.exam.entity.IpAddr;
import yangchen.exam.model.*;
import yangchen.exam.repo.ExamGroupRepo;
import yangchen.exam.repo.ExamInfoRepo;
import yangchen.exam.repo.ExamPaperRepo;
import yangchen.exam.repo.IpAddrRepo;
import yangchen.exam.service.examInfo.ExamInfoService;
import yangchen.exam.service.examination.ExamGroupService;
import yangchen.exam.service.examination.ExaminationService;
import yangchen.exam.service.submit.SubmitService;
import yangchen.exam.util.IpUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * @author YC
 * @date 2019/5/7 11:35
 * O(∩_∩)O)
 */
@Api(value = "ExamController")
@RestController
@RequestMapping(value = "/examInfo", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExamController {

    @Autowired
    private ExaminationService examinationService;

    @Autowired
    private ExamGroupService examGroupService;

    @Autowired
    private ExamInfoService examInfoService;

    @Autowired
    private ExamGroupRepo examGroupRepo;

    @Autowired
    private ExamPaperRepo examPaperRepo;

    @Autowired
    private ExamInfoRepo examInfoRepo;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private IpAddrRepo ipAddrRepo;

    @Autowired
    private SubmitService submitService;

    public static final Logger LOGGER = LoggerFactory.getLogger(ExamController.class);

    /**
     * 通过学号查询考试信息；
     * 包括考试题目，时间，等等信息；
     *
     * @param studentId
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public JsonResult getExamInfoByStudentId(@RequestParam Integer studentId) {
        List<ExaminationDetail> examinationDetails = examinationService.examInfoDetail(studentId);
        return JsonResult.succResult(examinationDetails);
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
        return examinationService.createExam(examParam);
    }

    @RequestMapping(value = "/examPaperInfo", method = RequestMethod.GET)
    public JsonResult getExamPaperInfo(@RequestParam Integer examGroupId) {
        List<ExamPageInfo> examPageInfo = examinationService.getExamPageInfo(examGroupId);
        return JsonResult.succResult(examPageInfo);
    }

    @RequestMapping(value = "/examPaperInfoFast", method = RequestMethod.GET)
    public JsonResult getExamPaperInfoFast(@RequestParam Integer examGroupId) {
        List<ExamPageInfo> examPageInfoFast = examinationService.getExamPageInfoFast(examGroupId);
        return JsonResult.succResult(examPageInfoFast);
    }

    @RequestMapping(value = "/changeExamPaperStatus", method = RequestMethod.GET)
    public JsonResult changeExamPaperStatus(Integer examGroupId,Integer studentId){
        // LOGGER.info("examGroupID=[{}],studentId=[{}]",examGroupId.toString(),studentId.toString());
        ExamInfo examInfo = examInfoRepo.findByStudentNumberAndExamGroupId(studentId, examGroupId);
        ExamPaper examPaper = examPaperRepo.findById(examInfo.getExaminationId()).get();
        examPaper.setFinished(!examPaper.getFinished());
        ExamPaper examPaperNew = examPaperRepo.save(examPaper);
        LOGGER.info("学生[{}]试卷状态修改，备注：教师端操作,将试卷状态置为[{}]",studentId.toString(),examPaper.getFinished().toString());
        return JsonResult.succResult(examPaperNew);
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
    public JsonResult getQuestionInfo(Integer id, Integer studentNumber) {
        ExamInfo examInfo = examInfoService.getExamInfoByExaminationId(id);
        Optional<ExamGroupNew> examGroupNew = examGroupRepo.findById(examInfo.getExamGroupId());

        //记录ip地址
        IpAddr ip = new IpAddr();
        ip.setIpAddress(IpUtil.getIpAddr(request));
        //LOGGER.info("学生[{}]的IP地址[{}]",studentNumber.toString(),IpUtil.getIpAddr(request).toString());
        ip.setBrowser(UserAgentUtil.parse(request.getHeader("user-agent")).getBrowser().getName());
        ip.setStudentId(studentNumber);
        ip.setExamGroupId(examInfo.getExamGroupId());
        ip.setExamGroupDesc(examGroupNew.get().getExamDesc());
        ip.setStudentName(examInfo.getStudentName());
        ipAddrRepo.save(ip);

        return examinationService.getQuestionInfoResult(studentNumber, id);
    }

    @RequestMapping(value = "/examination", method = RequestMethod.GET)
    public JsonResult getExamGroup(@RequestParam String teacherName, Integer id) {
        List<ExamGroupNew> allExamGroup = examGroupService.getAllExamGroup(teacherName,id);
        return JsonResult.succResult(allExamGroup);

    }

    @RequestMapping(value = "/examGroup/page", method = RequestMethod.GET)
    public JsonResult getPagedExamGroup(int page, int pageLimit, @RequestParam(required = false) String teacherId) {
        if (StringUtils.isEmpty(teacherId)) {
            Page<ExamGroupNew> pageExamGroup = examGroupService.getPageExamGroup(page - 1, pageLimit);
            return JsonResult.succResult(pageExamGroup);
        } else {
            Page<ExamGroupNew> pageExamGroupByTeacher = examGroupService.getPageExamGroupByTeacher(page - 1, pageLimit, teacherId);
            return JsonResult.succResult(pageExamGroupByTeacher);
        }

    }

    @RequestMapping(value = "/submit", method = RequestMethod.GET)
    public JsonResult submitTest(@RequestParam Integer id, @RequestParam Integer studentId,@RequestParam(required = false) Integer sign) {
        Boolean aBoolean = examinationService.submitTest(id, studentId, sign);
        return JsonResult.succResult(aBoolean);
    }

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public JsonResult getTtl(@RequestParam Integer examinationId) {
        ExamInfoResult examInfo = examInfoService.getExamInfoResultByExaminationId(examinationId);
        return JsonResult.succResult(examInfo);
    }

    @RequestMapping(value = "/groupInfo", method = RequestMethod.GET)
    public JsonResult getExamGroupInfo(@RequestParam Integer id) {
        List<ExamGroupNew> examGroup = examGroupService.getExamGroup(id);
        return JsonResult.succResult(examGroup);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public JsonResult deleteExamGroupInfo(@RequestParam Integer id) {
        examGroupService.deleteExamInfo(id);
        return JsonResult.succResult(null);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResult updateExamGroupInfo(@RequestBody ExamGroupNew examGroupNew) {
        examGroupService.updateExamInfo(examGroupNew.getId(), examGroupNew.getExamDesc(), examGroupNew.getExamTime(), examGroupNew.getBeginTime());
        return JsonResult.succResult(null);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public JsonResult editExamGroupInfo(@RequestParam String examGroupId) {
        ExamGroupNew examGroupNew = examGroupRepo.findById(Integer.valueOf(examGroupId)).get();
        LOGGER.info(examGroupId.toString());
        if (examGroupNew.getEndTime().before(new Timestamp(System.currentTimeMillis()))) {
            return JsonResult.errorResult(ResultCode.OVER_ENDTIME, "over", null);
        } else {
            return JsonResult.succResult(null);
        }
    }

    @GetMapping(value = "/getPracticeRecord")
    public JsonResult getPracticeRecord(@RequestParam(required = false)String condition,@RequestParam(required = false)String value,@RequestParam String teacherName,Integer page, Integer pageLimit){
        Page<SubmitPracticeModel> submitPracticeList = null;//
        if(StringUtils.isEmpty(condition)||StringUtils.isEmpty(value)){
            //condition value
            submitPracticeList = submitService.getSubmitPracticeList(teacherName, page, pageLimit);

        }else {
            submitPracticeList = submitService.getSubmitPracticeListCondition(condition,value,teacherName, page, pageLimit);
        }

        return JsonResult.succResult(submitPracticeList);
    }


    @RequestMapping(value = "/getServerTime",method = RequestMethod.GET)
    public JsonResult getSeverTime(){
        return JsonResult.succResult(new Timestamp(System.currentTimeMillis()));
    }


    @RequestMapping(value = "/getTimeDifferenceBeforeExamStart",method = RequestMethod.GET)
    public JsonResult getTimeDifferenceBeforeExamStart(Integer examinationId){
        System.out.println(examinationId);

        ExamInfo examInfo = examInfoRepo.findByExaminationId(examinationId);
        System.out.println(examInfo);
        System.out.println(examInfo.getExamGroupId());
        ExamGroupNew examGroupNew = examGroupRepo.findById(examInfo.getExamGroupId()).get();
        Timestamp beginTime = examGroupNew.getBeginTime();
        Timestamp nowTime = new Timestamp(System.currentTimeMillis());
        long timeDifference = beginTime.getTime() - nowTime.getTime();
        return JsonResult.succResult(new Timestamp(timeDifference));
    }


    @RequestMapping(value = "/getTimeDifferenceBeforeExamEnd",method = RequestMethod.GET)
    public JsonResult getTimeDifferenceBeforeExamEnd(Integer examinationId){
        ExamInfo examInfo = examInfoRepo.findByExaminationId(examinationId);
        ExamGroupNew examGroupNew = examGroupRepo.findById(examInfo.getExamGroupId()).get();
        Timestamp endTime = examGroupNew.getEndTime();
        Timestamp nowTime = new Timestamp(System.currentTimeMillis());
        long timeDifference = endTime.getTime() - nowTime.getTime();
        return JsonResult.succResult(new Timestamp(timeDifference));
    }

}
