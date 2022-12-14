package ro.tuc.ds2020.sockets;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ro.tuc.ds2020.dtos.DeviceDTO;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class Message {

    private Integer deviceId;
    private String deviceName;
    private double maxConsumption;
    private int hour;
    private String date;
    private double value;


}