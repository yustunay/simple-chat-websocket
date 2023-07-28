# Java Websocket Server

This is a guided programming exercise, so do not hesitate to ask questions, google, look at documentation. Use your resources.

* A client can be found [here](https://github.com/AlexRapalaFIS/ws-chat/tree/solid-client)
* A deployed client that you can use can be found [here](https://ws-chat-client-sage.vercel.app)
## Steps
1. Create a "Chat Room" on the server
2. Create a "User" and add them to the Chat Room
   1. The client will send a message in JSON format:
    ```
    {
        "username": "Alex"
    }
    ```

    A user will need to be created such that any further messages sent by this user can be sent as "Alex"

3. Send messages from a User
   1. This client will send a message in JSON format to the server like:
    ```
    {
        "message": "This is an example message"
    }
    ```

    The server should emit to all users in the chat room and the client should receive a JSON payload like:
   ```
    {
        "username": "Alex",
        "message": "This is an example message"
    }
    ```