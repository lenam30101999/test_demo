package tt.authenservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tt.authenservice.entity.authentication.Authen;

import java.util.Optional;

@Repository
public interface AuthenRepository extends MongoRepository<Authen, String> {
    Authen save(Authen authen);

    Optional<Authen> findById(String userId);

    void delete(Authen authen);
}
