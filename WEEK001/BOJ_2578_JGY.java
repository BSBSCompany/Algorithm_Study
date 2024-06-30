import java.io.*;
import java.util.*;

class Main {


    static boolean[][] bingo = new boolean[5][5];
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        Map<Integer, Position> numMap = new HashMap<>();
        StringTokenizer st = null;
        initBingoList();
        for(int i = 0; i < 5; i++){
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < 5; j++){
                numMap.put(Integer.parseInt(st.nextToken()), new Position(i, j));
            }
        }
        Position curPst = null;
        int answer = 0;
        for (int i = 0; i < 5; i++){
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j<5; j++){
                answer++;
                curPst = numMap.get(Integer.parseInt(st.nextToken()));
                bingo[curPst.x][curPst.y] = true;
                if (validBingo() == 3) {
                    bw.write(answer + "\n");
                    bw.flush();
                    bw.close();
                    return;
                }
            }
        }
    }

    static class Position {
        int x, y;
        Position(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    static Position[][] bingoList = new Position[12][5];
    static void initBingoList (){
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                bingoList[i][j] = new Position(i, j);
                bingoList[i+5][j] = new Position(j, i);
            }
        }
        for (int j = 0; j < 5; j++) {
            bingoList[10][j] = new Position(j, j);
        }
        for (int i = 0; i < 5; i++) {
            bingoList[11][i] = new Position(i, 4 - i);
        }
    }

    static int validBingo(){
        int cnt = 0;
        int bingoCnt = 0;
        for(int i = 0; i < 12; i++){
            for(int j = 0; j < 5; j++){
                if (bingo[bingoList[i][j].x][bingoList[i][j].y] == true)
                    cnt++;
            }
            if (cnt == 5) {
                bingoCnt++;
                if (bingoCnt == 3) return bingoCnt;
            }
            cnt = 0;
        }
        return bingoCnt;
    }
}
