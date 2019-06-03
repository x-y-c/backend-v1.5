package yangchen.exam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yangchen.exam.model.JsonResult;
import yangchen.exam.model.ScoreDetail;
import yangchen.exam.service.score.ScoreService;

import java.util.List;

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
    public JsonResult getScoreList(@RequestParam Long studentId) {
        List<ScoreDetail> scoreDetailByStudentId = scoreService.getScoreDetailByStudentId(studentId);
        return JsonResult.succResult(scoreDetailByStudentId);

    }
}
