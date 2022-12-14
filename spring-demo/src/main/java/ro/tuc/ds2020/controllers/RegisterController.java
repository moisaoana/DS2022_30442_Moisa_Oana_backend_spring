package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ro.tuc.ds2020.dtos.RegisterDTO;
import ro.tuc.ds2020.enums.Warning;
import ro.tuc.ds2020.services.UserService;

import java.util.logging.Logger;

@RestController
public class RegisterController {
    private final static Logger LOGGER = Logger.getLogger(RegisterController.class.getName());

    @Autowired
    private UserService userService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/register")
    public ResponseEntity createUser(@RequestBody RegisterDTO registerDTO){
        System.out.println("POST method for creating a new user: "+registerDTO.getName()+", "+registerDTO.getUsername()+", "+registerDTO.getPassword()+", "+registerDTO.getRole());
        Warning result=userService.insertUser(registerDTO);
        if(result==Warning.SUCCESS) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(registerDTO);
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Username already exists!");
        }
    }
}
