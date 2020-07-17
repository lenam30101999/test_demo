package tt.authenservice.entity;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import tt.authenservice.encrypt.EnCryptMD5;

@Data
public class Token {

    private String userId;

    private String token = EnCryptMD5.md5(RandomStringUtils.randomAlphabetic(8));

    private long expiryTime;

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
    
    public Token(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return userId + "_" + token;
    }
}
