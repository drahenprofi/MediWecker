window.MediWecker = {
    data: {
        serviceReference: null,
        invocationQueue: [],
        invokerReady: false
    },

    initAppUrl: function () {
        history.pushState(null, null, '#app');

        window.onpopstate = function () {
            history.go(1);
        }
    },

    init: function (serviceReference) {
        MediWecker.data.serviceReference = serviceReference;

        MediWecker.data.invocationQueue.forEach(lambda => {
            lambda();
        });
    },

    queueInvocation: function (lambda) {
        if (MediWecker.data.invokerReady) {
            lambda();
        } else {
            MediWecker.data.invocationQueue.push(lambda);
        }
    },

    showReminderPrompt: function (requestDataJson) {
        console.log("showReminderPrompt called");
        
        MediWecker.queueInvocation(() => {
            MediWecker.data.serviceReference.invokeMethodAsync("ShowReminderPromptAsync", requestDataJson);
        });
    }
}