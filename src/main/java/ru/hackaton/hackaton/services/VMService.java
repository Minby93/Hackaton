package ru.hackaton.hackaton.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class VMService {

    private static final String BASE_VM_NAME = "Base_Ubuntu"; // Имя базового шаблона ВМ
    private static final String SCRIPT_PATH = "/path/to/setup-vm.sh"; // Путь к скрипту настройки ВМ
    private static final String SSH_USER = "user"; // Стартовый пользователь в базовой ВМ
    private static final String SSH_KEY_PATH = "/path/to/private_key"; // Путь к приватному SSH-ключу (если нужен)
    @Autowired
    private UserService userService;

    // Метод создания новой виртуальной машины
    public ResponseEntity<String > createVM(String teamName, String username, String password) throws Exception {
        Long adminID = userService.getCurrentUser().getId();

        String vmName = "team-vm-" + teamName;

        // 1. Клонируем базовый образ
        executeCommand("vboxmanage clonevm " + BASE_VM_NAME + " --name " + vmName + " --register");

        // 2. Запускаем новую ВМ
        executeCommand("vboxmanage startvm " + vmName + " --type headless");

        // 3. Выполняем настройку ВМ через SSH
        configureVM(vmName, username, password);
    }

    // Метод настройки ВМ через SSH и Bash-скрипт
    private void configureVM(String vmName, String username, String password) throws Exception {
        String command = String.format(
                "ssh -o StrictHostKeyChecking=no -i %s %s@%s 'bash -s' < %s %s %s %s",
                SSH_KEY_PATH, SSH_USER, vmName, SCRIPT_PATH, username, password, vmName
        );
        executeCommand(command);
    }

    // Метод остановки виртуальной машины
    public void stopVM(String vmName) throws Exception {
        executeCommand("vboxmanage controlvm " + vmName + " poweroff");
    }

    // Метод удаления виртуальной машины
    public void deleteVM(String vmName) throws Exception {
        executeCommand("vboxmanage unregistervm " + vmName + " --delete");
    }

    // Метод выполнения команд
    private void executeCommand(String command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
        processBuilder.inheritIO();
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Ошибка выполнения команды: " + command);
        }
    }

}
