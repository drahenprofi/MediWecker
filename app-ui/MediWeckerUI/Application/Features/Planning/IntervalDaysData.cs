using System.Text.Json.Serialization;

namespace MediWeckerUI.Application.Features.Planning;

public class IntervalDaysData
{
    [JsonPropertyName("days")]
    public int Days { get; set; }
}