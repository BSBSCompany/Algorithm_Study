import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StockSimulation {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] splitInfo = br.readLine().split(" ");

        int N = Integer.parseInt(splitInfo[0]);
        int M = Integer.parseInt(splitInfo[1]);
        int Q = Integer.parseInt(splitInfo[2]);

        Hiber hiber = new Hiber(M);
        StockMarket stockMarket = new StockMarket();

        for (int i = 0; i < N; i++) {
            String[] stockInfo = br.readLine().split(" ");
            int G = Integer.parseInt(stockInfo[0]);
            String H = stockInfo[1];
            int P = Integer.parseInt(stockInfo[2]);

            stockMarket.stockGroup.put(H, G);

            if (!stockMarket.stockList.containsKey(G)) {
                Map<String, Long> stock = new HashMap<>();
                stock.put(H, (long) P);
                stockMarket.stockList.put(G, stock);
            } else {
                Map<String, Long> map = stockMarket.stockList.get(G);
                map.put(H, (long) P);
            }

        }


        for (int i = 0; i < Q; i++) {
            String[] stockAction = br.readLine().split(" ");
            int command = Integer.parseInt(stockAction[0]);

            if (command <= 5) {
                String company = stockAction[1];
                int commandOption = Integer.parseInt(stockAction[2]);
                stockMarket.findCommand(command, company, commandOption, hiber);
            } else {
                if (command == 6) {
                    hiber.currentMoneyPrint();
                }
                if (command == 7) {
                    hiber.totalMoneyPrint(stockMarket);
                }
            }


        }

        for (int i = 0; i < hiber.answer.size(); i++) {
            if (hiber.answer.size() - 1 == i) {
                System.out.print(hiber.answer.get(i));
            } else {
                System.out.println(hiber.answer.get(i));
            }
        }

    }
}

class Hiber {

    long money;
    Map<String, Integer> myStockList = new HashMap<>();
    List<Long> answer = new ArrayList<>();

    public Hiber(int money) {
        this.money = money;
    }


    void currentMoneyPrint() {
        long money = (long) this.money;
        answer.add(money);
    }

    void totalMoneyPrint(StockMarket stockMarket) {
        long money = (long) this.money;

        for (Map.Entry<String, Integer> myStock : myStockList.entrySet()) {
            Integer groupId = stockMarket.stockGroup.get(myStock.getKey());
            Map<String, Long> groupStockList = stockMarket.stockList.get(groupId);
            Long price = groupStockList.getOrDefault(myStock.getKey(), 0L);
            long myStockPurchase = (Long) price * myStock.getValue();
            money = (long) (money + myStockPurchase);
        }
        answer.add(money);
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }
}

class StockMarket {
    Map<Integer, Map<String, Long>> stockList = new HashMap<>();
    Map<String, Integer> stockGroup = new HashMap<>();


    void findCommand(int command, String company, int commandOption, Hiber hiber) {

        switch (command) {
            case 1:
                buyStock(company, commandOption, hiber);
                break;
            case 2:
                sellStock(company, commandOption, hiber);
                break;
            case 3:
                fluctuateStock(company, commandOption);
                break;
            case 4:
                fluctuateGroup(company, commandOption);
                break;
            case 5:
                percentGroup(company, commandOption);
                break;
        }
    }

    void buyStock(String company, int commandOption, Hiber hiber) {
        long money = hiber.getMoney();

        Integer groupId = stockGroup.get(company);
        Map<String, Long> groupStockList = stockList.get(groupId);
        Long price = groupStockList.get(company);

        long myStockPurchase = price * commandOption;

        if (myStockPurchase <= money) {
            money = money - myStockPurchase;
            hiber.setMoney(money);
            Integer count = hiber.myStockList.getOrDefault(company, 0);
            count = count + commandOption;
            hiber.myStockList.put(company, count);
        }

    }

    void sellStock(String company, int commandOption, Hiber hiber) {
        long money = hiber.getMoney();

        Integer groupId = stockGroup.get(company);
        Map<String, Long> groupStockList = stockList.get(groupId);
        Long price = groupStockList.get(company);

        Integer myStockCount = hiber.myStockList.getOrDefault(company, 0);
        if (myStockCount > 0) {
            if (commandOption >= myStockCount) {
                long totalPrice = price * myStockCount;
                money = money + totalPrice;
                hiber.setMoney(money);
                hiber.myStockList.remove(company);
            } else {
                long myPrice = price * commandOption;
                money = money + myPrice;
                hiber.setMoney(money);
                myStockCount = myStockCount - commandOption;
                hiber.myStockList.put(company, myStockCount);
            }
        }

    }

    void fluctuateStock(String company, int commandOption) {
        Integer groupId = stockGroup.get(company);
        Map<String, Long> groupStockList = stockList.get(groupId);
        Long price = groupStockList.get(company);
        price = price + commandOption;
        groupStockList.put(company, price);
    }

    void fluctuateGroup(String company, int commandOption) {
        int groupId = Integer.parseInt(company);
        Map<String, Long> groupStockList = stockList.get(groupId);

        for (Map.Entry<String, Long> entry : groupStockList.entrySet()) {
            Long price = entry.getValue();
            price = price + commandOption;
            groupStockList.put(entry.getKey(), price);
        }
    }

    void percentGroup(String company, int commandOption) {
        int groupId = Integer.parseInt(company);
        Map<String, Long> groupStockList = stockList.get(groupId);
        for (Map.Entry<String, Long> entry : groupStockList.entrySet()) {
            double price = entry.getValue();
            double ratePrice = (price * commandOption / 100);
            price = price + ratePrice;
            price = price - price % 10;
            groupStockList.put(entry.getKey(), (long) price);
        }
    }
}

