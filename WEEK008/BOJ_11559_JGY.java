import java.io.*;
import java.util.*;

public class Main {
    // 상 하 좌 우
    static int[] dr = { -1, 1, 0, 0 };
    static int[] dc = { 0, 0, -1, 1 };
    final static int ROW_CNT = 12;
    final static int COL_CNT = 6;
    final static int GATHERED_CNT = 4;
    final static char NON_PUYO = '.';

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;
        String str = "";
        Puyo[][] puyoMap = new Puyo[ROW_CNT][COL_CNT];
        List<Puyo> gatheredPuyo = new ArrayList<>();
        int chainingCnt = 0;
        boolean chaining = true;
        for(int i = 0; i < ROW_CNT; i++){
            st = new StringTokenizer(br.readLine());
            str = st.nextToken();
            for(int j = 0; j < COL_CNT; j++){
                puyoMap[i][j] = new Puyo(str.charAt(j));
            }
        }

        while(chaining) {
            chaining = false;
            for (int i = 0; i < ROW_CNT; i++) {
                for (int j = 0; j < COL_CNT; j++) {
                    if (puyoMap[i][j].color != NON_PUYO) {
                        puyoMap[i][j].searchAdjacentPuyo(puyoMap, gatheredPuyo, i, j);
                        if (puyoMap[i][j].deletePuyo(gatheredPuyo)) {
                            if (!chaining) {
                                chaining = true;
                                chainingCnt++;
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < ROW_CNT; i++) {
                for (int j = 0; j < COL_CNT; j++) {
                    puyoMap[i][j].initPuyo();
                }
            }

            for (int j = 0; j < COL_CNT; j++) {
                puyoMap[ROW_CNT - 1][j].dropPuyo(puyoMap, j);
            }
        }

        bw.write(chainingCnt + "");
        bw.flush();
        bw.close();
    }

    static class Puyo {
        char color;
        boolean visited;
        Puyo[] adjacentPuyo;
        Puyo(char color){
            this.color = color;
            adjacentPuyo = new Puyo[dr.length];
        }

        void searchAdjacentPuyo(Puyo[][] puyoMap, List<Puyo> gatheredPuyo, int r, int c){
            if (!this.visited) {
                this.visited = true;
                gatheredPuyo.add(this);
                for (int i = 0; i < dr.length; i++) {
                    if (!isEnd(r + dr[i], c + dc[i]) &&
                            puyoMap[r + dr[i]][c + dc[i]].color == this.color && !puyoMap[r + dr[i]][c + dc[i]].visited) {
                        adjacentPuyo[i] = puyoMap[r + dr[i]][c + dc[i]];
                    }
                }
                for (int i = 0; i < adjacentPuyo.length; i++) {
                    if (adjacentPuyo[i] != null) {
                        adjacentPuyo[i].searchAdjacentPuyo(puyoMap, gatheredPuyo, r + dr[i], c + dc[i]);
                    }
                }
            }
        }

        boolean deletePuyo(List<Puyo> gatheredPuyo){
            boolean deleteYN = false;
            if(gatheredPuyo.size() >= GATHERED_CNT){
                deleteYN = true;
                for(Puyo puyo : gatheredPuyo){
                    puyo.color = NON_PUYO;
                }
            }
            gatheredPuyo.clear();
            return deleteYN;
        }

        void initPuyo(){
            this.visited = false;
            for(int i = 0; i < adjacentPuyo.length; i++){
                if (adjacentPuyo[i] != null) adjacentPuyo[i] = null;
            }
        }

        void dropPuyo(Puyo[][] puyoMap, int c){
            int bottom = ROW_CNT - 1;
            for(int i = ROW_CNT - 1; i >= 0; i--) {
                if (puyoMap[i][c].color != NON_PUYO) {
                    if (bottom > i) {
                        puyoMap[bottom][c].color = puyoMap[i][c].color;
                        puyoMap[i][c].color = NON_PUYO;
                    }
                    bottom--;
                }
            }
        }

        boolean isEnd(int r, int c){
            return (r < 0 || r > ROW_CNT - 1 || c < 0 || c > COL_CNT - 1);
        }
    }
}
