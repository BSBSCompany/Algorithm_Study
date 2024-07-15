using System;
using System.Collections.Generic;
using System.Linq;

public class Program
{
    public static int R,C,N;
    public static Bomb[,] Map;
    public static int[] dxs = new int[]{0, 1, 0, -1};
    public static int[] dys = new int[] { -1, 0, 1, 0 };

    public class Bomb
    {
        public int x;
        public int y;
        public int duration;
        public bool isExplode;
        
        public Bomb(int x, int y)
        {
            this.x = x;
            this.y = y;
            this.duration = 0;
        }

        public void Tick()
        {
            duration++;

            if (duration >= 3)
            {
                this.isExplode = true;

                for (int i = 0; i < 4; i++)
                {
                    int newX = x + dxs[i];
                    int newY = y + dys[i];
                    if (newX < R && newX >= 0 && newY < C && newY >= 0)
                    {
                        Map[newX, newY].isExplode = true;
                    }
                }
            }
        }
    }
    //가장 처음에 봄버맨은 일부 칸에 폭탄을 설치해 놓는다. 모든 폭탄이 설치된 시간은 같다.
    public void Initialize()
    {
        int[] command = Array.ConvertAll(Console.ReadLine().Split(), int.Parse);
        R = command[0];
        C = command[1];
        N = command[2];
        Map = new Bomb[R, C];

        for (int i = 0; i < R; i++)
        {
            string line = Console.ReadLine();
            for (int j = 0; j < C; j++)
            {
                switch (line[j])
                {
                    case '.': Map[i, j] = null; break;
                    case 'O': Map[i, j] = new Bomb(i, j); break;
                }
            }
        }
    }
    

    public void PutBomb()
    {
        for (int i = 0; i < R; i++)
        {
            for (int j = 0; j < C; j++)
            {
                if (Map[i, j] == null)
                {
                    Map[i, j] = new Bomb(i, j);
                }
            }
        }
    }

    public void TickBomb(bool dontExplode)
    {
        for (int i = 0; i < R; i++)
        {
            for (int j = 0; j < C; j++)
            {
                if (Map[i, j] != null)
                {
                    Map[i,j].Tick();
                }
            }
        }

        if (dontExplode)
        {
            return;
        }
        for (int i = 0; i < R; i++)
        {
            for (int j = 0; j < C; j++)
            {
                if (Map[i, j] != null && Map[i,j].isExplode)
                {
                    Map[i, j] = null;
                }
            }
        }
    }
    

    public void Simulation()
    {
        for (int i = 0; i < N; i++)
        {
            if (i == 0)
            {
                //다음 1초 동안 봄버맨은 아무것도 하지 않는다.
                TickBomb(false);
            }
            else if (i % 2 == 1)
            {
                //다음 1초 동안 폭탄이 설치되어 있지 않은 모든 칸에 폭탄을 설치한다.
                //즉, 모든 칸은 폭탄을 가지고 있게 된다. 폭탄은 모두 동시에 설치했다고 가정한다.
                PutBomb();
                TickBomb(true);
            }
            else
            {
                //1초가 지난 후에 3초 전에 설치된 폭탄이 모두 폭발한다.
                TickBomb(false);
            }
        }
    }

    public void Print()
    {
        string answer = string.Empty;
        for (int i = 0; i < R; i++)
        {
            for (int j = 0; j < C; j++)
            {
                answer += Map[i, j] == null ? "." : "O";
            }

            answer += "\n";
        }
        Console.WriteLine(answer);
    }
    public void Solution()
    {
        Initialize();
        Simulation();
        Print();
    }

    public static void Main()
    {
        new Program().Solution();
    }
}
