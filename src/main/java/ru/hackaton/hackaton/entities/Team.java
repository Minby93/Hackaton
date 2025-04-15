package ru.hackaton.hackaton.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "members")
    private List<Long> listOfMembers;

    @Column(name = "name")
    private String name;

    @Column(name = "countOfMembers")
    private Integer countOfMembers;

    @Column(name = "columnID")
    private Long adminID;

    @Column(name = "enterRequest")
    private List<Long> enterRequest;
}
