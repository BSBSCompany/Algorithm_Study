import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;

public class Main {

    static int returnCount = 0;
    static int frontWordSize = 0;
    static int backWordSize = 0;
    static Stack<Character> strings = new Stack<>();
    static int cycle = 0;
    static int allCount = 100000000;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        returnCount = Integer.parseInt(br.readLine());
        String strWord = br.readLine();

        int length = strWord.length();
        if (length % 2 == 0) {
            int halfSize = length / 2;
            frontWordSize = halfSize;
            backWordSize = halfSize;
        } else {
            double halfDoubleSize = (double) length / 2;
            frontWordSize = (int) Math.ceil(halfDoubleSize);
            backWordSize = (int) Math.floor(halfDoubleSize);
        }

        String tmpString =strWord;
        for (int i = 1; i <= allCount; i++) {
            tmpString = betweenSetting(tmpString);
            if (strWord.equals(tmpString)) {
                cycle = i;
                break;
            }
        }

        int count = returnCount % cycle;
        String answer = strWord;
        for (int i = 0; i < count; i++) {
            answer = betweenSetting(answer);
        }

        System.out.print(answer);
    }

    private static String betweenSetting(String strWord) {
        StringBuilder sb = new StringBuilder(strWord);
        for (int i = 1; i < strWord.length(); i += 2) {
            char c = sb.charAt(i);
            strings.add(c);
        }

        for (int i = 1; i <= backWordSize; i++) {
            String pop = String.valueOf(strings.pop());
            sb.deleteCharAt(i);
            sb.append(pop);
        }


        return sb.toString();
    }

}
