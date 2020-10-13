package com.chatProject.Chat.FireBaseUtils.Model;


public class Room {
    String id;
    String name;
    String description;
    int currentActiveUsers;
    String createdAt;

    public Room(String id, String name, String description, int currentActiveUsers, String createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.currentActiveUsers = currentActiveUsers;
        this.createdAt = createdAt;
    }

    public Room() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCurrentActiveUsers() {
        return currentActiveUsers;
    }

    public void setCurrentActiveUsers(int currentActiveUsers) {
        this.currentActiveUsers = currentActiveUsers;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
