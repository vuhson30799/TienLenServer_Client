package main.client;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ClientForm extends JFrame {
    ClientForm() throws IOException {
        JFrame frame = new JFrame("Client Form");
        frame.setSize( 1100, 700);
        frame.setMinimumSize(new Dimension(1100, 700));
        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.DARK_GRAY);
        southPanel.setMinimumSize(new Dimension(0, 120));

        JPanel northPanel = new JPanel();
        northPanel.setBackground(Color.YELLOW);

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.GREEN);

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.RED);

        for (int i = 1; i <= 13; i++) {
            southPanel.add(new CardGui());
        }
        JButton submitBtn = new JButton("Submit");
        submitBtn.setSize(50, 10);
        southPanel.add(submitBtn);

        JTextArea textAreaNorth = new JTextArea();
        textAreaNorth.append("Player 1!!");
        northPanel.add(textAreaNorth);

        JTextArea textAreaWest = new JTextArea();
        textAreaWest.append("Player 2!!");
        rightPanel.add(textAreaWest);

        JTextArea textAreaEast = new JTextArea();
        textAreaEast.append("Player 3!!");
        leftPanel.add(textAreaEast);

        frame.getContentPane().add(BorderLayout.SOUTH, southPanel);
        frame.getContentPane().add(BorderLayout.NORTH, northPanel);
        frame.getContentPane().add(BorderLayout.WEST, rightPanel);
        frame.getContentPane().add(BorderLayout.EAST, leftPanel);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
