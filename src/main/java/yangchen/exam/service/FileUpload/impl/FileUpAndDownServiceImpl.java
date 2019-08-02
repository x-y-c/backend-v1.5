package yangchen.exam.service.FileUpload.impl;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import yangchen.exam.Component.ImageProperties;
import yangchen.exam.model.ResultCode;
import yangchen.exam.service.FileUpload.FileUpAndDownService;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class FileUpAndDownServiceImpl implements FileUpAndDownService {

    @Autowired
    private ImageProperties imageProperties;


    @Value("${image.basePath}")
    String basedir;

    @Override
    public Map<String, Object> uploadPicture(MultipartFile file) throws ServiceException {
        try {
            Map<String, Object> resMap = new HashMap<>();
            String[] IMAGE_TYPE = imageProperties.getImageType().split(",");
            String path = null;
            boolean flag = false;
            for (String type : IMAGE_TYPE) {
                if (StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), type)) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                resMap.put("result", ResultCode.SUCCESS);
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                //获取文件类型
                String fileType = file.getContentType();
                //获取文件后缀名

                String imageName = fileType.substring(fileType.indexOf("/") + 1);

                String oldFileName = file.getOriginalFilename();

                String newFileName = uuid + "." + imageName;


                if (file.getSize() > imageProperties.getFileSize()) {

                    String newUUID = UUID.randomUUID().toString().replace("-", "");
                    newFileName = newUUID + "." + imageName;
                    path = imageProperties.getUpPath() + "/" + basedir + "/" + newUUID + "." + imageName;

                    File oldFile = new File(path);
                    if (!oldFile.getParentFile().exists()) {
                        oldFile.getParentFile().mkdir();
                    }
                    file.transferTo(oldFile);
                    Thumbnails.of(oldFile).scale(imageProperties.getScaleRatio()).toFile(path);
                    resMap.put("path", "/" + basedir + "/" + newUUID + "." + imageName);
                } else {
                    path = imageProperties.getUpPath() + "/" + basedir + "/" + uuid + "." + imageName;

                    File uploadFile = new File(path);
                    if (!uploadFile.getParentFile().exists()) {
                        uploadFile.getParentFile().mkdir();
                    }
                    file.transferTo(uploadFile);
                    resMap.put("path", "/" + basedir + "/" + uuid + "." + imageName);
                }
                resMap.put("oldFileName", oldFileName);
                resMap.put("newFileName", newFileName);
                resMap.put("fileSize", file.getSize());
                return resMap;
            } else {
                resMap.put("result", "图片格式不正确，支持png|jpg|jpeg");
            }
            return resMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }
}
