import java.io.*;
import java.util.*;

public class Main {
    private final static int PICTURE_NUM = 10;
    private final static String[] shapes =  {"CIRCLE", "TRIANGLE", "SQUARE"};
    private final static String[] colors = {"YELLOW", "RED", "BLUE"};
    private final static String[] backs = {"GRAY", "WHITE", "BLACK"};
    private static boolean[] shape = {false, false, false};
    private static boolean[] color = {false, false, false};
    private static boolean[] back = {false, false, false};
    private static Map<String, Byte> attrIdxMap = null;
    private static Picture[] pictures = null;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        pictures = new Picture[PICTURE_NUM];
        attrIdxMap = new HashMap<>();
        List<String> HapList = new ArrayList<>();
        int playerScore = 0;
        boolean Gflag = false;
        for(int i = 0; i < shapes.length; i++){
            attrIdxMap.put(shapes[i], (byte)i);
            attrIdxMap.put(colors[i], (byte)i);
            attrIdxMap.put(backs[i], (byte)i);
        }
        StringTokenizer st = null;
        for(int i = 1; i < PICTURE_NUM; i++){
            st = new StringTokenizer(br.readLine());
            pictures[i] = new Picture(st.nextToken(), st.nextToken(), st.nextToken());
        }

        for(int i = 1; i < PICTURE_NUM; i++){
            for(int j = i+1; j < PICTURE_NUM; j++){
                for(int k = j+1; k < PICTURE_NUM; k++){
                    if (checkHap(i, j, k))
                        HapList.add(String.valueOf(i) + j + k);
                }
            }
        }
        String strGameNum = br.readLine();
        int gameNum = Integer.parseInt(strGameNum);
        String cmd = "";
        String[] cmdArr = new String[4];
        Byte[] pic = new Byte[3];
        for(int i = 0; i < gameNum; i++){
            cmd = br.readLine();
            if (cmd.split(" ")[0].equals("H")){
                for(int j = 0; j < pic.length; j++) {
                    pic[j] = Byte.parseByte(cmd.split(" ")[j + 1]);
                }
                Arrays.sort(pic);
                String playerSay = String.valueOf(pic[0]) + pic[1] + pic[2];
                if (HapList.contains(playerSay)) {
                    HapList.remove(playerSay);
                    playerScore ++;
                }
                else
                    playerScore --;
            }
            else {
                if(HapList.isEmpty() && !Gflag) {
                    playerScore += 3;
                    Gflag = true;
                }
                else
                    playerScore --;
            }
        }
        bw.write(playerScore + "");
        bw.flush();
        bw.close();
    }

    static void initAttribute(){
        for(int i = 0; i < shape.length; i++) {
            shape[i] = false;
            color[i] = false;
            back[i] = false;
        }
    }

    static boolean checkHap(int _pic1, int _pic2, int _pic3){
        Picture[] picList = new Picture[3];
        picList[0] = pictures[_pic1];
        picList[1] = pictures[_pic2];
        picList[2] = pictures[_pic3];
        for (Picture picture : picList) {
            shape[picture.shape] = true;
            color[picture.color] = true;
            back[picture.back]   = true;
        }
        int shapeCnt = 0, colorCnt = 0, backCnt = 0;
        for(int i = 0; i < shape.length; i++){
            if (shape[i]) shapeCnt ++;
            if (color[i]) colorCnt ++;
            if (back[i])  backCnt  ++;
        }
        initAttribute();
        return (
                shapeCnt != 2 && colorCnt != 2 && backCnt != 2
        );
    }

    static class Picture {
        byte shape = 0;
        byte color = 0;
        byte back  = 0;
        Picture(String _shape, String _color, String _back){
            this.shape = attrIdxMap.get(_shape);
            this.color = attrIdxMap.get(_color);
            this.back = attrIdxMap.get(_back);
        }
    }
}
