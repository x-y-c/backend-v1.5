package yangchen.exam.controller;


import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yangchen.exam.entity.QuestionNew;
import yangchen.exam.model.CompileFront;
import yangchen.exam.model.JsonResult;
import yangchen.exam.model.ResultCode;
import yangchen.exam.model.SourceCode;
import yangchen.exam.repo.QuestionRepo;
import yangchen.exam.service.compile.CompileCoreService;
import yangchen.exam.service.compileService.CompileService;

import java.io.IOException;

/**
 * @author yangchen
 */

@Api(value = "CompileController")
@RestController
@RequestMapping(value = "/compile", produces = MediaType.APPLICATION_JSON_VALUE)
public class CompileController {

    @Autowired
    private CompileCoreService compileCoreService;
    @Autowired
    private QuestionRepo questionRepo;
    Gson gson = new Gson();

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

    @ApiOperation(value = "获取编译结果")
    @ApiImplicitParams({@ApiImplicitParam(name = "code",value = "源代码",required = true,dataType = "String"),
                        @ApiImplicitParam(name = "examinationId",value = "试卷编号",required = true,dataType = "Integer")})
    @RequestMapping(value = {"/", ""}, method = RequestMethod.POST)
    public JsonResult compileTest(@RequestParam String code, @RequestParam Integer examinationId, @RequestParam Integer index, @RequestParam Integer studentId) {
        CompileFront compileFront = compileService.compileCode(examinationId, index, code, studentId);
        return JsonResult.succResult(compileFront);
    }

    public String add(String line,String memo){
        String result = "";
        String start = "/******start******/";
        String end = "/******end******/";
        int length1 = start.length();
        int index = line.indexOf(start);
        int index2 = line.indexOf(end);
        for(int i=0;i<index+length1;i++){
            result +=line.charAt(i);
        }
        String blank= memo;
        for(int i=0;i<blank.length();i++){
            result +=blank.charAt(i);
        }
        //index2=1
//        line.length()=3
        for(int i=index2;i<line.length();i++){
            result +=line.charAt(i);
        }
        System.out.println(result);
        return result;
    }

    @RequestMapping(value = "/sourceCode", method = RequestMethod.GET)
    public JsonResult compileSourceCode(@RequestParam String input, @RequestParam String questionBh) throws IOException, InterruptedException {
        Logger.info("the input =[{}],question=[{}]", input, questionBh);
        QuestionNew question = questionRepo.findByQuestionBh(questionBh);
        SourceCode sourceCode = gson.fromJson(question.getSourceCode(), SourceCode.class);
        String code = sourceCode.getKey().get(0).getCode();
        if ("100001".equals(question.getIsProgramBlank())){
            code = add(code, question.getMemo());
        }
        String filePath = compileCoreService.writeSourceCode(code);
        String compileResult = compileCoreService.compileCode();
        if (!StringUtils.isEmpty(compileResult)&&compileResult.indexOf("error")!=-1) {
            Logger.error("compileError =[{}]", compileResult);
            return JsonResult.errorResult(ResultCode.COMPILE_ERROR, "编译出错", compileResult);
        } else {
            String output = compileCoreService.getOutput(input);
            Logger.info("compileSuccess![{}]",output);
            return JsonResult.succResult(output);
        }

    }


}
