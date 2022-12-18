using Microsoft.AspNetCore.Components;
using Microsoft.AspNetCore.Components.Routing;
using Microsoft.JSInterop;

namespace MediWeckerUI.Application.Features.Navigation;

public class WebViewNavigationHistory
{
    private readonly IJSRuntime _js;
    private readonly NavigationManager _navigationManager;
    private readonly AppInterop _appInterop;
    public EventHandler BackEvent;

    public WebViewNavigationHistory(IJSRuntime js, NavigationManager navigationManager, AppInterop appInterop)
    {
        _js = js;
        _navigationManager = navigationManager;
        _appInterop = appInterop;

        _navigationManager.RegisterLocationChangingHandler(OnLocationChanging);
    }

    public async Task InitializeAsync()
    {
        await _js.InvokeVoidAsync("MediWecker.initAppUrl");
    }

    private async ValueTask OnLocationChanging(LocationChangingContext context)
    {
        Console.WriteLine($"WebViewNavigationHistory: Back event raised");
        
        context.PreventNavigation();

        int subscriberCount = BackEvent?.GetInvocationList().Length ?? 0;
        
        Console.WriteLine($"WebViewNavigationHistory: BackEvent subscriber count = {subscriberCount}");

        if (subscriberCount == 0)
        {
            Console.WriteLine($"WebViewNavigationHistory: No subscriber found, redirecting back event to native host app");
            await _appInterop.BackEventAsync();
        }
        else
        {
            Console.WriteLine($"WebViewNavigationHistory: Internal subscribers found, raising internal event");
            BackEvent?.Invoke(this, EventArgs.Empty);
        }
    }
}