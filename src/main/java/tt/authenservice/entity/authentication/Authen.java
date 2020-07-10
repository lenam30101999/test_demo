package tt.authenservice.entity.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection = "authen")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Authen {

    @Id
    private String id;

    private String token;

}
