package com.newrelic.codingchallenge;

import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;


public class Main {



    public static void main(String[] args) throws Exception {
        try {
            Client.main();
        }
        catch (ConnectException e)
        {
            System.out.println("Starting up server ....");
            Server server = new Server();
            server.runServer();
        }
    }
}