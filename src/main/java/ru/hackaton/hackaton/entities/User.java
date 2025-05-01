package ru.hackaton.hackaton.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.hackaton.hackaton.enums.Role;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
@Builder
public class User {

    /**
     * По хорошему использовать UserDTO для GET методов
     *
     * Так же нужно ограничить взаимодействие с EventController путём выдачи пользователям,
     * которые будут управлять мероприятиями роли EDITOR,
     * соответственно нужно будет добавить проверку наличия данной роли у пользователя
     *
     * Подумать как использовать роль ADMIN
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "fullName")
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private List<Role> role;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id + "," +
                "\"email\":\"" + (email != null ? email : "") + "\"," +
                "\"password\":\"" + (password != null ? password : "") + "\"," + // Маскируем пароль
                "\"username\":\"" + (username != null ? username : "") + "\"," +
                "\"fullName\":\"" + (fullName != null ? fullName : "") + "\"," +
                "\"role\":" + (role != null ? "\"" + role + "\"" : "null") +
                "\"createdAt\":" + (createdAt != null ? "\"" + createdAt + "\"" : "null") +
                "}";
    }
}
