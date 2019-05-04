package yangchen.exam.service.http;

import java.util.Map;

/**
 * @author YC
 * @date 2019/5/4 10:48
 * O(∩_∩)O)
 */
public interface IOkhttpService {

    String get(String url);

    String postJsonBody(String url, String body);

    String postForm(String url, Map<String, String> formData);

    String post(String url, Map<String, String> params);

    String get(String url, Map<String, String> params);

    String post(String url);
}
