package com.wwwyssa.lab6.server;

import com.wwwyssa.lab6.common.console.Console;
import com.wwwyssa.lab6.common.console.DefaultConsole;
import common.managers.ValidateManager;
import server.managers.ServerManager;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Console console = new DefaultConsole();

        String host = "localhost";
        String strPort = "8080";

        int port = Integer.parseInt(strPort);

        Configuration.setHost(host);
        Configuration.setPort(port);
        Configuration.setStartFileName("main.json");
        Configuration.setHistoryFileName("_.json");

        ServerManager serverManager = new ServerManager();
        try {
            serverManager.start();
            console.write("Сервер запущен");
        } catch (IOException e) {
            console.write(e.toString());
            console.write("Не получилось запустить сервер");
            return;
        }
        serverManager.run();
    }
}