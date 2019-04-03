package yangchen.exam.service.biz;

import yangchen.exam.model.ExamRecord;

import java.util.List;

//ExamRecord
//考试信息，考试id，考试时间
public interface ExamInfoService {

    List<ExamRecord> getExamRecordInfo(Long studentId);

    //考试信息，考试id，考试时间，班级
    List<ExamRecord> teacherGetExamRecordInfo(String teacherEngName);
}
