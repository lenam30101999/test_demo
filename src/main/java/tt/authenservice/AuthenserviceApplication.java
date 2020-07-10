package tt.authenservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class AuthenserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenserviceApplication.class, args);
    }

}
