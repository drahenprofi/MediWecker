using Blazored.Modal;
using Blazored.Modal.Services;
using Microsoft.AspNetCore.Components;

namespace MediWeckerUI.Shared;

public partial class ConfirmationModal
{
    [CascadingParameter]
    BlazoredModalInstance BlazoredModal { get; set; }

    [Parameter]
    public string Message { get; set; }

    [Parameter]
    public bool Destructive { get; set; }
    
    async Task CloseAsync() => await BlazoredModal.CloseAsync(ModalResult.Ok(true));
    async Task CancelAsync() => await BlazoredModal.CancelAsync();
}