import java.io.*;
import java.util.*;

public class Main {
    final static byte BOARD_SIZE = 7;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());
        byte N = Byte.parseByte(st.nextToken()); // 게임 로그 수
        Board board = new Board(BOARD_SIZE); // 게임 맵
        int r = 0, c = 0;
        for(int i = 0; i < N; i++){
            st = new StringTokenizer(br.readLine());
            r = Integer.parseInt(st.nextToken());
            c = Integer.parseInt(st.nextToken());
            board.putOnStone(r, c, i);
//            for(int k = 1; k < BOARD_SIZE; k++){
//                for (int j = 1; j < BOARD_SIZE; j++){
//                    bw.write(board.position[k][j]);
//                }
//                bw.write("\n");
//            }
//            bw.write("\n");
        }

        int BCnt = 0, WCnt = 0;
        for(int i = 1; i < BOARD_SIZE; i++){
            for (int j = 1; j < BOARD_SIZE; j++){
                bw.write(board.position[i][j]);
                if (board.position[i][j] == 'B') BCnt++;
                else if (board.position[i][j] == 'W') WCnt++;
            }
            bw.write("\n");
        }
        if (BCnt > WCnt) bw.write("Black\n");
        else  bw.write("White\n");
        bw.flush();
        bw.close();
    }
}

class Board {
    byte[] dR = { -1, -1, 0, 1, 1, 1, 0, -1 };
    byte[] dC = { 0, 1, 1, 1, 0, -1, -1, -1 };
    char[][] position;
    char[] stone = {'B', 'W'}; // 둘 돌 가져오기 위한 배열 B, W, B, W 번갈아가며 두기 위함

    Board(byte size){
        position = new char[size][size];
        initBoard();
    }

    void putOnStone(int r, int c, int logSeq){
        byte cnt;
        char diffStone = stone[(logSeq+1)%2];
        char myStone = stone[logSeq % 2];
        position[r][c] = myStone;
        for(int i = 0; i < dR.length; i++){
            cnt = 0;
            while((r + dR[i]*(cnt + 1) > 0 &&
                    r + dR[i]*(cnt + 1) < position.length &&
                    c + dC[i]*(cnt + 1) > 0 &&
                    c + dC[i]*(cnt + 1) < position.length) &&
                    position[r + dR[i]*(cnt + 1)][c + dC[i]*(cnt + 1)] == diffStone){ // 진행방향 앞이 다른 돌일 때
                cnt++;
            }
            if ((r + dR[i]*(cnt + 1) > 0 &&
                    r + dR[i]*(cnt + 1) < position.length &&
                    c + dC[i]*(cnt + 1) > 0 &&
                    c + dC[i]*(cnt + 1) < position.length) &&
                    position[r + dR[i]*(cnt + 1)][c + dC[i]*(cnt + 1)] != '.') { // 빈칸만 아니면 => 벽 또는 나 자신 => 그 사이에 있는 돌들 catch !
                for(int j = 1; j <= cnt; j++){
                    position[r + dR[i]*j][c + dC[i]*j] = myStone;
                }
            }
        }
    }

    void initBoard(){
        position[3][3] = 'W';
        position[4][4] = 'W';
        position[3][4] = 'B';
        position[4][3] = 'B';
        for(int i = 1; i < position.length; i++){
            for(int j = 1;j < position.length;j++){
                if (position[i][j] != 'W' && position[i][j] != 'B') position[i][j] = '.';
            }
        }
    }
}
