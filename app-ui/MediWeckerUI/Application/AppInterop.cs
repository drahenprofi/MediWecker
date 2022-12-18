using System.Text.Json;
using MediWeckerUI.Application.Features.Planning;
using Microsoft.AspNetCore.Components;
using Microsoft.JSInterop;

namespace MediWeckerUI.Application;

public class AppInterop
{
    private readonly IJSRuntime _js;
    private readonly NavigationManager _navigationManager;
    private bool _mockPermissionsGiven = false;
    
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

    public async Task<bool> GetIfNotificationsPermissionGivenAsync()
    {
        if (!IsInApp()) return _mockPermissionsGiven;

        return await _js.InvokeAsync<bool>("Android.getIfNotificationsPermissionGiven");
    }
    
    public async Task<bool> GetIfInternetPermissionGivenAsync()
    {
        if (!IsInApp()) return _mockPermissionsGiven;

        return await _js.InvokeAsync<bool>("Android.getIfInternetPermissionGiven");
    }
    
    public async Task AttemptRequestPermissionsAsync()
    {
        if (!IsInApp())
        {
            _mockPermissionsGiven = true;
            return;
        }

        await _js.InvokeVoidAsync("Android.attemptRequestPermissions");
    }

    public async Task<bool> GetAndResetPermissionsRequestCompletedFlagAsync()
    {
        if (!IsInApp()) return true;

        return await _js.InvokeAsync<bool>("Android.getAndResetPermissionsRequestCompleted");
    }

    public async Task<bool> GetWakeUpTimesSetupRequiredAsync()
    {
        if (!IsInApp()) return false;

        return !await _js.InvokeAsync<bool>("Android.getWakeUpTimesInitialized");
    }

    public async Task<WakeUpTimeData> GetWakeUpTimeDataAsync()
    {
        if (!IsInApp())
            return new WakeUpTimeData
            {
                Monday = 480,
                Tuesday = 420,
                Wednesday = 420,
                Thursday = 420,
                Friday = 420,
                Saturday = 420,
                Sunday = 420
            };
        
        var json = await _js.InvokeAsync<string>("Android.getWakeUpTimeData");
        
        return JsonSerializer.Deserialize<WakeUpTimeData>(json);
    }

    public async Task UpdateWakeUpTimeDataAsync(WakeUpTimeData data)
    {
        if (!IsInApp()) return;
        
        await _js.InvokeVoidAsync("Android.updateWakeUpTimes", JsonSerializer.Serialize(data));
    }
    
    public async Task<List<Medicine>> GetAllPlansAsync()
    {
        if (!IsInApp())
        {
            return new List<Medicine>
            {
                new()
                {
                    Id = 1,
                    Name = "Test Plan",
                    Amount = "200mg",
                    Rythm = JsonSerializer.Serialize(new Rythm
                    {
                        IntervalDays = new IntervalDaysData { Days = 1 },
                        Timepoints = new List<Timepoint>
                        {
                            new()
                            {
                                Type = TimepointType.Morning
                            }
                        }
                    })
                }
            };
        }

        var json = await _js.InvokeAsync<string>("Android.getMedicine");

        Console.WriteLine($"GetAllPlansAsync: {json}");

        return JsonSerializer.Deserialize<List<Medicine>>(json);
    }

    public async Task DeletePlanAsync(int id)
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
        if (!IsInApp())
        {
            Console.WriteLine($"UpdatePlanAsync: Not in app.");
            
            return;
        }

        await _js.InvokeVoidAsync("Android.updateMedicine", JsonSerializer.Serialize(medicine));
    }

    public async Task BackEventAsync()
    {
        if (!IsInApp()) return;

        await _js.InvokeVoidAsync("Android.navigateBackInApp");
    }
}