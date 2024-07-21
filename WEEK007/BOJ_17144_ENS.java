import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

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
                if (map[i][j] != -1) {
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

        initializeArray(map); // List에 담아주고 Map을 초기화 하는 작업 : 값을 새로 담기 위한 작업

        for (FineDust fineDust : fineDusts) {

            int currentValue = fineDust.value;
            int spreadValue = (int) Math.floor((double) currentValue / 5);
            int newCurrentValue;
            int enableSpread = 0;


            if (fineDust.value != -1) { // 뿌리기 전에 몇칸을 사용하는지 나타내는 메서드
                for (int i = 0; i < CIRCUIT; i++) {
                    int newDr = fineDust.r + dr[i];
                    int newDc = fineDust.c + dc[i];
                    if (newDr >= 0 && newDr < map.length && newDc >= 0 && newDc < map[0].length && map[newDr][newDc] != -1) { //주변을 탐색할수 있고, 공기청정기를 만나지 않는 조건
                        enableSpread++;
                    }
                }

                newCurrentValue = currentValue - Math.abs(spreadValue * enableSpread);


                for (int i = 0; i < CIRCUIT; i++) { //미세먼지에 대한 정보를 밀어넣는 방법

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
        for (int i = 0; i < map.length; i++) { //상하관계를 찾기 위한 로직
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
        int currentUpRow = upR - 1; // 공기청정기 위에서 시작
        int currentUpCol = 0;
        for (int i = 0; i < CIRCUIT; i++) {

            while (true) {
                newUpRow = currentUpRow + dr[i];
                newUpCol = currentUpCol + dc[i];

                if (!isUpCheck(upR, map, newUpRow, newUpCol)) { //가져오려는 값에 대해 OutOfRange 나오는 경우
                    break;
                }

                if (map[newUpRow][newUpCol] == -1) { // 가져올때 -1 즉 에어컨이 있을경우 공기를 정화하고 끝내는 메서드
                    map[currentUpRow][currentUpCol] = 0;
                    break;
                } else {
                    map[currentUpRow][currentUpCol] = map[newUpRow][newUpCol];
                }
                currentUpRow = newUpRow;
                currentUpCol = newUpCol;
            }

            //값이 넘어갔을경우 최대값 및 최솟값으로 변경해주는 로직
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
        int currentDownRow = downR + 1; // 공기청정기 아래에서 시작
        int currentDownCol = 0;
        for (int i = 0; i < CIRCUIT; i++) {

            while (true) {
                newDownRow = currentDownRow + drr[i];
                newDownCol = currentDownCol + drc[i];

                if (!isDownCheck(downR, map, newDownRow, newDownCol)) { //가져오려는 값에 대해 OutOfRange 나오는 경우
                    break;
                }

                if (map[newDownRow][newDownCol] == -1) { // 가져올때 -1 즉 에어컨이 있을경우 공기를 정화하고 끝내는 메서드
                    map[currentDownRow][currentDownCol] = 0;
                    break;
                } else {
                    map[currentDownRow][currentDownCol] = map[newDownRow][newDownCol];
                }
                currentDownRow = newDownRow;
                currentDownCol = newDownCol;
            }

            //값이 넘어갔을경우 최대값 및 최솟값으로 변경해주는 로직
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

    public static void initializeArray(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                if (array[i][j] != -1) {
                    array[i][j] = 0;
                }
            }
        }
    }

    private static boolean isUpCheck(int upR, int[][] map, int newUpRow, int newUpCol) {
        return newUpRow >= 0 && newUpRow <= upR && newUpCol >= 0 && newUpCol < map[0].length;
    }

    private static boolean isDownCheck(int downR, int[][] map, int newDownRow, int newDownCol) {
        return newDownRow >= downR && newDownRow < map.length && newDownCol >= 0 && newDownCol < map[0].length;
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
