package yangchen.exam.controller;


import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yangchen.exam.model.JsonResult;
import yangchen.exam.service.biz.CompileCoreService;
import yangchen.exam.util.HtmlUtil;
import yangchen.exam.util.UserUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author yangchen
 */
@RestController
@RequestMapping(value = "/compile", produces = MediaType.APPLICATION_JSON_VALUE)
public class CompileController {


    @Autowired
    private CompileCoreService compileCoreService;
    Logger Logger = LoggerFactory.getLogger(CompileController.class);

    @Autowired
    private HttpServletRequest servletRequest;

    /**
     * @param code
     * @return 这里的编译，如果通过的话，是没有返回值的，所以用null判断一下，null就返回success，
     * 如果有返回值，证明编译出错，需要显示出错信息，错误信息的格式是 error： 所以，这里简单处理一下，
     * 截取error以后的字符串
     * @throws IOException
     */



    /*
    //todo 添加代码的持久化，用户每次提交代码，都能够记录用户的提交记录和时间，代码信息 和 编译的结果等；
    * */

    @RequestMapping(value = {"/", ""}, method = RequestMethod.POST)
    public JsonResult compileCode(@RequestParam String code) throws IOException {
        Logger.info("before" + "\n" + code);

        String s = HtmlUtil.stripHtml(code);
        String ss = StringEscapeUtils.unescapeHtml4(s);
        ss =ss.replace(">",">"+"\n");
        Long submitTime = System.currentTimeMillis();
        String compile = compileCoreService.compile(ss, submitTime);

        Logger.info(compile);
//        String compile = null;
        if (compile == null || !compile.contains("error")) {
            Logger.info("[{}] compiled code [{}]", UserUtil.getUserId(servletRequest),code);
            return JsonResult.succResult("success");
        }
        int error = compile.lastIndexOf("error");
        if (error > 0) {
            String result = compile.substring(error);
            Logger.info("[{}] compiled code [{}]", UserUtil.getUserId(servletRequest),code);
            return JsonResult.succResult(result);
        }

        Logger.info("[{}] compiled code [{}]", UserUtil.getUserId(servletRequest),code);
        return JsonResult.succResult(compile);


    }


    /**
     * 这里给postman测试用，因为postman的发送数据没有传输转码的问题
     * @param code
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/postman"}, method = RequestMethod.POST)
    public JsonResult compileCodePostman(@RequestParam String code) throws IOException {
        Logger.info("before" + "\n" + code);
        Long submitTime = System.currentTimeMillis();
        String compile = compileCoreService.compile(code, submitTime);

        Logger.info(compile);
        if (compile == null || !compile.contains("error")) {
            return JsonResult.succResult("success");
        }
        int error = compile.lastIndexOf("error");
        if (error > 0) {
            String result = compile.substring(error);
            return JsonResult.succResult(result);
        }

        return JsonResult.succResult(compile);


    }
}
