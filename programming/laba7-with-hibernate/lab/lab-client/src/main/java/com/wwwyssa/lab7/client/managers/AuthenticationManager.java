package com.wwwyssa.lab7.client.managers;


import com.wwwyssa.lab7.client.util.Console;
import com.wwwyssa.lab7.common.util.Request;
import com.wwwyssa.lab7.common.util.Response;
import com.wwwyssa.lab7.common.util.User;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthenticationManager {
    private static String getHash(String password) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-224");
            md.update(password.getBytes());
            byte[] hashedPassword = md.digest();

            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hashedPassword) {
                stringBuilder.append(String.format("%02x", b));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Ошибка: алгоритм шифрования SHA-224 не поддерживается.");
            throw new RuntimeException(e);
        }
    }

    public static User sendAuthenticationRequest(ConnectionManager connectionManager, Console console, User user, String inputCommand) throws IOException, ClassNotFoundException {
        Request request = new Request(inputCommand, user);
        connectionManager.send(request);
        Response authResponse = connectionManager.receive();
        if (authResponse.getExecutionStatus() == null) {
            console.printError("Ошибка: сервер не ответил на запрос.");
            return null;
        }
        if (authResponse.getExecutionStatus().getExitCode()) {
            console.println(authResponse.getExecutionStatus().getAnswer());
            String id = authResponse.getExecutionStatus().getAnswer().toString();
            user.setId(Integer.parseInt(id));
            return user;
        } else {
            console.printError(authResponse.getExecutionStatus().getAnswer());
            return null;
        }
    }

    public static User authenticateUser(ConnectionManager networkManager, Console console) throws IOException, ClassNotFoundException {
        while (true) {
            console.println("Введите команду 'register' для регистрации или 'login' для авторизации:");
            String inputCommand = console.input().trim().toLowerCase();
            if (inputCommand.equals("register") || inputCommand.equals("login")) {
                console.println("Введите логин:");
                String username = console.input();
                console.println("Введите пароль для авторизации:");
                String password = getHash(console.input());
                User user = sendAuthenticationRequest(networkManager, console, new User(username, password), inputCommand);

                if (user != null) {
                    return user;
                }
            } else {
                console.printError("Команда '" + inputCommand + "' не найдена!");
            }
        }
    }
}