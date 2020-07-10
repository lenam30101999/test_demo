package tt.authenservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tt.authenservice.entity.authentication.Authentication;

import java.util.Optional;

@Repository
public interface AuthenticationRepository extends MongoRepository<Authentication, String> {
    Authentication save(Authentication authentication);

    Optional<Authentication> findById(String userId);

    void delete(Authentication authentication);
}
