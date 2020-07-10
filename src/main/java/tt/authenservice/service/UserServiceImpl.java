package tt.authenservice.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import tt.authenservice.encrypt.EnCryptMD5;
import tt.authenservice.entity.authentication.Authen;
import tt.authenservice.entity.profile.Profile;
import tt.authenservice.entity.profile.ProfileDTO;
import tt.authenservice.entity.user.User;
import tt.authenservice.entity.user.UserDTO;
import tt.authenservice.repository.AuthenRepository;
import tt.authenservice.repository.ProfileRepository;
import tt.authenservice.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AuthenRepository authenRepository;

    @Override
    @Caching(
            put = {@CachePut(value = "userCache", key = "#userDTO.id")},
            evict = {@CacheEvict(value = "profileCache", allEntries = true)}
    )
    public UserDTO createUser(UserDTO userDTO) {
        User saved = User.builder()
                .id(userDTO.getId())
                .userName(userDTO.getUserName())
                .passWord(convertPassword(userDTO.getPassWord()))
                .roleName(userDTO.getRoleName())
                .state(userDTO.getState())
                .build();

        saved = userRepository.save(saved);
        return convertToUserDTO(saved);
    }

    @Override
    @Caching(
            put = {@CachePut(value = "userCache", key = "#token")},
            evict = {@CacheEvict(value = "profileCache", allEntries = true)}
    )
    public UserDTO changePassword(String newPassword, String token) {
        String id = getIdUserFromAccessToken(token);
        User updated = findById(id);

        if (updated != null){
            updated.setPassWord(convertPassword(newPassword));
            userRepository.save(updated);

            return convertToUserDTO(updated);
        }else return null;
    }

    @Override
    @Caching(
            evict = {@CacheEvict(value = "userCache", key = "#accessToken")}
    )
    public UserDTO deleteUserByAccessToken(String accessToken) {
        String id = getIdUserFromAccessToken(accessToken);
        User deleted = findById(id);

        if (deleted != null){
            userRepository.deleteById(id);
            return convertToUserDTO(deleted);
        }else return null;
    }

    @Override
    public UserDTO findByUserName(String userName) {
        User user = userRepository.findByUserName(userName).orElse(null);
        return convertToUserDTO(user);
    }

    @Override
    @Cacheable(value = "userCache", key = "#user.id")
    public String getTokenByUsernameAndPassword(String username, String password) {
        User found = userRepository.findByUserName(username).orElse(null);

        if (found != null && matching(found.getPassWord(), password)){
            String token = generateToken(found);

            Authen authen = new Authen();
            authen.setId(found.getId());
            authen.setToken(token);
            authenRepository.save(authen);

            return token;
        }else return null;
    }

    @Override
    public ProfileDTO createProfileUser(ProfileDTO profileDTO, String accessToken) {
        String id = getIdUserFromAccessToken(accessToken);

        Profile saved = Profile.builder()
                .id(id)
                .phoneNumber(profileDTO.getPhoneNumber())
                .homeTown(profileDTO.getHomeTown())
                .email(profileDTO.getEmail())
                .build();
        saved = profileRepository.save(saved);
        return convertToProfileDTO(saved);
    }

    @Override
//    @Caching(
//            put = {@CachePut(value = "profileCache", key = "#profile.id")},
//            evict = {@CacheEvict(value = "userCache", allEntries = true)}
//    )
    public ProfileDTO updateProfileUser(ProfileDTO profileDTO, String accessToken) {
        String id = getIdUserFromAccessToken(accessToken);
        Profile profile = profileRepository.findById(id).orElse(null);
        Authen authen = authenRepository.findById(id).orElse(null);

        if (profile != null && accessToken.equals(authen.getToken())){
            profile.setEmail(profileDTO.getEmail());
            profile.setPhoneNumber(profileDTO.getPhoneNumber());
            profile.setHomeTown(profileDTO.getHomeTown());

            profileRepository.save(profile);
            return convertToProfileDTO(profile);
        }else return null;
    }

    @Override
//    @Cacheable(value = "profileCache", key = "#id")
    public ProfileDTO findProfileByAccessToken(String accessToken) {
        String id = getIdUserFromAccessToken(accessToken);
        Profile profile = profileRepository.findById(id).orElse(null);
        Authen authen = authenRepository.findById(id).orElse(null);

        if (profile != null && accessToken.equals(authen.getToken())){
            return convertToProfileDTO(profile);
        }else return null;
    }

    @Override
//    @Caching(
//            evict = {@CacheEvict(value = "profileCache", key = "#id")}
//    )
    public ProfileDTO deleteProfileByAccessToken(String accessToken) {
        String id = getIdUserFromAccessToken(accessToken);
        Profile deleted = profileRepository.findById(id).orElse(null);

        if (deleted != null){
            profileRepository.deleteById(id);
            return convertToProfileDTO(deleted);
        }else return null;
    }

    @Override
    public void deleteTokenWhenLogout(String accessToken) {
        String id = getIdUserFromAccessToken(accessToken);
        userRepository.deleteById(id);
    }

    private User findById(String id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null){
            return user;
        }else return null;
    }

    private String findAccessTokenByUserId(String userId) {
        Authen authen = authenRepository.findById(userId).orElse(null);
        String accessToken = authen.getToken();

        if (accessToken != null){
            return accessToken;
        }else return null;
    }

    private boolean matching(String password, String otherPassword){
        return password.equals(convertPassword(otherPassword));
    }

    private String convertPassword(String password){
        EnCryptMD5 encoderMD = new EnCryptMD5();
        return encoderMD.md5(password);
    }

    private String getIdUserFromAccessToken(String accessToken){
        List<String> array = Arrays.asList(accessToken.split("_"));
        return array.get(1);
    }

    private UserDTO convertToUserDTO(User user){
        UserDTO dto = new UserDTO();

        if (user != null){
            dto.setUserName(user.getUserName());
            dto.setPassWord(user.getPassWord());
            dto.setRoleName(user.getRoleName());
            dto.setState(user.getState());

            return dto;
        }else return null;
    }

    private ProfileDTO convertToProfileDTO(Profile profile){
        ProfileDTO dto = new ProfileDTO();

        if (profile != null){
            dto.setHomeTown(profile.getHomeTown());
            dto.setEmail(profile.getEmail());
            dto.setPhoneNumber(profile.getPhoneNumber());

            return dto;
        }else return null;
    }

    private String generateToken(User user)
    {
        String tokens = "c_user" + "_" + user.getId() + "_" + RandomStringUtils.randomAlphabetic(8);

        return tokens;
    }
}
