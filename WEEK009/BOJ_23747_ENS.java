import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Main {

    static Map<String, int[]> moveInfo = new HashMap<>();

    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {

        String[] worldInfo = br.readLine().split(" ");

        int row = Integer.parseInt(worldInfo[0]);
        int col = Integer.parseInt(worldInfo[1]);

        World[][] tripLog = new World[row][col];

        Hanbyul hanbyul = initMapSetting(tripLog);

        String[] walkInfo = br.readLine().split("");

        for (String action : walkInfo) {
            if (!"W".equals(action)) {
                hanbyul.move(moveInfo.get(action));
            } else {
                spreadWard(hanbyul, tripLog);
            }
        }

        nowHanbyulSettingAndPrint(tripLog, hanbyul);

    }

    private static void nowHanbyulSettingAndPrint(World[][] tripLog, Hanbyul hanbyul) {

        int row = hanbyul.row;
        int col = hanbyul.col;
        tripLog[row][col].isSeek = true;

        for (int[] value : moveInfo.values()) {
            int moveRow = row + value[0];
            int moveCol = col + value[1];
            if(isLimit(tripLog,moveRow,moveCol)){
                tripLog[moveRow][moveCol].isSeek = true;
            }
        }

        StringBuilder sb = new StringBuilder();

        for (World[] worlds : tripLog) {
            for (int j = 0; j < tripLog[0].length; j++) {
                if (worlds[j].isSeek) {
                    sb.append(".");
                } else {
                    sb.append("#");
                }
            }
            sb.append("\n");
        }

        System.out.print(sb);
    }

    private static void spreadWard(Hanbyul hanbyul, World[][] tripLog) {
        int initRow = hanbyul.getRow();
        int initCol = hanbyul.getCol();

        Queue<WardPosition> wardPositions = new LinkedList<>();
        wardPositions.add(new WardPosition(initRow, initCol));

        while (!wardPositions.isEmpty()) {

            WardPosition area = wardPositions.poll();
            World currentWorld = tripLog[area.row][area.col];
            currentWorld.isSeek = true;

            for (int[] value : moveInfo.values()) {
                int moveRow = area.row + value[0];
                int moveCol = area.col + value[1];

                if (isLimit(tripLog, moveRow, moveCol)) {
                    World nextWorld = tripLog[moveRow][moveCol];
                    if (currentWorld.c == nextWorld.c && !nextWorld.isSeek) {
                        nextWorld.isSeek = true;
                        wardPositions.add(new WardPosition(moveRow, moveCol));
                    }
                }
            }
        }


    }

    private static boolean isLimit(World[][] tripLog, int moveRow, int moveCol) {
        return moveRow >= 0 && moveRow < tripLog.length && moveCol >= 0 && moveCol < tripLog[0].length;
    }

    private static Hanbyul initMapSetting(World[][] tripLog) throws IOException {
        moveInfo.put("U", new int[]{-1, 0});
        moveInfo.put("D", new int[]{1, 0});
        moveInfo.put("L", new int[]{0, -1});
        moveInfo.put("R", new int[]{0, 1});

        for (int row = 0; row < tripLog.length; row++) {
            char[] charArray = br.readLine().toCharArray();
            for (int col = 0; col < tripLog[0].length; col++) {
                tripLog[row][col] = new World(charArray[col]);
            }
        }
        String[] hanbyulInfo = br.readLine().split(" ");

        return new Hanbyul(Integer.parseInt(hanbyulInfo[0])-1, Integer.parseInt(hanbyulInfo[1])-1);

    }
}

class World {
    char c;
    boolean isSeek;

    public World(char c) {
        this.c = c;
        this.isSeek = false;
    }
}

class Hanbyul {
    int row;
    int col;

    public Hanbyul(int row, int col) {
        this.row = row;
        this.col = col;
    }

    void move(int[] action) {
        row += action[0];
        col += action[1];
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}

class WardPosition {
    int row;
    int col;

    public WardPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
