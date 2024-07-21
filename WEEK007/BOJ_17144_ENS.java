import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FineDustBye {

    public static int[] dr = {-1, 0, 1, 0};
    public static int[] dc = {0, 1, 0, -1};

    public static int[] drr = {1, 0, -1, 0};
    public static int[] drc = {0, 1, 0, -1};

    static final int CIRCUIT = 4;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String[] command = br.readLine().split(" ");
        int mapRow = Integer.parseInt(command[0]);
        int mapCol = Integer.parseInt(command[1]);
        int time = Integer.parseInt(command[2]);

        int[][] map = new int[mapRow][mapCol];


        for (int i = 0; i < mapRow; i++) {
            String[] numberCount = br.readLine().split(" ");
            for (int j = 0; j < numberCount.length; j++) {
                int count = Integer.parseInt(numberCount[j]);
                if (count != 0) {
                    map[i][j] = count;
                }
            }
        }


        for (int i = 0; i < time; i++) {
            diffuse(map);
            airCleaner(map);
        }

        print(map);
    }

    private static void print(int[][] map) {
        int answer = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if(map[i][j] != -1){
                    answer += map[i][j];
                }
            }
        }
        System.out.print(answer);
    }


    private static void diffuse(int[][] map) {
        List<FineDust> fineDusts = new ArrayList<>();

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    fineDusts.add(new FineDust(i, j, map[i][j]));
                }
            }
        }

        initializeArray(map);

        for (FineDust fineDust : fineDusts) {

            int currentValue = fineDust.value;
            int spreadValue = (int) Math.floor((double) currentValue / 5);
            int newCurrentValue;
            int enableSpread = 0;


            if (fineDust.value != -1) {
                for (int i = 0; i < CIRCUIT; i++) {

                    int newDr = fineDust.r + dr[i];
                    int newDc = fineDust.c + dc[i];

                    if (newDr >= 0 && newDr < map.length && newDc >= 0 && newDc < map[0].length && map[newDr][newDc] != -1) {
                        enableSpread++;
                    }
                }

                newCurrentValue = currentValue - Math.abs(spreadValue * enableSpread);


                for (int i = 0; i < CIRCUIT; i++) {

                    int newDr = fineDust.r + dr[i];
                    int newDc = fineDust.c + dc[i];

                    if (newDr >= 0 && newDr < map.length && newDc >= 0 && newDc < map[0].length && map[newDr][newDc] != -1) {
                        map[newDr][newDc] += spreadValue;
                    }
                }
                map[fineDust.r][fineDust.c] += newCurrentValue;
            }

        }
    }

    private static void airCleaner(int[][] map) {
        int upCleaner = 0;
        int downCleaner = 0;
        for (int i = 0; i < map.length; i++) {
            if (map[i][0] == -1) {
                if (upCleaner == 0) {
                    upCleaner = i;
                    downCleaner = i;
                } else {
                    if (upCleaner > i) {
                        upCleaner = i;
                    } else {
                        downCleaner = i;
                    }
                }
            }
        }

        circuitCleaner(upCleaner, downCleaner, map);

    }

    private static void circuitCleaner(int upR, int downR, int[][] map) {

        int newUpCol = 0;
        int newUpRow = 0;
        int currentUpRow = upR - 1;
        int currentUpCol = 0;
        for (int i = 0; i < CIRCUIT; i++) {
            while (true) {
                    newUpRow = currentUpRow + dr[i];
                    newUpCol = currentUpCol + dc[i];

                    if (!isaBoolean(upR, map, newUpRow, newUpCol)) {
                        break;
                    }
                    if (map[newUpRow][newUpCol] == -1) {
                        map[currentUpRow][currentUpCol] = 0;
                        break;
                    } else {
                        map[currentUpRow][currentUpCol] = map[newUpRow][newUpCol];
                    }
                    currentUpRow = newUpRow;
                    currentUpCol = newUpCol;
            }
            if (newUpRow < 0) {
                currentUpRow = 0;
            } else if (newUpRow >= map.length) {
                currentUpRow = map.length - 1;
            }

            if (newUpCol < 0) {
                currentUpCol = 0;
            } else if (newUpCol >= map[0].length) {
                currentUpCol = map[0].length - 1;
            }

        }

        int newDownCol = 0;
        int newDownRow = 0;
        int currentDownRow = downR+1;
        int currentDownCol = 0;
        for (int i = 0; i < CIRCUIT; i++) {
            while (true) {
                    newDownRow = currentDownRow + drr[i];
                    newDownCol = currentDownCol + drc[i];
                    if (!isaBoolean2(downR, map, newDownRow, newDownCol)) {
                        break;
                    }
                    if (map[newDownRow][newDownCol] == -1) {
                        map[currentDownRow][currentDownCol] = 0;
                        break;
                    } else {
                        map[currentDownRow][currentDownCol] = map[newDownRow][newDownCol];
                    }
                    currentDownRow = newDownRow;
                    currentDownCol = newDownCol;
            }
            if (newDownRow < 0) {
                currentDownRow = 0;
            } else if (newDownRow >= map.length) {
                currentDownRow = map.length - 1;
            }

            if (newDownCol < 0) {
                currentDownCol = 0;
            } else if (newDownCol >= map[0].length) {
                currentDownCol = map[0].length - 1;
            }

        }


    }

    private static boolean isaBoolean(int upR, int[][] map, int newUpRow, int newUpCol) {
        return newUpRow >= 0 && newUpRow <= upR && newUpCol >= 0 && newUpCol < map[0].length;
    }

    private static boolean isaBoolean2(int upR, int[][] map, int newUpRow, int newUpCol) {
        return newUpRow >= upR && newUpRow<map.length && newUpCol >= 0 && newUpCol < map[0].length;
    }


    public static void initializeArray(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                if (array[i][j] != -1) {
                    array[i][j] = 0;
                }
            }
        }
    }

    private static boolean isLimitMap(int[][] map, int newUpRow, int newUpCol) {
        return newUpRow >= 0 && newUpRow < map.length && newUpCol >= 0 && newUpCol < map[0].length;
    }
}

class FineDust {
    int r;
    int c;
    int value;

    public FineDust(int r, int c, int value) {
        this.r = r;
        this.c = c;
        this.value = value;
    }
}
