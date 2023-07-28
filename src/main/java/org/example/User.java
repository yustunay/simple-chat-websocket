package org.example;

import org.java_websocket.WebSocket;

public class User {
    private int id;
    private String username;
    private WebSocket conn;

    public User() {}

    public User(int id, WebSocket conn) {
        this.id = id;
        this.conn = conn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public WebSocket getConn() {
        return conn;
    }

    public void setConn(WebSocket conn) {
        this.conn = conn;
    }
}
