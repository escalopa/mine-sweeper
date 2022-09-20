import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Minesweeper {

    int rows;
    int columns;
    char[][] shownGameField;
    boolean[][] hiddenGameField;

    Minesweeper(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        hiddenGameField = new boolean[rows][columns];
        shownGameField = new char[rows][columns];

    }

    public void startGame() {

        initializeGameField();
        generateBombs();
        printField();

        boolean gameOver = false;
        while (!gameOver) {

            // read action from player
            Coordinate coordinate = receiveInput();
            if (safeLocation(coordinate)) {
                showLocations(coordinate);
            } else {
                gameOver = true;
            }
            printField();
        }
    }

    static class Coordinate {

        private int xAxis;
        private char yAxis;

        public Coordinate() {}

        public Coordinate(int xAxis, char yAxis) {
            this.yAxis = yAxis;
            this.xAxis = xAxis;
        }

        public char getyAxis() {
            return yAxis;
        }

        public int getxAxis() {
            return xAxis;
        }

        public void setxAxis(int xAxis) {
            this.xAxis = xAxis;
        }

        public void setyAxis(char yAxis) {
            this.yAxis = yAxis;
        }
    }

    private void printField() {
        char x = 'A';
        int count = 0;
        System.out.print("   ");
        while (count < shownGameField[0].length) {
            System.out.print(x);
            count += 1;
            x += 1;
        }
        System.out.println();
        count = 1;
        for (char[] row : shownGameField) {
            String count_str = count <= 9 ? count + " |" : count + "|";
            System.out.print(count_str);
            for (char c : row) {
                System.out.print(c);
            }
            count += 1;
            System.out.println();
        }
    }

    private void initializeGameField() {
        for (char[] row : shownGameField) {
            Arrays.fill(row, '-');
        }
    }

    private void generateBombs() {
        Random r = new Random();
        for (boolean[] row : hiddenGameField) {
            int i = r.nextInt(row.length);
            row[i] = true;
        }
    }

    private void showLocations(Coordinate coordinate) {

        if (countSurroundingBombs(coordinate) == 0) {
            for (int i = coordinate.getxAxis() - 1; i < coordinate.getxAxis() + 2; i++) {
                for (int j = coordinate.getyAxis() - 1; j < coordinate.getyAxis() + 2; j++) {
                    if (i > 0 & j > 0 & j < columns & i < rows) {
                        char num = (char) countSurroundingBombs(coordinate);
                        if (num == '0' && shownGameField[i][j] != '-')
                            showLocations(new Coordinate(i, (char) j));
                        else shownGameField[i][j] = num;
                    }
                }
            }
        } else
            shownGameField[coordinate.getxAxis()][coordinate.getyAxis()] = (char) countSurroundingBombs(coordinate);
    }

    private int countSurroundingBombs(Coordinate coordinate) {

        int count = 0;
        for (int i = coordinate.getxAxis() - 1; i < coordinate.getxAxis() + 2; i++) {
            for (int j = coordinate.getyAxis() - 1; j < coordinate.getyAxis() + 2; j++) {
                if (i > 0 & j > 0 & j < columns & i < rows) {
                    if (hiddenGameField[i][j]) count++;

                }
            }
        }
        return count;
    }

    public Coordinate receiveInput() {

        Coordinate coordinate = new Coordinate();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Input :");
        coordinate.setyAxis(scanner.next().charAt(0));
        coordinate.setxAxis((scanner.nextInt() - 1));

        System.out.println(coordinate.xAxis + " " + coordinate.yAxis);
        if ((coordinate.getyAxis()) >= 'A' & coordinate.getxAxis() <= rows + 1 & coordinate.getxAxis() >= 1) {
            char clone = (char) (coordinate.getyAxis() - 'A');
            System.out.println(clone);
            coordinate.setyAxis(clone);

            return coordinate;
        }

        System.out.println("\n You have entered a wrong input ... Try again ");
        return receiveInput();
    }

    // return true if safe location, else false
    private boolean safeLocation(Coordinate coordinate) {

        return !hiddenGameField[(coordinate.getyAxis())][coordinate.getxAxis()];

    }
}

    /*
    private void showSurroundingLocations(Queue<Coordinate> locations2Open) {
        Coordinate coordinate = new Coordinate();
        while (!locations2Open.isEmpty()) {
            coordinate = locations2Open.
        }
    }
    public Queue<Coordinate> getAllSurroundingLocations(Coordinate coordinate) {
        Queue<Coordinate> list = new LinkedList<>();
        list.offer(coordinate);
        for (int i = coordinate.getxAxis() - 1; i < coordinate.getxAxis() + 2; i++) {
            for (int j = coordinate.getyAxis() - 1; j < coordinate.getyAxis() + 2; j++) {
                if (j== coordinate.getyAxis() & i == coordinate.getxAxis()) j++;
                list.offer(new Coordinate(i, (char) j));
            }
        }
        return list;
    }
    */

            /*
            Queue<Coordinate> list = getAllSurroundingLocations(coordinate);
            while(!list.isEmpty()){
                Coordinate coordinate1 = list.poll();
                shownGameField[coordinate1.getxAxis()][coordinate1.getyAxis()] = (char) countSurroundingBombs(coordinate);
            }
            */