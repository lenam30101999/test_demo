package tt.authenservice.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import tt.authenservice.config.RedissonConfig;
import tt.authenservice.entity.Token;

import java.util.Map;

public class CacheManager {

    @Autowired
    private static RedisTemplate redisTemplate;

    private long EXPIRES_IN_TWO_MINUTES = 2;

    public static void set(String key, String value) {
        if (key == null || value == null){
            return;
        }else {
            System.out.println(key + "____" + value);
            redisTemplate.opsForValue().set(key, value);
            System.out.println(key + "____" + value);

        }
    }

    public void setToken(Token user, String value){
//       set(GenerateKey.getUserKey(user.toString()), value, 120000);
        set("cz_2", value);
    }

//    public void remove(String key) {
//        map.remove(key);
//    }
//
//    public void size(){
//        System.out.println("Size: " + map.size());
//    }

//    public String get(String key) {
//        String value = map.get(key);
//
//
//        if (value != null && !object.isExpired()){
//            return object.toString();
//        }else if (object.isExpired()){
//            map.remove(key);
//            return null;
//        }else {
//            return null;
//        }
//    }
}
