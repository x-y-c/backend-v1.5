package yangchen.exam.model;


import lombok.Data;

/**
 * @author YC
 */
@Data
public class JsonResult {
    private String code;
    private String msg;
    private Object data;

    public JsonResult(Object data) {
        this("succ", data);
    }

    public JsonResult(String msg, Object data) {
        this(ResultCode.SUCCESS, msg, data);
    }

    public JsonResult(String code, String msg, Object data) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static JsonResult succResult(String msg, Object data) {
        return new JsonResult(ResultCode.SUCCESS, msg, data);
    }

    public static JsonResult succResult(Object data) {
        return JsonResult.succResult("成功", data);
    }


    public static JsonResult errorResult(String code, String msg, Object data) {
        return new JsonResult(code, msg, data);
    }


}
