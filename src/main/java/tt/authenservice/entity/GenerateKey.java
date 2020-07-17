package tt.authenservice.entity;

import lombok.Getter;

@Getter
public class GenerateKey {
    public final static String PREFIX_KEY = "cz_";

    public static String getUserKey(String token){
        return PREFIX_KEY + "user_" + token;
    }

    public static String getUserProfile(String token){
        return PREFIX_KEY + "profile_" + token;
    }

}
