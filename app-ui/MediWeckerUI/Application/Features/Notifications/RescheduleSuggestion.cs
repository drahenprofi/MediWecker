namespace MediWeckerUI.Application.Features.Notifications;

public class RescheduleSuggestion
{
    public long MedicineId { get; set; }
    public RescheduleSuggestionType Type { get; set; }
    public long SuggestedTimeFromMidnight { get; set; }
}