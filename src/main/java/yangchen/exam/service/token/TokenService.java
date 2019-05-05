package yangchen.exam.service.token;

import yangchen.exam.entity.Student;

public interface TokenService {
    String getToken(Student student);

}
