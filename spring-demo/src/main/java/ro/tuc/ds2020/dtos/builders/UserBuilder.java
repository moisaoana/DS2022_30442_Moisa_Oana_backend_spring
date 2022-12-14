package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.LoginDTO;
import ro.tuc.ds2020.dtos.RegisterDTO;
import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.enums.UserRole;

import java.util.ArrayList;
import java.util.List;

public class UserBuilder {

    private DeviceBuilder deviceBuilder;

    public UserBuilder(){
        deviceBuilder = new DeviceBuilder();
    }

    public LoginDTO toLoginDTO(User user) {
        return LoginDTO.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

    public RegisterDTO toRegisterDTO(User user){
        return RegisterDTO.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .name(user.getName())
                .role(user.getRole().toString())
                .build();
    }

    public UserDTO toUserDTO(User user){
        List<DeviceDTO> dtos = new ArrayList<>();
        System.out.println(user.getDevices());
        for(Device d: user.getDevices()){
            dtos.add(deviceBuilder.toDeviceDTO(d));
        }
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .role(user.getRole().toString())
                .devices(dtos)
                .build();
    }

    public User toEntityFromRegisterDTO(RegisterDTO registerDTO){
        return User.builder()
                .username(registerDTO.getUsername())
                .password(registerDTO.getPassword())
                .name(registerDTO.getName())
                .role(UserRole.valueOf(registerDTO.getRole()))
                .build();
    }

}
