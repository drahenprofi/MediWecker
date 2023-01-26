using System.Text.Json;
using System.Text.Json.Serialization;
using Blazored.Modal;
using FluentValidation;
using MediWeckerUI;
using MediWeckerUI.Application;
using MediWeckerUI.Application.Features.Navigation;
using Microsoft.AspNetCore.Components.Web;
using Microsoft.AspNetCore.Components.WebAssembly.Hosting;

var builder = WebAssemblyHostBuilder.CreateDefault(args);
builder.RootComponents.Add<App>("#app");
builder.RootComponents.Add<HeadOutlet>("head::after");

builder.Services.AddBlazoredModal();
builder.Services.AddScoped<AppInterop>();
builder.Services.AddScoped<WebViewNavigationHistory>();

typeof(Program).Assembly.GetTypes()
    .Where(type => type.IsAssignableTo(typeof(IValidator)))
    .ToList()
    .ForEach(
        validatorType => { builder.Services.AddTransient(validatorType); });


builder.Services.AddScoped(sp => new HttpClient { BaseAddress = new Uri(builder.HostEnvironment.BaseAddress) });

// Default JSON interop settings
AppInterop.InteropJsonSettings = new JsonSerializerOptions();
AppInterop.InteropJsonSettings.PropertyNameCaseInsensitive = true;
AppInterop.InteropJsonSettings.Converters.Add(new JsonStringEnumConverter(JsonNamingPolicy.CamelCase));

await builder.Build().RunAsync();