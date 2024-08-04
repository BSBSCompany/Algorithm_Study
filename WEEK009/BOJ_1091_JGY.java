import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int cardCount = Integer.parseInt(st.nextToken());
        int cardShuffleCount = 0;

        st = new StringTokenizer(br.readLine());
        int[] card = new int[cardCount];
        int[] shuffled = new int[cardCount];
        int[] P = new int[cardCount];
        int[] S = new int[cardCount];
        for(int i = 0; i < cardCount; i++){
            card[i] = i;
            P[i] = Integer.parseInt(st.nextToken());
        }
        st = new StringTokenizer(br.readLine());
        for(int i = 0; i < cardCount; i++){
            S[i] = Integer.parseInt(st.nextToken());
        }
        shuffled = card.clone();
        while(true){
            if(SpreadCard(shuffled, P)) {
                break;
            }
            if (Arrays.equals(card, shuffled) && cardShuffleCount > 0){
                cardShuffleCount = -1;
                break;
            }
            shuffled = Shuffling(shuffled, S);
            cardShuffleCount++;
        }
        bw.write(cardShuffleCount + "");
        bw.flush();
        bw.close();
    }

    static int[] Shuffling(int[] card, int[] S){
        int[] shuffled = new int[card.length];
        for(int i = 0; i < card.length; i++){
            shuffled[S[i]] = card[i];
        }
        return shuffled;
    }

    static boolean SpreadCard(int[] card, int[] P){
        int[] spreaded = new int[card.length];
        for(int i = 0; i < spreaded.length; i++){
            spreaded[card[i]] = i%3;
        }
        return (Arrays.equals(spreaded, P));
    }
}
