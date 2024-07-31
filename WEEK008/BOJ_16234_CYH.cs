public class BOJ_16234_CYH
{
    //상 하 좌 우
    private static int[,] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
    private static int days;
    private static int nSize;
    private static int L_Value;
    private static int R_Value;
    private static Nation[,] Map;
    
    public static void Main(string[] args)
    {
        init();
        days = 0;

        while (Simulation())
        {
            days++;
        }
        
        Console.WriteLine(days);
    }

    private static void init()
    {
        int[] inputs = Array.ConvertAll(Console.ReadLine().Split(), int.Parse);
        (nSize, L_Value, R_Value) = (inputs[0], inputs[1], inputs[2]);
        Map = new Nation[nSize, nSize];

        for (int r = 0; r < nSize; r++)
        {
            inputs = Array.ConvertAll(Console.ReadLine().Split(), int.Parse);
            for (int c = 0; c < nSize; c++)
            {
                Map[r, c] = new Nation(r, c, inputs[c]);
            }
        }
    }

    private static bool Simulation()
    {
        var isMoved = false;

        // 초기 국가간 국경선 세팅 및 visited 세팅
        for (var r = 0; r < nSize; r++)
        {
            for (var c = 0; c < nSize; c++)
            {
                Map[r, c].OpenBorderLine();
                Map[r,c].isVisited = false;
            }
        }

        Queue<(int, int)> queue = new Queue<(int, int)>();
        for (var r = 0; r < nSize; r++)
        {
            for (var c = 0; c < nSize; c++)
            {
                if (Map[r, c].isVisited) continue;
                
                queue.Enqueue((r,c));
                if (BFS(queue))
                {
                    isMoved = true;
                }
            }
        }
        
        return isMoved;
    }

    private static bool BFS(Queue<(int, int)> queue)
    {
        bool isMoved = false;
        List<Nation> nationList = new List<Nation>();
        var (row, col) = queue.Peek();
        nationList.Add(Map[row, col]);
        
        Map[row, col].isVisited = true;
        var totalSum = Map[row, col].peopleCnt;
        var cntNations = 1;
        while (queue.Count > 0)
        {
            var (nr, nc) = queue.Dequeue();
            for (int i = 0; i < 4; i++)
            {
                var (nextR, nextC) = (nr + directions[i, 0], nc + directions[i, 1]);
                if (!InRange(nextR, nextC)) continue;
                if(Map[nextR, nextC].isVisited) continue;

                if (Map[nr, nc].isLinked[i])
                {
                    queue.Enqueue((nextR, nextC));
                    nationList.Add(Map[nextR, nextC]);
                    Map[nextR, nextC].isVisited = true;
                    totalSum += Map[nextR, nextC].peopleCnt;
                    cntNations++;
                    isMoved = true;
                }
            }
        }

        if (!isMoved) return false;
        
        int newPeopleCnt = totalSum / cntNations;
        foreach (var nation in nationList)
        {
            nation.peopleCnt = newPeopleCnt;
        }

        return isMoved;
    }

    private static bool InRange(int row, int col)
    {
        return row >= 0 && row < nSize && col >= 0 && col < nSize;
    }

    private static bool InValue(int cnt1, int cnt2)
    {
        var sumResult = Math.Abs(cnt1 - cnt2);
        return sumResult >= L_Value && sumResult <= R_Value;
    }

    private class Nation
    {
        public int row;
        public int colum;
        public int peopleCnt;
        public bool isVisited;
        public bool[] isLinked = new bool[4];

        public Nation(int row, int col, int cnt)
        {
            this.row = row;
            this.colum = col;
            peopleCnt = cnt;
            CloseBorderLine();
            isVisited = false;
        }

        public void OpenBorderLine()
        {
            // 상하좌우 순서
            for (int i = 0; i < 4; i++)
            {
                var (nr, nc) = (row + directions[i,0], colum + directions[i,1]);
                
                isLinked[i] = InRange(nr, nc) && InValue(Map[row, colum].peopleCnt, Map[nr, nc].peopleCnt);
            }
        }
        
        public void CloseBorderLine()
        {
            for (var i = 0; i < isLinked.Length; i++)
            {
                isLinked[i] = false;
            }
        }
    }
}