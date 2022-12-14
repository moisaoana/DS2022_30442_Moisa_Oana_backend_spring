package ro.tuc.ds2020.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import ro.tuc.ds2020.dtos.ReceivedMeasurementDTO;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.Measurement;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.repositories.MeasurementRepository;
import ro.tuc.ds2020.sockets.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ReceiverService {

    private static final String QUEUE_NAME = "rabbitmq_queue";

    @Autowired
     public DeviceRepository deviceRepository;

    @Autowired
    public MeasurementRepository measurementRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    Map<Integer, Double> devicesConsumption = new HashMap<Integer, Double>();
    Map<Integer,Integer> measurementsCount = new HashMap<Integer, Integer>();


    public void receive() throws Exception {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setPort(5672);
            factory.setUsername("guest");
            factory.setPassword("guest");
            factory.setVirtualHost("/");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");



        DefaultConsumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + message + "'");
               // ReceivedMeasurementDTO measurement = (ReceivedMeasurementDTO) JSONValue.parse(message);
                Gson gson = new Gson();
                ReceivedMeasurementDTO measurement
                        = gson.fromJson(message,
                        ReceivedMeasurementDTO.class);
                if(!devicesConsumption.containsKey(measurement.getDeviceId())){
                    System.out.println("Val for  "+measurement.getDeviceId()+": "+measurement.getValue());
                    devicesConsumption.put(measurement.getDeviceId(),measurement.getValue());
                }else{
                    double sum = devicesConsumption.get(measurement.getDeviceId())+measurement.getValue();
                    System.out.println("Sum for "+measurement.getDeviceId()+": "+ sum);
                    devicesConsumption.put(measurement.getDeviceId(), sum);
                }
                if(!measurementsCount.containsKey(measurement.getDeviceId())){
                    measurementsCount.put(measurement.getDeviceId(),1);
                }else{
                    int count =  measurementsCount.get(measurement.getDeviceId())+1;
                    System.out.println("Count for "+measurement.getDeviceId()+": "+count);
                    measurementsCount.put(measurement.getDeviceId(), count);
                }

                //hourlySum += measurement.getValue();
                //nrOfMeasurements++;
                //System.out.println("Sum: "+hourlySum);
               // System.out.println("Nr: "+nrOfMeasurements);
                //System.out.println("Device id"+measurement.getDeviceId());
                if(measurementsCount.get(measurement.getDeviceId())==6){
                    Optional<Device> device = deviceRepository.findById(measurement.getDeviceId());
                    if(device.isPresent()){
                        if(devicesConsumption.get(measurement.getDeviceId())> device.get().getMaxHourlyEnergyConsumption()){
                            System.out.println("Limit exceeded");
                            sendNotification(device.get(), measurement.getTimestamp(),devicesConsumption.get(measurement.getDeviceId()));
                        }//else{
                            System.out.println("Insert");
                            Measurement newMeasurement = Measurement.builder()
                                    .timestamp(measurement.getTimestamp())
                                    .value(devicesConsumption.get(measurement.getDeviceId()))
                                    .device(device.get())
                                    .build();
                            measurementRepository.save(newMeasurement);

                       // }
                    }
                    //nrOfMeasurements = 0;
                    //hourlySum = 0;
                    measurementsCount.put(measurement.getDeviceId(), 0);
                    devicesConsumption.put(measurement.getDeviceId(),0.0);
                }
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);

    }


    @GetMapping("/sendNotification")
    public void sendNotification(Device device, String timestamp, double value) throws JsonProcessingException {
        Message message = Message.builder()
                .deviceId(device.getId())
                .deviceName(device.getName())
                .maxConsumption(device.getMaxHourlyEnergyConsumption())
                .hour(getLocalDateTimeFromString(timestamp).getHour())
                .date(getDate(timestamp))
                .value(value)
                .build();
        String json = new ObjectMapper().writeValueAsString(message);
        System.out.println(json);
        simpMessagingTemplate.convertAndSendToUser(device.getUser().getUsername(), "/reply", json);
    }

    private LocalDateTime getLocalDateTimeFromString(String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        return LocalDateTime.parse(timestamp, formatter);
    }

    private String getDate(String timestamp){
        return timestamp.substring(0,10);
    }


}
