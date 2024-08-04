import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.*;

public class Main {

    static Map<String, Integer> pictureMap = new HashMap<>();
    static List<Picture> pictures = new ArrayList<>();
    static List<String> sumList = new ArrayList<>();
    static int PICTURE_SIZE = 9;
    static int answer = 0;
    static boolean isGrainCheck = false;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


    public static void main(String[] args) throws IOException {
        initialization();
        findAll();
        findGrainSum();
        System.out.print(answer);
    }

    private static void initialization() throws IOException {
        pictureMap.put("CIRCLE", 0);
        pictureMap.put("TRIANGLE", 1);
        pictureMap.put("SQUARE", 2);
        pictureMap.put("YELLOW", 0);
        pictureMap.put("RED", 1);
        pictureMap.put("BLUE", 2);
        pictureMap.put("GRAY", 0);
        pictureMap.put("WHITE", 1);
        pictureMap.put("BLACK", 2);

        for (int i = 0; i < PICTURE_SIZE; i++) {
            String[] command = br.readLine().split(" ");
            int shape = pictureMap.get(command[0]);
            int color = pictureMap.get(command[1]);
            int backColor = pictureMap.get(command[2]);
            pictures.add(new Picture(shape, color, backColor));
        }
    }

    private static void findAll() {

        for (int i = 0; i < PICTURE_SIZE; i++) {
            for (int j = 0; j < PICTURE_SIZE; j++) {
                if (i == j) continue;
                for (int k = 0; k < PICTURE_SIZE; k++) {
                    if (i == k || j == k) continue;

                    int[] count = {i, j, k};
                    Arrays.sort(count);
                    searchSum(count);
                }
            }
        }
    }

    private static void searchSum(int[] count) {
        if (!sumList.contains(Arrays.toString(count))) {
            int picture1 = count[0];
            int picture2 = count[1];
            int picture3 = count[2];

            Picture pictureInfo1 = pictures.get(picture1);
            Picture pictureInfo2 = pictures.get(picture2);
            Picture pictureInfo3 = pictures.get(picture3);

            List<Data> data = new ArrayList<>();
            data.add(new Data(new int[]{pictureInfo1.getShape(), pictureInfo2.getShape(), pictureInfo3.getShape()}));
            data.add(new Data(new int[]{pictureInfo1.getColor(), pictureInfo2.getColor(), pictureInfo3.getColor()}));
            data.add(new Data(new int[]{pictureInfo1.getBackColor(), pictureInfo2.getBackColor(), pictureInfo3.getBackColor()}));


            boolean isNotSum = false;
            for (int i = 0; i < data.size(); i++) {
                Data list = data.get(i);
                Arrays.sort(list.pictureList);
                for (int j = 0; j < list.pictureList.length - 1; j++) {
                    if (j == 0) {
                        if (list.pictureList[j] == list.pictureList[j + 1]) {
                            list.isSame = true;
                        }
                    } else {
                        if (list.pictureList[j] == list.pictureList[j + 1]) {
                            if (!list.isSame) {
                                isNotSum = true;
                                break;
                            }
                        } else {
                            if (list.isSame) {
                                isNotSum = true;
                                break;
                            }
                        }
                    }
                }
                if (isNotSum) {
                    break;
                }
            }
            if (!isNotSum) {
                sumList.add(Arrays.toString(count));
            }
        }
    }

    private static void findGrainSum() throws IOException {
        int cycle = Integer.parseInt(br.readLine());
        for (int i = 0; i < cycle; i++) {
            String[] command = br.readLine().split(" ");

            if ("H".equals(command[0])) {
                int index1 = Integer.parseInt(command[1]) - 1;
                int index2 = Integer.parseInt(command[2]) - 1;
                int index3 = Integer.parseInt(command[3]) - 1;
                int[] sumIndex = new int[]{index1, index2, index3};
                Arrays.sort(sumIndex);
                String findIndexs = Arrays.toString(sumIndex);

                if (sumList.contains(findIndexs)) {
                    sumList.remove(findIndexs);
                    answer++;
                } else {
                    answer--;
                }

            } else {
                if (sumList.isEmpty() && !isGrainCheck) {
                    answer += 3;
                    isGrainCheck = true;
                } else {
                    answer--;
                }
            }
        }
    }
}

class Picture {
    int shape;
    int color;
    int backColor;

    public Picture(int shape, int color, int backColor) {
        this.shape = shape;
        this.color = color;
        this.backColor = backColor;
    }

    public int getShape() {
        return shape;
    }

    public int getColor() {
        return color;
    }

    public int getBackColor() {
        return backColor;
    }
}

class Data {
    int[] pictureList;
    boolean isSame;

    public Data(int[] pictureList) {
        this.pictureList = pictureList;
        this.isSame = false;
    }
}
