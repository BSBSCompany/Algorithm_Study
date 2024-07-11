using System;
using System.Collections.Generic;

public class Program
{
    public const int SIZE = 6;
    public const int SEARCHTARGET = 8;

    public static Node[,] GameBoard = new Node[SIZE, SIZE];
    public Stone FirstPlayer;

    private static int[] dx = { 0, 1, 1, 1, 0, -1, -1, -1 };
    private static int[] dy = { -1, -1, 0, 1, 1, 1, 0, -1 };

    public class Node
    {
        public Stone Stone;
        public Node()
        {
            Stone = Stone.None;
        }

        public List<(int x, int y)> Search(int vector, int currX, int currY, Stone myStone, List<(int x, int y)> list)
        {
            currX += dx[vector];
            currY += dy[vector];

            if (currX < 0 || currX >= SIZE || currY < 0 || currY >= SIZE)
                return null;

            if (GameBoard[currX, currY].Stone == Stone.None)
                return null;

            if (GameBoard[currX, currY].Stone == myStone)
                return list.Count > 0 ? list : null;

            list.Add((currX, currY));
            return GameBoard[currX, currY].Search(vector, currX, currY, myStone, list);
        }
    }

    public enum Stone
    {
        None,
        Black,
        White
    }

    /// <summary>
    /// 팔방탐색 실시!!
    /// </summary>
    public void Search(int x, int y, Stone stone)
    {
        for (int i = 0; i < SEARCHTARGET; i++)
        {
            List<(int x, int y)> catchList = GameBoard[x, y].Search(i, x, y, stone, new List<(int x, int y)>());

            if (catchList != null)
            {
                foreach (var node in catchList)
                {
                    PutStone(node.x, node.y, stone);
                }
            }
        }
    }

    /// <summary>
    /// 돌을 놓는다.
    /// </summary>
    public void PutStone(int x, int y, Stone stone)
    {
        GameBoard[x, y].Stone = stone;
    }

    /// <summary>
    /// 초기 게임판의 형태는 항상 (3,3), (4,4)에 백돌 두 개가,
    /// (3,4), (4,3)에 흑돌 두 개가 올려져 있는 상태이며,
    /// 흑돌이 선을 잡는다.
    /// </summary>
    public void Initialize()
    {
        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j < SIZE; j++)
            {
                GameBoard[i, j] = new Node();
            }
        }

        GameBoard[2, 2].Stone = Stone.White;
        GameBoard[3, 3].Stone = Stone.White;
        GameBoard[2, 3].Stone = Stone.Black;
        GameBoard[3, 2].Stone = Stone.Black;
        FirstPlayer = Stone.Black;
    }

    public void PrintResult()
    {
        string result = string.Empty;
        int white = 0, black = 0;
        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j < SIZE; j++)
            {
                switch (GameBoard[i, j].Stone)
                {
                    case Stone.None:
                        result += ".";
                        break;
                    case Stone.Black:
                        result += "B";
                        black++;
                        break;
                    case Stone.White:
                        result += "W";
                        white++;
                        break;
                }
            }
            result += "\n";
        }
        result += white > black ? "White" : "Black";
        Console.WriteLine(result);
    }

    public void Solution()
    {
        Initialize();

        int cmdCount = int.Parse(Console.ReadLine() ?? string.Empty);
        for (int i = 0; i < cmdCount; i++)
        {
            int[] cmd = Array.ConvertAll(Console.ReadLine().Split(), int.Parse);
            PutStone(cmd[0] - 1, cmd[1] - 1, FirstPlayer);
            Search(cmd[0] - 1, cmd[1] - 1, FirstPlayer);
            FirstPlayer = FirstPlayer == Stone.Black ? Stone.White : Stone.Black;
        }

        PrintResult();
    }

    public static void Main()
    {
        new Program().Solution();
    }
}
