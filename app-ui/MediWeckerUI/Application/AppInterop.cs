using System.Text.Json;
using MediWeckerUI.Application.Features.Notifications;
using MediWeckerUI.Application.Features.Planning;
using Microsoft.AspNetCore.Components;
using Microsoft.JSInterop;
using JSRuntimeExtensions = Microsoft.JSInterop.JSRuntimeExtensions;

namespace MediWeckerUI.Application;

public class AppInterop
{
    public EventCallback<ReminderPromptRequestData> OnReminderPromptShowRequest;
    public static JsonSerializerOptions InteropJsonSettings = default;

    private readonly IJSRuntime _js;
    private readonly NavigationManager _navigationManager;
    private bool _mockPermissionsGiven = true;

    public AppInterop(IJSRuntime js, NavigationManager navigationManager)
    {
        _js = js;
        _navigationManager = navigationManager;
    }

    private bool IsInApp()
    {
        return _navigationManager.BaseUri.StartsWith("https://appassets.androidplatform.net/");
    }

    public void RegisterOnReminderPromptShownRequestCallback(EventCallback<ReminderPromptRequestData> callback)
    {
        OnReminderPromptShowRequest = callback;
    }

    [JSInvokable]
    public async Task ShowReminderPromptAsync(string requestJson)
    {
        Console.WriteLine($"ShowReminderPromptAsync: requestJson = {requestJson}");
        var request = JsonSerializer.Deserialize<ReminderPromptRequestData>(requestJson, InteropJsonSettings);

        await ShowReminderPromptAsync(request);
    }

    public async Task ShowReminderPromptAsync(ReminderPromptRequestData request)
    {
        await OnReminderPromptShowRequest.InvokeAsync(request);
    }

    public async Task<List<RescheduleSuggestion>> SubmitReminderPromptResponseDataAsync(ReminderPromptResponseData data)
    {
        if (!IsInApp())
            return new List<RescheduleSuggestion>
            {
                new RescheduleSuggestion()
                {
                    Type = RescheduleSuggestionType.RescheduleAbsoluteTime,
                    MedicineId = 1,
                    SuggestedTimeFromMidnight = 600
                },
                new RescheduleSuggestion()
                {
                    Type = RescheduleSuggestionType.RescheduleWakeUpTime,
                    MedicineId = 1,
                    SuggestedTimeFromMidnight = 600
                }
            };

        var responseJson =
            await _js.InvokeAsync<string>("Android.submitReminderPromptResponse", JsonSerializer.Serialize(data));

        return JsonSerializer.Deserialize<List<RescheduleSuggestion>>(responseJson, InteropJsonSettings);
    }

    public async Task ConfirmRescheduleSuggestionAsync(RescheduleSuggestion suggestion)
    {
        if (!IsInApp()) return;

        await _js.InvokeVoidAsync("Android.confirmRescheduleSuggestion",
            JsonSerializer.Serialize(suggestion));
    }

    public async Task ShowAlertAsync(string message)
    {
        if (!IsInApp()) return;

        await JSRuntimeExtensions.InvokeVoidAsync(_js, "Android.showToast", message);
    }

    public async Task<bool> GetIfNotificationsPermissionGivenAsync()
    {
        if (!IsInApp()) return _mockPermissionsGiven;

        return await JSRuntimeExtensions.InvokeAsync<bool>(_js, "Android.getIfNotificationsPermissionGiven");
    }

    public async Task<bool> GetIfInternetPermissionGivenAsync()
    {
        if (!IsInApp()) return _mockPermissionsGiven;

        return await JSRuntimeExtensions.InvokeAsync<bool>(_js, "Android.getIfInternetPermissionGiven");
    }

