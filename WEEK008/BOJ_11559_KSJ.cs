using System;
using System.Collections.Generic;
using System.Linq;

public class Program
{
    public const int Sizex = 12;
    public const int Sizey = 6;
    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = {0, 1, 0, -1}; 
    public static Node[,] Field = new Node[Sizex, Sizey];
    public int Answer=0;
    
    
    public class Vector2
    {
        public int x;
        public int y;

        public Vector2(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    }
    public enum Color
    {
        None, //없음
        Red, //빨강
        Green, //초록
        Blue, //파랑
        Purple, //보라
        Yellow //노랑
    }
    
    public class Node
    {
        public int x;
        public int y;
        public Color Color;
        public bool Visit;
	
        public void Gravity()
        {
            Color originColor = Color;
            if (Color == Color.None)
            {
                return;
            }
            
            int originX = x;
            int newX = x;
            while (true)
            {
                newX++;
                
                if (newX >= Sizex || Field[newX, y].Color != Color.None)
                {
                    break;
                }
            }

            Field[originX, y].Color = Color.None;
            Field[newX-1, y].Color = originColor;
        }

        public Node(int x, int y, Color color)
        {
            this.x = x;
            this.y = y;
            Color = color;
        }
    }


    public bool PuyoPuyo()
    {
        for (int i = 0; i < Sizex; i++)
        {
            for (int j = 0; j < Sizey; j++)
            {
                Field[i, j].Visit = false;
            }
        }


        bool isPuyo=false;
        
        for (int i = 0; i < Sizex; i++)
        {
            for (int j = 0; j < Sizey; j++)
            {
                if (!Field[i, j].Visit && Field[i, j].Color != Color.None)
                {
                    List<Node> nodes = new List<Node>();
                    nodes.Add(Field[i,j]);
                    
                    Color originColor = Field[i, j].Color;
                    
                    Queue<Vector2> q = new Queue<Vector2>();
                    Field[i, j].Visit = true;
                    q.Enqueue(new Vector2(i,j));

                    while (q.Count>0)
                    {
                        Vector2 point = q.Dequeue();    
                        for (int k = 0; k < 4; k++)
                        {
                            int newX= point.x + dx[k];
                            int newY=point.y + dy[k];
                            if (newX < 0 ||newX >= Sizex || newY < 0 || newY >= Sizey)
                            {
                                continue;
                            }

                            if (!Field[newX,newY].Visit && Field[newX, newY].Color == originColor)
                            {
                                Field[newX, newY].Visit = true;
                                q.Enqueue(new Vector2(newX,newY));
                                nodes.Add(Field[newX,newY]);
                            }
                        }
                    }

                    if (nodes.Count >= 4)
                    {
                        isPuyo = true;
                        foreach (var node in nodes)
                        {
                            node.Color = Color.None;
                        }
                    }
                }


            }
        }

        return isPuyo;
    }

    public void Gravity()
    {
        for (int i = Sizex-1; i >=0; i--)
        {
            for (int j = Sizey-1; j >=0; j--)
            {
                Field[i,j].Gravity();
            }
        }
    }
    
    public void Initialize()
    {
        for (int i = 0; i < Sizex; i++)
        {
            string cmd = Console.ReadLine();
            for (int j = 0; j < Sizey; j++)
            {
                switch (cmd[j])
                {
                    case '.': Field[i, j] = new Node(i,j,Color.None); break;
                    case 'R': Field[i, j] = new Node(i,j,Color.Red); break;
                    case 'G': Field[i, j] = new Node(i,j,Color.Green); break;
                    case 'B': Field[i, j] = new Node(i,j,Color.Blue); break;
                    case 'P': Field[i, j] = new Node(i,j,Color.Purple); break;
                    case 'Y': Field[i, j] = new Node(i,j,Color.Yellow); break;
                }
            }
        }
    }

    public void Print()
    {
        Console.WriteLine(Answer);
    }
    public void Simulation()
    {
        bool isPuyo = PuyoPuyo();
        
        if (isPuyo)
        {
            Answer++;
            Gravity();
            Simulation();
        }
        else
        {
            Print();
        }
    }

    public void Solution()
    {
        Initialize();
        Simulation();
    }

    public static void Main()
    {
        new Program().Solution();
    }
}
