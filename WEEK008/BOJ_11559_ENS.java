import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PuyoPuyo {

    static int[] dr = new int[]{-1, 0, 1, 0};
    static int[] dc = new int[]{0, 1, 0, -1};

    private static final char RED = 'R';
    private static final char GREEN = 'G';
    private static final char BLUE = 'B';
    private static final char PURPLE = 'P';
    private static final char YELLOW = 'Y';
    private static final char EMPTY = '.';

    private static final int MAP_ROW = 12;
    private static final int MAP_COL = 6;
    private static final int SEARCH = 4;
    static boolean isDestroyed = false;
    static Puyo[][] map = new Puyo[MAP_ROW][MAP_COL];

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        initMap(br, map);
        Queue<Puyo> puyoQueue = new LinkedList<>();

        int answer = 0;

        searchPuyo(map, puyoQueue);
        gravity(map);

        while (isDestroyed) {
            initVisit(map);
            isDestroyed = false;
            answer++;
            searchPuyo(map, puyoQueue);
            gravity(map);
        }

        System.out.println(answer);
    }

    private static void initVisit(Puyo[][] map) {
        for (int r = 0; r < MAP_ROW; r++) {
            for (int c = 0; c < MAP_COL; c++) {
                map[r][c].isVisited = false;
            }
        }
    }

    private static void searchPuyo(Puyo[][] map, Queue<Puyo> puyoQueue) {
        List<PuyoPosition> positions = new ArrayList<>();
        for (int r = 0; r < MAP_ROW; r++) {
            for (int c = 0; c < MAP_COL; c++) {
                if (map[r][c].color != '.') {
                    char currentColor = map[r][c].color;
                    int puyoRow = map[r][c].r;
                    int puyoCol = map[r][c].c;
                    positions.add(new PuyoPosition(puyoRow, puyoCol));
                    map[puyoRow][puyoCol].isVisited = true;
                    for (int i = 0; i < SEARCH; i++) {
                        int newPuyoRow = puyoRow + dr[i];
                        int newPuyoCol = puyoCol + dc[i];
                        if (isSameColor(newPuyoRow, newPuyoCol, currentColor, map)) {
                            map[newPuyoRow][newPuyoCol].isVisited = true;
                            puyoQueue.offer(new Puyo(currentColor, newPuyoRow, newPuyoCol));
                            positions.add(new PuyoPosition(newPuyoRow, newPuyoCol));
                        }
                    }

                    while (!puyoQueue.isEmpty()) {
                        Puyo puyo = puyoQueue.poll();
                        for (int i = 0; i < SEARCH; i++) {
                            int newPuyoRow = puyo.r + dr[i];
                            int newPuyoCol = puyo.c + dc[i];
                            if (isSameColor(newPuyoRow, newPuyoCol, currentColor, map)) {
                                map[newPuyoRow][newPuyoCol].isVisited = true;
                                puyoQueue.offer(new Puyo(currentColor, newPuyoRow, newPuyoCol));
                                positions.add(new PuyoPosition(newPuyoRow, newPuyoCol));
                            }
                        }

                    }

                    if (positions.size() >= 4) {
                        isDestroyed = true;
                        for (PuyoPosition position : positions) {
                            map[position.row][position.col].color = '.';
                            map[position.row][position.col].isVisited = false;
                        }
                    }
                    positions.clear();
                }
            }
        }
    }

    private static void gravity(Puyo[][] map) {
        for (int row = MAP_ROW - 1; row >= 0; row--) {
            for (int col = 0; col < MAP_COL; col++) {
                Puyo puyo = map[row][col];

                if (puyo.color == '.') {
                    int currentRow = row;
                    int currentCol = col;
                    while (true) {
                        currentRow = currentRow + dr[0];
                        currentCol = currentCol + dc[0];

                        if (currentRow < 0) {
                            break;
                        }

                        if (puyo.color != map[currentRow][currentCol].color) {
                            map[row][col].color = map[currentRow][currentCol].color;
                            map[currentRow][currentCol].color = '.';
                            break;
                        }

                    }
                }

            }
        }
    }

    private static boolean isSameColor(int newPuyoRow, int newPuyoCol, char currentColor, Puyo[][] map) {
        if (newPuyoRow < 0 || newPuyoRow > map.length-1 || newPuyoCol < 0 || newPuyoCol > map[0].length-1) {
            return false;
        }
        return !map[newPuyoRow][newPuyoCol].isVisited && map[newPuyoRow][newPuyoCol].color == currentColor;
    }

    private static void initMap(BufferedReader br, Puyo[][] map) throws IOException {
        for (int r = 0; r < MAP_ROW; r++) {
            String[] positionInfo = br.readLine().split("");
            for (int c = 0; c < MAP_COL; c++) {
                map[r][c] = new Puyo(positionInfo[c].charAt(0), r, c);
            }
        }
    }
}

class Puyo {
    char color;
    int r;
    int c;
    boolean isVisited;

    public Puyo(char color, int r, int c) {
        this.color = color;
        this.r = r;
        this.c = c;
        this.isVisited = false;
    }

}

class PuyoPosition {
    int row;
    int col;

    public PuyoPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
