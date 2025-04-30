package ru.hackaton.hackaton.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "maxMembers")
    private Integer maxMembers;

    @Column(name = "startTime")
    private ZonedDateTime startTime;

    @Column(name = "endTime")
    private ZonedDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Team> teams = new ArrayList<>();

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id + "," +
                "\"name\":\"" + (name != null ? name : "") + "\"," +
                "\"description\":\"" + (description != null ? description : "") + "\"," +
                "\"teams\":\"" + (teams != null ? teams.stream().map(Team::getId).toList() : "") + "\"," +
                "\"maxMembers\":\"" + (maxMembers != null ? maxMembers : "") + "\"," +
                "\"startTime\":" + (startTime != null ? "\"" + startTime + "\"" : "null") + "," +
                "\"endTime\":" + (endTime != null ? "\"" + endTime + "\"" : "null") + "," +
                "\"createdBy\":" + (createdBy != null ? "\"" + createdBy.getId() + "\"" : "null") +
                "}";
    }
}
