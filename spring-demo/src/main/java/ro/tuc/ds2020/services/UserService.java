package ro.tuc.ds2020.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.dtos.RegisterDTO;
import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.dtos.builders.UserBuilder;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.enums.UserRole;
import ro.tuc.ds2020.enums.Warning;
import ro.tuc.ds2020.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final static Logger LOGGER = Logger.getLogger(UserService.class.getName());

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserBuilder userBuilder;

    public UserService(){
        bCryptPasswordEncoder=new BCryptPasswordEncoder();
        userBuilder = new UserBuilder();
        LOGGER.setLevel(Level.INFO);
    }

    public List<User> getAllUsers(){
        LOGGER.info("Getting all users from the DB");
        return userRepository.findAll();
    }

    public List<User> getRegularUsers() {
        LOGGER.info("Getting only regular users from DB");
        return  userRepository.findAll()
                .stream()
                .filter(u -> u.getRole()==UserRole.ROLE_USER)
                .collect(Collectors.toList());
    }

    public boolean isUsernameUnique(String username){
        LOGGER.info("Checking if username is unique");
        List<User> allUsers=userRepository.findAll();
        for(User u:allUsers){
            if(u.getUsername().equals(username)){
                LOGGER.warning("Duplicate username provided!");
                return false;
            }
        }
        LOGGER.info("Correct username provided!");
        return true;
    }

    public Warning insertUser(RegisterDTO registerDTO){
        LOGGER.info("Trying to insert user in DB");
        if(isUsernameUnique(registerDTO.getUsername())){
            registerDTO.setPassword(bCryptPasswordEncoder.encode(registerDTO.getPassword()));
            User user = userBuilder.toEntityFromRegisterDTO(registerDTO);
            userRepository.save(user);
            LOGGER.info("Successful insertion");
            return Warning.SUCCESS;
        }else{
            LOGGER.warning("Insertion failed because the username exists!");
            return Warning.DUPLICATE;
        }
    }

    public Warning deleteUser(String username){
        LOGGER.info("Delete user "+username);
        Optional<User> user=userRepository.findByUsername(username);
        if(user.isPresent()) {
            userRepository.delete(user.get());
            LOGGER.info("User deleted!");
            return Warning.SUCCESS;
        }
        LOGGER.warning("User not present in the DB!");
        return Warning.NOT_FOUND;
    }

    public UserDTO updateUser(UserDTO userDTO){
        Optional<User> user=userRepository.findById(userDTO.getId());
        if(user.isPresent()){
            if(!user.get().getUsername().equals(userDTO.getUsername())) {
                if (isUsernameUnique(userDTO.getUsername())) {
                    user.get().setName(userDTO.getName());
                    user.get().setUsername(userDTO.getUsername());
                    user.get().setRole(UserRole.valueOf(userDTO.getRole()));
                    userRepository.save(user.get());
                    return userDTO;
                } else {
                    return null;
                }
            }else{
                user.get().setName(userDTO.getName());
                user.get().setUsername(userDTO.getUsername());
                user.get().setRole(UserRole.valueOf(userDTO.getRole()));
                userRepository.save(user.get());
                return userDTO;
            }
        }
        return null;
    }
}
