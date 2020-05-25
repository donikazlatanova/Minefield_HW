package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/**
 * Клас, който представлява игра тип минно поле. Целта е да се прихванат и обезвредят всички мини от играча, достигайки финала.
 *
 * @author Donika Zlatanova
 */
public class Minefield {
    public static Scanner input = new Scanner(System.in);
    public static Random random = new Random();
    public static int rows = 0;
    public static int cols = 0;
    public static int mines = 0;
    public static int rowFigure = 0;
    public static int colFigure = 0;
    public static int possibleIndexRowStart = rows * cols;
    public static int possibleIndexColStart = rows * cols;
    public static int possibleIndexRowFinal = rows * cols;
    public static int possibleIndexColFinal = rows * cols;
    public static int possibleIndexRowMine = rows * cols;
    public static int possibleIndexColMine = rows * cols;


    public static String figure = "*";
    public static String mine = "Y";
    public static String empty = "N";
    public static String start = "S";
    public static String finish = "F";
    public static String visited = "V";
    public static String piece = "X";

    /**
     * В главния метод част от нужната информация ще се чете от текстов файл.
     *
     * @param args ;
     */
    public static void main(String[] args) {
        int rowWishedFromUser = 0;
        int colWishedFromUser = 0;
        int number_of_probes = 0;
        int number_of_disposal = 0;


        int maxNumberOfStart = 1;
        int maxNumberOfFinal = 1;

        int placedMines = 0;

        File fileReference = new File("resource/enemy_teritory");
        File fileReference2 = new File("resource/configurations");
        try {
            FileReader fileReferenceReader = new FileReader(fileReference);
            BufferedReader bufferedReader = new BufferedReader(fileReferenceReader);


            String lineReference;
            String dataName;
            int dataSize;
            while ((lineReference = bufferedReader.readLine()) != null) {
                String[] arrayData = lineReference.split("=");
                dataName = arrayData[0];
                dataSize = Integer.parseInt(arrayData[1]);
                if (dataName.equals("width")) {
                    rows = dataSize;
                    System.out.println("Ширина на дъската: " + rows);
                }
                if (dataName.equals("height")) {
                    cols = dataSize;
                    System.out.println("Дължина на дъската: " + cols);
                }
                if (dataName.equals("mines")) {
                    mines = dataSize;
                    System.out.println("Брой мини: " + mines);
                }


            }
            String[][] board = new String[rows][cols];
            String[][] newArray = new String[rows][cols];

            FileReader fileReferenceReader2 = new FileReader(fileReference2);
            BufferedReader bufferedReader2 = new BufferedReader(fileReferenceReader2);
            String lineReference2;
            String dataName2;
            int dataSize2;
            while ((lineReference2 = bufferedReader2.readLine()) != null) {
                String[] arrayData2 = lineReference2.split("=");
                dataName2 = arrayData2[0];
                dataSize2 = Integer.parseInt(arrayData2[1]);
                if (dataName2.equals("number_of_probes")) {
                    number_of_probes = dataSize2;
                    System.out.println("Брой проби за мини: " + number_of_probes);
                }
                if (dataName2.equals("number_of_disposal")) {
                    number_of_disposal = dataSize2;
                    System.out.println("Брой обезвреждания: " + number_of_disposal);
                }

            }

            manageBoardPieces(maxNumberOfStart, maxNumberOfFinal, board, newArray, placedMines);
            legend();
            action(rowWishedFromUser, colWishedFromUser, board, newArray, number_of_probes, number_of_disposal);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Отпечатва менюто с опции с възможни ходове.
     */
    public static void printMenu() {
        System.out.println("1. Проба за мина\n" +
                "2. Обезвреждане на мина\n" +
                "3. (пре)Мини");
    }

    /**
     * Изпълнява опция проба за мина - отпечатва къде има и къде няма мина около поисканата позиция.
     * Всички съседни клетки на проверяваната ще получат стойност Y ако върху тях има мина или N
     * ако върху тях няма. Няма да се отпечат тези съседни клетки, които принадлежат на редицата/колоната
     * на текущата позиция, на която сме
     * (в зависимост кое се изменя спрямо данните на текущата позиция).
     *
     * @param board             масив, в който се пазят всички позиици на мините
     * @param newArray          масив, чрез който ще отпечатваме определени позиции около желаните координати, като скриваме с "Х" останалите
     * @param rowWishedFromUser координат редица, посочен от потребителя
     * @param colWishedFromUser координат колона, посочен от потребителя
     * @param rowFigure         - координат редица на текущата позиция
     * @param colFigure         - координат колона на текущата позиция
     * @param number_of_probes  брой проби
     */
    public static void probeForMine(String[][] board, String[][] newArray, int rowWishedFromUser, int colWishedFromUser, int rowFigure, int colFigure, int number_of_probes) {
        if (number_of_probes > 0) {
            if (colWishedFromUser == colFigure + 1 && colWishedFromUser < board.length) {

                newArray[rowWishedFromUser][colWishedFromUser] = board[rowWishedFromUser][colWishedFromUser];
                if (colWishedFromUser + 1 < board.length)
                    newArray[rowWishedFromUser][colWishedFromUser + 1] = board[rowWishedFromUser][colWishedFromUser + 1];
                if (rowWishedFromUser - 1 >= 0 && colWishedFromUser + 1 < board.length)
                    newArray[rowWishedFromUser - 1][colWishedFromUser + 1] = board[rowWishedFromUser - 1][colWishedFromUser + 1];
                if (rowWishedFromUser + 1 < board.length && colWishedFromUser + 1 < board.length)
                    newArray[rowWishedFromUser + 1][colWishedFromUser + 1] = board[rowWishedFromUser + 1][colWishedFromUser + 1];
                if (rowWishedFromUser - 1 >= 0)
                    newArray[rowWishedFromUser - 1][colWishedFromUser] = board[rowWishedFromUser - 1][colWishedFromUser];
                if (rowWishedFromUser + 1 < board.length)
                    newArray[rowWishedFromUser + 1][colWishedFromUser] = board[rowWishedFromUser + 1][colWishedFromUser];
                for (String[] strings : newArray) {
                    System.out.println();
                    for (int j = 0; j < newArray.length; j++) {
                        System.out.print(" " + strings[j]);
                    }
                }


            }


            if (colWishedFromUser == colFigure - 1 && colWishedFromUser >= 0) {

                newArray[rowWishedFromUser][colWishedFromUser] = board[rowWishedFromUser][colWishedFromUser];
                if (colWishedFromUser - 1 >= 0)
                    newArray[rowWishedFromUser][colWishedFromUser - 1] = board[rowWishedFromUser][colWishedFromUser - 1];
                if (rowWishedFromUser - 1 >= 0 && colWishedFromUser - 1 >= 0)
                    newArray[rowWishedFromUser - 1][colWishedFromUser - 1] = board[rowWishedFromUser - 1][colWishedFromUser - 1];
                if (rowWishedFromUser + 1 < board.length && colWishedFromUser - 1 >= 0)
                    newArray[rowWishedFromUser + 1][colWishedFromUser - 1] = board[rowWishedFromUser + 1][colWishedFromUser - 1];
                if (rowWishedFromUser - 1 >= 0)
                    newArray[rowWishedFromUser - 1][colWishedFromUser] = board[rowWishedFromUser - 1][colWishedFromUser];
                if (rowWishedFromUser + 1 < board.length)
                    newArray[rowWishedFromUser + 1][colWishedFromUser] = board[rowWishedFromUser + 1][colWishedFromUser];
                for (String[] strings : newArray) {
                    System.out.println();
                    for (int j = 0; j < newArray.length; j++) {
                        System.out.print(" " + strings[j]);
                    }
                }

            }


            if (rowWishedFromUser == rowFigure - 1 && rowWishedFromUser >= 0) {
                newArray[rowWishedFromUser][colWishedFromUser] = board[rowWishedFromUser][colWishedFromUser];
                if (rowWishedFromUser - 1 >= 0)
                    newArray[rowWishedFromUser - 1][colWishedFromUser] = board[rowWishedFromUser - 1][colWishedFromUser];
                if (rowWishedFromUser - 1 >= 0 && colWishedFromUser - 1 >= 0)
                    newArray[rowWishedFromUser - 1][colWishedFromUser - 1] = board[rowWishedFromUser - 1][colWishedFromUser - 1];
                if (rowWishedFromUser - 1 >= 0 && colWishedFromUser + 1 < board.length)
                    newArray[rowWishedFromUser - 1][colWishedFromUser + 1] = board[rowWishedFromUser - 1][colWishedFromUser + 1];
                if (colWishedFromUser - 1 >= 0)
                    newArray[rowWishedFromUser][colWishedFromUser - 1] = board[rowWishedFromUser][colWishedFromUser - 1];
                if (colWishedFromUser + 1 < board.length)
                    newArray[rowWishedFromUser][colWishedFromUser + 1] = board[rowWishedFromUser][colWishedFromUser + 1];
                for (String[] strings : newArray) {
                    System.out.println();
                    for (int j = 0; j < newArray.length; j++) {
                        System.out.print(" " + strings[j]);
                    }
                }

            }


            if (rowWishedFromUser == rowFigure + 1 && rowWishedFromUser < board.length) {
                newArray[rowWishedFromUser][colWishedFromUser] = board[rowWishedFromUser][colWishedFromUser];
                if (rowWishedFromUser + 1 < board.length)
                    newArray[rowWishedFromUser + 1][colWishedFromUser] = board[rowWishedFromUser + 1][colWishedFromUser];
                if (rowWishedFromUser + 1 < board.length && colWishedFromUser - 1 >= 0)
                    newArray[rowWishedFromUser + 1][colWishedFromUser - 1] = board[rowWishedFromUser + 1][colWishedFromUser - 1];
                if (rowWishedFromUser + 1 < board.length && colWishedFromUser + 1 < board.length)
                    newArray[rowWishedFromUser + 1][colWishedFromUser + 1] = board[rowWishedFromUser + 1][colWishedFromUser + 1];
                if (colWishedFromUser - 1 >= 0)
                    newArray[rowWishedFromUser][colWishedFromUser - 1] = board[rowWishedFromUser][colWishedFromUser - 1];
                if (colWishedFromUser + 1 < board.length)
                    newArray[rowWishedFromUser][colWishedFromUser + 1] = board[rowWishedFromUser][colWishedFromUser + 1];
                for (String[] strings : newArray) {
                    System.out.println();
                    for (int j = 0; j < newArray.length; j++) {
                        System.out.print(" " + strings[j]);
                    }
                }

            }
            int restNumberOfProbes = number_of_probes - 1;
            System.out.println(" ");
            System.out.println("Останали брой проби: " + restNumberOfProbes);
        } else {
            System.out.println("Нямате повече проби.");
        }
    }

    /**
     * Отпечатва легендата в играта.
     */

    public static void legend() {
        System.out.println("Легенда\n" +
                "● S - вход в бойното поле\n" +
                "● F - изход от минното поле\n" +
                "● X - непосетени скрити клетки, съдържащи неизвестното\n" +
                "● V - посетено квадратче\n" +
                "● * - текуща позиция на бойната станция");
    }

    /**
     * Отпечатвата дъската със съответните индекси за редици/колони
     *
     * @param newArray масивът, който ще представлява дъската
     */
    public static void printBoard(String[][] newArray) {
        System.out.println(" ");
        System.out.print(" ");
        for (int row = 0; row < newArray.length; row++) {
            System.out.print("  " + row);
        }
        System.out.println();
        for (int row = 0; row < newArray.length; row++) {
            for (int col = 0; col < newArray[row].length; col++) {
                if (col < 1) {
                    System.out.print(row);
                    System.out.print("  " + newArray[row][col]);
                } else {

                    System.out.print("  " + newArray[row][col]);
                }
            }
            System.out.println();
        }
    }

    /**
     * Входа (S) и изхода (F) на минираното поле, винаги се намира върху една от четирите страни на
     * матрицата. Входа и изхода никога не могат да бъдат върху една и съща страна. Координатите на
     * входа и изхода се определят на случаен принцип.
     * Мините се разпределят на случаен принцип върху матрицата, като тяхното количество се взима от текстови
     * файл enemy_teritory.txt, който съдържа размерността на матрицата, както и броя на мините.
     *
     * @param maxNumberOfStart  възможен брой "Старт"(1)
     * @param maxNumberOfFinish възможен брой "Финал"(1)
     * @param board             масивът, в който ще пазим всички позиции на всички мини, старта и финала
     * @param newArray          масивът, в който ще отпечатваме (винаги) старта и финала
     * @param placedMines       поставени мини
     */
    public static void manageBoardPieces(int maxNumberOfStart, int maxNumberOfFinish, String[][] board,
                                         String[][] newArray, int placedMines) {
        for (int i = 0; i < maxNumberOfStart; i++) {
            possibleIndexRowStart = random.nextInt(board.length);
            possibleIndexColStart = random.nextInt(board[0].length);
            if (board[possibleIndexRowStart][possibleIndexColStart] == null) {
                if (possibleIndexRowStart == 0 || possibleIndexRowStart == board.length - 1 || possibleIndexColStart == 0 || possibleIndexColStart == board.length - 1) {
                    board[possibleIndexRowStart][possibleIndexColStart] = start;
                    newArray[possibleIndexRowStart][possibleIndexColStart] = start;
                    rowFigure = possibleIndexRowStart;
                    colFigure = possibleIndexColStart;
                    i++;
                } else {
                    i--;
                }
            }
        }
        for (int i = 0; i < maxNumberOfFinish; i++) {
            possibleIndexRowFinal = random.nextInt(board.length);
            possibleIndexColFinal = random.nextInt(board[0].length);
            if (board[possibleIndexRowFinal][possibleIndexColFinal] == null) {
                if ((possibleIndexRowFinal == 0 || possibleIndexRowFinal == board.length - 1 || possibleIndexColFinal == 0 || possibleIndexColFinal == board.length - 1) && (possibleIndexRowFinal != possibleIndexRowStart) && (possibleIndexColFinal != possibleIndexColStart)) {
                    board[possibleIndexRowFinal][possibleIndexColFinal] = finish;
                    newArray[possibleIndexRowFinal][possibleIndexColFinal] = finish;
                    i++;
                } else {
                    i--;
                }
            }
        }


        for (int i = 0; i < newArray.length; i++) {
            for (int j = 0; j < newArray[i].length; j++) {
                if ((newArray[i][j] != start) && (newArray[i][j] != finish)) {
                    newArray[i][j] = piece;
                }
            }
        }

        printBoard(newArray);
        newArray[rowFigure][colFigure] = figure;
        System.out.println("Започвате от старта. ");
        printBoard(newArray);
        System.out.println(" ");

        while (placedMines < mines) {
            possibleIndexRowMine = random.nextInt(board.length);
            possibleIndexColMine = random.nextInt(board[0].length);
            if (board[possibleIndexRowMine][possibleIndexColMine] == null) {
                board[possibleIndexRowMine][possibleIndexColMine] = mine;

                placedMines++;
            }
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == null) {
                    board[i][j] = empty;
                }
            }
        }
    }

