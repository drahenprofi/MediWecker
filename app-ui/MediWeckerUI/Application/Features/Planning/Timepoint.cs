﻿namespace MediWeckerUI.Application.Features.Planning;

public class Timepoint
{
    public TimepointType Type { get; set; }
    public int? AbsoluteTimeFromMidnight { get; set; }
    public TimepointModifierFlag Flags { get; set; }
}

public enum TimepointModifierFlag
{
    None = 0,
    BeforeEating = 1,
    AfterEating = 2,
    WhileEating = 4,
    WithoutFoodIntake = 8,
}