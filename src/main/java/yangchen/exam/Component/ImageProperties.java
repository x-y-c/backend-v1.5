package yangchen.exam.Component;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class ImageProperties {


    @Value("${image.fileSize}")
    private long fileSize;

    @Value("${image.scaleRatio}")
    private double scaleRatio;


    @Value("${image.upload}")
    private String upPath;

    @Value("${image.type}")
    private String imageType;
}
