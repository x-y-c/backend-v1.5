package yangchen.exam.service.compileService;

import yangchen.exam.model.CompileFront;

/**
 * @author YC
 * @date 2019/5/15 13:48
 * O(∩_∩)O)
 */
public interface CompileService {

    CompileFront compileCode(Integer examinationId, Integer index, String src, Integer studentId, String questionBh);

}
