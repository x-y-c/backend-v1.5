package yangchen.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author YC
 * @date 2019/6/1 15:25
 * O(∩_∩)O)
 */
@Entity
@Table(name = "score")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Score {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "student_id")
    private Integer studentId;

    @Column(name = "examination_id")
    private Integer examinationId;

//    @Column(name = "submit_time")
//    private Timestamp submitTime;

    @Column(name = "score")
    private Integer score;

    @Column(name = "number")
    private Integer index;
}
