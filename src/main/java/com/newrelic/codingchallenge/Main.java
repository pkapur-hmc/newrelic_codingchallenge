package com.newrelic.codingchallenge;

import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;


public class Main {



    public static void main(String[] args) throws Exception {
        try { // Create a client
            Client.main();
        }
        catch (ConnectException e) // Server has not been set up yet, so set it up
        {
            System.out.println("Starting up server ....");
            Server server = new Server();
            server.runServer();
        }
    }
}