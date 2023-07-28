package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Optional;

/**
 * A simple WebSocketServer implementation. Keeps track of a "chatroom".
 */
public class Main extends WebSocketServer {

    private final Room room = new Room();
    private final ObjectMapper mapper = new ObjectMapper();

    public Main(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public Main(InetSocketAddress address) {
        super(address);
    }

    public Main(int port, Draft_6455 draft) {
        super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.send("Welcome to the server!"); //This method sends a message to the new client
        broadcast("new connection: " + handshake
                .getResourceDescriptor()); //This method sends a message to all clients connected

        room.addUser(new User(conn.hashCode(), conn));
        System.out.println(conn.hashCode() + " entered the room!"); //conn.getRemoteSocketAddress().getAddress().getHostAddress()
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        broadcast(conn + " has left the room!");
        System.out.println(conn + " has left the room!");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        EventMessage eventMessage;
        try {
            eventMessage = mapper.readValue(message, EventMessage.class);
            Optional<User> user = getUserByConnHash(conn.hashCode());
            if (user.isPresent()) {
                if (eventMessage.getMessage() != null) {
                    EventMessage newMessage = new EventMessage();
                    newMessage.setUsername(user.get().getUsername());
                    newMessage.setMessage(eventMessage.getMessage());

                    room.getUsers().stream().forEach(u -> {
                        try {
                            u.getConn().send(mapper.writeValueAsString(newMessage));
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } else if (eventMessage.getUsername() != null) {
                    user.get().setUsername(eventMessage.getUsername());
                }
            }
        } catch (
                JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println(conn + ": " + message);
    }

    private Optional<User> getUserByConnHash(int hashCode) {
        return room.getUsers().stream().filter(u -> u.getId() == hashCode).findFirst();
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        conn.send(message.array());
        String stringMessage = new String(message.array(), message.position(), message.remaining());
        //room.addUser(new User(stringMessage));
        System.out.println(conn + ": " + stringMessage);
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        int port = 8887; // 843 flash policy port
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception ex) {
        }
        Main s = new Main(port);
        s.start();
        System.out.println("ChatServer started on port: " + s.getPort());

        BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String in = sysin.readLine();
            s.broadcast(in);
            if (in.equals("exit")) {
                s.stop(1000);
                break;
            }
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    @Override
    public void onStart() {
        System.out.println("Server started!");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }
}