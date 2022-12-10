using Microsoft.AspNetCore.Components;
using Microsoft.AspNetCore.Components.Routing;
using Microsoft.JSInterop;

namespace MediWeckerUI.Application.Features.Navigation;

public class WebViewNavigationHistory
{
    private readonly IJSRuntime _js;
    private readonly NavigationManager _navigationManager;
    public EventHandler BackEvent;

    public WebViewNavigationHistory(IJSRuntime js, NavigationManager navigationManager)
    {
        _js = js;
        _navigationManager = navigationManager;

        _navigationManager.RegisterLocationChangingHandler(OnLocationChanging);
    }

    public async Task InitializeAsync()
    {
        await _js.InvokeVoidAsync("MediWecker.initAppUrl");
    }

    private async ValueTask OnLocationChanging(LocationChangingContext context)
    {
        Console.WriteLine($"WebViewNavigationHistory.OnLocationChanging: Called");
        
        context.PreventNavigation();

        BackEvent?.Invoke(this, EventArgs.Empty);
    }
}