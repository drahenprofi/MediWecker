using System.Runtime.Serialization;

namespace MediWeckerUI.Application.Features.Planning;

public enum TimepointType
{
    [EnumMember(Value = "0")]
    AbsoluteTime,
    
    [EnumMember(Value = "1")]
    Morning,
    
    [EnumMember(Value = "2")]
    Midday,
    
    [EnumMember(Value = "3")]
    Evening,
    
    [EnumMember(Value = "4")]
    Night
}