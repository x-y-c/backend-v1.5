package yangchen.exam.controller;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yangchen.exam.Enum.QuestionTypeEnum;
import yangchen.exam.entity.QuestionNew;
import yangchen.exam.entity.TestCase;
import yangchen.exam.model.*;
import yangchen.exam.repo.QuestionRepo;
import yangchen.exam.service.compile.CompileCoreService;
import yangchen.exam.service.compileService.CompileService;
import yangchen.exam.service.http.OkhttpService;
import yangchen.exam.service.question.QuestionService;
import yangchen.exam.util.JavaJWTUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yangchen
 */

@Api(value = "CompileController")
@RestController
@RequestMapping(value = "/compile", produces = MediaType.APPLICATION_JSON_VALUE)
public class CompileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompileController.class);
    @Autowired
    private CompileCoreService compileCoreService;
    @Autowired
    private QuestionRepo questionRepo;
    @Autowired
    private QuestionService questionService;

    @Autowired
    private OkhttpService okhttpService;

    @Value("${compile.url.path}")
    private String compileUrl;

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
    public JsonResult compileTest(@RequestParam String code,
                                  @RequestParam(required = false) Integer examinationId,
                                  @RequestParam(required = false) Integer index,
                                  @RequestParam Integer studentId,
                                  @RequestParam(required = false) String questionBh) {
        /*Test*/
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                LOGGER.info("start === compileFront");
//                CompileFront compileFront = compileService.compileCode(examinationId, index, code, studentId, questionBh);
//            }
//        };
//
//        //Countdownlatch
//        for(int i = 0; i < 50; i++){//10-20
//            Thread thread = new Thread(runnable);
//            thread.start();
//        }
        /*Test*/

        CompileFront compileFront = compileService.compileCode(examinationId, index, code, studentId, questionBh);
        return JsonResult.succResult(compileFront);
    }

//    public String add(String line,String memo){
//        String result = "";
//        String start = "/******start******/";
//        String end = "/******end******/";
//        int length1 = start.length();
//        int index = line.indexOf(start);
//        int index2 = line.indexOf(end);
//        for(int i=0;i<index+length1;i++){
//            result +=line.charAt(i);
//        }
//        String blank= memo;
//        for(int i=0;i<blank.length();i++){
//            result +=blank.charAt(i);
//        }
//        //index2=1
//        //line.length()=3
//        for(int i=index2;i<line.length();i++){
//            result +=line.charAt(i);
//        }
//        System.out.println(result);
//        return result;
//    }


    @RequestMapping(value = "/sourceCode", method = RequestMethod.GET)
    public JsonResult compileSourceCode(@RequestParam String input, @RequestParam String questionBh) throws IOException, InterruptedException {
        Logger.info("the input =[{}],question=[{}]", input, questionBh);
        QuestionNew question = questionRepo.findByQuestionBh(questionBh);
        SourceCode sourceCode = gson.fromJson(question.getSourceCode(), SourceCode.class);
        String code = sourceCode.getKey().get(0).getCode();
        if ("100001".equals(question.getIsProgramBlank())){
            code = question.getMemo();
//            code = add(code, question.getMemo());
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


    /***
     * 检测题库中题目的正确与否
     * @return
     */
    @RequestMapping(value = "/cccheck",method = RequestMethod.GET)
    public JsonResult cccheck(){
        List<Integer> passNo = new ArrayList<>();
        List<Integer> errorNo = new ArrayList<>();
        List<QuestionNew> questionList = new ArrayList<>();
        try {
            questionList = questionRepo.findByQuestionTypeAndActivedIsTrue(QuestionTypeEnum.getQuestionTypeCode("编程题"));
        }catch (NullPointerException e){
            return JsonResult.errorResult(ResultCode.QUESTION_NUM_ERROR,"该题型题目数为0","空");
        }
        List<TestCase> TestCaseList = new ArrayList<>();
        for(QuestionNew question:questionList){
            //todo 代码填空和代码编程题的区分
            String src = "";
            if ("100001".equals(question.getIsProgramBlank())){
                src = question.getMemo();
            }
            else{
                SourceCode sourceCode = gson.fromJson(question.getSourceCode(), SourceCode.class);
                src = sourceCode.getKey().get(0).getCode();
            }

            CompileModel compileModel = new CompileModel();
            List<String> input = questionService.getQuestionInput(question.getQuestionBh());
            List<String> output = questionService.getQuestionOutput(question.getQuestionBh());

            compileModel.setInput(input);
            compileModel.setOutput(output);
            compileModel.setJudgeId(1);
            compileModel.setSrc(src);
            compileModel.setMemoryLimit(65535L);
            compileModel.setTimeLimit(1000L);

            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            gsonBuilder.disableHtmlEscaping();
            String compileModelJson = gson.toJson(compileModel);
            Logger.info(compileModelJson);
            String jsonResult = okhttpService.postJsonBody(-1,compileUrl, compileModelJson);
            Logger.info(jsonResult);
            CompileResult compileResult = gson.fromJson(jsonResult, CompileResult.class);
            if (compileResult.getResult() != null) {
                //for (Result result : compileResult.getResult()) {
                for (int i = 0; i < compileResult.getResult().size(); i++) {

                    if (compileResult.getResult().get(i).getResult().equals("0") || compileResult.getResult().get(i).getResult().equals("1")) {
                        //return JsonResult.succResult(compileResult.getResult().get(i).getResult());
                        passNo.add(question.getId());
                    }
                    else {
                        //return JsonResult.errorResult(ResultCode.TESTCASE_NOT_SAME,"测试用例不一致",question.getId());
                        errorNo.add(question.getId());
                    }
                }
            }
        }
        //return JsonResult.errorResult(ResultCode.QUESTION_NUM_ERROR,"该题型题目数为0","空");

        JavaJWTUtil.removeDuplicate(errorNo);
        return JsonResult.succResult(errorNo.toString());

    }
}
