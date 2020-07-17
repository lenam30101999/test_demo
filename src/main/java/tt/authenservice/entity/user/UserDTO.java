package tt.authenservice.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    @JsonIgnore
    private String id;

    @JsonProperty("username")
    @NotBlank(message = "username is not empty")
    @Size(min = 8, max = 80, message = "username more than 8 characters")
    private String userName;

    @JsonProperty("password")
    @NotBlank(message = "password is not empty")
    @Size(min = 6, max = 30, message = "password more than 6 characters")
    private String passWord;

    @JsonProperty("role")
    private RoleName roleName;

    @JsonProperty("state")
    private State state;

}
