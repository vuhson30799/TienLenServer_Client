package main.client;

import main.dto.PlayerData;
import main.dto.ServerToClientData;
import main.tools.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.stream.IntStream;

import static main.constant.Application.*;

@SuppressWarnings("java:S110")
public class ClientApplication extends JFrame implements Runnable {
    private PlayerData playerData;
    private MyPanel southPanel;
    private MyPanel northPanel;
    private MyPanel westPanel;
    private MyPanel eastPanel;
    private MyPanel centerPanel;

    public ClientApplication() {
        this.setTitle("Card Game");
        this.setSize( 1200, 700);
        this.setMinimumSize(new Dimension(1200, 700));
        southPanel = new MyPanel();
        southPanel.setBackground(Color.DARK_GRAY);
        southPanel.setMinimumSize(new Dimension(1200, 120));
        southPanel.setSize(1200, 120);

        northPanel = new MyPanel();
        northPanel.setBackground(BACKGROUND_COLOR);

        westPanel = new MyPanel();
        westPanel.setBackground(BACKGROUND_COLOR);

        eastPanel = new MyPanel();
        eastPanel.setBackground(BACKGROUND_COLOR);

        centerPanel = new MyPanel();
        centerPanel.setBackground(BACKGROUND_COLOR);

        this.getContentPane().add(BorderLayout.SOUTH, southPanel);
        this.getContentPane().add(BorderLayout.NORTH, northPanel);
        this.getContentPane().add(BorderLayout.WEST, westPanel);
        this.getContentPane().add(BorderLayout.EAST, eastPanel);
        this.getContentPane().add(BorderLayout.CENTER, centerPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void run() {
        try {
            Socket socket = initFrameChoosingServer();
            waitingForStartingSign(socket);
            this.setVisible(true);
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
                    //This implementation fixes the problem that panel needs to be resized to show all the component.
                    resize = refreshUI(inputData, resize);
                    isGameOver = inputData.isGameOver();
                }
            } while (!isGameOver);

            notifyWinner(socket);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            displayPopUp(centerPanel, e.getMessage());
        }

    }

    private void notifyWinner(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        Object object = inputStream.readObject();
        if (object instanceof ServerToClientData) {
            ServerToClientData inputData = (ServerToClientData) object;
            displayPopUp(this.centerPanel, inputData.getMessage());
            this.dispose();
        }
    }

    private void waitingForStartingSign(Socket socket) {
        try {
            displayPopUp(this.centerPanel, "Waiting for other players...");
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            Object object = inputStream.readObject();
            displayPopUp(this.centerPanel, String.valueOf(object));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private Socket initFrameChoosingServer() {
        ServerSubmissionForm form = new ServerSubmissionForm();
        form.run();
        Socket client = form.getClient();
        this.playerData = form.getPlayerData();
        form.dispose();
        return client;
    }

    private boolean refreshUI(ServerToClientData inputData, boolean resize) {

        southPanel.removeAll();
        northPanel.removeAll();
        westPanel.removeAll();
        eastPanel.removeAll();
        centerPanel.removeAll();

        southPanel.setPlayedCards(new ArrayList<>());
        Player player = inputData.getPlayers().get(playerData.getPlayerId());

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

        initDataForOtherPanels(playerData.getPlayerId(), inputData);
        createSetButton(inputData.getWhoseTurn());

        if (resize) {
            this.setSize( 1201, 700);
            resize = false;
        } else {
            this.setSize(1202, 700);
            resize =true;
        }

        southPanel.repaint();
        northPanel.repaint();
        westPanel.repaint();
        eastPanel.repaint();
        centerPanel.repaint();
        return resize;
    }

    private void initDataForOtherPanels(int playerId, ServerToClientData inputData) {
        IntStream.range(0, 4).filter(index -> index != playerId).forEach(index -> {
            String numberOfCard = String.valueOf(inputData.getPlayers().get(index).getHand().size());
            String playerName = inputData.getPlayers().get(index).getName();

            switch (playerId) {
                case 0:
                    setNumberOfCardsLeftForOthers(index, 1, 2, 3, numberOfCard, playerName);
                    break;
                case 1:
                    setNumberOfCardsLeftForOthers(index, 0, 2, 3, numberOfCard, playerName);
                    break;
                case 2:
                    setNumberOfCardsLeftForOthers(index, 0, 1, 3, numberOfCard, playerName);
                    break;
                case 3:
                    setNumberOfCardsLeftForOthers(index, 0, 1, 2, numberOfCard, playerName);
                    break;
                default:
                    break;
            }
        });
    }

    private void setNumberOfCardsLeftForOthers(int index, int west, int north, int east, String numberOfCard, String playerName) {
        try {
            BufferedImage myPicture = ImageIO.read(new File(BACK_CARD_IMAGE_PATH));
            Image img = myPicture.getScaledInstance(70,105, Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(img);
            JLabel label = new JLabel();
            label.setIcon(imageIcon);
            label.setText("<html>Player: " + playerName +
                    "<br>Cards left: " + numberOfCard);
            label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
            label.setForeground(Color.white);

            if (index == north) {
                northPanel.add(label);
                return;
            }

            label.setVerticalTextPosition(SwingConstants.BOTTOM);
            label.setHorizontalTextPosition(SwingConstants.CENTER);

            if (index == west) {
                westPanel.add(label);
                return;
            }

            if (index == east) {
                eastPanel.add(label);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createSetButton(int whoseTurn) {
        MyButton submitBtn = new MyButton("Play");
        MyButton passBtn = new MyButton("Pass");

        passBtn.setSize(50, 10);
        submitBtn.setSize(50, 10);

        submitBtn.setEnabled(playerData.getPlayerId() == whoseTurn);
        passBtn.setEnabled(playerData.getPlayerId() == whoseTurn);

        southPanel.add(submitBtn);
        southPanel.add(passBtn);

    }

    private void displayPopUp(MyPanel panel, String message) {
        JOptionPane.showMessageDialog(panel.getParent(), message);
    }
}
