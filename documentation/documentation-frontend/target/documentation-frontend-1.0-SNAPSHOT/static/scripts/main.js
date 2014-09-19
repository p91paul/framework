window.SPA_CONTAINER = $("#spa-container"); //single page application container, where the ajax views are loaded for default

require([
    "framework/plugins",
    "framework/core",
    "framework/ui",
    "framework/views",
    "framework/controllers",
    "framework/widgets",
    "controllers"], function(_p, core, ui, fviews, fcontrollers, fwidgets, controllers) {

    //register all js controllers here
    //specify singleton if controller must be a singleton
    ui.Navigation.instance().registerController("articles", function() { return new controllers.ArticlesController(); }, "singleton");
    ui.Navigation.instance().registerController("categories", function() { return new controllers.CategoriesController(); }, "singleton");

    //ajax navigation entry point
    ui.Navigation.instance().start();

    $(window).scroll(function() {
        var headerBounds = core.utils.bounds($(".doc-page-header"));
        var scrollTop = $(window).scrollTop();
        var headerBottom = headerBounds.height;
        if(scrollTop >= headerBottom) {
            $("header.navbar").addClass("navbar-scrolled");
        } else {
            $("header.navbar").removeClass("navbar-scrolled");
        }
    }).resize();

    $(".top-button").click(function() {
        $("html, body").animate({ scrollTop: "0px" }, 250);
    });

    if (location.href.indexOf("#") == -1) {
        location.href = "#/articles/view/landing";
    }
});