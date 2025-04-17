package ru.hackaton.hackaton.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "about")
    private String about;

    @Column(name = "teams")
    private List<Long> teams;

    @Column(name = "maxMembers")
    private Integer maxMembers;

    @Column(name = "startTime")
    private ZonedDateTime startTime;

    @Column(name = "endTime")
    private ZonedDateTime endTime;

    @Column(name = "adminID")
    private Long adminID;

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id + "," +
                "\"name\":\"" + (name != null ? name : "") + "\"," +
                "\"about\":\"" + (about != null ? about : "") + "\"," +
                "\"teams\":\"" + (teams != null ? teams : "") + "\"," +
                "\"maxMembers\":\"" + (maxMembers != null ? maxMembers : "") + "\"," +
                "\"startTime\":" + (startTime != null ? "\"" + startTime + "\"" : "null") +
                "\"endTime\":" + (endTime != null ? "\"" + endTime + "\"" : "null") +
                "\"adminID\":" + (adminID != null ? "\"" + adminID + "\"" : "null") +
                "}";
    }
}
