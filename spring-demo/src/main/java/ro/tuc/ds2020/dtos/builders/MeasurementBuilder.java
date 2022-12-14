package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.dtos.MeasurementDTO;
import ro.tuc.ds2020.entities.Measurement;

public class MeasurementBuilder {
    public MeasurementBuilder(){

    }

    public MeasurementDTO toMeasurementDTO(Measurement measurement){
        //transform timestamp from LocalDateTime to day,year,etc..
        //2014-04-28T16:00:49.455
        return MeasurementDTO.builder()
                .id(measurement.getId())
                .day(getDay(measurement.getTimestamp()))
                .month(getMonth(measurement.getTimestamp()))
                .year(getYear(measurement.getTimestamp()))
                .hour(getHour(measurement.getTimestamp()))
                .value(measurement.getValue())
                .build();
    }

    private Integer getYear(String timestamp){
        return Integer.parseInt(timestamp.substring(0,4));
    }

    private Integer getMonth(String timestamp){
        return Integer.parseInt(timestamp.substring(5,7));
    }

    private Integer getDay(String timestamp){
        return Integer.parseInt(timestamp.substring(8,10));
    }

    private Integer getHour(String timestamp){
        return Integer.parseInt(timestamp.substring(11,13));
    }
}
