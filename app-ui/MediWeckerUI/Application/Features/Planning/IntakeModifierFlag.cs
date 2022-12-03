namespace MediWeckerUI.Application.Features.Planning;

public enum IntakeModifierFlag
{
    None = 0,
    BeforeEating = 1,
    AfterEating = 2,
    WhileEating = 4,
    WithoutFoodIntake = 8,
}