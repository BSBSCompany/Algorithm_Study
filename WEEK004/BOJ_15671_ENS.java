import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Othello {

    static int[] dxs = {-1, -1, 0, 1, 1, 1, 0, -1};
    static int[] dys = {0, 1, 1, 1, 0, -1, -1, -1};

    static char[][] othelloMap = new char[6][6];

    static char WHITE_STONE = 'W';
    static char BLACK_STONE = 'B';
    static char BLANK = '.';


    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(br.readLine());

        initGame();

        for (int i = 0; i < N; i++) {

            char currentStone;

            if (i % 2 == 0) {
                currentStone = BLACK_STONE;
            } else {
                currentStone = WHITE_STONE;
            }

            String[] split = br.readLine().split(" ");
            int row = Integer.parseInt(split[0]);
            int col = Integer.parseInt(split[1]);

            othelloMap[row - 1][col - 1] = currentStone;

            List<Position> positions = new ArrayList<>();

            for (int j = 0; j < dxs.length; j++) {

                int x = dxs[j];
                int y = dys[j];

                int rowX = row - 1;
                int colY = col - 1;

                while (rowX >= 0 && rowX < 6 && colY >= 0 && colY < 6) {

                    rowX = rowX + x;
                    colY = colY + y;

                    if (rowX >= 0 && rowX < 6 && colY >= 0 && colY < 6) {

                        char targetStone = othelloMap[rowX][colY];

                        if (targetStone == currentStone) {
                            for (Position position : positions) {
                                othelloMap[position.getRow()][position.getCol()] = currentStone;
                            }
                            positions.clear();
                            break;
                        } else if (targetStone == BLANK) {
                            positions.clear();
                            break;
                        } else {
                            positions.add(new Position(rowX, colY));
                        }
                    }
                }
                positions.clear();
            }
        }

        int count = 0;

        for (int i = 0; i < othelloMap.length; i++) {
            for (int j = 0; j < othelloMap.length; j++) {
                if (othelloMap[i][j] == WHITE_STONE) {
                    count--;
                } else if (othelloMap[i][j] == BLACK_STONE) {
                    count++;
                }
                System.out.print(othelloMap[i][j]);
            }
            System.out.println();
        }

        if (count > 0) {
            System.out.print("Black");
        } else if (count < 0) {
            System.out.print("White");
        }
    }


    private static void initGame() {

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                othelloMap[i][j] = BLANK;
            }
        }

        othelloMap[2][2] = WHITE_STONE;
        othelloMap[3][3] = WHITE_STONE;
        othelloMap[3][2] = BLACK_STONE;
        othelloMap[2][3] = BLACK_STONE;
    }
}

class Position {
    int row;
    int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
