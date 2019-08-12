package yangchen.exam.service.compile;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompileCoreServiceImpl implements CompileCoreService {
    /**
     * 代码的保存路径放在配置文件中，方便修改，通过$sourceCode.path 来调用；
     */
    @Value("${sourceCode.path}")
    private String filePath;

    //        /home/code/main.c
//        /home/code/main
    @Override
    public String writeSourceCode(String sourceCode) throws IOException {

        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        fileOutputStream.write(sourceCode.getBytes());
        fileOutputStream.close();
        return filePath;
    }

    @Override
    public String compileCode() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File("/home/yangchen/sourceCode"));
        List<String> commands = new ArrayList();
        commands.add("gcc");
        commands.add("-o");
        commands.add("main");
        commands.add("main.c");
        processBuilder.command(commands);
        Process exec = processBuilder.start();
        InputStream inputStream = exec.getErrorStream();
        /**
         * BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8")); // 实例化输入流，并获取网页代码
         * String s; // 依次循环，至到读的值为空
         * StringBuilder sb = new StringBuilder();
         * while ((s = reader.readLine()) != null) {
         * sb.append(s);
         * }
         * reader.close();
         * String str = sb.toString();
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String s = null;
        StringBuilder stringBuilder = new StringBuilder();
        while ((s = reader.readLine()) != null) {
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }

    @Override
    public String getOutput(String input) throws IOException {

        ProcessBuilder builder = new ProcessBuilder();
        builder.directory(new File("/home/yangchen/sourceCode"));
        builder.command("./main");

        Process start = builder.start();
        InputStream inputStream = start.getInputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(start.getOutputStream()));
        bufferedWriter.write(input);
        bufferedWriter.close();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = null;
        String tmp = "";
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }


    @Override
    public String persistenceToDataBase(String output, String questionBh) {
        return null;
    }
}
