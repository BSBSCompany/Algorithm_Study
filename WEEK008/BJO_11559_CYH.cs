namespace codingTest;

public class BJO_11559_CYH
{
    private static Char[,] Map;
    private static int ans;
    private static int rowSize;
    private static int columSize;
    private static bool[,] visited;
    
    public static void Main(string[] args)
    {
        Init();

        while (Simulation())
        {
            ans += 1;
        }
        
        Console.WriteLine(ans);
    }

    private static void Init()
    {
        //맵 세팅
        rowSize = 12;
        columSize = 6;
        Map = new char[rowSize, columSize];
        for (int r = 0; r < rowSize; r++)
        {
            string inputLine = Console.ReadLine();
            for (int c = 0; c < columSize; c++)
            {
                if (inputLine != null) Map[r, c] = inputLine[c];
            }
        }
        
        //연쇄 횟수 초기화
        ans = 0;
        
        //BFS를 위한 세팅
        visited = new bool[rowSize, columSize];
    }

    private static bool Simulation()
    {
        bool isBomb = false;
        // visited 초기화
        for (int r = 0; r < rowSize; r++)
        {
            for (int c = 0; c < columSize; c++)
            {
                visited[r, c] = false;
            }
        }
        // BFS에서 사용할 Queue 초기화
        Queue<(int, int)> poyoQueue = new Queue<(int, int)>();
        // 터트려야 할 뿌요들을 담음 Queue
        Queue<(int, int)> bombQueue = new Queue<(int, int)>();
        
        for (var r = rowSize-1; r >= 0 ; r--)
        {
            for (var c = 0; c < columSize; c++)
            {
                if (Map[r, c] == '.' || visited[r,c]) continue;
                poyoQueue.Enqueue((r,c));
                if (BFS(poyoQueue,bombQueue))
                {
                    isBomb = true;
                    Bomb(bombQueue);
                }
                else
                {
                    bombQueue = new Queue<(int, int)>();
                }
            }
        }
        
        if (isBomb) 
            SetGravity();
        
        return isBomb;
    }

    private static bool BFS(Queue<(int, int)> poyoQueue,Queue<(int, int)> bombQueue)
    {
        int[,] directions = { { -1, 0 }, { 1, 0 }, { 0, 1 }, { 0, -1 } };
        var (row, col) = poyoQueue.Peek();
        bombQueue.Enqueue((row, col));
        Char thisColor = Map[row, col];
        visited[row, col] = true;
        int cnt = 1;
        
        while (poyoQueue.Count > 0)
        {
            var (nr, nc) = poyoQueue.Dequeue();
            for (int i = 0; i < directions.GetLength(0); i++)
            {
                var (newR, newC) = (nr + directions[i,0], nc + directions[i,1]);
                if (MoveAble(newR, newC, thisColor))
                {
                    cnt++;
                    poyoQueue.Enqueue((newR, newC));
                    bombQueue.Enqueue((newR, newC));
                    visited[newR, newC] = true;
                }
            }
        }

        // 좋지 않은 코드인것 같은데, 다른 로직을 한번 생각해보자.
        return cnt >= 4;
    }

    private static bool MoveAble(int nr, int nc, char myValue)
    {
        return InRange(nr, nc) && !visited[nr, nc] && myValue == Map[nr, nc];
    }

    private static bool InRange(int nr, int nc)
    {
        return nr >= 0 && nr < rowSize && nc >= 0 && nc < columSize;
    }

    private static void Bomb(Queue<(int, int)> bombQueue)
    {
        while (bombQueue.Count > 0)
        {
            var (nr, nc) = bombQueue.Dequeue();
            Map[nr, nc] = '.';
        }
    }

    private static void SetGravity()
    {
        //한깐씩 옆으로 이동하면서 중력 적용
        for (var c = 0; c < columSize; c++)
        {
            /*
             * 아래에서부터 올라오면서 제일 처음 . 위치를 찾는 경우 해당 위치를 저장해 놓는다. = nullPos
             * nullPos의 다음 위치에서부터 위로 올라가면서 뿌요가 놓여있는 위치를 찾는다
             * -> 찾았다면 찾은 위치를 저장(=existPos)
             * -> 못 찾았다면 그대로 다음 colum으로
             */
            int nullPos;        // 시작 "." 위치
            int startPos = -1;   // 뿌요 시작 위치
            for (var r = rowSize - 1; r > 0; r--)
            {
                if (Map[r, c] != '.') continue;
                nullPos = r; 
                
                //startPos 찾기
                for (var nr = r - 1; nr >= 0; nr--)
                {
                    if (Map[nr, c] == '.') continue;
    
                    startPos = nr;
                    int endPos = startPos;

                    //endPos 찾기
                    while (endPos >= 0 && Map[endPos, c] != '.')
                    {
                        endPos--;
                    }
                    
                    //nullPos 와 startPos ~ endPos 까지 내려주기
                    for (int i = startPos; i > endPos ; i--)
                    {
                        Map[nullPos, c] = Map[i, c];
                        Map[i, c] = '.';
                        nullPos--;
                    }
                    
                    //내려준 위치까지 이동
                    r = nullPos + 1;
                    nr = endPos;
                }
            }
        }
    }

    private static void DebugPrint()
    {
        for (int r = 0; r < rowSize; r++)
        {
            for (int c = 0; c < columSize; c++)
            {
                Console.Write(Map[r,c]);
            }
            Console.WriteLine();
        }
    }
}