    /**
     * Метод, в който се взимат желаните от потребителя координати, валидират се дали са в правилата на играта.
     * Потребителя избира от предоставените опции докато не попадне на мина или не спечели(стигне финала).
     *
     * @param rowWishedFromUser  координат редица, посочен от потребителя
     * @param colWishedFromUser  координат колона, посочен от потребителя
     * @param board              масивът, който пази всички индекси на всички заложени мини
     * @param newArray           масивът, в който ще отпчатваме само нужните според опцията и позицията
     * @param number_of_probes   брой възможни проби на мини; взима се от текстов файл configurations.txt още в main метода
     * @param number_of_disposal брой възможни обезвреждания на мини; взима се от текстов файл configurations.txt още в main метода
     */
    public static void action(int rowWishedFromUser, int colWishedFromUser, String[][] board, String[][] newArray, int number_of_probes, int number_of_disposal) {
        while (true) {
            int counterInvalidEntriesCo = 1;
            for (int i = 0; i < counterInvalidEntriesCo; i++) {
                System.out.println("Моля, въведете желаните от Вас координати за редица: ");
                rowWishedFromUser = input.nextInt();
                if (((rowWishedFromUser == rowFigure + 1) || (rowWishedFromUser == rowFigure - 1) || (rowWishedFromUser == rowFigure)) && (rowWishedFromUser >= 0 && rowWishedFromUser < board.length)) {
                    System.out.println("RowWishedFromUser: " + rowWishedFromUser);
                } else {
                    System.out.println("Невалидни координати за редица. Опитайте пак.");
                    i = -1;
                    continue;
                }

                System.out.println("Моля, въведете желаните от Вас координати за колона: ");
                colWishedFromUser = input.nextInt();
                if ((((rowWishedFromUser == rowFigure) && (colWishedFromUser != colFigure)) ||
                        ((rowWishedFromUser == rowFigure - 1) && (colWishedFromUser == colFigure)) || ((rowWishedFromUser == rowFigure + 1) && (colWishedFromUser == colFigure))) && (colWishedFromUser >= 0 && colWishedFromUser < board.length)) {
                    System.out.println("ColWishedFromUser: " + colWishedFromUser);
                } else {
                    System.out.println("Невалидни координати за колона. Опитайте пак.");
                    i = -1;
                    continue;
                }
            }

            printMenu();
            int counterInvalidEntries = 1;
            for (int i = 0; i < counterInvalidEntries; i++) {
                System.out.println(" ");
                System.out.println("Моля запишете номера на желаната опция: ");
                int optionNumberWishedFromUser = input.nextInt();
                switch (optionNumberWishedFromUser) {
                    case 1:
                        probeForMine(board, newArray, rowWishedFromUser, colWishedFromUser, rowFigure, colFigure, number_of_probes);
                        number_of_probes--;
                        counterInvalidEntries++;
                        break;
                    case 2:
                        disposal(number_of_disposal, board, newArray, rowWishedFromUser, colWishedFromUser);
                        number_of_disposal--;
                        break;
                    case 3:
                        go(board, newArray, rowWishedFromUser, colWishedFromUser);
                        break;
                    default:
                        System.out.println("Не същестува такъв номер опция. ");
                        i--;
                }
            }
        }
    }

