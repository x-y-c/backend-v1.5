package yangchen.exam.service.compile;


import java.io.IOException;

//编译核心部分
public interface CompileCoreService {

    //传入代码，进行编译
    String compile(String code,Long submitTime) throws IOException;



}
