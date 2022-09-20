package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Timer;
import java.util.*;


public class WindowPanel implements MouseListener {

    public final JPanel field = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

    private final int height, width, vertical, horizontal, unit;
    private final GameButton[][] buttonsMatrix;
    private  boolean[][] bombsField;
    private final List<Integer> checkedLocationsWithZeros = new LinkedList<>();
    private Icon flag, bomb;

    WindowPanel(int dimension, int height, int width, int Unit) {
        this.vertical = dimension;
        this.horizontal = dimension;
        this.unit = Unit;
        this.height = vertical * unit;
        this.width = horizontal * unit;
        buttonsMatrix = new GameButton[vertical][horizontal];
        bombsField = new boolean[vertical][horizontal];
    }

    public void startGame() {
        fillButtonMatrix();
        generateBombs();
    }

    public void loadIcons() {
        flag = new ImageIcon("flagResized.PNG");
        bomb = new ImageIcon("bombResized.PNG");

    }

    // fills the ButtonMatrix but buttons and add properties to them
    private void fillButtonMatrix() {

        for (int i = 0; i < vertical; i++) {
            for (int j = 0; j < horizontal; j++) {
                buttonsMatrix[i][j] = new GameButton();
                buttonsMatrix[i][j].setProperties(i, j, unit, this);
                buttonsMatrix[i][j].addMouseListener(this);
                field.add(buttonsMatrix[i][j]);
            }
        }
        field.setVisible(true);
    }

    // generates bombs all aver the map
    public void generateBombs() {
        int max = (vertical * horizontal) / 5;
        for (int i = 0; i < max; i++) {
            int k = new Random().nextInt((vertical - 1));
            int j = new Random().nextInt((horizontal - 1));
            bombsField[k][j] = true;
        }
    }

    // return the number of surrounding bombs of this point
    private int countSurroundingBombs(int x, int y) {

        int count = 0;
        for (int i = x - 1; i < x + 2; i++) {
            for (int j = y - 1; j < y + 2; j++) {
                if (i >= 0 & j >= 0 & j < horizontal & i < vertical) {
                    if (bombsField[i][j])
                        count++;
                }
            }
        }
        return count;
    }

    // return true if safe location, else false
    private boolean isSafeLocation(int x, int y) {
        return !bombsField[x][y];
    }

    private void showLocations(int x, int y) {

        if (countSurroundingBombs(x, y) == 0) {
            checkedLocationsWithZeros.add(x);
            checkedLocationsWithZeros.add(y);
            for (int i = x - 1; i < x + 2; i++) {
                for (int j = y - 1; j < y + 2; j++) {
                    if (i >= 0 & j >= 0 & i < vertical & j < horizontal) {
                        int num = countSurroundingBombs(x, y);
                        if (num == 0 && !isPassedLocation(i, j))
                            showLocations(i, j);
                        else if (num == 0) {
                            buttonsMatrix[i][j].setEnabled(false);
                            buttonsMatrix[i][j].setBackground(Color.WHITE);
                        } else {
                            buttonsMatrix[i][j].setText(String.valueOf(num));
                        }
                    }
                }
            }
        } else
            buttonsMatrix[x][y].setText(String.valueOf(countSurroundingBombs(x, y)));
    }

    // returns true if we passed this zero before else false
    private boolean isPassedLocation(int x, int y) {
        if (!checkedLocationsWithZeros.isEmpty()) {
            for (int i = 0; i < checkedLocationsWithZeros.size(); i += 2) {
                if (x == checkedLocationsWithZeros.get(i) && y == checkedLocationsWithZeros.get((i + 1)))
                    return true;
            }
        }
        return false;
    }

    private void gameOver() throws InterruptedException {
        for (int i = 0; i < vertical; i++) {
            for (int j = 0; j < horizontal; j++) {
                if (bombsField[i][j]) {
                    buttonsMatrix[i][j].setIcon(bomb);
                    buttonsMatrix[i][j].setEnabled(true);
                    //buttonsMatrix[i][j].setForeground(Color.RED);
                    //buttonsMatrix[i][j].setText("B");
                } else buttonsMatrix[i][j].setEnabled(false);
            }
        }
        new Timer().schedule(
                new TimerTask() {
                    public void run() {
                        try {
                            restartGame();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, 5000
        );
    }

    private void restartGame() throws InterruptedException {

        bombsField = new boolean[vertical][horizontal];
        generateBombs();
        _fillButtonMatrix();

    }

    private void _fillButtonMatrix() {

        for (int i = 0; i < vertical; i++) {
            for (int j = 0; j < horizontal; j++) {
                buttonsMatrix[i][j].setText(null);
                buttonsMatrix[i][j].setEnabled(true);
                buttonsMatrix[i][j].setIcon(null);
                buttonsMatrix[i][j].setBackground(Color.GRAY);
            }
        }
        checkedLocationsWithZeros.clear();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int xLocation = 0;
        int yLocation = 0;
        outerLoop:
        for (int i = 0; i < vertical; i++) {
            for (int j = 0; j < horizontal; j++) {
                if (e.getSource() == buttonsMatrix[i][j]) {
                    String[] buttonName = buttonsMatrix[i][j].getName().split(" ");
                    xLocation = Integer.parseInt(buttonName[0]);
                    yLocation = Integer.parseInt(buttonName[1]);
                    break outerLoop;
                }
            }
        }
        //System.out.println(countSurroundingBombs(xLocation,yLocation)+" "+bombsField[xLocation][yLocation]);
        if (!SwingUtilities.isRightMouseButton(e)) {
            if (isSafeLocation(xLocation, yLocation) && buttonsMatrix[xLocation][yLocation].getText() == null)
                showLocations(xLocation, yLocation);
            else if (!isSafeLocation(xLocation, yLocation)) {
                try {
                    gameOver();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }

        } else {
            if (buttonsMatrix[xLocation][yLocation].getIcon() == null) {
                buttonsMatrix[xLocation][yLocation].setIcon(flag);
            } else {
                buttonsMatrix[xLocation][yLocation].setIcon(null);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
