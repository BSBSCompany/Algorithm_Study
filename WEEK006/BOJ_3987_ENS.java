import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {


    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] area = br.readLine().split(" ");
        int row = Integer.parseInt(area[0]);
        int col = Integer.parseInt(area[1]);
        String[][] galaxy = new String[row][col];

        for (int i = 0; i < row; i++) {
            String[] type = br.readLine().split("");
            for (int j = 0; j < col; j++) {
                galaxy[i][j] = type[j];
            }
        }

        String[] signalArea = br.readLine().split(" ");
        int signalRow = Integer.parseInt(signalArea[0]);
        int signalCol = Integer.parseInt(signalArea[1]);

        Signal signal = new Signal(signalRow, signalCol);
        signal.startSignal(galaxy);
        signal.print();

    }
}

class Signal {

    static int U = 0;
    static int D = 2;
    static int R = 1;
    static int L = 3;


    static int[] dR = {-1, 0, 1, 0};
    static int[] dC = {0, 1, 0, -1};

    static int[] move = {0, 1, 2, 3};


    int currentPosition; // 현재 진행방향
    int startPosition;// 최초 진행방향
    int row;
    int col;

    int maxMove = 0;
    int maxPosition = 0;

    public Signal(int row, int col) {
        this.row = row;
        this.col = col;
    }

    void startSignal(String[][] galaxy) {

        for (int position : move) {
            int newRow = row - 1;
            int newCol = col - 1;
            startPosition = position;
            currentPosition = position;
            int signalMoveCount = 0;
            while (true) {

                newRow += dR[currentPosition];
                newCol += dC[currentPosition];
                signalMoveCount++;

                if (newRow >= galaxy.length || newRow < 0 || newCol >= galaxy[0].length || newCol < 0) {
                    if (maxMove < signalMoveCount) {
                        maxMove = signalMoveCount;
                        maxPosition = position;

                    } else if (maxMove == signalMoveCount) {

                        if (startPosition < maxPosition) {
                            maxMove = signalMoveCount;
                            maxPosition = startPosition;
                        }
                    }
                    break;
                }

                if ("/".equals(galaxy[newRow][newCol])) {
                    if (currentPosition == U) {
                        currentPosition = R;
                        continue;
                    }

                    if (currentPosition == R) {
                        currentPosition = U;
                        continue;
                    }

                    if (currentPosition == L) {
                        currentPosition = D;
                        continue;
                    }

                    if (currentPosition == D) {
                        currentPosition = L;
                        continue;
                    }
                }

                if ("\\".equals(galaxy[newRow][newCol])) {

                    if (currentPosition == U) {
                        currentPosition = L;
                        continue;
                    }

                    if (currentPosition == R) {
                        currentPosition = D;
                        continue;
                    }

                    if (currentPosition == D) {
                        currentPosition = R;
                        continue;
                    }

                    if (currentPosition == L) {
                        currentPosition = U;
                        continue;
                    }
                }

                if ("C".equals(galaxy[newRow][newCol])) {
                    if (maxMove < signalMoveCount) {
                        maxMove = signalMoveCount;
                        maxPosition = position;

                    } else if (maxMove == signalMoveCount) {
                        if (startPosition < maxPosition) {
                            maxMove = signalMoveCount;
                            maxPosition = startPosition;
                        }
                    }
                    break;
                }


                if (newRow == row-1 && newCol == col-1 && startPosition == currentPosition) {
                    if (maxMove < signalMoveCount) {
                        maxMove = 250001;
                        maxPosition = position;

                    } else if (maxMove == signalMoveCount) {
                        if (startPosition < maxPosition) {
                            maxMove = 250001;
                            maxPosition = startPosition;
                        }
                    }
                    break;
                }


            }

        }

    }

    void print() {
        if (maxPosition == U) {
            System.out.println("U");
        } else if (maxPosition == R) {
            System.out.println("R");
        } else if (maxPosition == D) {
            System.out.println("D");
        } else if (maxPosition == L) {
            System.out.println("L");
        }
        if (maxMove == 250001) {
            System.out.print("Voyager");
        } else {
            System.out.print(maxMove);
        }


    }
}
