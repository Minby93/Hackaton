package ru.hackaton.hackaton.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.hackaton.hackaton.entities.Event;
import ru.hackaton.hackaton.entities.User;
import ru.hackaton.hackaton.repositories.EventRepository;
import ru.hackaton.hackaton.repositories.UserRepository;

import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /**
     * Добавить метод для получения всех мероприятий
     */

    /**
     * Создание мероприятия
     */
    public ResponseEntity<String> createEvent(Event newEvent){
        try{

            User admin = userService.getCurrentUser();

            if (eventRepository.existsByName(newEvent.getName())){
                return ResponseEntity.status(400).body("Мероприятие с таким названием уже существует!");
            }

            Event event = new Event();

            event.setDescription(newEvent.getDescription());
            event.setName(newEvent.getName());
            event.setCreatedBy(admin);
            event.setMaxMembers(newEvent.getMaxMembers());
            event.setStartTime(newEvent.getStartTime());
            event.setEndTime(newEvent.getEndTime());

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
    public ResponseEntity<String> updateEvent(Event newEvent, Long eventID){
        try{
            Long adminID = userService.getCurrentUser().getId();

            if (!eventRepository.existsById(eventID)){
                return ResponseEntity.status(400).body("Мероприятия с таким ID не существует!");
            }
            if (!userRepository.existsById(adminID)){
                return ResponseEntity.status(400).body("Пользователя с таким ID не существует!");
            }

            Optional<Event> eventOpt = eventRepository.findById(eventID);

            if (eventOpt.isPresent()) {

                Event event = eventOpt.get();

                if (adminID == event.getCreatedBy().getId()) {

                    event.setDescription(newEvent.getDescription());
                    event.setName(newEvent.getName());
                    event.setMaxMembers(newEvent.getMaxMembers());
                    event.setStartTime(newEvent.getStartTime());
                    event.setEndTime(newEvent.getEndTime());

                    eventRepository.save(event);

                    return ResponseEntity.status(200).body("Изменения успешно применены!");
                }
                else {
                    return ResponseEntity.status(400).body("Вы не являетесь администратором данного мероприятия!");
                }
            }
            else return ResponseEntity.status(500).body("Ошибка при получении информации о мероприятии!");

        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /**
     * Удаление мероприятия
     */
    public ResponseEntity<String> deleteEvent(Long eventID){
        try{
            Long adminID = userService.getCurrentUser().getId();

            if (!eventRepository.existsById(eventID)){
                return ResponseEntity.status(400).body("Мероприятия с таким ID не существует!");
            }
            if (!userRepository.existsById(adminID)){
                return ResponseEntity.status(400).body("Пользователя с таким ID не существует!");
            }

            Optional<Event> eventOpt = eventRepository.findById(eventID);

            if (eventOpt.isPresent()){

                Event event = eventOpt.get();

                if (event.getCreatedBy().getId() == adminID) {

                    event = eventOpt.get();

                    eventRepository.delete(event);

                    return ResponseEntity.status(200).body("Мероприятие успешно удалено!");
                }
                else return ResponseEntity.status(400).body("У вас нет права на удаления этого мероприятия!");
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
