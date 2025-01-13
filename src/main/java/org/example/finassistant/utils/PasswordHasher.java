package org.example.finassistant.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {
    private static final String STATIC_SALT = "my_static_salt"; // Статическая соль

    // Метод для хэширования пароля
    public static String hashPassword(String password) {
        // Комбинируем пароль и статическую соль
        String saltedPassword = password + STATIC_SALT;
        // Хэшируем с помощью BCrypt
        return BCrypt.hashpw(saltedPassword, BCrypt.gensalt());
    }

    // Метод для проверки пароля
    public static boolean checkPassword(String password, String hashed) {
        // Комбинируем пароль и статическую соль
        String saltedPassword = password + STATIC_SALT;
        // Проверяем, соответствует ли хэш
        return BCrypt.checkpw(saltedPassword, hashed);
    }

    // Пример использования
    public static void main(String[] args) {
        // Хэширование пароля
        String password = "my_secure_password";
        String hashedPassword = hashPassword(password);
        System.out.println("Хэшированный пароль: " + hashedPassword);

        // Проверка пароля
        boolean isCorrect = checkPassword(password, hashedPassword);
        System.out.println("Пароль верен: " + isCorrect);

        boolean isIncorrect = checkPassword("wrong_password", hashedPassword);
        System.out.println("Пароль верен: " + isIncorrect);
    }
}
