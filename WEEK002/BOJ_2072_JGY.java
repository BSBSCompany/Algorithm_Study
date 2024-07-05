import java.io.*;
import java.util.*;

class Main {
    final static int[] dirR = {-1, -1, 0, 1, 1,  1,  0, -1};
    final static int[] dirC = {0 , 1 , 1, 1, 0, -1, -1, -1};
    static Point[][] map = null;
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        map = new Point[20][20];

        int count = 0;
        byte color = 1, r = 0, c = 0;
        StringTokenizer st = null;
        st = new StringTokenizer(br.readLine());
        count = Integer.parseInt(st.nextToken());
        for(int i = 0; i < count; i++){
            st = new StringTokenizer(br.readLine());
            r = Byte.parseByte(st.nextToken());
            c = Byte.parseByte(st.nextToken());
            map[r][c] = new Point(color, r, c);
            color *= -1;
            if (map[r][c].CheckFivePoint()) {
                bw.write((i+1) + " ");
                bw.flush();
                bw.close();
                return;
            }
        }
        bw.write(-1 + " ");
        bw.flush();
        bw.close();
    }

    static class Point{
        byte color;
        byte r, c;
        Point(byte color, byte r, byte c) {
            this.color = color;
            this.r = r;
            this.c = c;
        }
        boolean CheckFivePoint(){
            return (verticalCheck()
            || horizontalCheck()
            || diagonalLTRBCheck()
            || diagonalLBRTCheck());
        }
        boolean verticalCheck(){
            int cnt = 1;
            int tR = r;
            int tC = c;
            while(go(0, tR, tC)){
                cnt++;
                tR += dirR[0];
                tC += dirC[0];
            }
            tR = r;
            tC = c;
            while(go(4, tR, tC)){
                cnt++;
                tR += dirR[4];
                tC += dirC[4];
            }
            if (cnt == 5)
                return true;
            else
                return false;
        }
        boolean horizontalCheck(){
            int cnt = 1;
            int tR = r;
            int tC = c;
            while(go(1, tR, tC)){
                cnt++;
                tR += dirR[1];
                tC += dirC[1];
            }
            tR = r;
            tC = c;
            while(go(5, tR, tC)){
                cnt++;
                tR += dirR[5];
                tC += dirC[5];
            }
            if (cnt == 5)
                return true;
            else
                return false;

        }
        boolean diagonalLTRBCheck(){
            int cnt = 1;
            int tR = r;
            int tC = c;
            while(go(2, tR, tC)){
                cnt++;
                tR += dirR[2];
                tC += dirC[2];
            }
            tR = r;
            tC = c;
            while(go(6, tR, tC)){
                cnt++;
                tR += dirR[6];
                tC += dirC[6];
            }
            if (cnt == 5)
                return true;
            else
                return false;

        }
        boolean diagonalLBRTCheck(){
            int cnt = 1;
            int tR = r;
            int tC = c;
            while(go(3, tR, tC)){
                cnt++;
                tR += dirR[3];
                tC += dirC[3];
            }
            tR = r;
            tC = c;
            while(go(7, tR, tC)){
                cnt++;
                tR += dirR[7];
                tC += dirC[7];
            }
            if (cnt == 5)
                return true;
            else
                return false;

        }

        boolean go(int dir, int r, int c){
            if( r + dirR[dir] > -1 && r + dirR[dir] < 20
                    && c + dirC[dir] > -1 && c + dirC[dir] < 20
                    && map[r + dirR[dir]][c + dirC[dir]] != null
                    && map[r + dirR[dir]][c + dirC[dir]].color == color)
                return true;
            else
                return false;
        }
    }
}
