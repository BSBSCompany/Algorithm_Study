import java.io.*;
import java.util.*;

public class Main {
    static int[] dRright = { -1, 0, 1, 0 };
    static int[] dCright = { 0, 1, 0, -1 };
    static int[] dRreverse = { 1, 0, -1, 0 };
    static int[] dCreverse = { 0, 1, 0, -1 };
    static int R, C;
    static FineDust[][] fineDusts = null;
    final static int AIRCLEANER_CNT = 2;
    static AirCleaner airCleaner = null;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());
        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        int T = Integer.parseInt(st.nextToken());

        fineDusts = new FineDust[R][C];
        airCleaner = new AirCleaner();
        for(int i = 0; i < R; i++){
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < C; j++){
                int amount = Integer.parseInt(st.nextToken());
                if (amount == -1) {
                    airCleaner.setAirCleaner(i, j);
                    amount = 0;
                }
                fineDusts[i][j] = new FineDust(amount);
            }
        }

        for (int i = 0; i < T; i++){
            for(int j = 0; j < R; j++){
                for(int k = 0; k < C; k++){
                    fineDusts[j][k].diffuse(j, k);
                }
            }

            for(int j = 0; j < R; j++){
                for(int k = 0; k < C; k++){
                    fineDusts[j][k].amountTotal();
                }
            }

            airCleaner.AirCleaning();
        }

        int sum = 0;
        for(int j = 0; j < R; j++){
            for(int k = 0; k < C; k++){
                sum += fineDusts[j][k].amount;
            }
        }
        bw.write(sum + "");
        bw.flush();
        bw.close();
    }

    static class FineDust {
        int amount;
        int diffusedAmount = 0;

        FineDust(int amount){
            this.amount = amount;
        }

        List<FineDust> checkAround(int r, int c) {
            List<FineDust> diffusableList = new ArrayList<>();
            for(int i = 0; i < dRright.length; i++){
                if ( !isWall(r + dRright[i], c + dCright[i]) && !isAirCleaner(r + dRright[i], c + dCright[i])) {
                    diffusableList.add(fineDusts[r + dRright[i]][c + dCright[i]]);
                }
            }
            return diffusableList;
        }

        boolean isAirCleaner(int r, int c){
            for (int i = 0; i < AIRCLEANER_CNT; i++){
                if (airCleaner.cleaners[i].r == r && airCleaner.cleaners[i].c == c) {
                    return true;
                }
            }
            return false;
        }

        boolean isWall(int r, int c){
            return (
                    r < 0 || r >= R || c < 0 || c >= C
            );
        }

        void diffuse(int r, int c){
            List<FineDust> diffuseList = checkAround(r, c);
            int diffuseAmount = amount / 5;
            amount -= (diffuseAmount * diffuseList.size());
            for(FineDust fineDust : diffuseList){
                fineDust.diffusedAmount += diffuseAmount;
            }
        }

        void amountTotal(){
            amount += diffusedAmount;
            diffusedAmount = 0;
        }
    }

    static class AirCleaner{
        Position[] cleaners = new Position[AIRCLEANER_CNT];
        void setAirCleaner(int r, int c){
            for (int i = 0; i < AIRCLEANER_CNT; i++){
                if (cleaners[i] == null){
                    cleaners[i] = new Position(r, c);
                    break;
                }
            }
        }

        void AirCleaning(){
            topAirCleaning();
            bottomAirCleaning();
        }

        void topAirCleaning(){
            int tempR = cleaners[0].r;
            int tempC = cleaners[0].c;
            for (int i = 0; i < dRright.length; i++){
                while(!isTopLimit(tempR + dRright[i], tempC + dCright[i])){
                    fineDusts[tempR][tempC] = fineDusts[tempR + dRright[i]][tempC + dCright[i]];

                    tempR += dRright[i];
                    tempC += dCright[i];
                }
            }
            fineDusts[cleaners[0].r][cleaners[0].c] = new FineDust(0); // 공기청정기로 빨아들인 미세먼지 없애기
            fineDusts[tempR][tempC] = new FineDust(0); // 공기청정기 바로 앞 자리 => 공기정청기에서 나온 깨끗한 먼지
        }

        void bottomAirCleaning(){
            int tempR = cleaners[1].r;
            int tempC = cleaners[1].c;
            for (int i = 0; i < dRreverse.length; i++){
                while(!isBottomLimit(tempR + dRreverse[i], tempC + dCreverse[i])){
                    fineDusts[tempR][tempC] = fineDusts[tempR + dRreverse[i]][tempC + dCreverse[i]];

                    tempR += dRreverse[i];
                    tempC += dCreverse[i];
                }
            }
            fineDusts[cleaners[1].r][cleaners[1].c] = new FineDust(0); // 공기청정기로 빨아들인 미세먼지 없애기
            fineDusts[tempR][tempC] = new FineDust(0); // 공기청정기 바로 앞 자리 => 공기정청기에서 나온 깨끗한 먼지
        }

        boolean isTopLimit(int r, int c){
            return (
                    r < 0 || r > cleaners[0].r || c < 0 || c >= C || ( r == cleaners[0].r && c == cleaners[0].c)
            );
        }

        boolean isBottomLimit(int r, int c){
            return (
                    r < cleaners[1].r || r >= R || c < 0 || c >= C || ( r == cleaners[1].r && c == cleaners[1].c)
            );
        }
    }

    static class Position{
        int r, c;
        Position(int r, int c){
            this.r = r;
            this.c = c;
        }
    }
}
