window.SPA_CONTAINER = $("#spa-container"); //single page application container, where the ajax views are loaded for default

require([
    "framework/plugins",
    "framework/core",
    "framework/ui",
    "framework/views",
    "framework/controllers",
    "framework/widgets",
    "controllers",
    "components"], function(_p, core, ui, fviews, fcontrollers, fwidgets, controllers, components) {

    $.datepicker.setDefaults( $.datepicker.regional[ COUNTRY_CODE ] );

    //register all js controllers here
    //specify singleton if controller must be a singleton
    (function registerRoutes() {
        ui.Navigation.instance().registerController("crud", function() { return new fcontrollers.CrudController(); }, "singleton");
    })();

    fwidgets.Form.registerComponent("html", function(element) {
        var c = new components.HtmlComponent(this, element);
        c.init();
    });

    //ajax navigation entry point
    ui.Navigation.instance().start();
});