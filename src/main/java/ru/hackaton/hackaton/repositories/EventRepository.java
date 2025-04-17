package ru.hackaton.hackaton.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hackaton.hackaton.entities.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    boolean existsByName(String name);

}
