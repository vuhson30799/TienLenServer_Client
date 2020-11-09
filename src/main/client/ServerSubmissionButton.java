package main.client;

import main.dto.PlayerData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static main.constant.Application.PORT;

@SuppressWarnings("java:S110")
public class ServerSubmissionButton extends JButton implements MouseListener {
    private PlayerData inputPlayerData;
    private transient Socket client;

    public ServerSubmissionButton(String text) {
        super(text);
        addMouseListener(this);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        //TO-DO: do nothing for now
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() instanceof ServerSubmissionButton) {
            displayPopUp(this.getParent(), "Connecting to host...");
            ((ServerSubmissionButton) e.getSource()).setEnabled(false);
            List<Component> components = Arrays.stream(((ServerSubmissionButton) e.getSource()).getParent()
                    .getComponents())
                    .filter(component -> component instanceof JTextField)
                    .collect(Collectors.toList());
            JTextField comp1 = (JTextField) components.get(0);
            JTextField comp2 = (JTextField) components.get(1);

            String serverAddress = comp1.getText();
            String playerName = comp2.getText();
            PlayerData playerData = new PlayerData(playerName);
            try {
                Socket socket = new Socket(serverAddress, PORT);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(playerData);

                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                this.inputPlayerData = (PlayerData) inputStream.readObject();
                this.client = socket;
                displayPopUp(this.getParent(), "Connect successfully.");
            } catch (IOException | ClassNotFoundException ioException) {
                ioException.printStackTrace();
                ((ServerSubmissionButton) e.getSource()).setEnabled(true);
                displayPopUp(this.getParent(), "Can't connect to host: " + ioException.getMessage());
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //TO-DO: do nothing for now
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //TO-DO: do nothing for now
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //TO-DO: do nothing for now
    }

    public Socket getClient() {
        return client;
    }

    public PlayerData getInputPlayerData() {
        return inputPlayerData;
    }

    private void displayPopUp(Container container, String message) {
        JOptionPane.showMessageDialog(container, message);
    }
}
