using System.Text.Json.Serialization;

namespace MediWeckerUI.Application.Features.Planning;

public class Timepoint
{
    [JsonPropertyName("type")]
    public TimepointType Type { get; set; }
    
    [JsonPropertyName("absoluteTimeFromMidnight")]
    public int? AbsoluteTimeFromMidnight { get; set; }

    [JsonPropertyName("uuid")]
    public string UUID { get; set; }
}