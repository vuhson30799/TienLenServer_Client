package main.thread;

import main.dto.ServerToClientData;
import main.tools.GameManager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerSendingThread extends Thread{
    private Socket client;
    private GameManager gameManager;
    private boolean hasError;
    private String message;

    public ServerSendingThread(Socket client, GameManager gameManager) {
        this.client = client;
        this.gameManager = gameManager;
    }

    public ServerSendingThread(Socket client, GameManager gameManager, boolean hasError, String message) {
        this.client = client;
        this.gameManager = gameManager;
        this.hasError = hasError;
        this.message = message;
    }
    @Override
    public void run() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
            outputStream.writeObject(setSendingDataFromServer());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private ServerToClientData setSendingDataFromServer() {
        ServerToClientData data = new ServerToClientData();
        data.setCurrentSet(gameManager.getPile().getCurrSet());
        data.setPlayers(gameManager.getPlayers());
        data.setWhoseTurn(gameManager.getWhoseTurn());
        data.setGameOver(gameManager.isGameOver());
        data.setHasError(hasError);
        data.setMessage(message);
        return data;
    }
}
