package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Timer;
import java.util.*;


public class Panel implements MouseListener {

    public final JPanel field = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

    private final int height, width, unit;
    private int xLocation, yLocation;
    private final GameButton[][] buttonsMatrix;
    private boolean[][] bombsField;
    private final List<Integer> checkedLocationsWithZeros = new LinkedList<>();
    private Icon flag, bomb;

    Panel(int dimension, int Unit) {
        this.height = dimension;
        this.width = dimension;
        this.unit = Unit;
        buttonsMatrix = new GameButton[this.height][this.width];
        bombsField = new boolean[this.height][this.width];
    }

    public void startGame() {
        fillButtonMatrix();
        generateBombs();
    }

    public void loadIcons() {
        flag = new ImageIcon("flag.png");
        bomb = new ImageIcon("bomb.png");
    }

    // fills the ButtonMatrix but buttons and add properties to them
    private void fillButtonMatrix() {

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
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
        int max = (height * width) / 5;
        for (int i = 0; i < max; i++) {
            int k = new Random().nextInt((height - 1));
            int j = new Random().nextInt((width - 1));
            bombsField[k][j] = true;
        }
    }

    // return the number of surrounding bombs of this point
    private int countSurroundingBombs(int x, int y) {
        int count = 0;
        for (int i = x - 1; i < x + 2; i++) {
            for (int j = y - 1; j < y + 2; j++) {
                if (checkButtonExistenceWithCoordinates(i, j) && bombsField[i][j]) {
                    count++;
                }
            }
        }
        return count;
    }

    private void showLocations(int x, int y) {
        if (countSurroundingBombs(x, y) == 0) {
            checkedLocationsWithZeros.add(x);
            checkedLocationsWithZeros.add(y);
            for (int i = x - 1; i < x + 2; i++) {
                for (int j = y - 1; j < y + 2; j++) {
                    if (i >= 0 & j >= 0 & i < height & j < width) {
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

    private boolean checkButtonExistenceWithCoordinates(int i, int j) {
        return i >= 0 & j >= 0 & j < width & i < height;
    }

    // return true if safe location, else false
    private boolean isSafeLocation(int x, int y) {
        return !bombsField[x][y];
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

    private void findClickedButton(MouseEvent e) {
        outerLoop:
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (e.getSource() == buttonsMatrix[i][j]) {
                    String[] buttonName = buttonsMatrix[i][j].getName().split(" ");
                    xLocation = Integer.parseInt(buttonName[0]);
                    yLocation = Integer.parseInt(buttonName[1]);
                    break outerLoop;
                }
            }
        }
    }

    private void swapFlag(int xLocation, int yLocation) {
        System.out.println(buttonsMatrix[xLocation][yLocation].getIcon());
        if (buttonsMatrix[xLocation][yLocation].getIcon() == null) {
            buttonsMatrix[xLocation][yLocation].setIcon(flag);
        } else {
            buttonsMatrix[xLocation][yLocation].setIcon(null);
        }
    }

    private void gameOver() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (bombsField[i][j]) {
                    buttonsMatrix[i][j].setIcon(bomb);
                    buttonsMatrix[i][j].setEnabled(true);
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

        bombsField = new boolean[height][width];
        generateBombs();
        resetButtonMatrix();

    }

    private void resetButtonMatrix() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
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
        findClickedButton(e);
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (isSafeLocation(xLocation, yLocation) && buttonsMatrix[xLocation][yLocation].getText() == null)
                showLocations(xLocation, yLocation);
            else if (!isSafeLocation(xLocation, yLocation)) {
                gameOver();
            }
        } else if (SwingUtilities.isRightMouseButton(e)) {
            swapFlag(xLocation, yLocation);
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
