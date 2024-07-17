using System.Text;
public class BJO_9519_CYH
{

    private static int blinkCnt;
    private static string originalString;
    private static int halfIdx;
    private static List<string> stringList;
    private static StringBuilder sb;
    
    public static void Main(string[] args)
    {
        //초기화
        Init();
        
        // 규칙성을 찾기
        SetRule();
        
        //시작 글자 찾기.
        FindStartString();
    }

    private static void Init()
    {
        // 눈 깜빡임 횟수 입력
        int[] inputs = Array.ConvertAll(Console.ReadLine().Split(), int.Parse);
        blinkCnt = inputs[0];
        
        //blinkCnt 깜빡인 후 글자
        originalString = Console.ReadLine();
        
        // 바뀌기 시작해야 하는 idx를 저장
        halfIdx = originalString.Length/2;
        stringList = new List<string>();
    }

    private static void SetRule()
    {
        // 제일 처음 타자는 입력 받은 글자로 초기화.
        var nextString = GetNextString(originalString);
        while (!originalString.Equals(nextString))
        {
            //List에 등록
            stringList.Add(nextString);
            
            //다음 글자 업데이트
            nextString = GetNextString(nextString);
        }
        
        //마지막은 본인을 포함해야함. 그래야 계산하기 편함
        stringList.Add(originalString);
    }


    private static string GetNextString(string contents)
    {
        //초기화
        int len = contents.Length;
        sb = new StringBuilder(len);
        
        // 이제 앞뒤로 돌아가면서 sb에게 contents를 합쳐주도록 명령한다.
        //1. 뒤에서 k번째 글자는 앞에서 부터 k번째와 k+1번째 글자 사이로 이
        for (int k = 0; k < halfIdx; k++)
        {
            int index = len - (k + 1);
            var( s, e) = (contents[k], contents[index]);
            sb.Append(s);
            sb.Append(e);
        }
        // 홀수는 가운데 한개를 더 추가해 줘야함
        if(len%2 != 0) sb.Append(contents[halfIdx]);
        
        string nextString = sb.ToString();
        return nextString;
    }

    private static void FindStartString()
    {
        int repetitionCnt = stringList.Count;
        var result = blinkCnt % repetitionCnt;
        var stringArray = stringList.ToArray();
        int index = repetitionCnt - result - 1;
        Console.Write(stringArray[index]);
    }
}