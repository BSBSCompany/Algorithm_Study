using System;
using System.Collections.Generic;
using System.Linq;

public class Program
{
    public const int IMAGECOUNT = 9;
    public static List<Image> Images = new();
    public static List<Hap> HapList = new();
    public static int Score=0;
    public static bool isGetGeurl = false;
    
    public class Hap
    {
        public int x;
        public int y;
        public int z;

        public Hap(int x, int y, int z)
        {
            List<int> tmp = new List<int> { x, y, z };
            tmp.Sort();
            this.x = tmp[0];
            this.y = tmp[1];
            this.z = tmp[2];
        }
    }
    public class Image
    {
        public Shape Shape;
        public Color Color;
        public Background Background;

        public Image(string shape, string color, string background)
        {
            switch (shape)
            {
                case "CIRCLE": Shape = Shape.CIRCLE; break;
                case "TRIANGLE": Shape = Shape.TRIANGLE; break;
                case "SQUARE": Shape = Shape.SQUARE; break;
            }
            switch (color)
            {
                case "YELLOW": Color = Color.YELLOW; break;
                case "RED": Color = Color.RED; break;
                case "BLUE": Color = Color.BLUE; break;
            }
            switch (background)
            {
                case "GRAY": Background = Background.GRAY; break;
                case "WHITE": Background = Background.WHITE; break;
                case "BLACK": Background = Background.BLACK; break;
            }
        }
    }

    public bool IsGeurl()
    {
        return HapList.Count == 0 && !isGetGeurl;
    }
    public bool IsHap(Hap hapData)
    {
        Hap hap = HapList.Find(data => { return data.x == hapData.x && data.y == hapData.y && data.z == hapData.z; });
        if (hap != null)
        {
            HapList.Remove(hap);
            return true;
        }

        return false;
    }

    public void Simulation()
    {
        int cmdCount = int.Parse(Console.ReadLine());

        for (int i = 0; i < cmdCount; i++)
        {
            string[] command = Console.ReadLine().Split();
            
            if (command[0].Equals("H"))
            {
                if (IsHap(new Hap(int.Parse(command[1])-1,int.Parse(command[2])-1,int.Parse(command[3])-1)))
                {
                    UpdateScore(1);
                }
                else
                {
                    UpdateScore(-1);
                }
            }
            else
            {
                if (IsGeurl())
                {
                    isGetGeurl = true;
                    UpdateScore(3);
                }
                else
                {
                    UpdateScore(-1);
                }
            }
        }
    }
    
    bool[] visited = new bool[IMAGECOUNT];
    int[] result = new int[IMAGECOUNT];
    public void CreateHapList(int count)
    {
        if (count == 3)
        {
            Hap hap = new Hap(result[0], result[1], result[2]);
            if (diffHap(hap))
            {
                if (HapList.Find(data => { return data.x == hap.x && data.y == hap.y && data.z == hap.z; }) == null)
                {
                    HapList.Add(hap);
                }
            }
        }
        else
        {
            for (int i = 0; i < IMAGECOUNT; i++)
            {
                if (!visited[i])
                {
                    visited[i] = true;
                    result[count] = i;
                    CreateHapList(count + 1);
                    visited[i] = false;
                }
            }
        }

        bool diffHap(Hap hap)
        {
            bool shape = isDiff((int)Images[hap.x].Shape, (int)Images[hap.y].Shape, (int)Images[hap.z].Shape);
            bool color = isDiff((int)Images[hap.x].Color, (int)Images[hap.y].Color, (int)Images[hap.z].Color);
            bool background = isDiff((int)Images[hap.x].Background, (int)Images[hap.y].Background, (int)Images[hap.z].Background);

            return shape && color && background;
            
            bool isDiff(int property1,int property2,int property3)
            {
                if (property1 == property2 && property2 == property3)
                {
                    return true;
                }
                else if(property1 != property2 && property2 != property3 && property1 != property3)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
    }

    public void UpdateScore(int score)
    {
        Score += score;
    }
    public void Initialize()
    {
        for (int i = 0; i < IMAGECOUNT; i++)
        {
            string[] command = Console.ReadLine().Split();
            Images.Add(new Image(command[0],command[1],command[2]));
        }

        CreateHapList(0);
    }

    public void Solution()
    {
        Initialize();
        Simulation();
        Print();
    }

    public void Print()
    {
        Console.Write(Score);
    }

    public static void Main()
    {
        new Program().Solution();
    }
    public enum Shape
    {
        CIRCLE,
        TRIANGLE,
        SQUARE
    }

    public enum Background
    {
        GRAY,
        WHITE,
        BLACK
    }
    
    public enum Color
    {
        YELLOW,
        RED,
        BLUE
    }
}
