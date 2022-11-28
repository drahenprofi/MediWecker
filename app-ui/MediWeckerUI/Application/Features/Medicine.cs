using System.Text.Json.Serialization;

namespace MediWeckerUI.Application.Features;

public class Medicine
{
    [JsonPropertyName("id")]
    public int Id { get; set; }
    
    [JsonPropertyName("name")]
    public string Name { get; set; }
    
    [JsonPropertyName("amount")]
    public string Amount { get; set; }
    
    [JsonPropertyName("rythm")]
    public string Rythm { get; set; }
}