namespace BAEJOON_0706;

public class BOJ_1091_CYH
{
    private static int rerollCnt;
    private static byte[] rollRule;
    private static Card[] cards;
    private static byte n;
    
    public static void Main(string[] args)
    {
        Init();

        rerollCnt = 0;

        while (!Simulation())
        {
            rerollCnt++;
            
            if (CheckCardInitState())
            {
                rerollCnt = -1;
                break;
            }
        }

        Console.WriteLine(rerollCnt);
    }

    private static void Init()
    {
        var inputs = Array.ConvertAll(Console.ReadLine().Split(), byte.Parse);
        n = inputs[0];

        rollRule = new byte[n];
        cards = new Card[n];
        //P수열 입력
        var P_inputs = Array.ConvertAll(Console.ReadLine().Split(), byte.Parse);
        //S수열 입력
        var S_inputs = Array.ConvertAll(Console.ReadLine().Split(), byte.Parse);
        
        for (var i = 0; i < n; i++)
        {
            var card = new Card(i, P_inputs[i]);
            cards[i] = card;
            rollRule[i] = S_inputs[i];
        }
    }

    private static bool Simulation()
    {
        var isTargeting = false;

        if (CheckCardList()) return true;
        
        //배열에 담겨있는 카드들을 S수열을 통해 섞어준다.
        Card[] newCardGroup = new Card[n];
        for(var i = 0; i < n ; i ++)
        {
            // i 번째 카드를 S[i] 번째 위치로 이동
            newCardGroup[rollRule[i]] = cards[i];
            cards[i].myOrder = rollRule[i];
        }
	
        cards  = newCardGroup ;
        
        return isTargeting;
    }
    
    private static bool CheckCardList()
    {
        bool isAllOk = true;
        foreach (var card in cards)
        {
            if (card.CheckMyPlayer()) continue;
            
            isAllOk = false;
            break;
        }
        return isAllOk;
    }

    private static bool CheckCardInitState()
    {
        bool isAllOk = true;
        foreach (var card in cards)
        {
            if (card.CheckMyInitNum()) continue;
            
            isAllOk = false;
            break;
        }
        return isAllOk;
    }

    private class Card
    {
        public int myOrder;
        public int initOrder;
        public int goalPlayer;

        public Card(int order, int goalPlayer)
        {
            myOrder = order;
            initOrder = order;
            this.goalPlayer = goalPlayer;
        }

        public bool CheckMyPlayer()
        {
            return myOrder%3 == goalPlayer;
        }

        public bool CheckMyInitNum()
        {
            return initOrder == myOrder;
        }
    }
}