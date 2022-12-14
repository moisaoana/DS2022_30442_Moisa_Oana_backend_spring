package ro.tuc.ds2020.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.dtos.builders.DeviceBuilder;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.enums.UserRole;
import ro.tuc.ds2020.enums.Warning;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class DeviceService {

    private final static Logger LOGGER = Logger.getLogger(DeviceService.class.getName());

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserRepository userRepository;

    private DeviceBuilder deviceBuilder;

    public DeviceService(){
        deviceBuilder = new DeviceBuilder();
    }

    public Device insertDevice(DeviceDTO deviceDTO){
        LOGGER.info("Trying to insert device in DB");
        Optional<User> user = userRepository.findById(deviceDTO.getUserId());
        if(user.isPresent()) {
            Device device = deviceBuilder.toEntity(deviceDTO, user.get());
            deviceRepository.save(device);
            LOGGER.info("Successful insertion");
            return device;
        }
        return null;
    }

    public List<Device> getAllDevices(){
        return deviceRepository.findAll();
    }

    public List<Device> getAllDevicesByUser(String username){
        return deviceRepository.findAllByUserUsername(username);
    }

    public DeviceDTO updateDevice(DeviceDTO deviceDTO){
        Optional<Device> device=deviceRepository.findById(deviceDTO.getId());
        if(device.isPresent()){
            Optional<User> user = userRepository.findById(deviceDTO.getUserId());
            if(user.isPresent()) {
                device.get().setName(deviceDTO.getName());
                device.get().setDescription(deviceDTO.getDescription());
                device.get().setAddress(deviceDTO.getAddress());
                device.get().setMaxHourlyEnergyConsumption(deviceDTO.getMaxHourlyEnergyConsumption());
                device.get().setUser(user.get());
                deviceRepository.save(device.get());
                return  deviceDTO;
            }
        }
        return null;
    }

    public Warning deleteDevice(Integer id){
        LOGGER.info("Delete device "+id);
        Optional<Device> device=deviceRepository.findById(id);
        if(device.isPresent()) {
            List<Device> devices = device.get().getUser().getDevices();
            List<Device> newDevices= new ArrayList<>();
            for(Device d: devices) {
                if(!d.getId().equals(device.get().getId())){
                    newDevices.add(d);
                }
            }
            device.get().getUser().setDevices(newDevices);
            userRepository.save(device.get().getUser());
            deviceRepository.delete(device.get());
            LOGGER.info("Device deleted!");
            return Warning.SUCCESS;
        }
        LOGGER.warning("Device not present in the DB!");
        return Warning.NOT_FOUND;
    }

    public Optional<Device> findById(Integer id){
       return deviceRepository.findById(id);
    }
}
