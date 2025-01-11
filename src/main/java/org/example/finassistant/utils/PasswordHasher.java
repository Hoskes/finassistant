package org.example.finassistant.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {
    private static final String STATIC_SALT = "my_static_salt"; // Статическая соль

    // Статический метод для хэширования пароля
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // Комбинируем пароль и статическую соль
            String saltedPassword = password + STATIC_SALT;
            byte[] hashedBytes = md.digest(saltedPassword.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Ошибка при хэшировании пароля", e);
        }
    }

    // Статический метод для проверки пароля
    public static boolean checkPassword(String password, String hashed) {
        String hashedInput = hashPassword(password);
        return hashedInput.equals(hashed);
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
