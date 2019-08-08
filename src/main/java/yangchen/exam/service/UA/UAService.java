package yangchen.exam.service.UA;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UAService {

public static Logger LOGGER = LoggerFactory.getLogger(UAService.class);
    public void testUa(String ua) throws IOException {
//       UASparser uaSparser =null;
//
//                uaSparser = new UASparser(OnlineUpdater.getVendoredInputStream());

        UserAgent uatest = UserAgentUtil.parse(ua);

      LOGGER.info(  uatest.getBrowser().toString());//Chrome
        LOGGER.info(uatest.getVersion());//14.0.835.163
        LOGGER.info(uatest.getEngine().toString());//Webkit
        LOGGER.info(uatest.getEngineVersion());//535.1
        LOGGER.info(uatest.getOs().getName());//Windows 7
        LOGGER.info(uatest.getPlatform().toString());//Windows
        LOGGER.info(ua);

    }

}
