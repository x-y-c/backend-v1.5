package yangchen.exam.controller;

import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yangchen.exam.entity.Videourl;
import yangchen.exam.repo.newVideoRepo;
import yangchen.exam.util.MinioClientUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;


@RestController
public class TestController {
    @Autowired
    private newVideoRepo newvideoRepo;

    private String bucketName = "video";

    @GetMapping("test")
    public String  test2(HttpServletResponse response) throws Exception {
        MinioClientUtil minioClientUtil = new MinioClientUtil();
        String bucketName = "video";
        System.out.println(minioClientUtil.getConn().bucketExists(bucketName));
        return "test";
    }

    @GetMapping("/video/findAll")
    public List<Videourl> findAll(){
        return newvideoRepo.findAll();
    }

    @PostMapping("upload")
    public String upload(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String url = "http://119.3.217.233:10000/video/" + originalFilename;
            newvideoRepo.insertUrl(url);
            MinioClient minioClient = new MinioClientUtil().getConn();
            String bucketName = "video";
            InputStream inputStream = file.getInputStream();
            String contentType = file.getContentType();
            long size = file.getSize();

            PutObjectOptions options = new PutObjectOptions(size, -1);
            options.setContentType(contentType);
            minioClient.putObject(bucketName, originalFilename,inputStream, options);
            return "上传成功";
        } catch (Exception ex) {
            return "上传失败" + ex.getMessage();
        }
    }

    @GetMapping("download")
    public void download(@RequestParam String fileName, HttpServletResponse response) throws UnsupportedEncodingException {

        MinioClient minioClient = new MinioClientUtil().getConn();
        try {
            minioClient.statObject(bucketName, fileName); // 如果不存在, statObject()抛出异常
            InputStream stream = minioClient.getObject(bucketName, fileName);
            ServletOutputStream outputStream = response.getOutputStream();
            response.setHeader("filename",fileName);
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"),"ISO8859-1"));
            // 读取输入流直到EOF并打印到控制台。
            byte[] buf = new byte[16384];
            int bytesRead;
            while ((bytesRead = stream.read(buf, 0, buf.length)) >= 0) {
                outputStream.write(buf,0,bytesRead);
            }
            outputStream.flush();
            // 关闭流，此处为示例，流关闭最好放在finally块。
            outputStream.close();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//
//    @PostMapping("/save")
//    public String save(@RequestBody Videourl videourl){
//        Videourl result = newvideoRepo.save(videourl);
//        if(result != null){
//            return "success";
//        }else {
//            return "error";
//        }
//    }

