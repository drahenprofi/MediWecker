using MediWeckerUI.Application.Features.Planning;

namespace MediWeckerUI.Application.Features.Notifications;

public class ReminderPromptRequestData
{
    public Medicine Medicine { get; set; }
    public long ScheduledTimeUtc { get; set; }
}