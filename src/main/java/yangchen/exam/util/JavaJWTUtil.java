package yangchen.exam.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import yangchen.exam.model.TokenInfo;

import java.util.*;


//jwt工具类 将用户的登录信息进行加密

/**
 * @author yc
 */
public class JavaJWTUtil {
    public static String createToken() {
        Algorithm algorithm = Algorithm.HMAC256("yc");
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        Date nowDate = new Date();
        Date expireDate = getAfterDate(nowDate, 0, 0, 0, 2, 0, 0);

        String token = JWT.create()
                .withHeader(map)
                .withIssuer("SERVICE")
                .withSubject("this is test token")
                .withAudience("WEB")
                .withIssuedAt(nowDate)
                .withExpiresAt(expireDate)
                .sign(algorithm);
        return token;
    }


    public static String createTokenWithClaim(String id, String password) {
        Algorithm algorithm = Algorithm.HMAC256("yc");
        Map<String, Object> map = new HashMap<>();
        Date nowDate = new Date();
        Date expireDate = getAfterDate(nowDate, 0, 0, 0, 2, 0, 0);
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        String token = JWT.create()
                .withHeader(map)
                .withClaim("loginName", id)
                .withClaim("password", password)

                .withIssuer("SERVICE")
                .withSubject("this is test token")
                .withAudience("WEB")
                .withIssuedAt(nowDate)
                .withExpiresAt(expireDate)
                .sign(algorithm);
        return token;

    }

    public static String createTokenWithClaim(Long id, String password) {
        return createTokenWithClaim(String.valueOf(id), password);
    }

    public static Date getAfterDate(Date date, int year, int month, int day, int hour, int minute, int second) {
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        if (year != 0) {
            calendar.add(Calendar.YEAR, year);
        }
        if (month != 0) {
            calendar.add(Calendar.MONTH, month);
        }
        if (day != 0) {
            calendar.add(Calendar.DATE, day);
        }
        if (hour != 0) {
            calendar.add(Calendar.HOUR_OF_DAY, hour);
        }
        if (minute != 0) {
            calendar.add(Calendar.MINUTE, minute);
        }
        if (second != 0) {
            calendar.add(Calendar.SECOND, second);
        }
        return calendar.getTime();
    }

    public static TokenInfo verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256("yc");
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("SERVICE")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        String subject = jwt.getSubject();
        Map<String, Claim> claims = jwt.getClaims();
        Claim loginName = claims.get("loginName");
        Claim password = claims.get("password");
        System.out.println(loginName.asString());
        System.out.println(password.asString());
        List<String> audience = jwt.getAudience();
        System.out.println(subject);
        System.out.println(audience.get(0));
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setPassword(password.asString());
        tokenInfo.setUserId(loginName.asLong());
        return tokenInfo;
    }


    public static void main(String[] args) {
        String createToken = createTokenWithClaim(2015011446L, "19961012");
        System.out.println(createToken);
        System.out.println(verifyToken(createToken).toString());
    }


}
