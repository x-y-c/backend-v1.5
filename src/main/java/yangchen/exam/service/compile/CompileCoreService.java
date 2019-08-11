package yangchen.exam.service.compile;


import java.io.FileNotFoundException;
import java.io.IOException;

//编译核心部分
public interface CompileCoreService {

//    //1.解析文本，提取出sourceCode
//    String getSourceCode();

    //2.将sourceCode写入.c文件
    //return 文件路径
    String writeSourceCode(String sourceCode) throws IOException;

    String compileCode(String filepath) throws IOException;

    //3.调用runtime运行,传入input，得到output
    String getOutput(String filepath, String input);

    //4.将output写入数据库，返回到前端
    String persistenceToDataBase(String output, String questionBh);



}
