package security.jwt;

import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@ConfigurationProperties(prefix = "application.jwt")
@Getter @Setter
public class JwtConfig {
    private String secretKey;
    private String tokenPrefix;
    private int tokenExpirationAfterDays;
    private String authoritiesPrefix;
    private String singleAuthorityPrefix;

    public SecretKey getSecretKey(){
        StringBuilder preparedKey = new StringBuilder();
        for(int i=0;i<15;i++){
            preparedKey.append(secretKey);
        }
        return Keys.hmacShaKeyFor(preparedKey.toString().getBytes());
    }

    public String getAuthorizationHeader(){
        return HttpHeaders.AUTHORIZATION;
    }
}
