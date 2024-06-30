using System;
using System.Collections.Generic;

public class Program
{
    public const int BingoSize = 5;
    static Node[,] Bingo = new Node[BingoSize, BingoSize];

    
    public static void Main(string[] args)
    {
        for (byte i = 0; i < BingoSize; i++)
        {
            byte[] numbers = Array.ConvertAll(Console.ReadLine().Split(), byte.Parse);
            for (byte j = 0; j < BingoSize; j++)
            {
                Bingo[i, j] = new Node(i, j, numbers[j], false);
            }
        }

        int answer = 0;
        
        for (byte i = 0; i < BingoSize; i++)
        {
            byte[] numbers = Array.ConvertAll(Console.ReadLine().Split(), byte.Parse);
            for (byte j = 0; j < BingoSize; j++)
            {
                answer++;
                EraseBingo(numbers[j]);
                if (CheckBingo())
                {
                    Console.WriteLine(answer);
                    return;
                };
            }
        }
    }


    public static bool CheckBingo()
    {
        byte currentCount = 0;
        
        for (int i = 0; i < BingoSize; i++)
        {
            if (Bingo[i, 0].IsHorizontalBingo())
            {
                currentCount++;
            }
        }
        for (int i = 0; i < BingoSize; i++)
        {
            if (Bingo[0, i].IsVerticalBIngo())
            {
                currentCount++;
            }
        }

        if (Bingo[0, 0].IsDiagonalRightBIngo())
        {
            currentCount++;
        }
        if (Bingo[0, BingoSize-1].IsDiagonalLeftBIngo())
        {
            currentCount++;
        }

        if (currentCount >= 3)
        {
            return true;
        }

        return false;
    }
    public static void EraseBingo(byte number)
    {
        for (byte i = 0; i < BingoSize; i++)
        {
            for (byte j = 0; j < BingoSize; j++)
            {
                if (Bingo[i, j].number == number)
                {
                    Bingo[i, j].check = true;
                }
            }
        }
    }

    public class Node
    {
        public byte x;
        public byte y;
        public byte number;
        public bool check;

        public Node(byte x, byte y, byte number, bool check)
        {
            this.x = x;
            this.y = y;
            this.number = number;
            this.check = check;
        }
        
        public bool IsHorizontalBingo()
        {
            if (y + 1 == BingoSize)
            {
                return true;
            }
            
            if(Bingo[x,y+1].check)
            {
                return Bingo[x,y+1].IsHorizontalBingo();
            }

            return false;
        }
        public bool IsVerticalBIngo()
        {
            if (x + 1 == BingoSize)
            {
                return true;
            }
            
            if(Bingo[x+1,y].check)
            {
                return Bingo[x+1,y].IsVerticalBIngo();
            }

            return false;
        }
        public bool IsDiagonalRightBIngo()
        {
            
            if (x + 1 == BingoSize && y+1==BingoSize)
            {
                return true;
            }
            
            if(Bingo[x+1,y+1].check)
            {
                return Bingo[x+1,y+1].IsDiagonalRightBIngo();
            }

            return false;
        }
        public bool IsDiagonalLeftBIngo()
        {
            if (x + 1 == BingoSize && y-1==-1)
            {
                return true;
            }

            if(Bingo[x+1,y-1].check)
            {
                return Bingo[x+1,y-1].IsDiagonalLeftBIngo();
            }

            return false;
        }
    }
}
