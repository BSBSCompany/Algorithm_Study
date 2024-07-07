public class Program
{
        private static Dictionary<int, List<string>> StockGroupDic;
        private static Dictionary<string, int> oneStockValue;
    
        private static long myCash;
        private static Dictionary<string, int> myStocks;
        private static string ans;

        public static void Main(string[] args)
        {
            Program program = new Program();
            
            program.Init();

            string[] inputs = Console.ReadLine().Split();
            // n : 회사의 개수, m : 하이비가 보유한 현금, q : 질문 개수
            (int n, myCash, int q) = (int.Parse(inputs[0]), long.Parse(inputs[1]), int.Parse(inputs[2]));
            
            // 회사 입력 받기
            for (int i = 0; i < n; i++)
            {
                string[] inputString = Console.ReadLine().Split();
                int groupNum = int.Parse(inputString[0]);
                
                if (StockGroupDic.ContainsKey(groupNum))
                    StockGroupDic[groupNum].Add(inputString[1]);
                else
                {
                    List<string> group = new List<string>();
                    group.Add(inputString[1]);
                    StockGroupDic.Add(groupNum, group);
                }

                oneStockValue.Add(inputString[1], int.Parse(inputString[2]));
                myStocks.Add(inputString[1], 0);
            }
            
            // q질문 만큼 돌면서 입력 받기
            for (int i = 0; i < q; i++)
            {
                string[] commands = Console.ReadLine().Split();
                int command = int.Parse(commands[0]);

                switch (command)
                {
                    case (1):
                        program.BuyStocks(commands[1], int.Parse(commands[2]));
                        break;
                    case(2):
                        program.SellStocks(commands[1], int.Parse(commands[2]));
                        break;
                    case (3) :
                        int cnt = int.Parse(commands[2]);
                        program.UpdateStock(commands[1], cnt);
                        break;
                    case (4) :
                        cnt = int.Parse(commands[2]);
                        if(cnt > 0)
                            program.GroupIncrease(int.Parse(commands[1]), cnt);
                        else
                            program.GroupDecrease(int.Parse(commands[1]), cnt);
                        break;
                    case (5) :
                        int percent = int.Parse(commands[2]);
                        if(percent > 0)
                            program.GroupIncrease_Percent(int.Parse(commands[1]), percent);
                        else
                            program.GroupDecrease_Percent(int.Parse(commands[1]), percent);
                        break;
                    case (6) :
                        long ansNum = myCash;
                        ans += ansNum + "\n";
                        break;
                    case (7) :
                        long total = myCash;
                        foreach (var company in myStocks.Keys)
                            total += (long)oneStockValue[company] * myStocks[company];
                        ans += total + "\n";
                        break;
                    default:
                        break;
                }
                
            }
            
            Console.WriteLine(ans);
        }
        
        private void Init()
        {
            myStocks = new Dictionary<string, int>();
            StockGroupDic = new Dictionary<int, List<string>>();
            oneStockValue = new Dictionary<string, int>();
            myCash = 0;
            ans = "";
        }

        private void BuyStocks(string companyName, int cnt)
        {
            long totalValue = (long)oneStockValue[companyName] * cnt;
            
            // 살수 없다면 리턴
            if (myCash < totalValue) return;
            
            myStocks[companyName] += cnt;
            myCash -= totalValue;
        }

        private void SellStocks(string companyName, int cnt)
        {
            long totalValue = (long)oneStockValue[companyName] * cnt;
            
            if (myStocks[companyName] == 0) return;

            if (myStocks[companyName] <= cnt)
                cnt = myStocks[companyName];
            
            myCash += totalValue;
            myStocks[companyName] -= cnt;
        }

        private void UpdateStock(string companyName, int cnt)
        {
            oneStockValue[companyName] += cnt;
        }
        
        private void UpdateStockByPercent(string companyName, int percent)
        {
            percent = 100 + percent;
            oneStockValue[companyName] = (int)((long)oneStockValue[companyName] * percent / 100);
            
            oneStockValue[companyName] -= oneStockValue[companyName] % 10;

        }
        
        private void GroupIncrease(int groupCum, int cnt)
        {
            foreach (var company in StockGroupDic[groupCum])
            {
                UpdateStock(company, cnt);
            }
        }
        
        private void GroupIncrease_Percent(int groupCum, int percent)
        {
            foreach (var company in StockGroupDic[groupCum])
            {
                UpdateStockByPercent(company, percent);
            }
        }
        
        private void GroupDecrease(int groupCum, int cnt)
        {
            foreach (var company in StockGroupDic[groupCum])
            {
                UpdateStock(company, cnt);
            }
        }

        private void GroupDecrease_Percent(int groupCum, int percent)
        {
            foreach (var company in StockGroupDic[groupCum])
            {
                UpdateStockByPercent(company, percent);
            }
        }
        
}