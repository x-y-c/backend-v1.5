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

        for (IpAddr ipAddr : ipAddrList) {
            int studentNumber = ipAddr.getStudentId();
            String ipAddress = ipAddr.getIpAddress();
            Set<String> ipAddressSet = new TreeSet<>();
            if (hash.get(studentNumber) == null) {

                ipAddressSet.add(ipAddress);
                hash.put(studentNumber, ipAddressSet);
            } else {
                ipAddressSet = hash.get(studentNumber);
                ipAddressSet.add(ipAddress);
                hash.put(studentNumber, ipAddressSet);
            }
        }

        HashMap<Integer, Set<String>> result = new HashMap<>();
        //现在这个hash里面存的是所有学生对应的所有ip
        //下一步应该筛选有问题的数据

        Iterator maplist = hash.entrySet().iterator();
        while (maplist.hasNext()) {
            Map.Entry<Integer, Set<String>> entry = (Map.Entry<Integer, Set<String>>) maplist.next();
            if (entry.getValue().size() > 1) {
                result.put(entry.getKey(), entry.getValue());
            }
        }

        return result;
    }

    public List<IpAddr> searchIps(Integer examGroupId) {
        /**
         * 1.先通过examGroupId查询所有的ip；
         * 然后用hashMap过滤，set去重复ipAddr对象 ，即重复的ipAddr对象算一个
         * ！！！！！！！！！！！！！！！！！
         * ！！！！！！！！！！！！！！！！！！！！！！！！！！！！！重点在下面！！！！！！！！！！！！！！！！！！！！！
         * ！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
         *
         * 如果在筛选之后呢，同一个student的ip还是多于一个，则证明是异常的状态，需要返回给前端；
         */
        List<IpAddr> result = new ArrayList<>();
        List<IpAddr> ipAddrList = ipAddrRepo.findByExamGroupId(examGroupId);
        HashMap<Integer, Set<IpAddr>> checkList = new HashMap<>();
        for (IpAddr ipAddr : ipAddrList) {
            if (checkList.get(ipAddr.getStudentId())==null) {
                Set<IpAddr> ipAddrSet = new HashSet<>();
                ipAddrSet.add(ipAddr);
                checkList.put(ipAddr.getStudentId(), ipAddrSet);
            } else {
                checkList.get(ipAddr.getStudentId()).add(ipAddr);
            }
        }
        Iterator maplist = checkList.entrySet().iterator();
        while (maplist.hasNext()) {
            Map.Entry<Integer, Set<IpAddr>> entry = (Map.Entry<Integer, Set<IpAddr>>) maplist.next();
            if (entry.getValue().size()>1){
                result.addAll(entry.getValue());
            }
        }
        return result;
    }

    /**
     * 这里解释一下喽：
     * 首先，Java是如何判断对象相等的？ 例如 IpAddr1和IpAddr2 这两个对象
     *
     Ｏ(≧口≦)Ｏ   其实啊，就算IpAddr1 和IpAddr2 完全相同（就是那种 连id都相同的那种），
     Java默认也会认为这两个对象不同的

     Σ(っ °Д °;)っ 为什么？ Java 默认会通过 equals（）方法判断这两个对象是不是相同，equals 默认比较的是 两个对象的内存指针（内存地址）
     内存地址不同，就算内容相同也是 false的

     所以啊，是equals方法不好用了，（至少不适合这里的比较<哼></哼>）,所以，要重写equals方法了， 这里的需求是 ，ip地址相同即为对象相同，
     （嗯，是这么个意思）

     最后，在 ipAdrr中重写 equals 和 hashCode 方法就ok了


     <哼>
     @Override
     public boolean equals(Object o) {
     if (this == o) return true;
     if (o == null || getClass() != o.getClass()) return false;
     IpAddr ipAddr = (IpAddr) o;
     return ipAddress != null ? ipAddress.equals(ipAddr.ipAddress) : ipAddr.ipAddress == null;
     }
     @Override
     public int hashCode() {
     return ipAddress != null ? ipAddress.hashCode() : 0;
     }
     </哼>




     *
     *
     */
}
