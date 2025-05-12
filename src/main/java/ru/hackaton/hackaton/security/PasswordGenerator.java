package ru.hackaton.hackaton.security;

import java.security.SecureRandom;

public class PasswordGenerator {
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!()_";
    private static final String ALL_CHARS = LOWER + UPPER + DIGITS + SPECIAL;

    public static String generate(int length) {
        if (length < 8) {
            throw new IllegalArgumentException("Пароль должен быть не менее 8 символов!");
        }

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        // Гарантируем, что пароль содержит хотя бы по одному символу из каждой группы
        password.append(LOWER.charAt(random.nextInt(LOWER.length())));
        password.append(UPPER.charAt(random.nextInt(UPPER.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

        // Заполняем оставшуюся длину случайными символами
        for (int i = 4; i < length; i++) {
            password.append(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
        }

        // Перемешиваем символы, чтобы порядок не был предсказуемым
        return shuffleString(password.toString());
    }

    private static String shuffleString(String input) {
        char[] chars = input.toCharArray();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < chars.length; i++) {
            int j = random.nextInt(chars.length);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
        return new String(chars);
    }

}