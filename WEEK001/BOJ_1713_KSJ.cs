using System;
using System.Collections.Generic;

public class Program
{
    private static Frame[] Frames;
    public static void Main(string[] args)
    {
        int N = int.Parse(Console.ReadLine() ?? string.Empty);
        Frames = new Frame[N];
        int recommandCnt = int.Parse(Console.ReadLine());

        int[] cmd = Array.ConvertAll(Console.ReadLine().Split(), int.Parse);
        for (int i = 0; i < recommandCnt; i++)
        {
            Recommand(i,cmd[i]);
        }

        List<Frame> answer = new List<Frame>();
        for (int i = 0; i < Frames.Length; i++)
        {
            if (Frames[i] != null)
            {
                answer.Add(Frames[i]);
            }
        }
        answer.Sort((x,y) =>
        {
            if (x.student < y.student)
            {
                return -1;
            }

            return 1;
        });

        string result = "";
        foreach (var s in answer)
        {
            result += $"{s.student} ";
        }
        Console.WriteLine(result);
    }

    public static void Recommand(int time,int student)
    {
        int con = Contains(student);
        
        if (con!=-1)
        {
            Frames[con].recommend++;
        }
        else
        {
            int index = GetFrameIndex();
            Frames[index] = new Frame(index, student, 0, time);   
        }
    }

    public static int Contains(int student)
    {
        for (int i = 0; i < Frames.Length; i++)
        {
            if (Frames[i] != null && Frames[i].student==student)
            {
                return i;
            }
        }

        return -1;
    }
    public static int GetFrameIndex()
    {
        //1.만약 빈 자리가 있다면 return
        for (int i = 0; i < Frames.Length; i++)
        {
            if (Frames[i] == null)
            {
                return i;
            }
        }
        
        //2.가장 적은 recommand 찾기
        int min=Frames[0].recommend;
        for (int i = 0; i < Frames.Length; i++)
        {
            if (Frames[i].recommend < min)
            {
                min = Frames[i].recommend;
            }
        }

        List<Frame> minFrames = new List<Frame>();
        for (int i = 0; i < Frames.Length; i++)
        {
            if (Frames[i].recommend == min)
            {
                minFrames.Add(Frames[i]);
            }
        }
        minFrames.Sort((x, y) =>
        {
            if (x.time < y.time)
            {
                return -1;
            }

            return 1;
        });

        return minFrames[0].index;
    }

    public class Frame
    {
        public int index;
        public int student;
        public int recommend;
        public int time;

        public Frame(int index, int student, int recommend, int time)
        {
            this.index = index;
            this.student = student;
            this.recommend = recommend;
            this.time = time;
        }
    }
}
