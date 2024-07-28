import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PopulationMigration {

    static int[] dr = new int[]{-1, 0, 1, 0};
    static int[] dc = new int[]{0, 1, 0, -1};

    static List<OpenBorderLine> openBorderLines = new ArrayList<>();
    static int cycle = 4;
    static boolean isOpenBorder = false;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] command = br.readLine().split(" ");
        int count = Integer.parseInt(command[0]);
        int min = Integer.parseInt(command[1]);
        int max = Integer.parseInt(command[2]);
        int answer = 0;
        Nation[][] nations = new Nation[count][count];
        initMap(count, br, nations);

        findBorderNation(count, nations, min, max);

        while (isOpenBorder) {
            initVisit(nations);
            openBorderLines.clear();
            isOpenBorder = false;
            answer++;
            findBorderNation(count, nations, min, max);
        }

        System.out.print(answer);

    }

    private static void findBorderNation(int count, Nation[][] nations, int min, int max) {
        for (int r = 0; r < count; r++) {
            for (int c = 0; c < count; c++) {
                if (!nations[r][c].isVisited) {
                    nations[r][c].isVisited = true;
                    List<NationPosition> positions = new ArrayList<>();
                    NationPosition nationPosition = new NationPosition(r, c);
                    positions.add(nationPosition);
                    openBorderLines.add(new OpenBorderLine(nations[r][c].population, positions));

                    int borderSharingCount = 1;

                    Queue<Nation> nextNation = new LinkedList<>();

                    for (int k = 0; k < cycle; k++) {
                        int newRow = r + dr[k];
                        int newCol = c + dc[k];

                        if (isLimit(newRow, nations, newCol)) {

                            int calculatePeople = Math.abs(nations[r][c].population - nations[newRow][newCol].population);

                            if (isBetweenPeople(calculatePeople, min, max)) {
                                borderSharingCount++;
                                nations[newRow][newCol].isVisited = true;
                                OpenBorderLine openBorderLine = openBorderLines.get(openBorderLines.size()-1);
                                nextNation.offer(new Nation(nations[newRow][newCol].population, newRow, newCol));
                                openBorderLine.nationPositions.add(new NationPosition(newRow, newCol));
                                openBorderLine.sumPopulation(nations[newRow][newCol].population);
                            }

                        }
                    }

                    if (borderSharingCount >= 2) {
                        while (!nextNation.isEmpty()) {
                            Nation nation = nextNation.poll();

                            nations[nation.r][nation.c].isVisited = true;

                            for (int i = 0; i < cycle; i++) {

                                int newNationRow = nation.r + dr[i];
                                int newNationCol = nation.c + dc[i];

                                if (isLimit(newNationRow, nations, newNationCol)) {

                                    int calculatePeople = Math.abs(nations[nation.r][nation.c].population - nations[newNationRow][newNationCol].population);

                                    if (isBetweenPeople(calculatePeople, min, max)) {
                                        nations[newNationRow][newNationCol].isVisited = true;
                                        OpenBorderLine openBorderLine = openBorderLines.get(openBorderLines.size()-1);
                                        nextNation.offer(new Nation(nations[newNationRow][newNationCol].population, newNationRow, newNationCol));
                                        openBorderLine.nationPositions.add(new NationPosition(newNationRow, newNationCol));
                                        openBorderLine.sumPopulation(nations[newNationRow][newNationCol].population);
                                    }
                                }
                            }
                        }
                    } else {
                        openBorderLines.remove(openBorderLines.size()-1);
                    }

                }
            }
        }

        for (OpenBorderLine openBorderLine : openBorderLines) {
            isOpenBorder = true;
            int averagePeople = openBorderLine.calculatePopulation();

            List<NationPosition> nationPositions = openBorderLine.nationPositions;
            for (NationPosition position : nationPositions) {
                nations[position.r][position.c].population = averagePeople;
            }
        }

    }

    private static boolean isLimit(int newRow, Nation[][] nations, int newCol) {

        if (newRow < 0 || newRow > nations.length - 1 || newCol < 0 || newCol > nations[0].length - 1) {
            return false;
        }

        return !nations[newRow][newCol].isVisited;
    }

    private static boolean isBetweenPeople(int calculatePeoPle, int min, int max) {
        return calculatePeoPle >= min && calculatePeoPle <= max;
    }

    private static void initMap(int count, BufferedReader br, Nation[][] nations) throws IOException {
        for (int i = 0; i < count; i++) {
            String[] nation = br.readLine().split(" ");
            for (int j = 0; j < count; j++) {
                nations[i][j] = new Nation(Integer.parseInt(nation[j]), i, j);
            }
        }
    }

    private static void initVisit(Nation[][] nations) {
        for (int r = 0; r < nations.length; r++) {
            for (int c = 0; c < nations[0].length; c++) {
                nations[r][c].isVisited = false;
            }
        }
    }
}

class Nation {
    int population;
    boolean isVisited;
    int r;
    int c;

    public Nation(int population, int r, int c) {
        this.population = population;
        this.r = r;
        this.c = c;
        this.isVisited = false;
    }
}

class OpenBorderLine {
    int totalPopulation;
    List<NationPosition> nationPositions;

    public OpenBorderLine(int totalPopulation, List<NationPosition> nationPositions) {
        this.totalPopulation = totalPopulation;
        this.nationPositions = nationPositions;
    }

    void sumPopulation(int newPopulation) {
        this.totalPopulation += newPopulation;
    }

    int calculatePopulation() {
        return (int) Math.floor((double) totalPopulation / nationPositions.size());
    }
}

class NationPosition {
    int r;
    int c;

    public NationPosition(int r, int c) {
        this.r = r;
        this.c = c;
    }
}
