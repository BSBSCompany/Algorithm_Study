public class BJO_17144_CYH
{
    private static int[,] room;
    private static int[,] tempRoom;
    private static int worldTime;
    private static int rowCnt;
    private static int columCnt;
    private static AirCondition _airCondition;

    public static void Main(string[] args)
    {
        
        Init();

        for (int t = 0; t < worldTime; t++)
        {
            Simulation();
        }

        var ans = 0;
        foreach (var item in room)
        {
            ans += item;
        }
        
        Console.WriteLine(ans);
    }

    private static void Init()
    {
        // 행, 열, 시간 입력 받기
        int[] inputs = Array.ConvertAll(Console.ReadLine().Split(), int.Parse);
        (rowCnt, columCnt, worldTime) = (inputs[0], inputs[1], inputs[2]);
        
        //미세먼지 방 입력 받기 및 예비 룸 초기화
        room = new int[rowCnt + 1, columCnt + 1];
        for (int r = 1; r < rowCnt + 1; r++)
        {
            inputs = Array.ConvertAll(Console.ReadLine().Split(), int.Parse);
            
            for (int c = 0; c < columCnt; c++)
            {
                if (inputs[c] == -1)
                {
                    //공기 청정기 정의하기
                    if(_airCondition == null)
                        _airCondition = new AirCondition(r, c + 1);
                    
                    continue;
                }
                room[r, c + 1] = inputs[c];
            }
        }
        
        tempRoom = new int[rowCnt + 1, columCnt + 1];
    }


    private static void Simulation()
    {
        // 상태 초기화
        for (int r = 1; r <= rowCnt; r++)
        {
            for (int c = 1; c <= columCnt; c++)
            {
                tempRoom[r, c] = 0;
            }
        }

        // r,c 만큼 for문 돌면서 미세먼지 퍼트려 주기
        for (var r = 1; r <= rowCnt; r++)
        {
            for (var c = 1; c <= columCnt; c++)
            {
                if (IsAirCondition(r, c)) continue;
                
                SpreadDust(r, c);
            }
        }
        // 퍼트린 먼지들이랑 원래 먼지들 합쳐주기
        for (var r = 1; r <= rowCnt; r++)
        {
            for (var c = 1; c <= columCnt; c++)
            {
                room[r, c] += tempRoom[r, c];
            }
        }
        
        //공기 청정기 정화 시키기
        purify();
        
    }
    
    private static bool InRange(int r, int c)
    {
        return r >= 1 && r <= rowCnt && c >= 1 && c <= columCnt;
    }

    private static bool IsAirCondition(int r, int c)
    {
        return r >= _airCondition.r && r <= _airCondition.r + 1 && c == _airCondition.c;
    }

    private static void SpreadDust(int r, int c)
    {
        int[] drs = { -1, 0, 1, 0 };
        int[] dcs = { 0, 1, 0, -1 };
        
        // 현재 먼지 값 및 퍼트려야 하는 먼지 값 저장
        var originalValue = room[r, c];
        var spreadValue = originalValue/5;
        // 총 몇군대에 퍼트려야 하는지 갯수 세는 변수
        var cnt = 0;

        if (originalValue < 5) return;  // 5 보다 작은 값이면, 퍼트리지 못한다.
        
        for (int i = 0; i < drs.Length; i++)
        {
            var (nr, nc) = (r + drs[i], c + dcs[i]);
            
            if (!InRange(nr, nc)) continue; // 범위 안에 있지 않거나
            if (IsAirCondition(nr, nc)) continue; //공기 청정기라면 
            // 패스!

            cnt += 1;
            
            //퍼트리는 값 업데이트
            tempRoom[nr, nc] += spreadValue;
        }
        
        room[r, c] -= cnt * spreadValue; // 퍼트린 값 만큼 원래 값 업데이트
    }

    private static void purify()
    {
        //공기 청정기 위치 받아와서
        var (startPoint_R, startPos_C) = (_airCondition.r, _airCondition.c);
        var (curR, curC) = (startPoint_R, startPos_C);
        
        // 상, 우, 하, 좌
        int[] drs = { -1, 0, 1, 0 };
        int[] dcs = { 0, 1, 0, -1 };
        var ro = 0;
        //윗 부분 정화 시키기
        (curR, curC) = (curR + drs[ro], curC + dcs[ro]);
        var (nextR, nextC) = (curR + drs[ro], curC + dcs[ro]);
        while (true)
        {
            room[curR, curC] = room[nextR, nextC]; // 바람 불어서 옮겨 줄 값 땡겨주기
            (curR, curC) = (nextR, nextC); // 다음 위치로 이동

            if (!InRange(curR + drs[ro], curC + dcs[ro]) || curR + drs[ro] == startPoint_R + 1) ro =  (ro + 1)%4; // 방향 틀어서 다음 위치 업데이트
            (nextR, nextC) = (curR + drs[ro], curC + dcs[ro]); //다음 위치 업데이트
            if (IsAirCondition(nextR, nextC))
            {
                room[curR, curC] = 0;
                break;
            }
        } 
        
        //아랫 부분 정화 시키기
        startPoint_R += 1;
        
        // 하, 우, 상, 좌
        drs = new[] { 1, 0, -1, 0 };
        dcs = new[] { 0, 1, 0, -1};
        ro = 0;
        (curR, curC) = (startPoint_R, startPos_C);
        (curR, curC) = (curR + drs[ro], curC + dcs[ro]);
        //아랫 부분 정화 시키기
        (nextR, nextC) = (curR + drs[ro], curC + dcs[ro]);
        while (true)
        {
            room[curR, curC] = room[nextR, nextC]; // 바람 불어서 옮겨 줄 값 땡겨주기
            (curR, curC) = (nextR, nextC); // 다음 위치로 이동

            if (!InRange(curR + drs[ro], curC + dcs[ro]) || curR + drs[ro] == startPoint_R - 1) ro =  (ro + 1)%4; // 방향 틀어서 다음 위치 업데이트
            (nextR, nextC) = (curR + drs[ro], curC + dcs[ro]); //다음 위치 업데이트
            if (IsAirCondition(nextR, nextC))
            {
                room[curR, curC] = 0;
                break;
            }

        } 
    }

    // 디버그를 위한 함수
    /*private static void PrintArray(int[,] array)
    {
        for (int r = 1; r <= rowCnt; r++)
        {
            for (int c = 1; c <= columCnt; c++)
            {
                Console.Write(room[r,c] + " ");
            }
            Console.WriteLine();
        }
        Console.WriteLine();
    }*/
    
    
    private class AirCondition
    {
        public int r;
        public int c;
        
        public AirCondition(int r, int c)
        {
            (this.r, this.c) = (r,c);
        }
    }
}


