using System;
using System.Collections.Generic;
using System.Linq;

public class Program
{
    private Dictionary<int, Dictionary<string, Stock>> StockMarket;
    private Dictionary<string, int> groupInfo;
    private MyStock myStock;
    static int N, M, Q;
    
    public static void Main(string[] args)
    {
        Program program = new Program();
        string answer = "";
        
        //1.주식 정보 초기화
        program.Initialize();
        
        //2.커맨드 입력
        for (int i = 0; i < Q; i++)
        {
            string[] cmd = Console.ReadLine().Split();
            switch (int.Parse(cmd[0]))
            {
                case 1: program.Buy(cmd[1], int.Parse(cmd[2])); break;
                case 2: program.Sell(cmd[1], int.Parse(cmd[2])); break;
                case 3: program.PriceChange(cmd[1], int.Parse(cmd[2])); break;
                case 4: program.GroupPriceNumberChange(int.Parse(cmd[1]), int.Parse(cmd[2])); break;
                case 5: program.GroupPricePercentChange(int.Parse(cmd[1]), int.Parse(cmd[2])); break;
                case 6: answer+=program.PrintCurrentMoney(); break;
                case 7: answer+=program.CalculateAllStockMoney(); break;
            }
        }
        
        //3.결과 출력
        Console.WriteLine(answer);
    }

    public void Buy(string stockName, int count)
    {
        Stock stock = StockMarket[groupInfo[stockName]][stockName];
        long allprice = stock.price * count;
        myStock.BuyStock(stockName, count, allprice);
    }

    public void Sell(string stockName, int count)
    {
        Stock stock = StockMarket[groupInfo[stockName]][stockName];
        myStock.SellStock(stockName,count,stock.price);
    }

    public void PriceChange(string stockName,int price)
    {
        Stock stock = StockMarket[groupInfo[stockName]][stockName];
        stock.NumberChange(price);
    }

    public void GroupPriceNumberChange(int groupNumber,int price)
    {
        List<Stock> stocks = StockMarket[groupNumber].Values.ToList();
        foreach (var stock in stocks)
        {
            stock.NumberChange(price);
        }
    }
    
    public void GroupPricePercentChange(int groupNumber,int percent)
    {
        List<Stock> stocks = StockMarket[groupNumber].Values.ToList();
        foreach (var stock in stocks)
        {
            stock.PercentChange(percent);
        }
    }

    public string PrintCurrentMoney()
    {
        return myStock.money+"\n";
    }

    public string CalculateAllStockMoney()
    {
        long priceAll=0;
        foreach (var mystock in myStock.myStock)
        {
            Stock stock = StockMarket[groupInfo[mystock.Key]][mystock.Key];
            priceAll += stock.price * mystock.Value;
        }
        return (priceAll+myStock.money)+"\n";
    }
    
    public void Initialize()
    {
        int[] input1 = Array.ConvertAll(Console.ReadLine().Split(), int.Parse);
        N = input1[0];
        M = input1[1];
        Q = input1[2];
        
        StockMarket = new Dictionary<int, Dictionary<string, Stock>>();
        groupInfo = new Dictionary<string, int>();
        myStock = new MyStock(M);

        for (int i = 0; i < N; i++)
        {
            string[] stockCommand = Console.ReadLine().Split();
            int groupNumber = int.Parse(stockCommand[0]);
            string companyName = stockCommand[1];
            int price = int.Parse(stockCommand[2]);

            groupInfo.Add(companyName,groupNumber);
            myStock.myStock.Add(companyName,0);
                
            if (!StockMarket.ContainsKey(groupNumber))
            {
                StockMarket.Add(groupNumber,new Dictionary<string, Stock>());
            }
            StockMarket[groupNumber][companyName] = new Stock(price);
        }
    }
    
    public class MyStock
    {
        public Dictionary<string, int> myStock;
        public long money;
        public void BuyStock(string stockName,int count,long totalPrice)
        {
            if (totalPrice > money)
            {
                return;
            }

            money -= totalPrice;
            myStock[stockName] += count;
        }

        public void SellStock(string stockName,int count,long stockPrice)
        {
            if (myStock[stockName] == 0)
            {
                return;
            }

            if (myStock[stockName] <= count)
            {
                count = myStock[stockName];
            }

            money += stockPrice * count;
            myStock[stockName] -= count;
        }
        
        public MyStock(int money)
        {
            this.myStock = new Dictionary<string, int>();
            this.money = money;
        }
    }
    public class Stock
    {
        public long price;

        public Stock(int price)
        {
            this.price = price;
        }
        public void NumberChange(int change)
        {
            price += change;

            if (price < 0)
            {
                price = 0;
            }
        }
        public void PercentChange(int change)
        {
            if (change == 0)
            {
                return;
            }

            price = price * (100 + change) / 100;
            price -= price % 10;
        }
    }
}
