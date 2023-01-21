namespace MediWeckerUI.Application.Features.Notifications;

public class ReminderPromptResponseData
{
    public long MedicineId { get; set; }
    public long ScheduledTimeUtc { get; set; }
    public long? ActualTimeUtc { get; set; }
}