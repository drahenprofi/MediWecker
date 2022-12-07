using System.Text.Json.Serialization;

namespace MediWeckerUI.Application.Features.Planning;

public class SpecificDaysData
{
    [JsonPropertyName("monday")]
    public bool Monday { get; set; }
    
    [JsonPropertyName("tuesday")]
    public bool Tuesday { get; set; }
    
    [JsonPropertyName("wednesday")]
    public bool Wednesday { get; set; }
    
    [JsonPropertyName("thursday")]
    public bool Thursday { get; set; }
    
    [JsonPropertyName("friday")]
    public bool Friday { get; set; }
    
    [JsonPropertyName("saturday")]
    public bool Saturday { get; set; }
    
    [JsonPropertyName("sunday")]
    public bool Sunday { get; set; }
}