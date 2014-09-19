define([
    "framework/core",
    "framework/model",
    "framework/views",
    "framework/controllers",
    "views"], function(core, fmodel, fviews, fcontrollers, views) {

    var exports = {};

    var CategoriesController = fcontrollers.Controller.extend({
        ctor: function() {
            CategoriesController.super.ctor.call(this);
        },

        index: function(params) {
            var code = params._1;

            var service = new fmodel.AjaxService();
            service.set({
                method: "GET",
                url: BASE + "categories/" + code
            });

            var view = new views.CategoryIndexView(service);
            return view;
        },

        view: function(params) {

        }
    });

    var ArticlesController = fcontrollers.Controller.extend({
        ctor: function() {
            ArticlesController.super.ctor.call(this);
        },

        index: function(params) {
            return this.view(params);
        },

        view: function(params) {
            var code = params._1;

            var service = new fmodel.AjaxService();
            service.set({
                method: "GET",
                url: BASE + "articles/" + code
            });

            var view = new views.ArticleView(service);
            return view;
        }
    });

    exports.CategoriesController = CategoriesController;
    exports.ArticlesController = ArticlesController;

    return exports;
});