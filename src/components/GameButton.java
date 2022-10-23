package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public class GameButton extends JButton {
    public void setProperties(int i, int j, int unit, MouseListener mouseListener) {
        this.setName(i + " " + j);
        this.setText(null);
        this.setBackground(Color.GRAY);
        this.setFocusPainted(false);
        this.setPreferredSize(new Dimension(unit, unit));
        this.setVisible(true);
        this.setEnabled(true);
        this.setFont(new Font("", Font.LAYOUT_NO_LIMIT_CONTEXT, 20));
        this.setForeground(Color.GREEN);
        this.addMouseListener(mouseListener);
    }
}
