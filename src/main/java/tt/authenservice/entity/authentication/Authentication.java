package tt.authenservice.entity.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Document(collection = "authen")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Authentication {

    @Id
    private String id;

    private String token;

    private LocalDateTime expiration;
}
