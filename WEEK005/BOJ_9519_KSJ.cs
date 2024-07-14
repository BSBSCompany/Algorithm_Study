using System;
using System.Collections.Generic;
using System.Linq;

public class Program
{
    public int X;
    public const int MagicNumber=1000000000;
    public int forNumber = 0;

    public string Reverse(string word)
    {
        List<char> words = word.ToList();
        Stack<char> backWords = new Stack<char>();

        for (int i = 1; i < words.Count; i+=2)
        {
            backWords.Push(words[i]);
            words[i] = (char)0;
        }

        string newWord = string.Empty;
        foreach (var w in words)
        {
            if (w != 0)
            {
                newWord += w;
            }
        }

        while (backWords.Count > 0)
        {
            newWord += backWords.Pop();
        }

        return newWord;
    }
    public void Initialize()
    {
        X = int.Parse(Console.ReadLine() ?? string.Empty);
    }
    public void Solution()
    {
        Initialize();
        string command = Console.ReadLine();
        string origin = command;
        for (int i = 1; i < MagicNumber; i++)
        {
            command = Reverse(command);
            if (command.Equals(origin))
            {
                forNumber = i;
                break;
            }
        }

        int newX = X % forNumber;
        string answer = origin;
        for (int i = 0; i < newX; i++)
        {
            answer = Reverse(answer);
        }
        Console.WriteLine(answer);
    }

    public static void Main()
    {
        new Program().Solution();
    }
}
