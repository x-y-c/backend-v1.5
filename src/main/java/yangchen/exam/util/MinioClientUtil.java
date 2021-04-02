package yangchen.exam.util;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;

public class MinioClientUtil {
    public MinioClient getConn(){
        MinioClient minioClient = null;
        try {
//            minioClient = new MinioClient("http://119.3.217.233", 10000, "AKIAIOSFODNN7EXAMPLE", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY",false);
            minioClient = new MinioClient("http://59.64.78.95", 9000, "bistu", "bistu666",false);
            return minioClient;
        } catch (InvalidEndpointException e) {
            e.printStackTrace();
        } catch (InvalidPortException e) {
            e.printStackTrace();
        }finally {
            return minioClient;
        }

    }
}