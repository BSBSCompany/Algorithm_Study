import java.io.*;
import java.util.*;

class Main {
    static final int candidateCnt = 101;

    static Frame[] frames = new Frame[candidateCnt];

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringTokenizer st = null;
        st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(br.readLine());
        int m = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        int num = 0;
        int cnt = 0;
        for (int i = 0; i < m; i++){
            num = Integer.parseInt(st.nextToken());
            if (frames[num] == null){
                if (cnt < n){
                    frames[num] = new Frame(i);
                    cnt++;
                } else {
                    frames[findMinRecommandedFrame()] = null;
                    frames[num] = new Frame(i);
                }
            } else {
                frames[num].cnt++;
            }
        }
        for (int i = 0; i < candidateCnt; i++){
            if(frames[i] != null){
                bw.write(i + " ");
            }
        }
        bw.flush();
        bw.close();

    }

    static class Frame{
        int cnt = 0;
        int t = 0;
        Frame(int t){
            cnt = 1;
            this.t = t;
        }
    }

    static int findMinRecommandedFrame() {
        int minCnt = 99999;
        int minT = 99999;
        int minNum = 0;
        for(int i = 0; i < candidateCnt; i++) {
            if (frames[i] != null){
                if (frames[i].cnt <= minCnt){
                    if (frames[i].cnt == minCnt && frames[i].t >= minT)
                        continue;
                    minCnt = frames[i].cnt;
                    minT = frames[i].t;
                    minNum = i;
                }
            }
        }
        return minNum;
    }
}
