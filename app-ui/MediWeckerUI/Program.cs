using System.Reflection;
using Blazored.Modal;
using FluentValidation;
using MediWeckerUI;
using MediWeckerUI.Application;
using Microsoft.AspNetCore.Components.Web;
using Microsoft.AspNetCore.Components.WebAssembly.Hosting;

var builder = WebAssemblyHostBuilder.CreateDefault(args);
builder.RootComponents.Add<App>("#app");
builder.RootComponents.Add<HeadOutlet>("head::after");

builder.Services.AddBlazoredModal();
builder.Services.AddScoped<AppInterop>();

typeof(Program).Assembly.GetTypes().Where(type => type.IsAssignableTo(typeof(IValidator))).ToList().ForEach(
    validatorType =>
    {
        builder.Services.AddTransient(validatorType);
    });


builder.Services.AddScoped(sp => new HttpClient { BaseAddress = new Uri(builder.HostEnvironment.BaseAddress) });

await builder.Build().RunAsync();