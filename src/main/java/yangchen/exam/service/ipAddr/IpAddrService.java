package yangchen.exam.service.ipAddr;

import yangchen.exam.entity.IpAddr;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface IpAddrService {

    HashMap<Integer, Set<String>> searchIp(Integer examGroupId);

    List<IpAddr> searchIps(Integer examGroupId);

}
