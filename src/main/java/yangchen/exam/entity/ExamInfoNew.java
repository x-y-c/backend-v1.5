package yangchen.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "exam_info_new")
public class ExamInfoNew {

    //主键自增
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "student_id")
    private Integer studentId;

    @Column(name = "exam_group_id")
    private Integer examGroupId;

    @Column(name = "exam_paper_id")
    private Integer examPaperIId;

    @Column(name = "exam_score")
    private Integer examScore;

}
