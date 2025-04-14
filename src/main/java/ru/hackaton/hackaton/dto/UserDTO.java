package ru.hackaton.hackaton.dto;

import lombok.Data;
import ru.hackaton.hackaton.entities.User;
import ru.hackaton.hackaton.enums.Role;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private String username;
    private String fullName;
    private Role role;

    public static UserDTO fromEntity(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setRole(user.getRole());
        return dto;
    }
}
