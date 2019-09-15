package yangchen.exam.service.submit;

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

    List<SubmitPracticeModel> getSubmitPracticeList();

}
