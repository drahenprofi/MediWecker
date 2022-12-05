using System.Text.Json.Serialization;

namespace MediWeckerUI.Application.Features.Planning;

public class Rythm
{
    [JsonPropertyName("intervalDays")]
    public IntervalDaysData IntervalDays { get; set; }
    
    [JsonPropertyName("specificDays")]
    public SpecificDaysData SpecificDays { get; set; }

    [JsonPropertyName("timepoints")]
    public List<Timepoint> Timepoints { get; set; } = new List<Timepoint>();
}