package ru.hackaton.hackaton.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hackaton.hackaton.entities.Team;
import ru.hackaton.hackaton.entities.VM;

import java.util.Optional;

@Repository
public interface VmRepository extends JpaRepository<VM, Long> {

    Optional<VM> findByTeam(Team team);

}
