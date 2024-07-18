import java.io.*;
import java.util.*;

public class Main {
    static final char[] dChar = {'U', 'R', 'D', 'L'};
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        Signal signal = new Signal(N, M);

        for(int i = 0; i < N; i++){
            st = new StringTokenizer(br.readLine());
            String strSt = st.nextToken();
            for(int j = 0; j < M; j++) {
                signal.galaxy[i][j] = new Galaxy(strSt.charAt(j));
            }
        }

        st = new StringTokenizer(br.readLine());
        int pR = Integer.parseInt(st.nextToken()) - 1;
        int pC = Integer.parseInt(st.nextToken()) - 1;
        signal.rOrigin = pR;
        signal.cOrigin = pC;
        signal.initPosition(pR, pC);
        signal.start();
        Result result = new Result();
        signal.result(result);
        bw.write(dChar[result.maxD] + "\n" + ((result.max == N*M)?"Voyager":result.max));
        bw.flush();
        bw.close();
    }
}

class Galaxy{
    char data;

    Galaxy(char data) {
        this.data = data;
    }
}

class Signal{
    int[] dR = {-1, 0, 1, 0};
    int[] dC = {0, 1, 0, -1};
    Galaxy[][] galaxy;
    int[] times;
    int N, M, rOrigin, cOrigin, r, c;
    int d;
    int time;
    Signal(int N, int M){
        d = 0;
        time = 0;
        times = new int[4];
        this.N = N;
        this.M = M;
        this.galaxy = new Galaxy[N][M];
    }

    void start(){
        for(int i = 0; i < dR.length; i++){
            while(times[i] <= 0){
                move();
                if(isWall() || isC()) clear(i);
                else if(isPlanetSlash()) turn();
                else if(isPlanetReverseSlash()) turnReverse();
                else if (isVoyager(i)) voyager(i);
            }
        }
    }

    void move(){
        r += dR[d];
        c += dC[d];
        time++;
    }

    boolean isPlanetSlash(){
        return galaxy[r][c].data == '/';
    }
    boolean isPlanetReverseSlash(){
        return galaxy[r][c].data == '\\';
    }

    boolean isC(){
        return galaxy[r][c].data == 'C';
    }

    boolean isWall() {
        return (
                r < 0 || r > N-1 ||
                c < 0 || c > M-1
                );
    }

    void turn(){
        if (d % 2 == 0) d++;
        else d--;
    }

    void turnReverse(){
        d = 3 - d;
    }

    void clear(int currentD){
        times[currentD] = time;
        time = 0;
        d = currentD + 1;
        initPosition(rOrigin, cOrigin);
    }

    boolean isVoyager(int currentD){
        return currentD == d && r == rOrigin && c == cOrigin;
    }

    void voyager(int currentD){
        times[currentD] = N*M;
        time = 0;
        d = currentD + 1;
        initPosition(rOrigin, cOrigin);
    }

    void initPosition(int r, int c){
        this.r = r;
        this.c = c;
    }

    void result(Result result){
        int max = times[0];
        int maxD = 0;
        for(int i = 1; i < times.length; i++){
            if (max < times[i]) {
                max = times[i];
                maxD = i;
            }
        }
        result.max = max;
        result.maxD = maxD;
    }
}

class Result {
    int max;
    int maxD;
}
