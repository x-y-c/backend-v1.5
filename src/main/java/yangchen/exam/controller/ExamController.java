package yangchen.exam.controller;

import yangchen.exam.model.ExamCreatedParam;
import yangchen.exam.model.JsonResult;

/**
 * @author YC
 * @date 2019/5/7 11:35
 * O(∩_∩)O)
 */
public class ExamController {


    /**
     * 通过学号查询考试信息；
     * 包括考试题目，时间，等等信息；
     *
     * @param studentId
     * @return
     */
    public JsonResult getExamInfoByStuentId(Long studentId) {
        return null;
    }


    public JsonResult createExam(ExamCreatedParam examCreatedParam) {
        return null;
    }

    public JsonResult queryExamUnused() {
        return null;
    }

    public JsonResult queryExamUsed() {
        return null;
    }
}
