namespace MediWeckerUI.Application.Features.Planning;

public class CalendarItem
{
    public Medicine Medicine { get; set; }
    public long ScheduledTimeUtc { get; set; }
    public long ActualTimeUtc { get; set; }
}