package tt.authenservice.entity.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDTO {

    @JsonIgnore
    private String id;

    @JsonProperty("hometown")
    private String homeTown;

    @JsonProperty("phonenumber")
    private String phoneNumber;

    @JsonProperty("email")
    private String email;
}
