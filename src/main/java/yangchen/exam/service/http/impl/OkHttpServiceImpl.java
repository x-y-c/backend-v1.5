package yangchen.exam.service.http.impl;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import yangchen.exam.service.http.OkhttpService;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author YC
 * @date 2019/5/4 10:52
 * O(∩_∩)O)
 */

@Service
public class OkHttpServiceImpl implements OkhttpService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OkHttpServiceImpl.class);
    private static final OkHttpClient okhttpClient = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

    public static MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    public String get(String url) {
        Long ts = LocalDate.now().toEpochDay();
        LOGGER.info("Call GET url=[{}] ts=[{}] ", url, ts);
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            Response response = okhttpClient.newCall(request).execute();
            String result = response.body().string();
            LOGGER.info("Call GET url=[{}] ts=[{}] code=[{}], result=[{}]", url, ts, response.code(), result);
            return result;
        } catch (Exception e) {
            LOGGER.error("Call GET url=[{}] ts=[{}] e=[{}]", url, ts, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public String postJsonBody(Integer studentId,String url, String body) {
        Long ts = LocalDate.now().toEpochDay();
        LOGGER.info("学生[{}]提交 Call POST json url=[{}] ts=[{}] body=[{}]",studentId, url, ts, body);
        try {
            RequestBody requestBody = RequestBody.create(JSON, body);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            Response response = okhttpClient.newCall(request).execute();
            String result = response.body().string();
            LOGGER.info("学生[{}]提交 Call POST json url=[{}] ts=[{}] code=[{}] result=[{}]",studentId, url, ts, response.code(), result);
            return result;
        } catch (Exception e) {
            LOGGER.error("学生[{}]提交 Call POST json url=[{}] ts=[{}] e=[{}]",studentId, url, ts, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public String postForm(String url, Map<String, String> formData) {
        Long ts = LocalDate.now().toEpochDay();
        LOGGER.info("Call POST form url=[{}] ts=[{}] body=[{}]", url, ts, formData);
        try {
            FormBody.Builder builder = new FormBody.Builder();
            formData.forEach((k, v) -> builder.add(k, v));
            RequestBody requestBody = builder.build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            Response response = okhttpClient.newCall(request).execute();
            String result = response.body().string();
            LOGGER.info("Call POST json url=[{}] ts=[{}] code=[{}] result=[{}]", url, ts, response.code(), result);
            return result;
        } catch (Exception e) {
            LOGGER.error("Call POST form url=[{}] ts=[{}] e=[{}]", url, ts, e.getMessage(), e);
            return null;
        }
    }

    private HttpUrl buildHttpUrl(String url, Map<String, String> params) {
        HttpUrl oldUrl = HttpUrl.parse(url);
        HttpUrl.Builder urlBuilder = new HttpUrl.Builder();
        assert oldUrl != null;
        urlBuilder.scheme(oldUrl.scheme());
        urlBuilder.host(oldUrl.host());
        urlBuilder.port(oldUrl.port());
        oldUrl.pathSegments().forEach(urlBuilder::addPathSegment);
        params.forEach(urlBuilder::addEncodedQueryParameter);
        return urlBuilder.build();
    }

    @Override
    public String post(String url, Map<String, String> params) {
        return null;
    }

    @Override
    public String get(String url, Map<String, String> params) {
        HttpUrl httpUrl = buildHttpUrl(url, params);
        return get(httpUrl.toString());
    }

    @Override
    public String post(String url) {
        return null;
    }
}
