define([
    "framework/core",
    "framework/model",
    "framework/views",
    "framework/controllers",
    "framework/webparts"], function(core, fmodel, fviews, fcontrollers, webparts) {

    var exports = {};

    function updateContent(response, container) {
        $.loader.hide();
        $(".doc-page-header").addClass("out");
        if (core.utils.isEmpty(response.value.customHeader)) {
            window.setTimeout(function () {
                $(".doc-page-header").find(".container")
                    .empty()
                    .append(_E("h1").html(response.value.title))
                    .append(_E("p").html(response.value.description));

                var bounds = core.utils.bounds($(".doc-page-header").find(".container"));
                $(".doc-page-header").animate({ height: bounds.height + 80 + "px" }, 250);

                window.setTimeout(function() { $(".doc-page-header").removeClass("out"); }, 50);
            }, 500);
        } else {
            window.setTimeout(function() {
                $(".doc-page-header").find(".container").empty().append(_E("div").addClass("customHeader").html(response.value.customHeader));
                var bounds = core.utils.bounds($(".doc-page-header").find(".container"));
                $(".doc-page-header").animate({ height: bounds.height + 80 + "px" }, 250);
                window.setTimeout(function() { $(".doc-page-header").removeClass("out"); }, 50);
            }, 500);
        }

        container.empty().html(response.content).css({ opacity: 0 }).animate({ opacity: 1 }, 250);
    }

    var ArticleView = fviews.ContentView.extend({
        ctor: function(service) {
            ArticleView.super.ctor.apply(this, arguments);

            var self = this;

            self.content = null;
            self.container = $("#content-container");
            self.service = service;

            self.service.on({
                load: function(response) {
                    updateContent(response, self.container);

                    $("li[data-category-id], li[data-article-id]").removeClass("active");
                    $("li[data-article-id=" + response.value.id + "]").addClass("active");

                   Prism.highlightAll();
                },

                error: function(error) {
                    $.loader.hide();
                    $.notify.error(error);
                }
            });
        },

        show: function() {
            $.loader.show();
            this.service.load()
        }
    });

    var CategoryIndexView = fviews.ContentView.extend({
        ctor: function(service) {
            CategoryIndexView.super.ctor.apply(this, arguments);

            var self = this;

            self.content = null;
            self.container = $("#content-container");
            self.service = service;

            self.service.on({
                load: function(response) {
                    updateContent(response, self.container);

                    $("li[data-category-id], li[data-article-id]").removeClass("active");
                    $("li[data-category-id=" + response.value.id + "]").addClass("active");
                },

                error: function(error) {
                    $.loader.hide();
                    $.notify.error(error);
                }
            });
        },

        show: function() {
            $.loader.show();
            this.service.load()
        }
    });

    exports.ArticleView = ArticleView;
    exports.CategoryIndexView = CategoryIndexView;

    return exports;
});