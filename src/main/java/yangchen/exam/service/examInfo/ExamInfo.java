package yangchen.exam.service.examInfo;

import java.sql.Timestamp;

/**
 * @author YC
 * @date 2019/5/5 10:19
 * O(∩_∩)O)
 */
public interface ExamInfo {

    /**
     * 根据阶段创建题目，默认为5道题；
     *
     * @param category
     */
    void createExamInfo(String category);


    /**
     * 根据阶段和题目数创建题目，
     *
     * @param category
     * @param number
     */
    void createExamInfo(String category, Integer number);


    /**
     * @param category  阶段
     * @param number    题数
     * @param grade     班级
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param ttl       持续时间
     */
    void createExam(String category, Integer number, String grade, Timestamp startTime, Timestamp endTime, Long ttl);


}
