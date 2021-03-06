package tt.authenservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tt.authenservice.entity.user.User;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findById(String id);

    Optional<User> findByUserName(String userName);

    void deleteById(String userId);

    User save(User user);
}
