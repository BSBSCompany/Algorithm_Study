namespace codingTest;

public class BOJ_16722_CYH
{
    //열거형 선언
    private enum Shape { CIRCLE, TRIANGLE, SQUARE };
    private enum SColor { YELLOW, RED, BLUE };
    private enum BColor { GRAY, WHITE, BLACK };
    
    //결!합! 게임의 조합 리스트
    private const int nodeCnt = 10;
    private static Node[] nodeList;
    private static int gameScore;  // 게임 점수
    private static int gameSetCnt; // 게임 세트 수
    private static string[] playList; // 게임 시뮬레이션 입력 리스트
    private static Dictionary<(int, int, int), bool> hapDict;
    private static bool isGet3Score;
    
    public static void Main(string[] args)
    {
        Init();
        
        FindAllHapList(0, 1, new []{0, 0, 0});
        
        foreach (var command in playList)
        {
            Simulation(command.Split());
        }
        
        Console.WriteLine(gameScore);
    }

    private static void Init()
    {
        // 결!합! 조합 리스트 입력 받기
        nodeList = new Node[nodeCnt];
        for (int i = 1; i < nodeCnt; i++)
        {
            var inputs = Console.ReadLine().Split();
            Shape shape = GetShape(inputs[0]);
            SColor scolor = GetsColor(inputs[1]);
            BColor bColor = GetbColor(inputs[2]);
            nodeList[i] = new Node(shape, scolor, bColor);
        }
        
        // 세트 수 입력 받기
        var gameSetInput = Array.ConvertAll(Console.ReadLine().Split(), int.Parse);
        gameSetCnt = gameSetInput[0];
        playList = new string[gameSetCnt];
        // 게임 시뮬레이션 리스트 입력 
        for (int i = 0; i < gameSetCnt; i++)
        {
            playList[i] = Console.ReadLine();
        }
        
        // 조합들을 저장할 hashMap 초기화
        hapDict = new Dictionary<(int, int, int), bool>();
        isGet3Score = false;
    }

    // "합!"인 리스트를 찾아서 세팅해준다.
    private static void FindAllHapList(int cnt, int curOrder ,int[] nums)
    {
        if(curOrder > nodeCnt) return;

        if (cnt == 3)
        {
            if(IsHap(nums))
                hapDict.Add((nums[0], nums[1], nums[2]), false);
		
            return;
        }
	
        nums[cnt] = curOrder;
        FindAllHapList(cnt+1, curOrder+1 , nums);
        nums[cnt] = 0;
        FindAllHapList(cnt, curOrder+1 , nums);
    }
    
    private static bool IsHap(int[] nums)
    {
        var (idx1, idx2, idx3) = (nums[0], nums[1], nums[2]);
        bool shapeTf = (nodeList[idx1].shape != nodeList[idx2].shape && nodeList[idx1].shape != nodeList[idx3].shape && nodeList[idx2].shape != nodeList[idx3].shape)
                       || (nodeList[idx1].shape == nodeList[idx2].shape && nodeList[idx1].shape == nodeList[idx3].shape && nodeList[idx2].shape == nodeList[idx3].shape);

        bool sColorTf = (nodeList[idx1].scolor != nodeList[idx2].scolor && nodeList[idx1].scolor != nodeList[idx3].scolor && nodeList[idx2].scolor != nodeList[idx3].scolor)
                        || (nodeList[idx1].scolor== nodeList[idx2].scolor&& nodeList[idx1].scolor == nodeList[idx3].scolor&& nodeList[idx2].scolor == nodeList[idx3].scolor);

        bool bColorTf = (nodeList[idx1].bcolor != nodeList[idx2].bcolor && nodeList[idx1].bcolor != nodeList[idx3].bcolor && nodeList[idx2].bcolor != nodeList[idx3].bcolor)
                        || (nodeList[idx1].bcolor == nodeList[idx2].bcolor && nodeList[idx1].bcolor == nodeList[idx3].bcolor && nodeList[idx2].bcolor == nodeList[idx3].bcolor);

        return shapeTf && sColorTf && bColorTf;
    }

    private static void Simulation(string[] commands)
    {
        if (commands[0] == "H")
        {
            int[] combList = Array.ConvertAll(new[] { commands[1], commands[2], commands[3] }, int.Parse);
            Array.Sort(combList);

            bool existKey = hapDict.TryGetValue((combList[0], combList[1], combList[2]), out bool isUsed);
            if (!existKey || isUsed)
            {
                SetHapScore(false);
                return;
            }

            SetHapScore(true);
            hapDict[(combList[0], combList[1], combList[2])] = true;
        }
        else // commands[0] == "G"
        {
            bool isAllHap = true;
            foreach (var isUsed in hapDict.Values)
            {
                if (isUsed) continue;

                isAllHap = false;
                break;
            }

            SetGeolScore(isAllHap);
        }
    }

    private static void SetHapScore(bool isGood)
    {
        gameScore = isGood ? gameScore + 1 : gameScore-1;
    }
    
    private static void SetGeolScore(bool isGood)
    {
        if (isGet3Score || !isGood)
        {
            gameScore -= 1;
            return;
        }

        gameScore += 3;
        isGet3Score = true;
    }
    
    private static Shape GetShape(string input)
    {
        return input switch
        {
            "CIRCLE" => Shape.CIRCLE,
            "TRIANGLE" => Shape.TRIANGLE,
            _ => Shape.SQUARE
        };
    }
    
    private static SColor GetsColor(string input)
    {
        return input switch
        {
            "YELLOW" => SColor.YELLOW,
            "RED" => SColor.RED,
            _ => SColor.BLUE
        };
    }
    
    private static BColor GetbColor(string input)
    {
        return input switch
        {
            "GRAY" => BColor.GRAY,
            "BLACK" => BColor.BLACK,
            _ => BColor.WHITE
        };
    }
    
    private static void DebugLog()
    {
        foreach (var key in hapDict.Keys)
        {
            Console.WriteLine($"{key}, {hapDict[key]}");
        }
    }
    
    private class Node
    {
        public Shape shape;
        public SColor scolor;
        public BColor bcolor;

        public Node(Shape shape, SColor scolor, BColor bcolor)
        {
            this.shape = shape;
            this.scolor = scolor;
            this.bcolor = bcolor;
        }
    }
}