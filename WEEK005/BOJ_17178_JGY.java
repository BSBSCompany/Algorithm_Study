import java.io.*;
import java.util.*;

public class Main {
    static final int LINE_COUNT = 5;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        Stack<Ticket> wait = new Stack<>();
        Ticket lastEnter = null;
        Ticket[][] lines = new Ticket[n][LINE_COUNT];
        for(int i = 0; i < n; i++){
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < LINE_COUNT; j++){
                String[] ticket = st.nextToken().split("-");
                char area = ticket[0].charAt(0);
                int no = Integer.parseInt(ticket[1]);
                lines[i][j] = new Ticket(area, no);
            }
        }

        Ticket leaveTicket;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < LINE_COUNT; j++){
                // 대기줄이 비어있거나,
                // 입장 줄의 현재 티켓이 대기 줄의 티켓보다 작을 때 대기줄 서기
                if (wait.empty() || lines[i][j].compareTo(wait.peek()) < 0) {
                    wait.push(lines[i][j]);
                }
                // 대기줄이 비어있지 않고 중복 티켓은 없으니 입장 줄의 현재 티켓이 대기 줄의 티켓보다 클 경우 더 작은 대기줄 티켓은 입장 시키기
                else {
                    leaveTicket = wait.pop();
                    if (lastEnter != null && lastEnter.compareTo(leaveTicket) > 0){ // 마지막 입장 한 티켓이 지금 입장하려는 티켓보다 더 클 경우
                        bw.write("BAD");
                        bw.flush();
                        bw.close();
                        return;
                    } else {
                        lastEnter = leaveTicket;
                    }
                    j--;
                }
            }
        }
        while(!wait.empty()){
            leaveTicket = wait.pop();
            if (lastEnter != null && lastEnter.compareTo(leaveTicket) > 0){ // 마지막 입장 한 티켓이 지금 입장하려는 티켓보다 더 클 경우
                bw.write("BAD");
                bw.flush();
                bw.close();
                return;
            } else {
                lastEnter = leaveTicket;
            }
        }

        bw.write("GOOD");
        bw.flush();
        bw.close();
    }
}

class Ticket{
    char area;
    int no;
    Ticket(char area, int no){
        this.area = area;
        this.no = no;
    }
    int compareTo(Ticket anotherTicket){
        if (this.area > anotherTicket.area){
            return 1;
        } else if ((this.area < anotherTicket.area)) {
            return -1;
        } else {
            if (this.no > anotherTicket.no) return 1;
            else return -1;
        }
    }
}
