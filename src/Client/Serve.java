package Client;

import java.net.*;
import java.io.*;
import Common.*;

public class Serve implements Runnable{
    ServerSocket server;
    Socket client;
    ObjectInputStream input;
    Dot dot;
    Dot dot2;

    public Serve(Dot d){
        dot = d;
        try {
            server = new ServerSocket(4445);
        } catch (Exception e) {
            //TODO: handle exception
        }
        
    }

    public void run(){
        try {
            while(true){
                client = server.accept();
                input = new ObjectInputStream(client.getInputStream());
                dot2 = (Dot)input.readObject();
                dot.currentPosition=dot2.currentPosition;
                dot.lastPosition=dot2.lastPosition;
                dot.target=dot2.target;
                
                input.close();
                client.close();
            }
        } catch (Exception e) {
            //TODO: handle exception
        }

    }
    
}
