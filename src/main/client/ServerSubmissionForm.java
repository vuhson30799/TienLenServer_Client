package main.client;

import main.dto.PlayerData;

import javax.swing.*;
import java.awt.*;
import java.net.Socket;

@SuppressWarnings({"java:S106", "java:S110"})
public class ServerSubmissionForm extends JFrame implements Runnable {
    private PlayerData playerData;
    private final ServerSubmissionButton button;
    private transient Socket client;
    public ServerSubmissionForm() {
        this.setTitle("Card Game");
        button = new ServerSubmissionButton("Play");
        this.setLayout(new GridLayout(3, 2));
        this.setMinimumSize(new Dimension(500, 500));
        this.add(new JLabel("Server: "));
        this.add(new JTextField(3));
        this.add(new JLabel("Name: "));
        this.add(new JTextField(3));
        this.add(button);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    @Override
    public void run(){
        while (this.button.getClient() == null) {
            System.out.println();
        }
        this.setPlayerData(button.getInputPlayerData());
        this.setClient(button.getClient());
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public void setPlayerData(PlayerData playerData) {
        this.playerData = playerData;
    }

    public Socket getClient() {
        return client;
    }

    public void setClient(Socket client) {
        this.client = client;
    }
}
