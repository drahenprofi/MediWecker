window.MediWecker = {
    initAppUrl: function () {
        //window.location.hash = "#init";
        //history.pushState(null, null, '#init');
        history.pushState(null, null, '#app');

        window.onpopstate = function () {
            history.go(1);
        }
    }
}