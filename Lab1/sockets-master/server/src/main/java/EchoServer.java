//import java.io.*;
//import java.net.*;
//
//public class EchoServer {
//    public static void main(String[] args) {
//        try {
//            ServerSocket serverSocket = new ServerSocket(7788);
//
//            Socket socket = serverSocket.accept();
//
//            BufferedReader in = new BufferedReader(
//                new InputStreamReader(socket.getInputStream()));
//            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//
//            String message = in.readLine();
//            out.println("RECEIVED: " + message);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}

// The code below handles connections sequentially
import java.io.*;
import java.net.*;

public class EchoServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(7788);

            while (true) {
                Socket socket = serverSocket.accept();

                // Create a new thread to handle the client
                Thread clientThread = new Thread(() -> handleClient(socket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try {
            System.out.println("Client Connected...");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received: " + message);
                out.println("RECEIVED: " + message);
            }

            // Close the client socket and streams
            in.close();
            out.close();
            System.out.println("Client Disconnected...");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


// The code below is to handle parallel connections
//import java.io.*;
//import java.net.*;
//
//public class EchoServer {
//    public static void main(String[] args) {
//        try {
//            ServerSocket serverSocket = new ServerSocket(7788);
//
//            while (true) {
//                Socket socket = serverSocket.accept();
//
//                // Create a new thread to handle the client
//                Thread clientThread = new Thread(new ClientHandler(socket));
//                clientThread.start();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
//
//class ClientHandler implements Runnable {
//    private Socket socket;
//
//    public ClientHandler(Socket socket) {
//        this.socket = socket;
//    }
//
//    @Override
//    public void run() {
//        try {
//            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//
//            String message;
//            while ((message = in.readLine()) != null) {
//                System.out.println("Received: " + message);
//                out.println("Echo: " + message);
//            }
//
//            // Close the client socket and streams
//            in.close();
//            out.close();
//            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
