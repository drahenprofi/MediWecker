namespace MediWeckerUI.Application.Features.Planning;

public class WakeUpTimeData
{
    public int Monday { get; set; }
    public int Tuesday { get; set; }
    public int Wednesday { get; set; }
    public int Thursday { get; set; }
    public int Friday { get; set; }
    public int Saturday { get; set; }
    public int Sunday { get; set; }
    
    public bool IsSingleTime()
    {
        return new List<int>
        {
            Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday
        }.GroupBy(x => x).Count() == 1;
    }
}