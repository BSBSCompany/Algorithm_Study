public class BOJ_15671_CYH
{
    public static readonly int BoardSize = 7;
    public static int[] dxs = { 0, 1, 1, 1, 0, -1, -1, -1 };
    public static int[] dys = { -1, -1, 0, 1, 1, 1, 0, -1 };
    public static Player[,] Board;
    public static Player curTurn;
    
    public static void Main(string[] arg)
    {
        byte[] logCnt = Array.ConvertAll(Console.ReadLine().Split(), byte.Parse);

        Init();
        
        // 입력 받기
        for (var i = 0; i < logCnt[0]; i++)
        {
            int[] input = Array.ConvertAll(Console.ReadLine().Split(), int.Parse);
            var (x, y) = (input[1], input[0]); // 행열 입력 받고
            putStone(x, y, curTurn);
            Simulation(x, y);
            ChangeTurn();
        }

        PrintWinner();
    }

    public static void Init()
    {
        Board = new Player[BoardSize, BoardSize];
        
        putStone(3, 3, Player.White);
        putStone(4, 4, Player.White);
        
        putStone(3, 4, Player.Black);
        putStone(4, 3, Player.Black);

        curTurn = Player.Black;
    }
    
    public static void Simulation(int x, int y)
    {
        CheckLineChange(x, y);
    }

    // 8방향 체크 및 업데이트
    public static void CheckLineChange(int x, int y)
    {
        int directionCnt = dxs.Length;
        Player stoneColor = Board[y, x];
        for (var i = 0; i < directionCnt; i++)
        {
            var (next_x, next_y) = MoveNext(x, y, i);
            // 범위 안에 있지 않으면 다음으로 패스
            if (!InRange(next_x, next_y)) continue;
            // 이동할 다음 위치가 같은 색이거나, 돌이 없는경우눈 패스
            if (Board[next_y, next_x] == stoneColor || Board[next_y, next_x] == Player.None) continue;

            //큐에 넣고 빼면서 변화 시켜주기.
            Queue<Tuple<int, int>> q = new Queue<Tuple<int, int>>();

            bool isMyColor = false;
            // 놓는 위치가 범위 안에 있고, 보드 다음 이동 좌표에 같은 색의 돌과 플레이어가 없는게 아니면이 아니고, 
            while (true)
            {
                q.Enqueue(new Tuple<int, int>(next_x, next_y));
                (next_x, next_y) = MoveNext(next_x, next_y, i);

                if (!InRange(next_x, next_y)) break;
                if (Board[next_y, next_x] == Player.None) break;
                if (Board[next_y, next_x] == stoneColor)
                {
                    isMyColor = true;
                    break;
                }
            }
            
            // 내 돌 색과 만나지 않았다면
            if (!isMyColor) continue;

            while (q.Count != 0)
            {
                var (nx, ny) = q.Dequeue();
                Board[ny, nx] = stoneColor;
            }
        }
    }

    public static bool InRange(int x, int y)
    {
        return x >= 1 && x < BoardSize && y >= 1 && y < BoardSize;
    }

    public static (int, int) MoveNext (int x, int y, int i)
    {
        return (x + dxs[i], y + dys[i]);
    }

    public static void putStone(int x, int y, Player color)
    {
        Board[y,x] = color;
    }

    public static void ChangeTurn()
    {
        curTurn = curTurn == Player.Black ? Player.White : Player.Black;
    }

    public static void PrintWinner()
    {
        var (blackScore, whiteScore) = (0, 0);
        for (int i = 1; i < BoardSize; i++)
        {
            for (int j = 1; j < BoardSize; j++)
            {
                Player stone = Board[i, j];
                if (stone is Player.None)
                {
                    Console.Write('.');
                    continue;
                }

                if (stone is Player.Black)
                {
                    blackScore += 1;
                    Console.Write('B');
                }
                else
                {
                    whiteScore += 1;
                    Console.Write('W');
                }
            }
            Console.WriteLine();
        }
        
        Console.WriteLine(blackScore < whiteScore ? "White" : "Black");
    }
}

public enum Player
{
    None,
    White,
    Black,
}