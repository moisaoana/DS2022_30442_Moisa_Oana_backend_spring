package ro.tuc.ds2020.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.Measurement;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.repositories.MeasurementRepository;

import java.util.Optional;
import java.util.Random;
import java.util.logging.Logger;

@Service
public class MeasurementService {

    private final static Logger LOGGER = Logger.getLogger(MeasurementService.class.getName());

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    //2022-10-20T12:00:.000

    public void insertMeasurements(int deviceId, int day, int month, int year, int min, int max){
        for(int i=1;i<24;i++){
            Optional<Device> device = deviceRepository.findById(deviceId);
            StringBuilder m = new StringBuilder();
            m.append(year);
            m.append("-");
            if(month<10) {
                m.append("0").append(month);
            }else{
                m.append(month);
            }
            m.append("-");
            if(day<10){
                m.append("0").append(day);
            }else{
                m.append(day);
            }
            m.append("T");
            if(i<10){
                m.append("0").append(i);
            }else{
                m.append(i);
            }
            m.append(":00:.000");
            Random r = new Random();
            int randVal = r.nextInt(max-min) + min;
            if(device.isPresent()) {
                Measurement measurement = Measurement.builder()
                        .timestamp(m.toString())
                        .value(randVal)
                        .device(device.get())
                        .build();
                measurementRepository.save(measurement);
                //System.out.println(measurement.getTimestamp());
            }
        }
    }

    public void insertMeasurement(Measurement measurement){
        measurementRepository.save(measurement);
    }
}
