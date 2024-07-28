using System;
using System.Collections.Generic;
using System.Linq;

public class Program
{
    public static Node[,] Land;
    public static int N, L, R;
    public static readonly Dictionary<Vector, (int, int)> VectorDic = new();
    public int Answer;
    
    public enum Vector
    {
        Left,
        Up,
        Right,
        Down
    }
    public class Node
    {
        public int x;
        public int y;
        public int People;
        public bool Visit;
        public List<Vector> Vectors;
        
        public Node(int x, int y, int people)
        {
            this.x = x;
            this.y = y;
            People = people;
            Vectors = new List<Vector>();
        }
    }

    public bool IsBetween(int x)
    {
        return L <= x && x <= R;
    }
    public bool IsOver(int x,int y)
    {
        return x < 0 || y < 0 || x >= N || y >= N;
    }

    public List<List<Node>> GetUnion()
    {
        List<List<Node>> union = new List<List<Node>>();
        
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if (!Land[i, j].Visit)
                {
                    List<Node> nodes = new List<Node>();
                    nodes.Add(Land[i,j]);
                    
                    Queue<Node> BFSqueue = new Queue<Node>();
                    Land[i, j].Visit = true;
                    BFSqueue.Enqueue(Land[i, j]);

                    while (BFSqueue.Count > 0)
                    {
                        Node node = BFSqueue.Dequeue();

                        for (int k = 0; k < node.Vectors.Count; k++)
                        {
                            int newX = node.x + VectorDic[node.Vectors[k]].Item1;
                            int newY = node.y + VectorDic[node.Vectors[k]].Item2;
                            if (!Land[newX, newY].Visit)
                            {
                                Land[newX, newY].Visit = true;
                                BFSqueue.Enqueue(Land[newX, newY]);
                                nodes.Add(Land[newX, newY]);
                            }
                        }
                    }

                    if (nodes.Count > 1)
                    {
                        union.Add(nodes);
                    }
                }
            }
        }

        return union;
    }
    public void SetVector()
    {
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                Land[i, j].Visit = false;
                Land[i, j].Vectors.Clear();
                for (int k = 0; k < 4; k++)
                {
                    Vector vector = (Vector)k;
                    int newX = Land[i, j].x + VectorDic[vector].Item1;
                    int newY = Land[i, j].y + VectorDic[vector].Item2;
                    if (!IsOver(newX, newY) && IsBetween(Math.Abs(Land[newX, newY].People-Land[i,j].People)))
                    {
                        Land[i, j].Vectors.Add(vector);
                    }
                }
            }
        }
    }
    
    public void Initialize()
    {
        VectorDic.Add(Vector.Left,(0,-1));
        VectorDic.Add(Vector.Up,(-1,0));
        VectorDic.Add(Vector.Down,(1,0));
        VectorDic.Add(Vector.Right,(0,1));
        
        int[] mainCommand = Array.ConvertAll(Console.ReadLine().Split(), int.Parse);

        N = mainCommand[0];
        L = mainCommand[1];
        R = mainCommand[2];
        Land = new Node[N, N];
        
        for (int i = 0; i < N; i++)
        {
            int[] people = Array.ConvertAll(Console.ReadLine().Split(), int.Parse);
            for (int j = 0; j < N; j++)
            {
                Land[i, j] = new Node(i, j, people[j]);
            }
        }
    }

    public void Print()
    {
        Console.WriteLine(Answer);
    }
    public void Simulation()
    {
        SetVector();
        List<List<Node>> union = GetUnion();
        
        if (union.Count>0)
        {
            Answer++;
            MovePeople(union);
            Simulation();
        }
        else
        {
            Print();
        }
    }

    public void MovePeople(List<List<Node>> union)
    {
        foreach (var nodes in union)
        {
            int total=0;
            
            foreach (var node in nodes)
            {
                total += node.People;
            }

            int average = total / nodes.Count;
            foreach (var node in nodes)
            {
                node.People = average;
            }
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
