using Microsoft.AspNetCore.Components;

namespace MediWeckerUI.Application.Features.Navigation;

public class WebViewNavigationManager
{
    [Parameter]
    public RenderFragment<RouteData> Content { get; set; }
    
    public async Task NavigateTo()
    {
        
    }
}