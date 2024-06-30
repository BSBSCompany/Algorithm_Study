import java.io.IOException;
import java.util.Scanner;

public class Bingo_V2 {

    static BingoInfo[][] bingoMap = new BingoInfo[5][5];

    static int count = 0;
    static int answer = 0;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        bingoInit();
        bingoSearch();
    }

    private static void bingoInit() throws IOException {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int number = sc.nextInt();
                bingoMap[i][j] = new BingoInfo(number);
            }
        }
    }

    private static void bingoSearch() {
        int seq = 1;
        for (int i = 0; i < 25; i++) {

            int targetNumber = sc.nextInt();

            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    if (bingoMap[j][k].number == targetNumber) {
                        bingoMap[j][k].isChecked = true;
                    }
                }
            }

            if (i > 4) {
                boolean straight = straight(seq);
                boolean down = down(straight, seq);
                boolean leftDiagonal = leftDiagonal(down, seq);
                boolean rightDiagonal = rightDiagonal(leftDiagonal, seq);

                if (straight || down || leftDiagonal || rightDiagonal) {
                    break;
                }
            }
            count = 0;
            seq++;
        }

        System.out.print(answer);
    }

    private static boolean leftDiagonal(boolean down, int targetNumber) {
        if (down) {
            return true;
        }

        boolean isLine = true;


        for (int i = 0; i < 5; i++) {

            if (!bingoMap[i][i].isChecked) {
                isLine = false;
                break;
            }


        }

        if (isLine) {
            count++;
        }

        if (count == 3) {
            answer = targetNumber;
            return true;
        }
        return false;
    }

    private static boolean rightDiagonal(boolean leftDiagonal, int targetNumber) {
        if (leftDiagonal) {
            return true;
        }

        boolean isLine = true;


        for (int i = 0; i < 5; i++) {

            if (!bingoMap[i][4 - i].isChecked) {
                isLine = false;
                break;
            }


        }

        if (isLine) {
            count++;
        }

        if (count == 3) {
            answer = targetNumber;
            return true;
        }

        return false;
    }

    private static boolean down(boolean straight, int targetNumber) {
        if (straight) {
            return true;
        }
        for (int i = 0; i < 5; i++) {
            boolean isLine = true;
            for (int j = 0; j < 5; j++) {
                if (!bingoMap[j][i].isChecked) {
                    isLine = false;
                    break;
                }
            }
            if (isLine) {
                count++;
            }

            if (count == 3) {
                answer = targetNumber;
                return true;
            }
        }
        return false;
    }

    private static boolean straight(int targetNumber) {
        for (int i = 0; i < 5; i++) {
            boolean isLine = true;
            for (int j = 0; j < 5; j++) {
                if (!bingoMap[i][j].isChecked) {
                    isLine = false;
                    break;
                }
            }
            if (isLine) {
                count++;
            }
            if (count == 3) {
                answer = targetNumber;
                return true;
            }
        }
        return false;
    }
}

class BingoInfo {
    int number;
    boolean isChecked;

    public BingoInfo(int number) {
        this.number = number;
    }
}
