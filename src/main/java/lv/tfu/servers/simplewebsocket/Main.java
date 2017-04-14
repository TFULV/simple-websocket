package lv.tfu.servers.simplewebsocket;

import org.glassfish.tyrus.server.Server;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    protected static boolean WELCOME_MESSAGE = true;
    protected static boolean ECHO_SERVER = true;

    private static ArrayList<Session> clients = new ArrayList<Session>();
    private static int PORT = 8080;
    private enum Commands {
        DEFAULT,
        HELP,
        PRINT,
        PRIVATE,
        EXIT
    }

    public static void main(String[] args) {
        runServer();
    }

    private static void runServer() {
        Server server = new Server("localhost", Main.PORT, "/", WebSocketClient.class);
        boolean shutDown = false;

        try {
            server.start();
            System.out.println("Now you can visit the website http://websocket.org/echo.html and begin to chatting!\n - For help, write the HELP command - ");

            Scanner scanner = new Scanner(System.in);
            while(true) {
                String commandLine = scanner.nextLine();
                String[] command = commandLine.split(" ");

                Commands enumVal = Commands.DEFAULT;
                try {
                    enumVal = Commands.valueOf(command[0].toUpperCase());
                }
                catch (IllegalArgumentException ignored) {}

                switch (enumVal) {
                    case HELP:
                        System.out.println("Commands:\n print [string] - Write to all connected clients\n to [session-id] [string] - Write to current client\n exit - The process ends");
                        break;
                    case PRINT:
                        if(commandLine.length() < 5) {
                            System.out.println("You did not enter a message");
                        }
                        else {
                            String message = commandLine.substring(6);
                            sendMessage(message);
                        }
                        break;
                    case PRIVATE:
                        if(commandLine.length() < 44) {
                            System.out.println("You did not enter a message");
                        }
                        else {
                            String message = commandLine.substring(45);
                            String searchingId = commandLine.substring(8, 44);
                            sendMessage(message, searchingId);
                        }
                        break;
                    case EXIT:
                        shutDown = true;
                        break;
                    default:
                        System.err.println("Unknown command! Try help to show command list");
                        break;
                }

                if(shutDown) {
                    System.out.println("The process ends...");
                    break;
                }
            }
        } catch (DeploymentException e) {
            throw new RuntimeException(e);
        } finally {
            server.stop();
        }
    }

    protected static void addClient(Session session) {
        Main.clients.add(session);
    }

    protected static ArrayList<Session> getClients() {
        return Main.clients;
    }

    protected static void removeClient(Session session) {
        Main.clients.remove(session);
    }

    private static void sendMessage(String message) {
        sendMessage(message, null);
    }

    private static void sendMessage(String message, String sessionId) {
        if(Main.getClients().size() > 0) {
            try {
                int count = 0;
                for (Session session: Main.getClients()) {
                    if(sessionId != null) {
                        if(session.getId().equals(sessionId)) {
                            session.getBasicRemote().sendText(message);
                            count++;
                            break;
                        }
                    }
                    else {
                        session.getBasicRemote().sendText(message);
                        count++;
                    }
                }
                System.out.println("Has been sent to " + count + " client" + (count > 1 ? "s" : ""));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("No connected clients");
        }
    }
}