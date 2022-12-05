using System.Text.Json;
using Blazored.Modal;
using Blazored.Modal.Services;
using MediWeckerUI.Application;
using MediWeckerUI.Application.Features.Planning;
using MediWeckerUI.Shared;
using Microsoft.AspNetCore.Components;

namespace MediWeckerUI.Pages.Plans;

public partial class Edit
{
    [Parameter]
    public int Id { get; set; }

    [Inject]
    public AppInterop Interop { get; set; }

    [CascadingParameter]
    public IModalService ModalService { get; set; }

    [CascadingParameter]
    public BaseNavigationLayout BaseLayout { get; set; }

    [Inject]
    public NavigationManager NavigationManager { get; set; }

    public bool IsEditingName { get; set; }
    public bool IsEditingAmount { get; set; }
    public bool IsEditingRythm { get; set; }
    public bool IsEditingTimepoints { get; set; }

    public EditRythmComponent EditRythmComponent { get; set; }
    
    public Medicine Plan { get; set; }
    public Application.Features.Planning.Rythm Rythm { get; set; }
    public Application.Features.Planning.Rythm RythmEditCopy { get; set; }

    protected override async Task OnParametersSetAsync()
    {
        Plan = (await Interop.GetAllPlansAsync()).First(x => x.Id == Id);
        Rythm = JsonSerializer.Deserialize<Application.Features.Planning.Rythm>(Plan.Rythm);
    }


    protected async Task OnClickDeleteAsync()
    {
        var options = new ModalOptions { UseCustomLayout = true };

        var parameters = new ModalParameters();
        parameters.Add(nameof(ConfirmationModal.Message),
            $"Sind Sie sich sicher, dass Sie den Plan \"{Plan.Name}\" wirklich permanent löschen möchten? Die Aktion kann nicht rückgängig gemacht werden.");
        parameters.Add(nameof(ConfirmationModal.Destructive), true);

        var result = await ModalService.Show<ConfirmationModal>("", parameters, options).Result;

        if (result.Cancelled)
        {
            return;
        }

        await Interop.DeletePlanAsync(Plan.Id);
        await Interop.ShowAlertAsync($"\"{Plan.Name}\" wurde gelöscht.");

        BaseLayout.Navigate<Index>();
    }

    protected async Task OnClickEditNameAsync()
    {
        IsEditingName = true;

        StateHasChanged();
    }

    protected async Task OnClickEditAmountAsync()
    {
        IsEditingAmount = true;

        StateHasChanged();
    }

    protected async Task OnClickEditRythmAsync()
    {
        RythmEditCopy = JsonSerializer.Deserialize<Application.Features.Planning.Rythm>(Plan.Rythm);
        IsEditingRythm = true;

        StateHasChanged();
    }

    protected async Task OnClickEditTimepointsAsync()
    {
        RythmEditCopy = JsonSerializer.Deserialize<Application.Features.Planning.Rythm>(Plan.Rythm);
        IsEditingTimepoints = true;

        StateHasChanged();
    }

    protected async Task OnNewNameAsync(string newName)
    {
        Plan.Name = newName;

        await Interop.UpdatePlanAsync(Plan);
        await Interop.ShowAlertAsync("Name aktualisiert.");

        IsEditingName = false;

        InvokeAsync(StateHasChanged);
    }

    protected async Task OnNewAmountAsync(string amount)
    {
        Plan.Amount = amount;

        await Interop.UpdatePlanAsync(Plan);
        await Interop.ShowAlertAsync("Menge aktualisiert.");

        IsEditingAmount = false;

        InvokeAsync(StateHasChanged);
    }

    protected async Task OnBackwardAsync()
    {
        IsEditingRythm = false;
        IsEditingTimepoints = false;

        RythmEditCopy = JsonSerializer.Deserialize<Application.Features.Planning.Rythm>(Plan.Rythm);

        InvokeAsync(StateHasChanged);
    }

    protected async Task OnRythmSubmitAsync()
    {
        Plan.Rythm = JsonSerializer.Serialize(RythmEditCopy);
        Rythm = JsonSerializer.Deserialize<Application.Features.Planning.Rythm>(Plan.Rythm);

        Console.WriteLine($"New state: {Plan.Rythm}");
        
        await Interop.UpdatePlanAsync(Plan);
        await Interop.ShowAlertAsync("Rythmus aktualisiert.");
        
        IsEditingRythm = false;
        
        StateHasChanged();
    }
    
    protected async Task OnTimepointsSubmitAsync()
    {
        Plan.Rythm = JsonSerializer.Serialize(RythmEditCopy);
        Rythm = JsonSerializer.Deserialize<Application.Features.Planning.Rythm>(Plan.Rythm);
        
        Console.WriteLine($"New state: {Plan.Rythm}");
        
        await Interop.UpdatePlanAsync(Plan);
        await Interop.ShowAlertAsync("Uhrzeiten aktualisiert.");
        
        IsEditingTimepoints = false;
        
        StateHasChanged();
    }
}