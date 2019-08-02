package yangchen.exam.service.token.impl;

import org.springframework.stereotype.Service;
import yangchen.exam.entity.StudentNew;
import yangchen.exam.service.token.TokenService;
import yangchen.exam.util.JavaJWTUtil;

/**
 * @author yc
 */
@Service
public class TokenServiceImpl implements TokenService {
    @Override
    public String getToken(StudentNew student) {
        String tokenWithClaim = JavaJWTUtil.createTokenWithClaim(student.getStudentId(), student.getPassword());
        return tokenWithClaim;
    }
}
