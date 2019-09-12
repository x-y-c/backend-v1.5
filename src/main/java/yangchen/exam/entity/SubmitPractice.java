package yangchen.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
}