    /**
     * Метод, който обезврежда бомбата на посочените координати.
     *
     * @param number_of_disposal брой обезвеждания; взима се информацията от файл configurations.txt
     * @param board              дъската, която пази позициите на всички мини и изпразва квадратчето на мината след обезвреждането й
     * @param newArray           дъската, която дублира нужните й квадратчета за визуализиране и пази посетените
     * @param rowWishedFromUser  координат редица, желан от потребителя
     * @param colWishedFromUser  координат колона, желан от потребителя
     */
    public static void disposal(int number_of_disposal, String[][] board, String[][] newArray, int rowWishedFromUser, int colWishedFromUser) {
        if (number_of_disposal > 0) {
            if (board[rowWishedFromUser][colWishedFromUser].equals(mine)) {
                System.out.println("Обезвреждане..:");
                board[rowWishedFromUser][colWishedFromUser] = empty;
                newArray[rowWishedFromUser][colWishedFromUser] = empty;
                newArray[rowFigure][colFigure] = visited;
                newArray[rowWishedFromUser][colWishedFromUser] = figure;
                rowFigure = rowWishedFromUser;
                colFigure = colWishedFromUser;
                printBoard(newArray);
                int restNumberOfDisposal = number_of_disposal - 1;
                System.out.println(" ");
                System.out.println("Останали брой проби: " + restNumberOfDisposal);
            }
        } else {
            System.out.println("Нямате повече обезвреждания. ");
        }

    }

