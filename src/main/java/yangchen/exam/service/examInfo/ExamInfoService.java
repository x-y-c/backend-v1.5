package yangchen.exam.service.examInfo;

import yangchen.exam.entity.Examination;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author YC
 * @date 2019/5/5 10:19
 * O(∩_∩)O)
 */
public interface ExamInfoService {

    /**
     * 根据阶段创建题目，默认为5道题；
     *
     * @param category
     */
    Examination createExamInfo(String category);


    /**
     * 根据阶段和题目数创建题目，
     *
     * @param category
     * @param number
     */
    Examination createExamInfo(String category, Integer number);


    /**
     * @param category  阶段 example ： 阶段3
     * @param number    题数 example： 5
     * @param grades    班级 example: 软工1501，软工1502
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param ttl       持续时间
     * @param desc      考卷描述
     */
    void createExam(String category, Integer number, List<String> grades, Timestamp startTime, Timestamp endTime,
                    Long ttl, String desc);


}
