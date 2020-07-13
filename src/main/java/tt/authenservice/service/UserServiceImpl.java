package tt.authenservice.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import tt.authenservice.encrypt.EnCryptMD5;
import tt.authenservice.entity.authentication.Authentication;
import tt.authenservice.entity.authentication.AuthenticationDTO;
import tt.authenservice.entity.profile.Profile;
import tt.authenservice.entity.profile.ProfileDTO;
import tt.authenservice.entity.user.User;
import tt.authenservice.entity.user.UserDTO;
import tt.authenservice.repository.AuthenticationRepository;
import tt.authenservice.repository.ProfileRepository;
import tt.authenservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Override
    @Caching(
            put = {@CachePut(value = "userCache", key = "#userDTO.id")}
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
            put = {@CachePut(value = "userCache", key = "#token")}
    )
    public UserDTO changePassword(String newPassword, String token) {
        String id = getIdUserFromAccessToken(token);
        User updated = findById(id);

        if (updated != null && checkAccessTokenExpired(token)){
            updated.setPassWord(convertPassword(newPassword));
            userRepository.save(updated);

            return convertToUserDTO(updated);
        }else return null;
    }

    @Override
    @Caching(
            evict = {@CacheEvict(value = "userCache", key = "#accessToken"),
                    @CacheEvict(value = "profileCache", key = "#accessToken")}
    )
    public UserDTO deleteUserByAccessToken(String accessToken) {
        String id = getIdUserFromAccessToken(accessToken);
        User deleted = findById(id);

        if (deleted != null && checkAccessTokenExpired(accessToken)){
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
//    @CachePut(value = "userCache", key = "#")
    public AuthenticationDTO getTokenByUsernameAndPassword(String username, String password) {
        User found = userRepository.findByUserName(username).orElse(null);

        if (found != null && matching(found.getPassWord(), password)){
            Authentication authentication = authenticationRepository.findById(found.getId()).orElse(new Authentication());

            if (authentication.getToken() == null){
                String token = generateToken(found);
                String refreshToken = generateToken(found);

                authentication = Authentication.builder()
                        .id(found.getId())
                        .token(token)
                        .refreshToken(refreshToken)
                        .expiration(createExpires())
                        .build();
                authentication.setCreatedAt(LocalDateTime.now());

                authenticationRepository.save(authentication);

                return convertToAuthenticationDTO(authentication);
            }else{
                return convertToAuthenticationDTO(authentication);
            }
        }else return null;
    }

    @Override
    public String getNewAccessTokenByRefreshToken(String refreshToken) {
        String id = getIdUserFromAccessToken(refreshToken);
        Authentication authentication = authenticationRepository.findById(id).orElse(null);
        User user = userRepository.findById(id).orElse(null);

        if (!checkAccessTokenExpired(authentication.getToken()) && authentication != null){
            String accessToken = generateToken(user);
            LocalDateTime expires = createExpires();

            authentication.setToken(accessToken);
            authentication.setExpiration(expires);
            authentication.setCreatedAt(LocalDateTime.now());

            authenticationRepository.save(authentication);
            return accessToken;
        }else return null;
    }

    @Override
    @Caching(
            put = {@CachePut(value = "profileCache", key = "#accessToken")}
    )
    public ProfileDTO createProfileUser(ProfileDTO profileDTO, String accessToken) {
        String id = getIdUserFromAccessToken(accessToken);

        if (checkAccessTokenExpired(accessToken)){
            Profile saved = Profile.builder()
                    .id(id)
                    .phoneNumber(profileDTO.getPhoneNumber())
                    .homeTown(profileDTO.getHomeTown())
                    .email(profileDTO.getEmail())
                    .build();
            saved.setCreatedAt(LocalDateTime.now());

            saved = profileRepository.save(saved);
            return convertToProfileDTO(saved);
        }else return null;
    }

    @Override
    @Caching(
            put = {@CachePut(value = "profileCache", key = "#accessToken")}
    )
    public ProfileDTO updateProfileUser(ProfileDTO profileDTO, String accessToken) {
        String id = getIdUserFromAccessToken(accessToken);
        Profile profile = profileRepository.findById(id).orElse(null);
        Authentication authentication = authenticationRepository.findById(id).orElse(null);

        if (profile != null && checkAccessTokenExpired(accessToken)
                && accessToken.equals(authentication.getToken())){
            profile.setEmail(profileDTO.getEmail());
            profile.setPhoneNumber(profileDTO.getPhoneNumber());
            profile.setHomeTown(profileDTO.getHomeTown());

            profileRepository.save(profile);
            return convertToProfileDTO(profile);
        }else return null;
    }

    @Override
    @Cacheable(value = "profileCache", key = "#accessToken")
    public ProfileDTO findProfileByAccessToken(String accessToken) {
        String id = getIdUserFromAccessToken(accessToken);
        Profile profile = profileRepository.findById(id).orElse(null);
        Authentication authentication = authenticationRepository.findById(id).orElse(null);

        if (profile != null && checkAccessTokenExpired(accessToken)
                && accessToken.equals(authentication.getToken())){
            return convertToProfileDTO(profile);
        }else return null;
    }

    @Override
    @Caching(
            evict = {@CacheEvict(value = "profileCache", key = "#accessToken")}
    )
    public ProfileDTO deleteProfileByAccessToken(String accessToken) {
        String id = getIdUserFromAccessToken(accessToken);
        Profile deleted = profileRepository.findById(id).orElse(null);

        if (deleted != null && checkAccessTokenExpired(accessToken)){
            profileRepository.deleteById(id);
            return convertToProfileDTO(deleted);
        }else return null;
    }

    @Override
    @Caching(
            evict = {@CacheEvict(value = "profileCache", key = "#accessToken"),
                    @CacheEvict(value = "userCache", key = "#accessToken")}
    )
    public void deleteTokenWhenLogout(String accessToken) {
        String id = getIdUserFromAccessToken(accessToken);
        authenticationRepository.deleteById(id);
    }

    private User findById(String id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null){
            return user;
        }else return null;
    }

    private boolean matching(String password, String otherPassword){
        return password.equals(convertPassword(otherPassword));
    }

    private String convertPassword(String password){
        EnCryptMD5 encoderMD = new EnCryptMD5();
        return encoderMD.md5(password);
    }

    private boolean checkAccessTokenExpired(String accessToken){
        String id = getIdUserFromAccessToken(accessToken);
        Authentication authentication = authenticationRepository.findById(id).orElse(null);

        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
        LocalDateTime now = LocalDateTime.ofInstant(cal.toInstant(), zid);

        if (authentication.getExpiration().isAfter(now)){
            return true;
        }else return false;
    }

    private LocalDateTime createExpires(){
        LocalDateTime expires;

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 3);
        TimeZone tz = cal.getTimeZone();
        ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
        expires = LocalDateTime.ofInstant(cal.toInstant(), zid);

        return expires;
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

    private AuthenticationDTO convertToAuthenticationDTO(Authentication authentication){
        AuthenticationDTO dto = new AuthenticationDTO();

        if (authentication != null){
            dto.setToken(authentication.getToken());
            dto.setRefreshToken(authentication.getRefreshToken());
            dto.setExpiration(authentication.getExpiration());

            return dto;
        }else return null;
    }

    private String getIdUserFromAccessToken(String accessToken){
        List<String> array = Arrays.asList(accessToken.split("_"));
        return array.get(2);
    }

    private String generateToken(User user)
    {
        EnCryptMD5 enCryptMD5 = new EnCryptMD5();
        String tokens = "c_user" + "_" + user.getId() + "_" + enCryptMD5.md5(RandomStringUtils.randomAlphabetic(8));

        return tokens;
    }
}
