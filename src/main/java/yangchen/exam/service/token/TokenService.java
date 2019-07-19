package yangchen.exam.service.token;

import yangchen.exam.entity.StudentNew;

public interface TokenService {
    String getToken(StudentNew student);

}
