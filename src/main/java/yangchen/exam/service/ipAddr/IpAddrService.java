package yangchen.exam.service.ipAddr;

import yangchen.exam.entity.IpAddr;

import java.util.List;

public interface IpAddrService {

    List<IpAddr> searchIp(Integer examGroupId);

}