    public async Task<List<CalendarItem>> GetCalendarItemsAsync(DateTimeOffset from, DateTimeOffset to)
    {
        var random = new Random();

        if (!IsInApp())
        {
            var items = new List<CalendarItem>();

            for (var i = 0; i < 5; i++)
            {
                items.Add(new CalendarItem
                {
                    Medicine = (await GetAllPlansAsync()).First(),

                    ScheduledTimeUtc = DateTimeOffset.UtcNow.AddMinutes(random.Next(-600, 6000))
                        .ToUnixTimeMilliseconds(),
                    ActualTimeUtc = 1,
                    UserResponded = true
                });
            }

            return items;
        }


        //Console.WriteLine($"GetCalendarItemsAsync serializing request");

        var requestJson = JsonSerializer.Serialize(new CalendarRequest
            { From = from.ToUnixTimeMilliseconds(), To = to.ToUnixTimeMilliseconds() });

        //Console.WriteLine($"GetCalendarItemsAsync request JSON is {requestJson}");

        var json = await _js.InvokeAsync<string>("Android.getCalendarData", requestJson);

        //Console.WriteLine($"GetCalendarItemsAsync returned JSON is {json}");

        return JsonSerializer.Deserialize<List<CalendarItem>>(json, InteropJsonSettings);
    }

    public async Task AttemptRequestPermissionsAsync()
    {
        if (!IsInApp())
        {
            _mockPermissionsGiven = true;
            return;
        }

        await JSRuntimeExtensions.InvokeVoidAsync(_js, "Android.attemptRequestPermissions");
    }

    public async Task<bool> GetAndResetPermissionsRequestCompletedFlagAsync()
    {
        if (!IsInApp()) return true;

        return await JSRuntimeExtensions.InvokeAsync<bool>(_js, "Android.getAndResetPermissionsRequestCompleted");
    }

    public async Task<bool> GetUserTimesDataSetupRequiredAsync()
    {
        if (!IsInApp()) return false;

        return !await JSRuntimeExtensions.InvokeAsync<bool>(_js, "Android.getUserTimesInitialized");
    }

    public async Task<UserTimeData> GetUserTimesDataAsync()
    {
        if (!IsInApp())
        {
            return new UserTimeData
            {
                WakeupMonday = 480,
                WakeupTuesday = 420,
                WakeupWednesday = 420,
                WakeupThursday = 420,
                WakeupFriday = 420,
                WakeupSaturday = 420,
                WakeupSunday = 420,

                SleepMonday = 1320,
                SleepTuesday = 1320,
                SleepWednesday = 1320,
                SleepThursday = 1320,
                SleepFriday = 1320,
                SleepSaturday = 1320,
                SleepSunday = 1320
            };
        }

        var json = await JSRuntimeExtensions.InvokeAsync<string>(_js, "Android.getUserTimesData");

        return JsonSerializer.Deserialize<UserTimeData>(json, InteropJsonSettings);
    }

    public async Task UpdateUserTimesDataAsync(UserTimeData data)
    {
        if (!IsInApp()) return;

        await JSRuntimeExtensions.InvokeVoidAsync(_js, "Android.updateUserTimesData", JsonSerializer.Serialize(data));
    }

    public async Task MarkUserTimesSetupCompletedAsync()
    {
        if (!IsInApp()) return;

        await JSRuntimeExtensions.InvokeVoidAsync(_js, "Android.userTimesInitialized");
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

        var json = await JSRuntimeExtensions.InvokeAsync<string>(_js, "Android.getMedicine");

        Console.WriteLine($"GetAllPlansAsync: {json}");

        return JsonSerializer.Deserialize<List<Medicine>>(json, InteropJsonSettings);
    }

    public async Task DeletePlanAsync(int id)
    {
        if (!IsInApp()) return;

        await JSRuntimeExtensions.InvokeVoidAsync(_js, "Android.deleteMedicine", id);
    }

    public async Task AddPlanAsync(Medicine medicine)
    {
        if (!IsInApp()) return;

        var json = JsonSerializer.Serialize(medicine);

        Console.WriteLine($"AddPlanAsync: {json}");

        await JSRuntimeExtensions.InvokeVoidAsync(_js, "Android.insertMedicine", json);
    }

    public async Task UpdatePlanAsync(Medicine medicine)
    {
        if (!IsInApp())
        {
            //Console.WriteLine("UpdatePlanAsync: Not in app.");

            return;
        }

        await JSRuntimeExtensions.InvokeVoidAsync(_js, "Android.updateMedicine", JsonSerializer.Serialize(medicine));
    }

    public async Task BackEventAsync()
    {
        if (!IsInApp()) return;

        await JSRuntimeExtensions.InvokeVoidAsync(_js, "Android.navigateBackInApp");
    }

    private class CalendarRequest
    {
        public long From { get; set; }
        public long To { get; set; }
    }
}