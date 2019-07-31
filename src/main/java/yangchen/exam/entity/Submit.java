package yangchen.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author YC
 * @date 2019/5/14 4:30
 * O(∩_∩)O)
 */

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "submit")
@Builder
public class Submit {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "submit_time")
    private Timestamp submitTime;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "question_id")
    private String questionId;

    @Column(name = "examination_id")
    private Integer examinationId;

    @Column(name = "src")
    private String src;
}
