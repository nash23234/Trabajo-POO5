package Client;
import java.awt.event.*;
import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

import Common.Casilla;
import Common.Constantes;
import Common.Dot;
import Common.Mapa;
import Common.Target;

import java.net.*;
import java.io.*;

public class GUI implements ActionListener, Constantes{

    JFrame ventana;
    JButton next;
    Mapa mapa;
    Target target;

    Dot dot;

    Socket client;
    ObjectOutputStream output;
    
    public GUI(){

        ventana = new JFrame();
        mapa = new Mapa(this);


        ventana.add(mapa.panelTablero);

        next = new JButton("continuar");
        next.setActionCommand("next");

        ventana.add(next, BorderLayout.SOUTH);

        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.pack();
        ventana.setVisible(true);
        dot = new Dot();
        target = new Target();
        Serve server = new Serve(dot);
        Thread hilo = new Thread(server);
        hilo.start();
        //moveDot();
        run();


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(dot.currentPosition!=target.coords)
            mapa.tablero[target.coords[X]][target.coords[Y]].clearTarget();//limpia la vieja cordenada
        ((Casilla)e.getSource()).setAsTarget();//lo muestra
        target.coords = ((Casilla)e.getSource()).getCoords();//le envia las nuevas cordenadas

        try {
            client = new Socket("127.0.0.1", 4444);
            output = new ObjectOutputStream(client.getOutputStream());
            output.writeObject(target);
            output.flush();
            output.close();
            client.close();
        } catch (Exception ex) {
            //TODO: handle exception
        }
    }

    public void moveDot(){
        mapa.tablero[dot.lastPosition[X]][dot.lastPosition[Y]].clearDot();
        mapa.tablero[dot.currentPosition[X]][dot.currentPosition[Y]].setAsDot();
    }

    public void run(){
        mapa.tablero[dot.currentPosition[X]][dot.currentPosition[Y]].setAsDot();
        while (true){
            dot.move();
            moveDot();
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
    }

}