package ro.tuc.ds2020.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ro.tuc.ds2020.dtos.DeviceWithMeasurementDTO;
import ro.tuc.ds2020.dtos.MeasurementDTO;
import ro.tuc.ds2020.dtos.builders.DeviceBuilder;
import ro.tuc.ds2020.dtos.builders.MeasurementBuilder;
import ro.tuc.ds2020.dtos.builders.UserBuilder;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.Measurement;
import ro.tuc.ds2020.services.DeviceService;
import ro.tuc.ds2020.services.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceService deviceService;

    private UserBuilder userBuilder;

    private DeviceBuilder deviceBuilder;

    private MeasurementBuilder measurementBuilder;

    public UserController(){
        userBuilder = new UserBuilder();
        deviceBuilder = new DeviceBuilder();
        measurementBuilder = new MeasurementBuilder();
    }

    @GetMapping("/viewdevicesbyuser/{username}")
    @PreAuthorize("hasRole('USER')")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<DeviceWithMeasurementDTO> getDevices(@PathVariable String username){
       List<Device> devices= deviceService.getAllDevicesByUser(username);
       List<DeviceWithMeasurementDTO> dtos = new ArrayList<>();
       for(Device d: devices){
           List<Measurement> measurements = d.getMeasurements();
           List<MeasurementDTO> measurementDTOS =new ArrayList<>();
           for(Measurement m: measurements){
               measurementDTOS.add(measurementBuilder.toMeasurementDTO(m));
           }
           dtos.add(deviceBuilder.toDeviceWithMeasurementDTO(d,measurementDTOS));
       }
       return dtos;
    }
}
