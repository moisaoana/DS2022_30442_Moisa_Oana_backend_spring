package ro.tuc.ds2020.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ReceivedMeasurementDTO {

    @NotNull
    private String timestamp;

    @NotNull
    private double value;

    @NotNull
    private Integer deviceId;
}
