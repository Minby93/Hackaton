package ru.hackaton.hackaton.entities;

import jakarta.persistence.*;
import lombok.*;
import ru.hackaton.hackaton.enums.Role;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "username")
    private String username;

    @Column(name = "fullName")
    private String fullName;

    @Column(name = "role")
    private Role role;

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id + "," +
                "\"email\":\"" + (email != null ? email : "") + "\"," +
                "\"password\":\"" + (password != null ? password : "") + "\"," + // Маскируем пароль
                "\"username\":\"" + (username != null ? username : "") + "\"," +
                "\"FullName\":\"" + (fullName != null ? fullName : "") + "\"," +
                "\"role\":" + (role != null ? "\"" + role + "\"" : "null") +
                "}";
    }
}
