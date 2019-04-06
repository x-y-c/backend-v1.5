package yangchen.exam.service.biz.impl;

import org.springframework.stereotype.Service;
import yangchen.exam.entity.Student;
import yangchen.exam.service.biz.TokenService;
import yangchen.exam.util.JavaJWTUtil;

/**
 * @author yc
 */
@Service
public class TokenServiceImpl implements TokenService {
    @Override
    public String getToken(Student student) {
        String tokenWithClaim = JavaJWTUtil.createTokenWithClaim(student.getStudentId(), student.getPassword());
        return tokenWithClaim;
    }
}
