package yangchen.exam.service.compile;

import org.springframework.stereotype.Service;
import yangchen.exam.service.compile.CompileCoreService;

import java.io.*;
import java.util.ArrayList;

@Service
public class CompileCoreServiceImpl implements CompileCoreService {


    private String path = "/home/yangchen/coding/examCode";
    private String codeFileName = "Main.c";

    @Override
    public String compile(String code, Long submitTime) throws IOException {
        String codeDir = String.format("%s/%s", path,submitTime);
        String codeName = String.format("%s/%s", codeDir, codeFileName);
        File codeFile = new File(codeName);
        if (!codeFile.exists()) {
            codeFile.getParentFile().mkdirs();
            codeFile.createNewFile();
        }
        FileWriter writer = new FileWriter(codeFile);
        writer.write(code);
        writer.flush();
        ArrayList<String> compileCommands = new ArrayList<>();

        compileCommands.add("gcc");
        compileCommands.add("-c");
//        compileCommands.add("/home/yangchen/coding/examCode/Main.c");
        compileCommands.add(String.format("%s/Main.c", codeDir));

        ProcessBuilder processBuilder = new ProcessBuilder(compileCommands);
        processBuilder.directory(new File(codeDir));
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");
        }

        return output.toString().isEmpty() ? null : output.toString();


    }
}
