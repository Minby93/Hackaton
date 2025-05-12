package ru.hackaton.hackaton.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "vm")
@Data
public class VM {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "vmName")
    private String vmName;

    @OneToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id + "," +
                "\"vmName\":\"" + (vmName != null ? vmName : null) + "\"," +
                "\"team\":\"" + (team != null ? team.getId() : null) + "," +
                "}";
    }
}
