package yangchen.exam.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="project_submit")
public class ProjectSubmit {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "student_id")
    private Integer studentId;

    @Column(name = "project_paper_id")
    private Integer examPaperId;

    @Column(name = "question_index")
    private Integer questionIndex;

    @Column(name = "src")
    private String src;

    @Column(name = "code_lines")
    private Integer codeLines;

    @Column(name = "score")
    private Integer score;

    @Column(name = "submit_time")
    private Timestamp submitTime;

}
