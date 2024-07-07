import java.io.*;
import java.util.*;

class Main {

    static long mycash = 0;
    static Map<String, Long> myStockInfo = new HashMap<>();
    static Map<Integer, List<String>> stockGroupInfo = new HashMap<>();
    static Map<String, Long> stockInfo = new HashMap<>();

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        StringTokenizer st = null;
        st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        mycash = Integer.parseInt(st.nextToken());
        int q = Integer.parseInt(st.nextToken());

        for(int i = 0; i < n; i++){
            st = new StringTokenizer(br.readLine());
            int g = Integer.parseInt(st.nextToken());
            String h = st.nextToken();
            long p = Integer.parseInt(st.nextToken());
            stockInfo.put(h, p);
            if(stockGroupInfo.containsKey(g)){
                stockGroupInfo.get(g).add(h);
            }else {
                List<String> newGroup = new ArrayList<>();
                newGroup.add(h);
                stockGroupInfo.put(g, newGroup);
            }
        }
        String h;
        int no, group, data;
        for(int i = 0; i < q; i++){
            st = new StringTokenizer(br.readLine());
            no = Integer.parseInt(st.nextToken());
            switch(no){
                case 1:
                case 2:
                case 3:
                    h = st.nextToken();
                    data = Integer.parseInt(st.nextToken());
                    menuProcessing(no, h, data);
                    break;
                case 4:
                case 5:
                    group = Integer.parseInt(st.nextToken());
                    data = Integer.parseInt(st.nextToken());
                    menuProcessing(no, group, data);
                    break;
                case 6:
                case 7:
                    menuProcessing(no, bw);
                    break;
            }
        }
        bw.flush();
        bw.close();
    }

    static void menuProcessing(int no, String h, int data){
        if (no == 1){
            buy(h, data);
        } else if (no == 2){
            sale(h, data);
        } else if (no == 3){
            updown(h, data);
        }
    }

    static void menuProcessing(int no, int group, int data){
        if (no == 4){
            groupUpDown(group, data);
        } else if (no == 5){
            groupUpDownPercent(group, data);
        }
    }

    static void menuProcessing(int no, BufferedWriter bw) throws Exception{
        if (no == 6){
            bw.write(getCash() + "\n");
        }else {
            bw.write(getTotalStock() + "\n");
        }
    }

    static void buy(String h, int qty){
        if (mycash >= stockInfo.get(h) * qty) {
            if (myStockInfo.containsKey(h)) {
                long myStock = myStockInfo.get(h);
                myStock += qty;
                myStockInfo.put(h, myStock);
            }else {
                myStockInfo.put(h, (long)qty);
            }
            mycash -= (stockInfo.get(h) * qty);
        }
    }

    static void sale(String h, int qty){
        if (myStockInfo.containsKey(h)){
            if (myStockInfo.get(h) > qty){
                long myStock = myStockInfo.get(h);
                myStock -= qty;
                myStockInfo.put(h, myStock);
                mycash += (stockInfo.get(h) * qty);
            } else {
                long myStock = myStockInfo.remove(h);
                mycash += (stockInfo.get(h) * myStock);
            }
        }
    }

    static void updown(String h, long amount){
        long stock = stockInfo.get(h);
        stock += amount;
        stockInfo.put(h, stock);
    }
    static void updownPercent(String h, int percent){
        double stock = stockInfo.get(h);
        double amount = (stock * Math.abs(percent) * 0.01);
        if (percent < 0){
            amount *= -1;
        }
        stock += amount;
        stock = Math.floor(stock/10)*10;
        stockInfo.put(h, (long)stock);
    }

    static void groupUpDown(int group, long amount){
        for( String stock : stockGroupInfo.get(group)){
            updown(stock, amount);
        }
    }

    static void groupUpDownPercent(int group, int percent){
        for(String stock : stockGroupInfo.get(group)){
            updownPercent(stock, percent);
        }
    }

    static long getCash(){
       return mycash;
    }

    static long getTotalStock(){
        long total = 0;
        for (Map.Entry<String, Long> set : myStockInfo.entrySet()){
            total += (set.getValue() * stockInfo.get(set.getKey()));
        }
        return total + mycash;
    }
}
