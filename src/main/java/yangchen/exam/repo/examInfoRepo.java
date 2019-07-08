package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yangchen.exam.entity.ExamInfo;

import java.sql.Timestamp;
import java.util.List;

public interface examInfoRepo extends JpaRepository<ExamInfo, Integer> {

    List<ExamInfo> findByStudentNumber(Long studentId);

    ExamInfo findByExaminationId(Integer examinationId);

    List<ExamInfo> findByExamGroupId(Integer examGroupId);


    List<ExamInfo> findByStudentNumberAndExamStartAfter(Long studentId, Timestamp timestamp);

    List<ExamInfo> findByStudentNumberAndExamStartBeforeAndExamEndAfter(Long studentId, Timestamp timestamp, Timestamp endTime);

    List<ExamInfo> findByStudentNumberAndExamEndBefore(Long studentId, Timestamp timestamp);

    @Query(value = "select  * from exam_info join examation on exam_info.examination_id=examation.id\n" +
            "where examation.used=1 and  student_number=?", nativeQuery = true)
    List<ExamInfo> getFinishedExam(Long studentId);

}
