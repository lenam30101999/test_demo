package tt.authenservice.service;


import tt.authenservice.entity.profile.ProfileDTO;
import tt.authenservice.entity.user.UserDTO;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    UserDTO changePassword(String newPassword, String token);

    UserDTO deleteUserByAccessToken(String accessToken);

    UserDTO findByUserName(String userName);

    String getTokenByUsernameAndPassword(String username, String password);

    ProfileDTO createProfileUser(ProfileDTO profileDTO, String accessToken);

    ProfileDTO updateProfileUser(ProfileDTO profileDTO, String accessToken);

    ProfileDTO findProfileByAccessToken(String accessToken);

    ProfileDTO deleteProfileByAccessToken(String accessToken);

    void deleteTokenWhenLogout(String accessToken);

}
