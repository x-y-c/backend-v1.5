package yangchen.exam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yangchen.exam.model.ExcelSubmitModel;
import yangchen.exam.model.JsonResult;
import yangchen.exam.model.ScoreAdmin;
import yangchen.exam.model.ScoreDetail;
import yangchen.exam.service.score.ScoreService;
import yangchen.exam.util.ExportUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author YC
 * @date 2019/6/3 15:16
 * O(∩_∩)O)
 */
@RestController
@RequestMapping(value = "/score", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScoreController {


    @Autowired
    private ScoreService scoreService;


    @GetMapping(value = "/info")
    public JsonResult getScoreList(@RequestParam Integer studentId) {
        List<ScoreDetail> scoreDetailByStudentId = scoreService.getScoreDetailByStudentId(studentId);
        return JsonResult.succResult(scoreDetailByStudentId);
    }


    @GetMapping(value = "/admin")
    public JsonResult getScoreAdmin(@RequestParam Integer examGroupId) {
        List<ScoreAdmin> scoreAdminByExamGroupId = scoreService.getScoreAdminByExamGroupId(examGroupId);
        return JsonResult.succResult(scoreAdminByExamGroupId);
    }


    @GetMapping(value = "/csv")
    public String downloadByCSV(HttpServletResponse response, @RequestParam Integer examGroupId) {
        List<Map<String, Object>> dataList = null;
        List<ScoreAdmin> scoreAdminList = scoreService.getScoreAdminByExamGroupId(examGroupId);
        String sTitle = "学号,成绩,姓名,考试";
        String fName = "score_";
        String mapKey = "studentId,score,studentName,examDesc";
        dataList = new ArrayList<>();
        Map<String, Object> map = null;
        for (ScoreAdmin scoreAdmin : scoreAdminList) {
            map = new HashMap<>();
            map.put("studentId", scoreAdmin.getStudentId());
            map.put("score", scoreAdmin.getScore());
            map.put("studentName", scoreAdmin.getStudentName());
            map.put("examDesc", scoreAdmin.getExamDesc());
            dataList.add(map);
        }
        try (final OutputStream os = response.getOutputStream()) {
            ExportUtil.responseSetProperties(fName, response);
            ExportUtil.doExport(dataList, sTitle, mapKey, os);
            return null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return "数据导出错误";
    }


    @GetMapping(value = "/excel")
    public void downloadScoreExcel(HttpServletResponse response,@RequestParam Integer examGroupId) throws IOException {
        scoreService.exportScore(response,examGroupId);
    }


    @GetMapping("/xuyangchen")
    public void downloadSubmit(HttpServletResponse response,@RequestParam Integer id) throws IOException {
        List<ExcelSubmitModel> excelSubmitModels = scoreService.exportSubmit(response,id);
    }


    @GetMapping("/xyc")
    public void downloadSubmitAll(HttpServletResponse response,@RequestParam Integer id)throws IOException{
        scoreService.exportSubmitAll(response,id);
    }

    //todo 定时任务 在考试结束后1分钟操作  将所有试卷提交的任务
    public void submitTest(){

    }
}