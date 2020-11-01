package main.client;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CardGui extends JLabel implements MouseListener {
    private int originalX;
    private int originalY;

    public CardGui() throws IOException {
        super();
        String imagePath = "./resources/images/hearts/ace.png";
        BufferedImage myPicture = ImageIO.read(new File(imagePath));
        Image img = myPicture.getScaledInstance(70,105, Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(img);
        this.setIcon(imageIcon);
        this.setOriginalX(this.getX());
        this.setOriginalY(this.getY());
        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JPanel parent = new JPanel();
        if (e.getSource() instanceof  CardGui) {
            CardGui component = (CardGui) e.getSource();
            if (component.getParent() instanceof JPanel) {
                parent = (JPanel) component.getParent();
            }
        }

        if (this.getY() <= 0) {
            this.setLocation(this.getX(), this.getY() + 10);
        } else {
            this.setLocation(this.getX(), this.getY() - 10);

        }
        parent.repaint();
        this.repaint();

    }

    @Override
    public void mousePressed(MouseEvent e) {
        //TO-DO: do nothing for now
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

    public int getOriginalX() {
        return originalX;
    }

    public void setOriginalX(int originalX) {
        this.originalX = originalX;
    }

    public int getOriginalY() {
        return originalY;
    }

    public void setOriginalY(int originalY) {
        this.originalY = originalY;
    }
}
