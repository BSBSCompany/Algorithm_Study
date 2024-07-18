import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {


    static Queue<String> gotoLine = new LinkedList<>();
    static Stack<String> waitLine = new Stack<>();

    public static void main(String[] args) throws IOException {

        Queue<String> orderList = compareOrder();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int count = Integer.parseInt(br.readLine());

        for (int i = 0; i < count; i++) {
            String[] oneLine = br.readLine().split(" ");
            gotoLine.addAll(Arrays.asList(oneLine));
            orderList.addAll(Arrays.asList(oneLine));
        }


        while (!gotoLine.isEmpty() || !waitLine.isEmpty()) {


            String order = orderList.peek();

            if (!waitLine.isEmpty()) {
                String waitList = waitLine.peek();
                if (order.equals(waitList)) {
                    orderList.poll();
                    waitLine.pop();
                    continue;
                }
            }

            if (!gotoLine.isEmpty()) {
                String targetUser = gotoLine.poll();

                if (order.equals(targetUser)) {
                    orderList.poll();
                    continue;
                }

                waitLine.add(targetUser);
            }

            if(!waitLine.isEmpty() && gotoLine.isEmpty()){
                break;
            }
        }


        if(orderList.isEmpty()){
            System.out.print("GOOD");
        }else {
            System.out.print("BAD");
        }

    }

    private static Queue<String> compareOrder() {
        Comparator<String> customComparator = (s1, s2) -> {
            //문자비교
            char c1 = s1.charAt(0);
            char c2 = s2.charAt(0);
            if (c1 != c2) {
                return Character.compare(c1, c2);
            }
            // 첫 번째 문자가 같으면 숫자를 비교 (1-1000)
            int num1 = Integer.parseInt(s1.substring(2));
            int num2 = Integer.parseInt(s2.substring(2));
            return Integer.compare(num1, num2);
        };

        return new PriorityQueue<>(customComparator);
    }
}
