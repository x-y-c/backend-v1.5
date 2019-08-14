package yangchen.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author YC
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TestCase")
@Data
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class TestCase {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "TestCaseBh", length = 32)
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


    /**
     * String testCaseBh, Double scoreWeight, String testCaseInput,
     * String testCaseOutput, String questionId, Integer operate
     */
    public TestCase(String testCaseBh, Double scoreWeight, String testCaseInput,
                    String testCaseOutput, String questionId) {
        this.testCaseBh = testCaseBh;
        this.scoreWeight = scoreWeight;
        this.testCaseInput = testCaseInput;
        this.testCaseOutput = testCaseOutput;
        this.questionId = questionId;
    }
}
