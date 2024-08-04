import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {

    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int[] initCards;

    public static void main(String[] args) throws IOException {

        int N = Integer.parseInt(br.readLine());

        int[] P = new int[N];
        int[] S = new int[N];
        int[] cards = new int[N];
        initCards = new int[N];

        initialization(P, S, cards);

        int minShuffleCount = 0;
        minShuffleCount = cardSimulation(P, S, cards);

        System.out.print(minShuffleCount);

    }


    private static void initialization(int[] P, int[] S, int[] cards) throws IOException {

        String[] pInfo = br.readLine().split(" ");
        for (int i = 0; i < pInfo.length; i++) {
            P[i] = Integer.parseInt(pInfo[i]);
        }

        String[] sInfo = br.readLine().split(" ");
        for (int i = 0; i < sInfo.length; i++) {
            S[i] = Integer.parseInt(sInfo[i]);
        }

        for (int i = 0; i < pInfo.length; i++) {
            cards[i] = i;
        }

        initCards = Arrays.copyOf(cards, cards.length);

    }

    private static int cardSimulation(int[] p, int[] s, int[] cards) {
        int answer = 0;
        while (!isCorrect(cards, p)) {
            cards = cardShuffle(s, cards);
            answer++;
            if (Arrays.equals(cards, initCards)) {
                return -1;
            }
        }
        return answer;
    }

    private static int[] cardShuffle(int[] s, int[] card) {
        int[] newShuffleCards = new int[card.length];
        for (int i = 0; i < card.length; i++) {
            newShuffleCards[s[i]] = card[i];
        }
        return newShuffleCards;
    }

    private static boolean isCorrect(int[] cards, int[] p) {
        for (int i = 0; i < cards.length; i++) {
            if (p[cards[i]] != i % 3) { // 플레이어에게 순서대로 주기위해 % 3을 사용함 0,1,2,0,1,2.... 카드의i번째의 받는 위치를 판단하기 위한 로직
                return false;
            }
        }
        return true;
    }

}
