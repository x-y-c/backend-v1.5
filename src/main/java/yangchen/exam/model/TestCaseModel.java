package yangchen.exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yangchen.exam.entity.TestCase;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseModel {

    private String testCaseBh;

    private Double scoreWeight;

    private String testCaseInput;

    private String testCaseOutput;

    private String questionId;

    private Boolean show;


    public TestCaseModel(TestCase testCase) {
        this.testCaseBh = testCase.getTestCaseBh();
        this.questionId = testCase.getQuestionId();
        this.testCaseInput = testCase.getTestCaseInput();
        this.testCaseOutput = testCase.getTestCaseOutput();
        this.scoreWeight = testCase.getScoreWeight();
        this.show = Boolean.FALSE;
    }
}
