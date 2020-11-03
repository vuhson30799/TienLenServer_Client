package main.client;

import main.dto.ServerToClientData;
import main.tools.Player;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientForm extends JFrame implements Runnable {
    private MyPanel southPanel;
    private MyPanel northPanel;
    private MyPanel westPanel;
    private MyPanel eastPanel;
    private MyPanel centerPanel;
    ClientForm() {

        this.setTitle("Client Form");
        this.setSize( 1200, 700);
        this.setMinimumSize(new Dimension(1200, 700));
        southPanel = new MyPanel();
        southPanel.setBackground(Color.DARK_GRAY);
        southPanel.setMinimumSize(new Dimension(1200, 120));
        southPanel.setSize(1200, 120);

        northPanel = new MyPanel();
        northPanel.setBackground(Color.YELLOW);

        westPanel = new MyPanel();
        westPanel.setBackground(Color.GREEN);

        eastPanel = new MyPanel();
        eastPanel.setBackground(Color.RED);

        centerPanel = new MyPanel();
        centerPanel.setBackground(Color.decode("0x1b6d3c"));

        JTextArea textAreaNorth = new JTextArea();
        textAreaNorth.append("Player 1!!");
        northPanel.add(textAreaNorth);

        JTextArea textAreaWest = new JTextArea();
        textAreaWest.append("Player 2!!");
        eastPanel.add(textAreaWest);

        JTextArea textAreaEast = new JTextArea();
        textAreaEast.append("Player 3!!");
        westPanel.add(textAreaEast);

        this.getContentPane().add(BorderLayout.SOUTH, southPanel);
        this.getContentPane().add(BorderLayout.NORTH, northPanel);
        this.getContentPane().add(BorderLayout.WEST, westPanel);
        this.getContentPane().add(BorderLayout.EAST, eastPanel);
        this.getContentPane().add(BorderLayout.CENTER, centerPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket("localhost", 5000);
            boolean resize = false;
            boolean isGameOver = false;
            do {
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                Object object = inputStream.readObject();
                if (object instanceof ServerToClientData) {
                    ServerToClientData inputData = (ServerToClientData) object;
                    if (inputData.isHasError()) {
                        displayPopUp(centerPanel, inputData.getMessage());
                        continue;
                    }
                    southPanel.removeAll();
                    centerPanel.removeAll();
                    southPanel.setPlayedCards(new ArrayList<>());
                    Player player = inputData.getPlayers().get(inputData.getWhoseTurn());

                    southPanel.setPlayer(player);
                    player.getHand().forEach(card -> {
                        try {
                            southPanel.add(BorderLayout.CENTER, new MyCard(card));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    inputData.getCurrentSet().forEach(card -> {
                        try {
                            centerPanel.add(new MyCard(card));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    createSetButton();
                    southPanel.repaint();
                    centerPanel.repaint();
                    //This implementation fixes the problem that panel needs to be resized to show all the component.
                    if (resize) {
                        this.setSize( 1201, 700);
                        resize = false;
                    } else {
                        this.setSize(1202, 700);
                        resize =true;
                    }
                    isGameOver = inputData.isGameOver();
                }
            } while (!isGameOver);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void createSetButton() {
        MyButton submitBtn = new MyButton("Play");
        MyButton passBtn = new MyButton("Pass");
        passBtn.setSize(50, 10);
        submitBtn.setSize(50, 10);

        southPanel.add(submitBtn);
        southPanel.add(passBtn);

    }

    private void displayPopUp(MyPanel panel, String message) {
        JOptionPane.showMessageDialog(panel.getParent(), message);
    }

    public MyPanel getSouthPanel() {
        return southPanel;
    }

    public void setSouthPanel(MyPanel southPanel) {
        this.southPanel = southPanel;
    }

    public MyPanel getNorthPanel() {
        return northPanel;
    }

    public void setNorthPanel(MyPanel northPanel) {
        this.northPanel = northPanel;
    }

    public MyPanel getWestPanel() {
        return westPanel;
    }

    public void setWestPanel(MyPanel westPanel) {
        this.westPanel = westPanel;
    }

    public MyPanel getEastPanel() {
        return eastPanel;
    }

    public void setEastPanel(MyPanel eastPanel) {
        this.eastPanel = eastPanel;
    }

    public MyPanel getCenterPanel() {
        return centerPanel;
    }

    public void setCenterPanel(MyPanel centerPanel) {
        this.centerPanel = centerPanel;
    }
}
