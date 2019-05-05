package yangchen.exam.controller;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yangchen.exam.model.CompileModel;
import yangchen.exam.model.CompileResult;
import yangchen.exam.model.JsonResult;
import yangchen.exam.service.biz.CompileCoreService;
import yangchen.exam.service.http.IOkhttpService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private IOkhttpService okhttpService;
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
//    @RequestMapping(value = {"/", ""}, method = RequestMethod.POST)
//    public JsonResult compileCode(@RequestParam String code) throws IOException {
//        Logger.info("before" + "\n" + code);
//
//        String s = HtmlUtil.stripHtml(code);
//        String ss = StringEscapeUtils.unescapeHtml4(s);
//        ss = ss.replace(">", ">" + "\n");
//        Long submitTime = System.currentTimeMillis();
//        String compile = compileCoreService.compile(ss, submitTime);
//
//        Logger.info(compile);
////        String compile = null;
//        if (compile == null || !compile.contains("error")) {
//            Logger.info("[{}] compiled code [{}]", UserUtil.getUserId(servletRequest), code);
//            return JsonResult.succResult("success");
//        }
//        int error = compile.lastIndexOf("error");
//        if (error > 0) {
//            String result = compile.substring(error);
//            Logger.info("[{}] compiled code [{}]", UserUtil.getUserId(servletRequest), code);
//            return JsonResult.succResult(result);
//        }
//
//        Logger.info("[{}] compiled code [{}]", UserUtil.getUserId(servletRequest), code);
//        return JsonResult.succResult(compile);
//    }


    /**
     * 更新注释，首先，前端传来的代码，可以不被转义，使用codeMirror组件就可以避免不必要的转义，
     * 其次，编译项目为了保证安全，不和该项目放在一起，通过http请求来调用，
     * 第三，从前端收到的代码，json序列化的时候，gson默认会进行转义，这里，使用GsonBuilder ，声明不要做编码转义就可以了；
     *
     * @param code
     * @return
     */
    @RequestMapping(value = {"/", ""}, method = RequestMethod.POST)
    public JsonResult comileTest(@RequestParam String code) {
        Logger.info("!!!!!!!!!!!!!!!!!!!!!!!!!!______________" + code + "\n");

        CompileModel compileModel = new CompileModel();
        List<String> input = new ArrayList<>();

        //todo 之后完成添加测试用例的方法，现在写成固定的；
        input.add("");

        compileModel.setInput(input);

        List<String> output = new ArrayList<>();
        output.add("hello world");

        compileModel.setOutput(output);

        compileModel.setTimeLimit(1000L);
        compileModel.setJudgeId(1);
        compileModel.setSrc(code);
        compileModel.setMemoryLimit(65535L);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.disableHtmlEscaping();

        Gson gson = gsonBuilder.create();
        String s = gson.toJson(compileModel);
        String s1 = okhttpService.postJsonBody("http://acm.swust.edu.cn/OnlineJudge/judge.do", s);

        CompileResult compileResult = gson.fromJson(s1, CompileResult.class);
        Logger.info(compileResult.toString());
        if (compileResult.getGlobalMsg() == null) {

            return JsonResult.succResult(1);
        } else {
            return JsonResult.succResult(compileResult.getGlobalMsg());
        }

    }


    /**
     * 这里给postman测试用，因为postman的发送数据没有传输转码的问题
     *
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
