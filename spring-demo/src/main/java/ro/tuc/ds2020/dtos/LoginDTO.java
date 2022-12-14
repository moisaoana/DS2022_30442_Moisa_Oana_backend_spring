package ro.tuc.ds2020.dtos;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.hateoas.RepresentationModel;

@Builder
@AllArgsConstructor
public class LoginDTO extends RepresentationModel<LoginDTO> {
    @NotNull
    private String username;

    @NotNull
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