    /**
     * Директно преминаване от текуща позиция към избраните координати. Ако попаднем на мина - край на играта/
     *
     * @param board             дъската, която пази позициите на всички мини и посетени квадратчета и текущото (след преместването) място
     * @param newArray          дъската, която пази посетените квадратчета и текущото (след преместването) място
     * @param rowWishedFromUser координат редица, желан от потребителя
     * @param colWishedFromUser координат колона, желан от потребителя
     */
    public static void go(String[][] board, String[][] newArray, int rowWishedFromUser, int colWishedFromUser) {
        board[rowFigure][colFigure] = visited;
        newArray[rowFigure][colFigure] = visited;


        if (board[rowWishedFromUser][colWishedFromUser].equals(mine)) {
            System.out.println("Попаднахте на мина. Край на играта.");
            System.exit(0);
        } else {
            if (board[rowWishedFromUser][colWishedFromUser].equals(finish)) {
                System.out.println("Поздравления, достигнахте финала! Чудесна игра!");
                System.exit(0);
            } else {
                board[rowWishedFromUser][colWishedFromUser] = figure;
                newArray[rowWishedFromUser][colWishedFromUser] = figure;
                rowFigure = rowWishedFromUser;
                colFigure = colWishedFromUser;
                printBoard(newArray);

            }
        }
    }

}


