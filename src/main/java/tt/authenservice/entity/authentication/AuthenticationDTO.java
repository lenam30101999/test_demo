package tt.authenservice.entity.authentication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationDTO {

    @JsonIgnore
    private String id;

    @JsonProperty("access_token")
    private String token;

//    @JsonProperty("refresh_token")
//    private String refreshToken;

    @JsonProperty("expiries")
    private LocalDateTime expiration;
}
