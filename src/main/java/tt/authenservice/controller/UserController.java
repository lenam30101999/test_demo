package tt.authenservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tt.authenservice.entity.profile.ProfileDTO;
import tt.authenservice.entity.user.User;
import tt.authenservice.entity.user.UserDTO;
import tt.authenservice.service.UserService;

@RestController
@RequestMapping(value = "api/v1/users")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> registerProfile(@RequestBody ProfileDTO profileDTO,
                                             @RequestParam("accessToken") String accessToken){
        try {
            ProfileDTO saved = userService.createProfileUser(profileDTO, accessToken);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        }catch (NullPointerException e){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE.getReasonPhrase(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping
    public ResponseEntity<?> changePassword(@RequestBody User user,
                                            @RequestParam("accessToken") String token){
        try {
            String newPassword = user.getPassWord();
            UserDTO updated = userService.changePassword(newPassword, token);

            return new ResponseEntity<>(updated, HttpStatus.OK);
        }catch (NullPointerException e){
            return new ResponseEntity<>(HttpStatus.REQUEST_TIMEOUT.getReasonPhrase(),HttpStatus.REQUEST_TIMEOUT);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestParam("accessToken") String accessToken){
        UserDTO userDTO = userService.deleteUserByAccessToken(accessToken);
        ProfileDTO profileDTO = userService.deleteProfileByAccessToken(accessToken);

        if (userDTO != null && profileDTO != null){
            return new ResponseEntity<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/profile")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileDTO profileDTO,
                                           @RequestParam("accessToken") String accessToken){
        try {
            ProfileDTO updated = userService.updateProfileUser(profileDTO, accessToken);

            return new ResponseEntity<>(updated,HttpStatus.OK);
        }catch (NullPointerException e){
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED.getReasonPhrase(),HttpStatus.NOT_MODIFIED);
        }
    }

    @GetMapping
    public ResponseEntity<?> findProfileById(@RequestParam("accessToken") String token){
        try {
            ProfileDTO profile = userService.findProfileByAccessToken(token);

            return new ResponseEntity<>(profile,HttpStatus.OK);
        }catch (NullPointerException e){
            return new ResponseEntity<>(HttpStatus.REQUEST_TIMEOUT.getReasonPhrase(),HttpStatus.REQUEST_TIMEOUT);
        }
    }

    @GetMapping(value = "new")
    public ResponseEntity<?> getNewAccessToken(@RequestParam("refreshToken") String refreshToken){
        try {
            String accessToken = userService.getNewAccessTokenByRefreshToken(refreshToken);

            return new ResponseEntity<>(accessToken,HttpStatus.OK);
        }catch (NullPointerException e){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE.getReasonPhrase(),HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
