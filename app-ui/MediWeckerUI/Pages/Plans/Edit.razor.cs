using Blazored.Modal;
using Blazored.Modal.Services;
using FluentValidation;
using MediWeckerUI.Application;
using MediWeckerUI.Application.Features;
using MediWeckerUI.Shared;
using Microsoft.AspNetCore.Components;
using Microsoft.VisualBasic;

namespace MediWeckerUI.Pages.Plans;

public partial class Edit
{
    [Parameter]
    public string Id { get; set; }

    [Inject]
    public AppInterop Interop { get; set; }

    [CascadingParameter]
    public IModalService ModalService { get; set; }
    
    [CascadingParameter]
    public BaseNavigationLayout BaseLayout { get; set; }
    
    [Inject]
    public NavigationManager NavigationManager { get; set; }

    public bool IsEditingName { get; set; }
    
    public Medicine Plan { get; set; }

    protected override async Task OnParametersSetAsync()
    {
        Plan = (await Interop.GetAllPlansAsync()).First(x => x.Id == Id);
    }
    

    protected async Task OnClickDeleteAsync()
    {
        var options = new ModalOptions { UseCustomLayout = true };

        var parameters = new ModalParameters();
        parameters.Add(nameof(ConfirmationModal.Message), $"Sind Sie sich sicher, dass Sie den Plan \"{Plan.Name}\" wirklich permanent löschen möchten? Die Aktion kann nicht rückgängig gemacht werden.");
        parameters.Add(nameof(ConfirmationModal.Destructive), true);

        var result = await ModalService.Show<ConfirmationModal>("", parameters, options).Result;

        if (result.Cancelled)
        {
            return;
        }

        await Interop.DeletePlanAsync(Plan.Id);
        await Interop.ShowAlertAsync($"\"{Plan.Name}\" wurde gelöscht.");

        BaseLayout.Navigate<Pages.Plans.Index>();
    }

    protected async Task OnClickEditNameAsync()
    {
        IsEditingName = true;
        
        StateHasChanged();
    }

    private async Task OnNewNameAsync(string newName)
    {
        Plan.Name = newName;

        await Interop.UpdatePlanAsync(Plan);
        await Interop.ShowAlertAsync("Name aktualisiert.");

        IsEditingName = false;
        
        InvokeAsync(StateHasChanged);
    }
}