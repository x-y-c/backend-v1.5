package yangchen.exam.service.biz;

import yangchen.exam.entity.Student;

public interface TokenService {
    String getToken(Student student);

}
