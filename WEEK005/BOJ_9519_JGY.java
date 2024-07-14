import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int X = Integer.parseInt(st.nextToken());
        StringBuilder origin = new StringBuilder(br.readLine());
        int k;
        if (origin.length() % 2 == 0) k = origin.length() / 2;
        else k = (origin.length() - 1) / 2;
        StringBuilder copyOrig = new StringBuilder();
        copyOrig.append(origin);
        StringBuilder tempSb;
        int cycle = 0;

        // cycle 횟수 찾기
        for(int i = 0; i < X; i++){
             tempSb = new StringBuilder(k);
            for (int j = 1; j <= k; j++){
                char element = copyOrig.charAt(j);
                copyOrig.deleteCharAt(j);
                tempSb.append(element);
            }
            copyOrig.append(tempSb.reverse());
            cycle++;
            if (copyOrig.compareTo(origin) == 0){
                break;
            }
        }
        // cycle에서 X%cycle인 n 번째 결과 찾기
        for(int i = 0; i < (X%cycle); i++){
            tempSb = new StringBuilder(k);
            for (int j = 1; j <= k; j++){
                char element = copyOrig.charAt(j);
                copyOrig.deleteCharAt(j);
                tempSb.append(element);
            }
            copyOrig.append(tempSb.reverse());
        }
        bw.write(copyOrig.toString());
        bw.flush();
        bw.close();
    }
}
