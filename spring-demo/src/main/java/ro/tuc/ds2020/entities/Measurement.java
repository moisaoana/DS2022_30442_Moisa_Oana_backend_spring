package ro.tuc.ds2020.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Builder
@AllArgsConstructor
public class Measurement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "timestamp", nullable = false)
    private String timestamp;

    @Column(name = "value", nullable = false)
    private double value;

    @ManyToOne
    @JoinColumn(name="device_id", nullable=false)
    private Device device;

    public Measurement(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
