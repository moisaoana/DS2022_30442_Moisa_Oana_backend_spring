package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.dtos.*;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.User;

import java.util.List;

public class DeviceBuilder {

    public DeviceBuilder(){

    }
    public Device toEntity(DeviceDTO deviceDTO, User user){
        return Device.builder()
                .name(deviceDTO.getName())
                .description(deviceDTO.getDescription())
                .address(deviceDTO.getAddress())
                .maxHourlyEnergyConsumption(deviceDTO.getMaxHourlyEnergyConsumption())
                .user(user)
                .build();
    }

    public DeviceDTO toDeviceDTO(Device device){
        return DeviceDTO.builder()
                .id(device.getId())
                .name(device.getName())
                .description(device.getDescription())
                .address(device.getAddress())
                .maxHourlyEnergyConsumption(device.getMaxHourlyEnergyConsumption())
                .userId(device.getUser().getId())
                .build();
    }

    public DisplayDeviceDTO toDisplayDevice(Device device, UserDTO user){
        return DisplayDeviceDTO.builder()
                .id(device.getId())
                .name(device.getName())
                .description(device.getDescription())
                .address(device.getAddress())
                .maxHourlyEnergyConsumption(device.getMaxHourlyEnergyConsumption())
                .user(user)
                .build();
    }

    public DeviceWithMeasurementDTO toDeviceWithMeasurementDTO(Device device, List<MeasurementDTO> measurementDTOList){
        return DeviceWithMeasurementDTO.builder()
                .id(device.getId())
                .name(device.getName())
                .description(device.getDescription())
                .address(device.getAddress())
                .maxHourlyEnergyConsumption(device.getMaxHourlyEnergyConsumption())
                .userId(device.getUser().getId())
                .measurements(measurementDTOList)
                .build();
    }
}
