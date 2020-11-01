package main.client;

import javax.swing.*;
import java.io.IOException;

public class TestGui {
    public static void main(String[] args) throws IOException {
        ClientForm clientForm = new ClientForm();
        clientForm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
