package ru.hackaton.hackaton.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.hackaton.hackaton.entities.Event;
import ru.hackaton.hackaton.repositories.EventRepository;
import ru.hackaton.hackaton.repositories.UserRepository;

import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Создание мероприятия
     */
    public ResponseEntity<String> createEvent(Event newEvent, Long adminID){
        try{
            if (eventRepository.existsByName(newEvent.getName())){
                return ResponseEntity.status(400).body("Мероприятие с таким названием уже существует!");
            }

            Event event = new Event();

            event.setAbout(newEvent.getAbout());
            event.setName(newEvent.getName());
            event.setAdminID(adminID);
            event.setMaxMembers(event.getMaxMembers());
            event.setStartTime(event.getStartTime());
            event.setEndTime(event.getEndTime());

            eventRepository.save(event);

            return ResponseEntity.status(200).body("Мероприятие успешно создано");

        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /**
     * Изменение мероприятия
     */
    public ResponseEntity<String> updateEvent(Event newEvent, Long adminID){
        try{
            if (!eventRepository.existsByName(newEvent.getName())){
                return ResponseEntity.status(400).body("Мероприятия с таким названием не существует!");
            }
            if (!userRepository.existsById(adminID)){
                return ResponseEntity.status(400).body("Пользователя с таким ID не существует!");
            }

            Event event = new Event();

            event.setAbout(newEvent.getAbout());
            event.setName(newEvent.getName());
            event.setAdminID(adminID);
            event.setMaxMembers(event.getMaxMembers());
            event.setStartTime(event.getStartTime());
            event.setEndTime(event.getEndTime());

            eventRepository.save(event);

            return ResponseEntity.status(200).body("Изменения успешно применены!");

        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /**
     * Удаление мероприятия
     */
    public ResponseEntity<String> deleteEvent(Long eventID, Long adminID){
        try{
            if (!eventRepository.existsById(eventID)){
                return ResponseEntity.status(400).body("Мероприятия с таким ID не существует!");
            }
            if (!userRepository.existsById(adminID)){
                return ResponseEntity.status(400).body("Пользователя с таким ID не существует!");
            }

            Optional<Event> eventOpt = eventRepository.findById(eventID);

            if (eventOpt.isPresent()){

                Event event = new Event();

                event = eventOpt.get();

                eventRepository.delete(event);

                return ResponseEntity.status(200).body("Мероприятие успешно удалено!");
            }

            return ResponseEntity.status(500).body("Ошибка при получении информации о мероприятии!");

        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /**
     * Получение мероприятия
     */
    public ResponseEntity<String> getEvent(Long eventID){
        try{
            if (!eventRepository.existsById(eventID)){
                return ResponseEntity.status(400).body("Мероприятия с таким ID не существует!");
            }

            Optional<Event> eventOpt = eventRepository.findById(eventID);

            if (eventOpt.isPresent()){

                Event event = new Event();

                event = eventOpt.get();

                return ResponseEntity.status(200).body(event.toString());
            }

            return ResponseEntity.status(500).body("Ошибка при получении информации о мероприятии!");

        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
