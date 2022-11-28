namespace MediWeckerUI.Application.Features.Planning;

public enum TimepointModifierFlag
{
    None = 0,
    BeforeEating = 1,
    AfterEating = 2,
    WhileEating = 4,
    WithoutFoodIntake = 8,
}