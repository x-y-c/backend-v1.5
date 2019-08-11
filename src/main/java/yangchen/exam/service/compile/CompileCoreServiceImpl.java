package yangchen.exam.service.compile;

import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class CompileCoreServiceImpl implements CompileCoreService {
    //        @Value("${sourceCode.path}")
    private String filePath = null;

    //        /home/code/main.c
//        /home/code/main
    @Override
    public String writeSourceCode(String sourceCode) throws IOException {

        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        fileOutputStream.write(sourceCode.getBytes());
        return filePath;
    }

    @Override
    public String compileCode(String filepath) throws IOException {
//        Process exec = Runtime.getRuntime().exec("gcc -o main "+filepath);
//        exec.
        return null;
    }

    @Override
    public String getOutput(String filepath, String input) {
        return null;
    }

    @Override
    public String persistenceToDataBase(String output, String questionBh) {
        return null;
    }
}
