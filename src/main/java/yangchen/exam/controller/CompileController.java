package yangchen.exam.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yangchen.exam.model.CompileFront;
import yangchen.exam.model.JsonResult;
import yangchen.exam.service.compileService.CompileService;

import java.io.IOException;

/**
 * @author yangchen
 */
@RestController
@RequestMapping(value = "/compile", produces = MediaType.APPLICATION_JSON_VALUE)
public class CompileController {


    Logger Logger = LoggerFactory.getLogger(CompileController.class);


    @Autowired
    private CompileService compileService;
    /**
     * @param code
     * @return 这里的编译，如果通过的话，是没有返回值的，所以用null判断一下，null就返回success，
     * 如果有返回值，证明编译出错，需要显示出错信息，错误信息的格式是 error： 所以，这里简单处理一下，
     * 截取error以后的字符串
     * @throws IOException
     */


    /**
     * 更新注释，首先，前端传来的代码，可以不被转义，使用codeMirror组件就可以避免不必要的转义，
     * 其次，编译项目为了保证安全，不和该项目放在一起，通过http请求来调用，
     * 第三，从前端收到的代码，json序列化的时候，gson默认会进行转义，这里，使用GsonBuilder ，声明不要做编码转义就可以了；
     *
     * @param code
     * @return
     */
    @RequestMapping(value = {"/", ""}, method = RequestMethod.POST)
    public JsonResult compileTest(@RequestParam String code, @RequestParam Integer examinationId, @RequestParam Integer index, @RequestParam Integer studentId) {
        CompileFront compileFront = compileService.compileCode(examinationId, index, code, studentId);
        return JsonResult.succResult(compileFront);
    }


}
