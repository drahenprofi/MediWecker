namespace MediWeckerUI.Application.Features.Planning;

public class UserTimeData
{
    public int WakeupMonday { get; set; }
    public int WakeupTuesday { get; set; }
    public int WakeupWednesday { get; set; }
    public int WakeupThursday { get; set; }
    public int WakeupFriday { get; set; }
    public int WakeupSaturday { get; set; }
    public int WakeupSunday { get; set; }
    
    public int SleepMonday { get; set; }
    public int SleepTuesday { get; set; }
    public int SleepWednesday { get; set; }
    public int SleepThursday { get; set; }
    public int SleepFriday { get; set; }
    public int SleepSaturday { get; set; }
    public int SleepSunday { get; set; }
    
    public bool IsWakeupSingleTime()
    {
        return new List<int>
        {
            WakeupMonday, WakeupTuesday, WakeupWednesday, WakeupThursday, WakeupFriday, WakeupSaturday, WakeupSunday
        }.GroupBy(x => x).Count() == 1;
    }
    
    public bool IsSleepSingleTime()
    {
        return new List<int>
        {
            SleepMonday, SleepTuesday, SleepWednesday, SleepThursday, SleepFriday, SleepSaturday, SleepSunday
        }.GroupBy(x => x).Count() == 1;
    }
}