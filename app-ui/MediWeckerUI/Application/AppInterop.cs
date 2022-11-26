using System.Text.Json;
using MediWeckerUI.Application.Features;
using Microsoft.AspNetCore.Components;
using Microsoft.JSInterop;

namespace MediWeckerUI.Application;

public class AppInterop
{
    private readonly IJSRuntime _js;
    private readonly NavigationManager _navigationManager;

    public AppInterop(IJSRuntime js, NavigationManager navigationManager)
    {
        _js = js;
        _navigationManager = navigationManager;
    }

    private bool IsInApp()
    {
        return _navigationManager.BaseUri.StartsWith("https://appassets.androidplatform.net/");
    }

    public async Task ShowAlertAsync(string message)
    {
        if (!IsInApp()) return;
        
        await _js.InvokeVoidAsync("Android.showToast", message);
    }

    public async Task<List<Medicine>> GetAllPlansAsync()
    {
        if (!IsInApp())
            return new List<Medicine>
            {
                new Medicine
                {
                    Id = "62467383-784D-4C3A-BB64-8FC412DBE8BB",
                    Name = "Test Plan",
                    Amount = "200mg",
                    Rythm = null
                }
            };

        return JsonSerializer.Deserialize<List<Medicine>>(await _js.InvokeAsync<string>("Android.getMedicine"));
    }

    public async Task DeletePlanAsync(string id)
    {
        if (!IsInApp()) return;

        await _js.InvokeVoidAsync("Android.deleteMedicine", id);
    }

    public async Task AddPlanAsync(Medicine medicine)
    {
        if (!IsInApp()) return;
        
        await _js.InvokeVoidAsync("Android.insertMedicine", JsonSerializer.Serialize(medicine));
    }

    public async Task UpdatePlanAsync(Medicine medicine)
    {
        if (!IsInApp()) return;
        
        await _js.InvokeVoidAsync("Android.updateMedicine", JsonSerializer.Serialize(medicine));
    }
}