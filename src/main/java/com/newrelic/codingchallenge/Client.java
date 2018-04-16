package com.newrelic.codingchallenge;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    /*
    main method for Client class that initializes the client and communicates with the server
     */
    public static void main(String args[]) throws Exception {

        Socket socket = new Socket("localhost", 4000); // Create new socket on localhost and port 4000
        BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Input stream from server
        PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), true); // Output stream to server
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in)); // Input stream from stdin

        String response;
        while(true) {
            String clientInput = input.readLine(); // Read stdin

            serverOut.println(clientInput); // Send input to server

            response = serverIn.readLine(); // Get response from server

            if (response.startsWith("REMOVE_CLIENT")) { // Terminate the client if required
                break;
            }

            else if (response.startsWith("CONTINUE")) // Continue with client
            {
                continue;
            }
        }
        socket.close();
        serverIn.close();
        serverOut.close();
        input.close();



    }
}
