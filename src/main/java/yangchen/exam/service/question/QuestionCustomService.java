package yangchen.exam.service.question;

import afu.org.checkerframework.checker.oigj.qual.O;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.Enum.DifficultEnum;
import yangchen.exam.repo.QuestionStatisticalRepo;
import yangchen.exam.repo.QuestionTableRepo;

import java.util.*;

import static java.lang.Integer.valueOf;

@Service
public class QuestionCustomService {
    @Autowired
    private QuestionStatisticalRepo questionStatisticalRepo;
    private QuestionTableRepo questionTableRepo;

    public QuestionCustomService(QuestionTableRepo questionTableRepo) {
        this.questionTableRepo = questionTableRepo;
    }

    public List<Object> makeChart(String value) {
        //String[] data1 = new String[5];
        List<Object> data1 = new ArrayList<>();
//        Map<String, Integer> result = new HashMap<String, Integer>();
        Map<String, Integer> result = new LinkedHashMap<>();
        for(DifficultEnum e : DifficultEnum.values()) {
            result.put(e.getDifficultName(), this.questionStatisticalRepo.countnum(value, ""+e.getDifficultCode()));
           //System.out.println(result);
        }

        for(String key : result.keySet())  {
            String data = key + result.get(key);
            //System.out.println(data);
            data1.add(data);
            //System.out.println(data);
        }
        System.out.println(data1);
        return data1;
    }

    public List<Object> getTable(String value){
        List<String> data = Arrays.asList(value.split(","));
        List<Object> data1 = this.questionTableRepo.showSearch(data.get(0), data.get(1), data.get(2));
        System.out.println(data1);
        return data1;
    }


}
