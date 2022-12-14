package ro.tuc.ds2020.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@AllArgsConstructor
public class UserDTO {

    @NotNull
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    private String username;

    @NotNull
    private String role;

    @NotNull
    private List<DeviceDTO> devices;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<DeviceDTO> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceDTO> devices) {
        this.devices = devices;
    }
}
