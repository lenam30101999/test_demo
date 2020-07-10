package tt.authenservice.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tt.authenservice.entity.profile.Profile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(value = "id",
                allowGetters = true)
public class UserDTO {

    private String id;

    @JsonProperty("username")
    private String userName;

    @JsonProperty("password")
    private String passWord;

    @JsonProperty("role")
    private RoleName roleName;

    @JsonProperty("state")
    private State state;

    private Profile profile;
}
