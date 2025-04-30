package ru.hackaton.hackaton.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "teams")
@Builder
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "countOfMembers")
    private Integer countOfMembers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id", nullable = false)
    private User leader;

    @Column(name = "enterRequest")
    private List<Long> enterRequest;

    @ManyToMany
    @JoinTable(
            name = "team_members",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> members = new ArrayList<>();

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id + "," +
                "\"event\":\"" + (event != null ? event.getId() : "") + "\"," +
                "\"name\":\"" + (name != null ? name : "") + "\"," +
                "\"countOfMembers\":\"" + (countOfMembers != null ? countOfMembers : "") + "\"," +
                "\"leader\":\"" + (leader != null ? leader.getId() : "") + "\"," +
                "\"enterRequest\":\"" + (enterRequest != null ? enterRequest : "") + "\"," +
                "\"members\":\"" + (members != null ? members.stream().map(User::getId).toList() : "") + "\"," +
                '}';
    }
}
