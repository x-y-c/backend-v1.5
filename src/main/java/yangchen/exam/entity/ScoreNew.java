package yangchen.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "score_new")
@Data
public class ScoreNew {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    //学号
    @Column(name = "student_id")
    private Integer studentId;

    //试卷id
    @Column(name = "exam_paper_id")
    private Integer examPaperId;

    @Column(name = "question_id")
    private Integer questionId;

    @Column(name = "score")
    private Integer score;

}
