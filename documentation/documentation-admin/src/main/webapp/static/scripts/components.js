/**
 * Applica (www.applicamobile.com).
 * User: bimbobruno
 * Date: 11/09/14
 * Time: 20:02
 * To change this template use File | Settings | File Templates.
 */

define([
    "framework/core",
    "framework/model",
    "framework/views",
    "framework/controllers"], function(core, fmodel, fviews, fcontrollers) {

    var exports = {};

    var HtmlComponent = core.AObject.extend({
        ctor: function(form, element) {
            HtmlComponent.super.ctor.call(this);

            this.form = form;
            this.element = element;
        },

        init: function() {
            var self = this;
            $(this.element).summernote({
                height: "250"
            });

            //override form method
            self.form.on("beforeSubmit", function() {
                $(self.element).val($(self.element).code())
            });
        }
    });

    exports.HtmlComponent = HtmlComponent;

    return exports;
});