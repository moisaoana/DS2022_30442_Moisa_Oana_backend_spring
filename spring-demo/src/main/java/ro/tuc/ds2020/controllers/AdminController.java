package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.DisplayDeviceDTO;
import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.dtos.builders.DeviceBuilder;
import ro.tuc.ds2020.dtos.builders.UserBuilder;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.enums.Warning;
import ro.tuc.ds2020.services.DeviceService;
import ro.tuc.ds2020.services.MeasurementService;
import ro.tuc.ds2020.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
public class AdminController {

    private final static Logger LOGGER = Logger.getLogger(AdminController.class.getName());

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private MeasurementService measurementService;

    private UserBuilder userBuilder;

    private DeviceBuilder deviceBuilder;

    public AdminController(){
        userBuilder = new UserBuilder();
        deviceBuilder = new DeviceBuilder();
    }

    @GetMapping("/viewusers")
    @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<UserDTO> getUsers(){
        List<User> users = userService.getAllUsers();
        List<UserDTO> dtos = new ArrayList<>();
        for(User user: users){
            dtos.add(userBuilder.toUserDTO(user));
        }
        //measurementService.insertMeasurements(5,21,10,2022,1,20);
        return dtos;
    }

    @GetMapping("/viewdevices")
    @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<DisplayDeviceDTO> getDevices(){
        List<Device> devices = deviceService.getAllDevices();
        List<DisplayDeviceDTO> dtos = new ArrayList<>();
        for(Device device: devices){
            UserDTO userDTO = userBuilder.toUserDTO(device.getUser());
            dtos.add(deviceBuilder.toDisplayDevice(device,userDTO));
        }
        return dtos;
    }

    @GetMapping("/getregularusers")
    @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<UserDTO> getRegularUsers(){
        List<User> users = userService.getRegularUsers();
        List<UserDTO> dtos = new ArrayList<>();
        for(User user: users){
            dtos.add(userBuilder.toUserDTO(user));
        }
        return dtos;
    }

    @DeleteMapping("/deleteuser/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity deleteUser(@PathVariable String username){
        System.out.println("here"+username);
        LOGGER.info("Delete user "+username);
        Warning result=userService.deleteUser(username);
        if(result==Warning.SUCCESS) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body("AUser deleted successfully!");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User doesn't exist!");
        }
    }

    @DeleteMapping("/deletedevice/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity deleteDevice(@PathVariable Integer id){
        System.out.println("DELETE " + id);
        LOGGER.info("Delete device "+id);
        Warning result=deviceService.deleteDevice(id);
        if(result==Warning.SUCCESS) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Device deleted successfully!");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Device doesn't exist!");
        }
    }

    @PutMapping("/updateuser")
    @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity updateUser(@RequestBody UserDTO userDTO){
        LOGGER.info("PUT method for updating the user");
        UserDTO user= userService.updateUser(userDTO);
        if(user!=null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(user);
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Username already exists!");
        }
    }

    @PutMapping("/updatedevice")
    @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity updateDevice(@RequestBody DeviceDTO deviceDTO){
        System.out.println(deviceDTO.getUserId());
        LOGGER.info("PUT method for updating the device");
        DeviceDTO device= deviceService.updateDevice(deviceDTO);
        return ResponseEntity.status(HttpStatus.OK)
                    .body(device);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createdevice")
    public ResponseEntity createDevice(@RequestBody DeviceDTO deviceDTO){
        System.out.println(deviceDTO.getName()+" "+deviceDTO.getDescription()+ " "+ deviceDTO.getAddress()+" "+deviceDTO.getMaxHourlyEnergyConsumption()+" "+ deviceDTO.getUserId());
        Device device=deviceService.insertDevice(deviceDTO);
        if(device!=null) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(device);
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Insertion failed!");
        }
    }



}
