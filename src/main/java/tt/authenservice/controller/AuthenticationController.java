package tt.authenservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tt.authenservice.entity.Message;
import tt.authenservice.entity.authentication.AuthenticationDTO;
import tt.authenservice.entity.user.UserDTO;
import tt.authenservice.service.UserService;

import javax.validation.Valid;

@RestController
public class AuthenticationController extends BaseController{

    @Autowired
    private UserService userService;

    @PostMapping(value = "/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO){
        Message message;
        UserDTO found = userService.findByUserName(userDTO.getUserName());

        if (found != null){
            message = new Message("Username has existed");
            return new ResponseEntity<>(message, HttpStatus.NOT_ACCEPTABLE);
        }else {
            message = new Message("OK");
            UserDTO saved = userService.createUser(userDTO);

            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> signin(@RequestBody UserDTO userDTO){
        try {
            String username = userDTO.getUserName();
            String password = userDTO.getPassWord();

            String accessToken = userService.getTokenByUsernameAndPassword(username, password);
            return new ResponseEntity<>(accessToken, HttpStatus.OK);
        }catch (NullPointerException e){
            Message message = new Message("Wrong username or password");
            return new ResponseEntity<>(message,HttpStatus.SEE_OTHER);
        }
    }

    @DeleteMapping(value = "/logout")
    public ResponseEntity<?> logout(@RequestParam("accessToken") String accessToken){
        try {
            Message message = new Message("Username has existed");
            userService.deleteTokenWhenLogout(accessToken);

            return new ResponseEntity<>(message, HttpStatus.OK);
        }catch (NullPointerException e){
            return new ResponseEntity<>(HttpStatus.SEE_OTHER.getReasonPhrase(),HttpStatus.SEE_OTHER);
        }
    }
}
