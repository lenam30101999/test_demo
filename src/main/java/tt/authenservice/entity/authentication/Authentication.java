package tt.authenservice.entity.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import tt.authenservice.entity.BaseEntity;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Document(collection = "authen")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Authentication extends BaseEntity {

    @Id
    private String id;

    private String token;

//    private String refreshToken;

    private LocalDateTime expiration;
}
