package org.example.finassistant.security;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {

    // Хэширование пароля
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Проверка пароля
    public boolean checkPassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }

    // Пример использования
    public static void main(String[] args) {
        PasswordHasher hasher = new PasswordHasher();

        // Хэширование пароля
        String password = "my_secure_password";
        String hashed = hasher.hashPassword(password);
        System.out.println("Хэшированный пароль: " + hashed);

        // Проверка пароля
        boolean isCorrect = hasher.checkPassword("my_secure_password", hashed);
        System.out.println("Пароль верный: " + isCorrect);
    }
}

