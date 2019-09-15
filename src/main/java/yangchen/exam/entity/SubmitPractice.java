package yangchen.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "submit_practice")
public class SubmitPractice {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "submit_time")
    private Timestamp submitTime;

    @Column(name = "student_id")
    private Integer studentId;

    @Column(name = "question_id")
    private String questionId;

    @Column(name = "src")
    private String src;

    @Column(name = "score")
    private Integer score;
}
