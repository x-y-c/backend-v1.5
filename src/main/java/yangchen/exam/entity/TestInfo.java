package yangchen.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;


/**
 * @author yc
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "test_info")
public class TestInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "question_id")
    private Integer questionId;

    @Column(name = "submited_time")
    private Timestamp submitedTime;

    @Column(name = "code")
    private String code;

    @Column(name = "correct_rate")
    private Double correctRate;

    @Column(name = "testCase_id")
    private String TestCaseId;
}
