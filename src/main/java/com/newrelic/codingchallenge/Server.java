package com.newrelic.codingchallenge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.Log4JLogger;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {

    private AtomicInteger numClients; // Number of connected clients
    private Set receivedInputs; // Thread safe set storing unique numbers
    private DisplayedInfo info; // Class storing information to be displayed at 10s intervals
    private ExecutorService pool; // Executor that tracks all connected clients, limits to 5
    private List<Socket> listOfClients; // Thread safe list of all connected clients
    private Log logger = LogFactory.getLog("numbers.log");

    /*
    Default constructor for object of type Server
     */
    public Server()
    {
        numClients = new AtomicInteger(0);
        ConcurrentHashMap inputsMap = new ConcurrentHashMap<>();
        receivedInputs = inputsMap.newKeySet();
        info = new DisplayedInfo();
        pool = Executors.newFixedThreadPool(5);
        listOfClients = Collections.synchronizedList(new ArrayList<>());


    }

    /*
    Run() method for server type
     */
    public void runServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(4000); // Create new server socket on port 4000
        System.out.println("Server is running");

        // Outputted message every 10s providing information on number of unique and repeated numbers
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                info.displayAndResetInfo();
            }
        };

        Timer timer = new Timer();
        long delay = 10000;
        long interval = 10000;

        timer.scheduleAtFixedRate(task, delay, interval);

        try {
            while (true) { // Accept up to 5 concurrent clients
                Socket socket = serverSocket.accept();
                numClients.incrementAndGet();
                ThreadedServer serverThread = new ThreadedServer(socket, numClients, this);
                pool.execute(serverThread);
                listOfClients.add(socket);

            }
        } finally {
            serverSocket.close();
        }
    }


    /*
    Shutdown all clients
     */
    public void shutdown() throws IOException {
        pool.shutdown();
        for (Socket socket : listOfClients)
        {
            socket.shutdownInput();
            socket.shutdownOutput();
        }
    }

    /*
    main method that creates and runs a multi-threaded server
     */
    public static void main(String[] args) throws Exception
    {
        Server server = new Server();
        server.runServer();
    }


    /*
    Subclass of server that multi-threads
     */
    public class ThreadedServer implements Runnable {
        Socket socket; // Socket the client is connected to
        BufferedReader input; // Input stream
        PrintWriter output; // Output stream
        AtomicInteger id; // ID of client
        Server parentServer; // Server that client is connected to

        /*
        Constructor for object of type ThreadedServer
        Input: Socket socket, AtomicInteger id, Server parentServer
         */
        ThreadedServer(Socket socket, AtomicInteger id, Server parentServer) throws IOException {
            this.socket = socket;
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            this.id = id;
            this.parentServer = parentServer;

        }

        /*
        Checks to see if input is valid 9 decimal string
        rtype: boolean
         */
        private boolean validInput(String input)
        {
            if (input == null)
                return false;


            String pattern = "^[0-9]{9}$";
            return input.matches(pattern);

        }

        /*
        Override the runnable run() method to run ThreadedServer
         */
        @Override
        public void run()
        {
            try {
                while (true)
                {
                    String message = input.readLine(); // Get client inputted string
                    if (message.compareTo("terminate") == 0) // If client has said terminate, shutdown server and client
                    {
                        output.println("REMOVE_CLIENT");
                        parentServer.shutdown();
                        break;
                    }
                    if (validInput(message)) // If valid input, add to set as necessary
                    {
                        if (receivedInputs.add(message))
                        {
                            info.incrementUnique();
                            output.println("CONTINUE");
                            logger.info(message);
                        }
                        else
                        {
                            info.incrementDuplicates();
                            output.println("CONTINUE");
                        }
                    }
                    else // If invalid input, remove the client with no message
                    {
                        output.println("REMOVE_CLIENT");
                        listOfClients.remove(socket);
                        socket.close();
                        numClients.decrementAndGet();
                        break;
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
