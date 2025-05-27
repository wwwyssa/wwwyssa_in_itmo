package com.wwwyssa.lab6.common.models;

public class ServerUser {
    private final int id;
    private final String name;
    private final String passwordDigest;
    private final String salt;

    public ServerUser(int id, String name, String passwordDigest, String salt) {
        this.id = id;
        this.name = name;
        this.passwordDigest = passwordDigest;
        this.salt = salt;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPasswordDigest() {
        return passwordDigest;
    }

    public String getSalt() {
        return salt;
    }


}
