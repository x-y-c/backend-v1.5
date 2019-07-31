package yangchen.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author YC
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TestCase")
@Data
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TestCaseBh")
    private String testCaseBh;

    @Column(name = "ScoreWeight")
    private Double scoreWeight;

    @Column(name = "TestCaseInput")
    private String testCaseInput;

    @Column(name = "TestCaseOutput")
    private String testCaseOutput;

    @Column(name = "TestCaseTips")
    private String testCaseTips;

    @Column(name = "QuestionId")
    private String questionId;

    @Column(name = "Memo")
    private String memo;
}
