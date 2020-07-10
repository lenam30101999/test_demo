package tt.authenservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tt.authenservice.entity.user.UserDTO;
import tt.authenservice.service.UserService;

@RestController
public class AuthenController extends BaseController{

    @Autowired
    private UserService userService;

    @PostMapping(value = "/register")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO){
        try {
            UserDTO found = userService.findByUserName(userDTO.getUserName());

            if (found != null){
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE.getReasonPhrase(), HttpStatus.NOT_ACCEPTABLE);
            }else {
                UserDTO saved = userService.createUser(userDTO);
                return new ResponseEntity<>(saved, HttpStatus.CREATED);
            }
        }catch (NullPointerException e){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE.getReasonPhrase(),HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> signin(@RequestBody UserDTO userDTO){
        try {
            String username = userDTO.getUserName();
            String password = userDTO.getPassWord();

            String token = userService.getTokenByUsernameAndPassword(username, password);

            return new ResponseEntity<>(token, HttpStatus.OK);
        }catch (NullPointerException e){
            return new ResponseEntity<>(HttpStatus.SEE_OTHER.getReasonPhrase(),HttpStatus.SEE_OTHER);
        }
    }

    @DeleteMapping(value = "/logout")
    public ResponseEntity<?> logout(@RequestParam String accessToken){
        try {
            userService.deleteTokenWhenLogout(accessToken);

            return new ResponseEntity<>(HttpStatus.REQUEST_TIMEOUT.getReasonPhrase(), HttpStatus.REQUEST_TIMEOUT);
        }catch (NullPointerException e){
            return new ResponseEntity<>(HttpStatus.SEE_OTHER.getReasonPhrase(),HttpStatus.SEE_OTHER);
        }
    }
}
