using MediWeckerUI.Application.Features.Planning;

namespace MediWeckerUI.Application.Features.Notifications;

public class ReminderPromptRequestData
{
    public long MedicineId { get; set; }
    public long AlarmId { get; set; }
    public long ScheduledTimeUtc { get; set; }
}