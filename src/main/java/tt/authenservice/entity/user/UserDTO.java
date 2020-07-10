package tt.authenservice.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    @JsonIgnore
    private String id;

    @JsonProperty("username")
    private String userName;

    @JsonProperty("password")
    private String passWord;

    @JsonProperty("role")
    private RoleName roleName;

    @JsonProperty("state")
    private State state;

}
