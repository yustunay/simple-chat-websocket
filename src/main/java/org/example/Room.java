package org.example;

import java.util.ArrayList;
import java.util.List;

public class Room {
    List<User> users = new ArrayList<>();

    public Room() {}

    public void addUser(User user){
       users.add(user);
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
