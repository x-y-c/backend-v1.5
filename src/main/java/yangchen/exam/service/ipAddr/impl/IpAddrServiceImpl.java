package yangchen.exam.service.ipAddr.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.IpAddr;
import yangchen.exam.repo.IpAddrRepo;
import yangchen.exam.service.ipAddr.IpAddrService;

import java.util.*;

@Service
public class IpAddrServiceImpl implements IpAddrService {

    @Autowired
    private IpAddrRepo ipAddrRepo;


    @Override
    public HashMap<Integer, Set<String>> searchIp(Integer examGroupId) {

        HashMap<Integer, Set<String>> hash = new HashMap<>();

        List<IpAddr> ipAddrList = ipAddrRepo.findByExamGroupId(examGroupId);
        //todo  加油，我洗澡去了 哈哈哈哈好的

        for(IpAddr ipAddr : ipAddrList){
            int studentNumber = ipAddr.getStudentId();
            String ipAddress = ipAddr.getIpAddr();
            Set<String> ipAddressSet = new TreeSet<>();
            if(hash.get(studentNumber)==null){

                ipAddressSet.add(ipAddress);
                hash.put(studentNumber,ipAddressSet);
            }
            else{
                ipAddressSet = hash.get(studentNumber);
                ipAddressSet.add(ipAddress);
                hash.put(studentNumber,ipAddressSet);
            }
        }

        HashMap<Integer, Set<String>> result = new HashMap<>();
        //现在这个hash里面存的是所有学生对应的所有ip
        //下一步应该筛选有问题的数据

        Iterator maplist = hash.entrySet().iterator();
        while (maplist.hasNext()){
            Map.Entry<Integer, Set<String>> entry = (Map.Entry<Integer, Set<String>>) maplist.next();
            if(entry.getValue().size()>1){
                result.put(entry.getKey(),entry.getValue());
            }
        }

        return result;
    }
}
