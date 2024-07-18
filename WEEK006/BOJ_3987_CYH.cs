public class BOJ_3987_CYH
{
    public enum Direction
    {
        U = 0,
        R,
        D, 
        L
    }

    private static int RangeDir = 4; 
    private static readonly int[] drs = { -1, 0, 1, 0 };
    private static readonly int[] dcs = { 0, 1, 0, -1 };
    private static int maxR;
    private static int maxC;
    private static int[] ans; //무한대이면 -1로 기록
    private static int flowTime;
    private static char[,] Galaxy;
    private static int initR;
    private static int initC;
    private static Signal mySignal;

    public static void Main(string[] args)
    {
        Init();
        
        for (int i = 0; i < RangeDir; i++)
        {
            flowTime = 1;
            Simulation((Direction)i);
        }

        Print();
    }
    
    private static void Init()
    {
        int[] inputs = Array.ConvertAll(Console.ReadLine().Split(), int.Parse);
        (maxR, maxC) = (inputs[0], inputs[1]);
        Galaxy = new char[maxR,maxC];

        ans = new int[RangeDir];
        for (int i = 0; i < maxR; i++)
        {
            string line = Console.ReadLine();
            for (var j = 0; j < line.Length; j++)
            {
                Galaxy[i, j] = line[j];
            }
        }
        
        inputs = Array.ConvertAll(Console.ReadLine().Split(), int.Parse);
        (initR, initC) = (inputs[0]-1, inputs[1]-1);
    }

    private static void Simulation(Direction nowDir)
    {
        Init_Signal(nowDir);
        var (nr, nc) = mySignal.NextPos();

        //다음 위치가 현재 위치가 아니면, 계속 돈다.
        while (true)
        {
            //무한대 도는 경우 : 초기 위치로 오는 경우
            if (nr == initR && nc == initC && mySignal.currDir == nowDir)
            {
                ans[(int)nowDir] = -1;
                break;
            }
            
            // 가려는 위치가 안전하지 않은 경우
            if (!ItsOk(nr, nc))
            {
                ans[(int)nowDir] = flowTime;
                break;
            }

            if (IsBlackHall(nr, nc))
            {
                ans[(int)nowDir] = flowTime;
                break;
            }
            
            mySignal.Shot();
            flowTime++;
            
            // 이동한 위치의 char에 따라서 방향 바꾸기.
            mySignal.Rotate(Galaxy[nr, nc]);
            (nr, nc) = mySignal.NextPos();
        }
    }

    private static void Init_Signal(Direction nowDir)
    {
        mySignal = new Signal(initR, initC, nowDir);
    }

    private static bool ItsOk(int nr, int nc)
    {
        // 벽인지 체크
        return nr >= 0 && nr < maxR && nc >= 0 && nc < maxC;
    }

    private static bool IsBlackHall(int nr, int nc)
    {
        //블랙홀인지 체크
        return Galaxy[nr, nc] == 'C';
    }

    private static void Print()
    {
        Direction ansDir = Direction.U;
        int maxTime = ans[0];
        string ansTime = ans[0].ToString();
        
        for (var i = 0; i < RangeDir; i++)
        {
            if (ans[i] == -1)
            {
                ansDir = (Direction)i;
                ansTime = "Voyager";
                break;
            }

            if (maxTime >= ans[i]) continue;
            
            ansDir = (Direction)i;
            maxTime = ans[i];
            ansTime = maxTime.ToString();
        }

        string ansChar = "";
        
        switch (ansDir)
        {
            case Direction.U:
                ansChar = "U";
                break;
            case Direction.R:
                ansChar = "R";
                break;
            case Direction.D:
                ansChar = "D";
                break;
            case Direction.L:
                ansChar = "L";
                break;
        }
        
        Console.WriteLine(ansChar);
        Console.WriteLine(ansTime);
    }

    public class Signal
    {
        public Direction currDir;
        public int r;
        public int c;

        public Signal(int r, int c, Direction dir)
        {
            this.r = r;
            this.c = c;
            this.currDir = dir;
        }

        public Tuple<int, int> NextPos()
        {
            return new Tuple<int, int>(r + drs[(int)currDir], c + dcs[(int)currDir]);
        }

        public void Shot()
        {
            (this.r, this.c) = (r + drs[(int)currDir], c + dcs[(int)currDir]);
        }

        public void Rotate(Char ro)
        {
            int myRo = (int)currDir;
            switch(ro)
            {
                case '/':
                    myRo = myRo == 1 || myRo == 3 ? (myRo + 3) % 4 : (myRo + 1);
                    break;
                case '\\':
                    myRo = myRo == 1 || myRo == 3 ? (myRo + 1) % 4 : (myRo + 3) % 4;
                    break;
                default:
                    break;
            }

            currDir = (Direction)myRo;
        }
    }
}