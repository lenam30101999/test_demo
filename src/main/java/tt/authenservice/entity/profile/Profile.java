package tt.authenservice.entity.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import tt.authenservice.entity.BaseEntity;

import javax.persistence.Id;

@Document(collection = "profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile extends BaseEntity {

    @Id
    private String id;

    private String homeTown;

    private String phoneNumber;

    private String email;

}
