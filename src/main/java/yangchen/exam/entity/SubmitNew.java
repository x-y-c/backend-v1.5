package yangchen.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "submit_new")
public class SubmitNew {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    //题目id
    @Column(name = "question_id")
    private Integer questionId;

    //试卷id
    @Column(name = "exam_paper_id")
    private Integer examPaperId;

    @Column(name = "code")
    private String code;
}
