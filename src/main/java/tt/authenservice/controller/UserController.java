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

    @PutMapping
    public ResponseEntity<?> changePassword(@RequestBody User user,
                                            @RequestParam("token") String token){
        try {
            String newPassword = user.getPassWord();
            UserDTO updated = userService.changePassword(newPassword, token);

            return new ResponseEntity<>(updated, HttpStatus.OK);
        }catch (NullPointerException e){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE.getReasonPhrase(),HttpStatus.NOT_ACCEPTABLE);
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

    @GetMapping(value = "/")
    public ResponseEntity<?> findProfileById(@RequestParam("accessToken") String token){
        try {
            ProfileDTO profile = userService.findProfileByAccessToken(token);

            return new ResponseEntity<>(profile,HttpStatus.OK);
        }catch (NullPointerException e){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE.getReasonPhrase(),HttpStatus.NOT_ACCEPTABLE);
        }
    }
}