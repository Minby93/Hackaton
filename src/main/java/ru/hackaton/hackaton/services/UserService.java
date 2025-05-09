package ru.hackaton.hackaton.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.hackaton.hackaton.configs.MyUserDetails;
import ru.hackaton.hackaton.dto.UserDTO;
import ru.hackaton.hackaton.entities.User;
import ru.hackaton.hackaton.enums.Role;
import ru.hackaton.hackaton.repositories.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Добавить метод для получения всех пользователей
     */

    /**
     * Создание пользователя
     */
    public ResponseEntity<String> createUser(User newUser){
        try{

            if (userRepository.existsByEmail(newUser.getEmail())){
                return ResponseEntity.status(400).body("Email уже занят!");
            }

            if (userRepository.existsByUsername(newUser.getUsername())){
                return ResponseEntity.status(400).body("Логин уже занят!");
            }

            User user = new User();

            user.setEmail(newUser.getEmail());
            user.setUsername(newUser.getUsername());
            user.setPassword(passwordEncoder.encode(newUser.getPassword())  );
            user.setFullName(newUser.getFullName());
            user.setRole(Arrays.asList(Role.USER));

            userRepository.save(user);

            return ResponseEntity.status(200).body("Пользователь успешно создан!");
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /**
     * Изменение пользователя
     */
    public ResponseEntity<String> updateUser(User newUser, Long id){
        try{
            User currentUser = getCurrentUser();

            if(currentUser.getId() == id) {

                Optional<User> userOpt = userRepository.findById(id);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    user.setEmail(newUser.getEmail() != null ? newUser.getEmail() : user.getEmail());
                    user.setUsername(newUser.getUsername() != null ? newUser.getUsername() : user.getUsername());
                    user.setPassword(newUser.getPassword() != null ? newUser.getPassword() : user.getPassword());
                    user.setFullName(newUser.getFullName() != null ? newUser.getFullName() : user.getFullName());
                    ////   user.getRole().add(newUser.getRole() != null ? newUser.getRole() : Role.USER );

                    userRepository.save(user);

                    return ResponseEntity.status(200).body("Пользователь успешно изменён!");
                }
                else return ResponseEntity.status(500).body("Ошибка при получении информации пользователя!");
            }
            else return ResponseEntity.status(400).body("Вы не имеете права на изменение этого пользователя!");
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /**
     * Получение пользователя по ID
     */
    public ResponseEntity<String> getUser(Long id){
        try {

            if (!userRepository.existsById(id)) {
                return ResponseEntity.status(400).body("Пользователя с таким ID не существует!");
            }

            Optional<User> userOpt = userRepository.findById(id);

            if (userOpt.isPresent()){

                User user = userOpt.get();

                return ResponseEntity.status(200).body(UserDTO.fromEntity(user).toString());

            }
            else {
                return ResponseEntity.status(500).body("Не удалось найти пользователя!");
            }
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /**
     * Получение всех пользователей
     */
    public ResponseEntity<String> getAllUser(){
        try{

            List<User> users = userRepository.findAll();

            return ResponseEntity.status(200).body(users.stream().map(UserDTO::fromEntity).toList().toString());
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /**
     * Удаление пользователя
     */
    public ResponseEntity<String> deleteUser(Long id){
        try{

            if (!userRepository.existsById(id)) {
                return ResponseEntity.status(400).body("Пользователя с таким ID не существует!");
            }

            Optional<User> userOpt = userRepository.findById(id);

            if (userOpt.isPresent()){

                User user = userOpt.get();

                if (getCurrentUser().getId() == user.getId()) {

                    userRepository.delete(user);

                    return ResponseEntity.status(200).body("Аккаунт успешно удален!");
                }
                else return ResponseEntity.status(400).body("Вы не имеете права на удаление этого пользователя!");

            }
            else {
                return ResponseEntity.status(500).body("Не удалось найти пользователя!");
            }



        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    public User getCurrentUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof MyUserDetails) {
            username = ((MyUserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        return userRepository.findByUsername(username).orElse(null);
    }

}
