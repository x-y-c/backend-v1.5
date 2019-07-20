package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yangchen.exam.entity.ExamInfo;

import java.sql.Timestamp;
import java.util.List;

public interface examInfoRepo extends JpaRepository<ExamInfo, Integer> {

    List<ExamInfo> findByStudentNumber(Integer studentId);

    ExamInfo findByExaminationId(Integer examinationId);

    List<ExamInfo> findByExamGroupId(Integer examGroupId);


    List<ExamInfo> findByStudentNumberAndExamStartAfter(Integer studentId, Timestamp timestamp);

    List<ExamInfo> findByStudentNumberAndExamStartBeforeAndExamEndAfter(Integer studentId, Timestamp timestamp, Timestamp endTime);

    List<ExamInfo> findByStudentNumberAndExamEndBefore(Integer studentId, Timestamp timestamp);

    @Query(value = "select  * from exam_info join examation on exam_info.examination_id=examation.id\n" +
            "where examation.used=1 and  student_number=?", nativeQuery = true)
    List<ExamInfo> getFinishedExam(Integer studentId);

}
