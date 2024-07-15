public class BOJ_16918_CYH
{
    private static int maxRow;
    private static int maxColum;
    private static Bomb[,] worldMap;
    private static int MaxTime;
    private static int worldTime;
    private static readonly int[] dcs = { 0, 1, 0, -1 };
    private static readonly int[] drs = { -1, 0, 1, 0 };

    public static void Main(string[] arg)
    {
        init();
        worldTime++;
        while (worldTime < MaxTime)
        {
            worldTime++;
            Simulation();
        }
        
        printResult();
    }

    //초기화 함수
    private static void init()
    {
        worldTime = 0;
        int[] userInput = Array.ConvertAll(Console.ReadLine().Split(), int.Parse);
        maxRow = userInput[0];
        maxColum = userInput[1];
        MaxTime = userInput[2];
        worldMap = new Bomb[maxRow, maxColum];

        for (int i = 0; i < maxRow; i++)
        {
            string inputString = Console.ReadLine();
            for (int j = 0; j < maxColum; j++)
            {
                worldMap[i,j] = new Bomb(i, j);
                if (inputString[j].Equals('O'))
                    worldMap[i,j].Alive(worldTime);
            }
        }
    }
    
    private static void Simulation()
    {
        // 터트려 줄 폭탄이 있으면 터트리고 아니라면 폭탄을 놓는 차례임.
        if (!IsCheckingBombTime(out Queue<Bomb> bombQ))
        {
            SetUpBomb();
            return;
        }
        
        while (bombQ.Count > 0)
        {
            var bomb = bombQ.Dequeue();

            // 십자가형태로 터트려 주기
            Boommmmm(bomb.r, bomb.c);
        }
    }
    
    private static bool IsCheckingBombTime(out Queue<Bomb> q)
    {
        q = new Queue<Bomb>();
        
        foreach (var bomb in worldMap)
        {
            if (bomb.isActive && bomb.its3seconds(worldTime))
                q.Enqueue(bomb);
        }

        return q.Count > 0;
    }

    private static void SetUpBomb()
    {
        foreach (var bomb in worldMap)
        {
            if (bomb.isActive) continue;
            bomb.Alive(worldTime);
        }
    }
    
    //해당 폭탄 중심으로 터트려 주기.
    private static void Boommmmm(int r, int c)
    {
        // 본인 먼저 터트려 주기
        worldMap[r, c].Death();
        
        var lenDirection = drs.Length;
        for (var i = 0; i < lenDirection; i++)
        {
            var (dr, dc) = (drs[i], dcs[i]);
            var (nr, nc) = (r + dr, c + dc);
            
            // 터트리려는 곳이 범위 밖이라면 패스
            if (!InRange(nr, nc)) continue;
            // 터트리려는 곳에 폭탄이 없거나, 동시에 터져야 할 폭탄이라면 패스
            if (!worldMap[nr, nc].isActive || worldMap[nr, nc].its3seconds(worldTime)) continue;
            
            worldMap[nr, nc].Death(); // 그 외의 폭탄들은 전부 터트려 준다.
        }
    }

    //범위 체크
    private static bool InRange(int r, int c) => r >= 0 && r < maxRow && c >= 0 && c < maxColum;

    //결과 출력 함수
    private static void printResult()
    {
        for (int i = 0; i < maxRow; i++)
        {
            for (int j = 0; j < maxColum; j++)
            {
                Console.Write(worldMap[i, j].isActive ? 'O' : '.');
            }
            Console.WriteLine();
        }
        Console.WriteLine();
    }

    public class Bomb
    {
        public bool isActive;
        public int setTime;
        public int r;
        public int c;

        public Bomb(int r, int c)
        {
            this.isActive = false;
            this.setTime = -1;
            (this.r, this.c) = (r, c);
        }

        public void Alive(int worldTime)
        {
            isActive = true;
            setTime = worldTime;
        }
        
        public void Death()
        {
            isActive = false;
            setTime = -1;
        }
        
        public bool its3seconds(int worldTime)
        {
            return (worldTime-setTime) == 3;
        }
    }
}