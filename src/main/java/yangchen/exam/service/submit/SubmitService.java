package yangchen.exam.service.submit;

import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;
import yangchen.exam.entity.Submit;
import yangchen.exam.entity.SubmitPractice;
import yangchen.exam.model.SubmitPracticeModel;

import java.util.List;

/**
 * @author YC
 * @date 2019/5/14 4:43
 * O(∩_∩)O)
 */
public interface SubmitService {
    /**
     * 保存代码记录
     * @param submit
     * @return
     */
    Submit addSubmit(Submit submit);

    //List<SubmitPracticeModel> getSubmitPracticeList(String teacherName,Integer page,Integer pageLimit);
    Page<SubmitPracticeModel> getSubmitPracticeList(String teacherName, Integer page, Integer pageLimit);

    Page<SubmitPracticeModel> getSubmitPracticeListCondition(String condition,String value,String teacherName, Integer page, Integer pageLimit);

    Submit getCodeHistory(Integer questionId, Integer examinationId);

}
