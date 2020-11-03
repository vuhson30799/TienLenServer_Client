package main.client;

import javax.swing.*;

public class TestGui {
    public static void main(String[] args) {
        ClientForm clientForm = new ClientForm();
        clientForm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        clientForm.run();
    }
}
