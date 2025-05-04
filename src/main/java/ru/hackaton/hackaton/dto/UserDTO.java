package ru.hackaton.hackaton.dto;

import lombok.Data;
import ru.hackaton.hackaton.entities.User;
import ru.hackaton.hackaton.enums.Role;

import java.util.List;


@Data
public class UserDTO {
    private Long id;
    private String email;
    private String username;
    private String fullName;
    private List<Role> role;

    public static UserDTO fromEntity(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setRole(user.getRole());
        return dto;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id + "," +
                "\"email\":\"" + (email != null ? email : null)  + "\"," +
                "\"username\":\"" + (username != null ? username : null)+ "\"," +
                "\"fullName\":\"" + (fullName != null ? fullName : null) + "\"," +
                "\"role\":\"" + (role != null ? role : null) + "\"" +
                '}';
    }
}
