package yangchen.exam.service.submit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.Enum.StageEnum;
import yangchen.exam.repo.StudentStatisticalRepo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentStatisticalService {
    @Autowired
    private StudentStatisticalRepo studentStatisticalRepo;

    public List<Object> makeChart(Integer userId) {
        List<Object> data1 = new ArrayList<>();
        Map<String, Integer> result = new LinkedHashMap<>();
        //Integer result = new Integer(userId);
        for(StageEnum e : StageEnum.values()) {
            result.put(e.getStageName(), this.studentStatisticalRepo.countstage(userId,e.getStageCode()));
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


}


