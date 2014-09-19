/**
 * Applica (www.applicamobile.com).
 * User: bimbobruno
 * Date: 2/21/13
 * Time: 1:42 PM
 */

define(["framework/core"], function(core) {

    (function ($) {

        window._E = function (el) {
            return $(document.createElement(el));
        };

        jQuery.fn.appendIf = function(condition, el) {
            if(condition) {
                jQuery.fn.append.call(this, el);
            }

            return this;
        };

        var methods = {
            init: function (opts) {
                var options = {
                    min: -1,
                    max: -1
                };

                $.extend(options, opts);

                $(this).data("options", options);
                $(this)
                    .keydown(
                    function (event) {
                        // Allow: backspace, delete, tab, escape, and
                        // enter
                        if (event.keyCode == 46
                            || event.keyCode == 8
                            || event.keyCode == 9
                            || event.keyCode == 27
                            || event.keyCode == 13
                            ||
                            // Allow: Ctrl+A
                            (event.keyCode == 65 && event.ctrlKey === true)
                            ||
                            // Allow: home, end, left, right
                            (event.keyCode >= 35 && event.keyCode <= 39)) {
                            // let it happen, don't do anything
                            return;
                        } else {
                            // Ensure that it is a number and stop the
                            // keypress
                            if (event.shiftKey
                                || (event.keyCode < 48 || event.keyCode > 57)
                                && (event.keyCode < 96 || event.keyCode > 105)) {
                                event.preventDefault();
                            }
                        }
                    });

                $(this).change(function () {
                    var options = $(this).data("options");
                    var value = parseFloat($(this).val());
                    if (options.min != -1 && value < options.min) {
                        $(this).val(options.min);
                    }
                    if (options.max != -1 && value > options.max) {
                        $(this).val(options.max);
                    }
                });

            },

            min: function (value) {
                var options = $(this).data("options");
                options.min = value;
            },

            max: function (value) {
                var options = $(this).data("options");
                options.max = value;
            }
        };

        $.fn.numeric = function (opts) {
            if (methods[opts]) {
                methods[opts].apply(this, Array.prototype.slice.call(arguments, 1));
            } else {
                methods.init.apply(this, arguments);
            }
        };
    })(jQuery);

    /* loader */
    (function ($) {
        var ANIM_TIME = 500;
        var PADDING = 10;
        var element = null;

        function animate(size, position, aElement) {
            var right = position.left + size.width - PADDING - $(aElement).width();
            var left = position.left + PADDING;
            $(aElement).animate({
                left: right + "px"
            }, ANIM_TIME, function () {
                $(aElement).animate({
                    left: left + "px"
                }, ANIM_TIME, function () {
                    animate(size, position, aElement);
                });
            });
        }

        $.loader = {
            show: function (opts) {
                var options = $.extend({
                    parent: window,
                    opacity: 0.2,
                    zIndex: false
                }, opts);

                if ($(options.parent).data("loader-visible"))
                    return;

                element = $(options.parent).data("loader-element");
                $(options.parent).data("loader-visible", true);

                if (!element) {
                    (element = _E("div")).addClass("loader").appendTo("body")
                        .hide();

                    if (options.zIndex) {
                        $(element).css("z-index", options.zIndex);
                    }

                    $(options.parent).data("loader-element", element);
                }

                $(element).show();

                var size, position;
                if (window == options.parent) {
                    size = {
                        width: $(window).width(),
                        height: $(window).height()
                    };
                    position = {
                        top: 0,
                        left: 0
                    };
                } else {
                    size = core.utils.getFullElementSize(options.parent);
                    position = $(options.parent).offset();
                }

                var left = position.left + (size.width / 2 - $(element).width() / 2);
                var top = position.top + (size.height / 2 - $(element).height() / 2);

                $.overlay.show(options);
                $(element).stop().css({
                    top: top + "px",
                    left: left + "px"
                });

                //animate(size, position, element);
            },

            hide: function (opts) {
                var options = $.extend({
                    parent: window,
                    opacity: 0.2
                }, opts);

                element = $(options.parent).data("loader-element");
                if (!element)
                    return;
                if (!$(options.parent).data("loader-visible"))
                    return;
                $(options.parent).data("loader-visible", false);

                if (element) {
                    $.overlay.hide(options);
                    $(element).stop().hide();
                }
            }
        };
    })(jQuery);

    (function ($) {
        $.fn.contextMenu = function (opts) {
            $(this).each(
                function () {
                    var menuOpen = false;
                    var self = this;
                    var firstTimeOpened = true;

                    if (!opts)
                        throw "Options not defined";
                    if (!opts.items)
                        throw "No items";

                    opts = $.extend({
                        position: "bottom-left",
                        open: function () {
                        },
                        close: function () {
                        }
                    }, opts);

                    var menu = _E("div").addClass("contextMenu").hide();
                    var show = function () {
                        $(".contextMenu").hide();

                        menu.slideDown(150);
                        menuOpen = true;
                        if (firstTimeOpened) {
                            // position relative to parent
                            var position = $(self).position();
                            var size = core.utils.getFullElementSize($(self));

                            var top = 0, left = 0;
                            if (opts.position.indexOf("left") != -1) {
                                left = position.left;
                            } else if (opts.position.indexOf("right") != -1) {
                                left = position.left + size.width;
                            }
                            if (opts.position.indexOf("top") != -1) {
                                top = position.top;
                            } else if (opts.position.indexOf("bottom") != -1) {
                                top = position.top + size.height;
                            }

                            $(menu).css({
                                top: top + "px",
                                left: left + "px"
                            });

                            if (menu.position().left + menu.width() > $("body")
                                .width()) {
                                menu.css("right", "5px");
                            }
                        }
                        firstTimeOpened = false;

                        if (opts.show)
                            opts.show.call(this);
                    };
                    var hide = function () {
                        menu.fadeOut(150);
                        menuOpen = false;

                        if (opts.hide)
                            opts.hide.call(this);
                    };

                    for (i in opts.items) {
                        (function (item) {
                            var method = opts.items[item];
                            menu.append(_E("a").attr("href", "javascript:;")
                                .html(item).click(function () {
                                    method.apply(self);
                                    hide();
                                    return false;
                                })

                            );
                        })(i);
                    }

                    $(this).click(function () {
                        if (menuOpen)
                            hide();
                        else
                            show();
                        return false;
                    });
                    $(document).click(function () {
                        if (menuOpen) {
                            hide();
                        }
                    });

                    var ppos = $(this).parent().css("position");
                    if (ppos != "absolute" && ppos != "relative"
                        && ppos != "fixed") {
                        $(this).parent().css("position", "relative");
                    }
                    $(this).parent().append(menu);
                });
            return this;
        };
    }(jQuery));
    /* toolbarSubMenu */
    (function ($) {
        $.fn.toolbarSubMenu = function (opts) {
            $(this).each(
                function () {
                    var menuOpen = false;
                    var self = this;
                    var firstTimeOpened = true;

                    if (!opts)
                        throw "Options not defined";
                    if (!opts.items)
                        throw "No items";

                    opts = $.extend({
                        position: "bottom-left",
                        open: function () {
                        },
                        close: function () {
                        }
                    }, opts);

                    var menu = _E("div").addClass("toolbarSubMenu").hide();
                    var show = function () {
                        $(".toolbarSubMenu").slideUp();

                        menu.slideDown(150);
                        menuOpen = true;
                        if (firstTimeOpened) {
                            // position relative to parent
                            var position = $(self).position();
                            var size = core.utils.getFullElementSize($(self));

                            var top = 0, left = 0;
                            if (opts.position.indexOf("left") != -1) {
                                left = position.left;
                            } else if (opts.position.indexOf("right") != -1) {
                                left = position.left + size.width;
                            }
                            if (opts.position.indexOf("top") != -1) {
                                top = position.top;
                            } else if (opts.position.indexOf("bottom") != -1) {
                                top = position.top + size.height;
                            }

                            $(menu).css({
                                top: top + "px",
                                left: left + "px"
                            });

                            if (menu.position().left + menu.width() > $("body")
                                .width()) {
                                menu.css("right", "5px");
                            }
                        }
                        firstTimeOpened = false;

                        if (opts.show)
                            opts.show.call(this);
                    };
                    var hide = function () {
                        menu.slideUp(150);
                        menuOpen = false;

                        if (opts.hide)
                            opts.hide.call(this);
                    };

                    var size = opts.items.length;
                    for (var i = 0; i < size; i++) {
                        var item = opts.items[i];
                        var entry = _E("a").attr("href", "javascript:;").data(
                                'd', item).text(item.label).click(function () {
                                $(this).data('d').command();
                                hide();
                                return false;
                            });
                        if (item.icon) {
                            entry.prepend(_E('img').attr('src',
                                    IMAGES_BASE + 'menu/' + item.icon).attr(
                                    'alt', item.icon));
                        }
                        menu.append(entry);
                    }

                    $(this).click(function () {
                        if (menuOpen)
                            hide();
                        else
                            show();
                        return false;
                    });
                    $(document).click(function () {
                        if (menuOpen) {
                            hide();
                        }
                    });

                    var ppos = $(this).parent().css("position");
                    if (ppos != "absolute" && ppos != "relative" && ppos != "fixed") {
                        $(this).parent().css("position", "relative");
                    }
                    $(this).parent().append(menu);
                });
            return this;
        };
    }(jQuery));

    /* selectList */
    (function ($) {
        $.fn.selectList = function (opts) {
            var main = null;
            var selectionContainer = null;
            var options = null;

            function fixValue(value) {
                return value.replace("'", "\\\'");
            }

            function createSelection(label, value, group) {
                var selection = $("<a />")
                    .addClass("selection")
                    .attr("href", "javascript:;")
                    .attr("data-value", value)
                    .attr("data-selected", false)
                    .attr('data-group', group)
                    .attr("id", "sel_" + value)
                    .html(label)
                    .click(function () {
                        if (options.multiselect) {
                            var isSelected = $(this).attr(
                                "data-selected") == "true";
                            if (isSelected) {
                                $(this).attr("data-selected", false)
                                    .removeClass("selected")
                                    .find(".selected-icon").hide();
                            } else {
                                $(this).attr("data-selected", true)
                                    .addClass("selected")
                                    .find(".selected-icon").show();
                            }
                        } else {
                            $(selectionContainer).find(
                                    ".selection[data-selected=true]")
                                .attr("data-selected", false)
                                .removeClass("selected")
                                .find(".selected-icon").hide();
                            $(this).attr("data-selected", true)
                                .addClass("selected")
                                .find(".selected-icon").show();

                        }
                    })
                    .prepend(_E("i").addClass("glyphicon glyphicon-icon-check selected-icon").css("margin-right", "3px").hide());

                return selection;
            }

            function createGroup(label) {
                var group = $('<a />')
                    .addClass('group')
                    .attr('href', 'javascript:;')
                    .html(label)
                    .click(function () {
                        var group = $(this).text();
                        $(selectionContainer).find('.selection[data-group="' + group + '"]').click();
                    });

                return group;
            }

            function selectAll() {
                $(selectionContainer).find('.selection').click();
            }

            function synchSelection() {
                $(selectionContainer).empty();
                $(main).find("input[type=hidden][name=" + options.name + "]").each(
                    function (i, input) {
                        $(selectionContainer).append(
                            createSelection($(input).attr("data-label"), $(
                                input).val(), $(input).attr(
                                'data-group')));
                    });
            }

            function synchHiddens() {
                if ($(main).find('.group').length == 0)
                    $(main).find("input[type=hidden][name=" + options.name + "]")
                        .remove();
                $(selectionContainer).find(".selection").each(
                    function (i, selection) {
                        $(main).append(
                            _E("input").attr("type", "hidden").attr(
                                    "data-label", $(selection).text()).val(
                                    $(selection).attr("data-value")).attr(
                                    "name", options.name));
                    });
            }

            function storeReferences(element) {
                $(element).data("selectList_options", options);
            }

            function restoreReferences(element) {
                main = element;
                options = $(element).data("selectList_options");
                selectionContainer = $(main).find(".selectionContainer");
            }

            var methods = {
                init: function (opts) {
                    main = this;
                    options = {
                        allowDuplicates: false,
                        multiselect: true
                    };
                    $.extend(options, opts);

                    if (!opts.name)
                        throw "Name not specified";

                    selectionContainer = _E("div")
                        .addClass("selectionContainer")
                        .addClass("custom-input");

                    $(this).addClass("selectList").append(selectionContainer);

                    synchSelection();

                    storeReferences(main);
                },

                add: function (item) {
                    restoreReferences(this);
                    if (!options.allowDuplicates)
                        if ($(selectionContainer).find(".selection[data-value='" + fixValue(item.value) + "']").size() > 0)
                            return;

                    $(selectionContainer).append(createSelection(item.label, item.value));

                    synchHiddens();

                    $(this).trigger("change");
                },

                addAll: function (items) {
                    restoreReferences(this);
                    var group = null;
                    $(items).each(
                        function (i, item) {
                            if (!options.allowDuplicates && item.group == null)
                                if ($(selectionContainer).find(".selection[data-value='" + fixValue(item.value) + "']").size() > 0)
                                    return;

                            if (group == null || group != item.group) {
                                group = item.group;
                                if (!core.utils.stringIsNullOrEmpty(group)) {
                                    $(selectionContainer).append(createGroup(group));
                                }
                            }
                            $(selectionContainer).append(createSelection(item.label, item.value, item.group));
                        });

                    synchHiddens();

                    $(this).trigger("change");
                },

                removeItem: function (value) {
                    restoreReferences(this);
                    $(selectionContainer).find(".selection[data-value='" + fixValue(value) + "']").remove();

                    synchHiddens();

                    $(this).trigger("change");
                },

                removeSelection: function (value) {
                    restoreReferences(this);
                    $(selectionContainer).find(".selection[data-selected=true]").remove();

                    synchHiddens();

                    $(this).trigger("change");
                },

                clear: function () {
                    restoreReferences(this);
                    $(selectionContainer).find(".selection").remove();
                    synchHiddens();

                    $(this).trigger("change");
                },

                getSelectedData: function () {
                    restoreReferences(this);
                    var data = [];
                    $(selectionContainer).find(".selection[data-selected=true]")
                        .each(function (i, res) {
                            data.push({
                                label: $(res).text(),
                                value: $(res).attr("data-value")
                            });
                        });

                    return data;
                },

                getData: function () {
                    restoreReferences(this);
                    var data = [];
                    $(selectionContainer).find(".selection").each(function (i, res) {
                        data.push({
                            label: $(res).text(),
                            value: $(res).attr("data-value")
                        });
                    });

                    return data;
                },

                selectAll: function () {
                    restoreReferences(this);
                    selectAll();
                }
            };

            if (methods[opts]) {
                return methods[opts].apply(this, Array.prototype.slice.call(
                    arguments, 1));
            } else {
                methods.init.apply(this, arguments);
            }
        };
    })(jQuery);

    /* masterDetail */
    (function ($) {
        $.masterDetail = {
            defaultBinder: {
                toForm: function (form, item) {
                    if (item) {
                        for (var key in item) {
                            $(form).find("input[data-bind='" + key + "']").val(
                                item[key]);
                        }
                    } else {
                        $(form).find("input[data-bind]").val("");
                    }
                },

                toItem: function (form, item) {
                    $(form).find("input[data-bind]").each(function () {
                        var bind = $(this).attr("data-bind");
                        item[bind] = $(this).val();
                    });
                },

                validate: function (form, item, errors) {
                }
            }
        };

        $.fn.masterDetail = function (opts) {
            var main = null;
            var options = null;
            var editingItem = null;
            var isNewItem = false;

            var binder = $.masterDetail.defaultBinder;

            function storeReferences(element) {
                $(element).data("masterDetail_options", options);
            };

            function restoreReferences(element) {
                main = element;
                options = $(element).data("masterDetail_options");
            };

            function createDialog() {
                $(options.form)
                    .dialog(
                    {
                        appendTo: main,
                        zIndex: 10000,
                        autoOpen: false,
                        modal: true,
                        width: $(options.form).attr("data-width") || 640,
                        height: $(options.form).attr("data-height") || 480,
                        title: options.formTitle,
                        resizable: false,
                        buttons: {
                            OK: function () {
                                options.binder.toItem(options.form,
                                    editingItem);
                                if (options.binder.validate) {
                                    var errors = [];
                                    options.binder.validate(
                                        options.form, editingItem,
                                        errors);
                                    if (errors.length != 0) {
                                        var errorString = "";
                                        for (var i = 0; i < errors.length; i++) {
                                            errorString += errors[i]
                                                + "\r\n";
                                        }
                                        alert(errorString);
                                        return;
                                    }
                                }
                                if (isNewItem) {
                                    addItemToList(editingItem);
                                } else {
                                    replaceSelectedListItem(editingItem);
                                }
                                $(this).dialog("close");
                            },
                            Annulla: function () {
                                $(this).dialog("close");
                            }
                        }
                    });
            }
            ;

            function addItemToList(item) {
                var json = JSON.stringify(item);
                $(main).selectList("add", {
                    label: options.labelFormatter(item),
                    value: json
                });
            };

            function replaceSelectedListItem(item) {
                $(main).selectList("removeSelection");
                addItemToList(item);
            };

            function createButtons() {
                var buttonsContainer = _E("div").addClass("buttonsContainer");

                // add buttons
                $(buttonsContainer)
                    .append(
                        _E("input").attr("type", "button").attr("value",
                                "Aggiungi").click(function () {
                                isNewItem = true;
                                editingItem = {};
                                options.binder.toForm(options.form, null);
                                $(options.form).dialog("open");
                            }))
                    .append(
                        _E("input")
                            .attr("type", "button")
                            .attr("value", "Modifica")
                            .click(
                            function () {
                                isNewItem = false;
                                var items = $(main).selectList(
                                    "getSelectedData");
                                if (items.length > 0) {
                                    editingItem = $
                                        .parseJSON(items[0].value);
                                    options.binder.toForm(
                                        options.form,
                                        editingItem);
                                    $(options.form).dialog("open");
                                } else {
                                    alert("Nessun elemento selezionato");
                                }
                            })).append(
                        _E("input").attr("type", "button").attr("value",
                                "Rimuovi").click(function () {
                                $(main).selectList("removeSelection");
                            }));

                $(main).append(buttonsContainer);
            };

            var methods = {
                init: function (opts) {
                    main = this;
                    options = {
                        labelFormatter: function (item) {
                            return item.label;
                        },
                        form: null,
                        name: null,
                        formTitle: "Create/Edit",
                        binder: binder
                    };
                    $.extend(options, opts);

                    if (!options.name)
                        throw "Name not specified";
                    if (!options.form)
                        throw "Form not specified";

                    $(this).addClass("masterDetail");
                    $(this).selectList({
                        name: options.name,
                        allowDuplicates: true,
                        multiselect: false
                    });

                    createDialog();
                    createButtons();

                    $(main).append($("<div style='clear: both;'></div>"));

                    storeReferences(main);
                }
            };

            if (methods[opts]) {
                return methods[opts].apply(this, Array.prototype.slice.call(
                    arguments, 1));
            } else {
                methods.init.apply(this, arguments);
            }
        };
    })(jQuery);

    /* searchableInput */
    (function ($) {
        $.fn.searchableInput = function (opts) {
            var main = null;
            var buttonsContainer = null;
            var keywordTextbox = null;
            var resultsDiv = null;
            var dialog = null;
            var options = null;
            var resultsIsSelectList = false;
            var singleValue = null;
            var mainText = null;

            function fixValue(value) {
                return value.replace("'", "\\\'");
            }

            function createDialog() {
                keywordTextbox = _E("input")
                    .attr("type", "text")
                    .addClass("keyword")
                    .addClass("form-control")
                    .keyup(function (event) {
                        if (event.which == 13) {
                            doSearch();
                            event.preventDefault();

                        }
                    });

                resultsDiv = _E("div");
                resultsDiv.html(msg.MSG_SEARCHABLEINPUT_HELP);

                dialog = _E("div")
                    .addClass("searchableInputDialog")
                    .append(keywordTextbox)
                    .append(resultsDiv);

                var buttons = {
                    OK: {
                        primary: true,
                        command: function () {
                            var items = $(resultsDiv).selectList("getSelectedData");
                            if (options.mode == "multi") {
                                $(main).selectList("addAll", items);
                            } else if (options.mode == "single") {
                                setSingleValue(items[0]);
                            }
                            $(this).modalDialog("close");
                        }
                    }
                };

                if (options.mode == 'multi') {
                    buttons[msg.TOOLBAR_SELECT_ALL] = {
                        action: true,
                        command: function () {
                            $(resultsDiv).selectList("selectAll");
                        }
                    };
                }
                $(dialog).modalDialog({
                    height: 385,
                    width: 260,
                    autoOpen: false,
                    title: options.title || "Selezionare",
                    buttons: buttons,
                    close: function () {
                        $(main).focus();
                    }
                });

                $(main).data("searchableInput_dialog", dialog);
            }

            function doSearch() {
                if (!resultsIsSelectList) {
                    $(resultsDiv).empty().selectList({
                        name: "results_" + options.name,
                        allowDuplicates: false,
                        multiselect: options.mode == "multi"
                    });
                    resultsIsSelectList = true;
                }

                var url = options.serviceUrl;
                var keyword = $(keywordTextbox).val();

                $.post(url, {
                    keyword: keyword
                }, function (response) {
                    $(resultsDiv).selectList("clear");
                    if (response && !response.error) {
                        $(resultsDiv).selectList("addAll", response.value);
                    } else {
                        alert("Errore durante il recupero dei dati: " + response.error);
                    }
                }, "json");
            }

            function initMulti() {
                $(main).selectList(options);

                buttonsContainer = _E("div")
                    .addClass("inline")
                    .addClass("buttonsContainer");

                // add buttons
                $(buttonsContainer)
                    .append(
                        _E("input").attr("type", "button")
                            .attr("value", "Aggiungi")
                            .addClass("btn btn-small btn-block btn-primary")
                            .click(function () {
                                $(dialog).modalDialog("open");
                                if (!main.autoloaded
                                    && options.autoloadResults) {
                                    main.autoloaded = true;
                                    doSearch();
                                }
                            })
                    )
                    .append(
                        _E("input")
                            .attr("type", "button")
                            .attr("value", "Rimuovi")
                            .addClass("btn btn-small btn-block")
                            .click(function () {
                                $(main).selectList("removeSelection");
                            })
                    );

                if (opts.allowCustom) {
                    $(buttonsContainer)
                        .append(
                            _E("input")
                                .attr("type", "button")
                                .addClass("btn btn-small btn-block")
                                .attr("value", "Altro")
                                .css("margin-top", "10px")
                                .click(
                                function () {
                                    var customItem = prompt("Specificare altro");
                                    if (customItem) {
                                        $(main)
                                            .selectList(
                                            "add",
                                            {
                                                label: customItem,
                                                value: customItem
                                            });
                                    }
                                }));
                }

                // clear floating divs
                $(main)
                    .addClass("searchableInput")
                    .addClass("searchableInputMulti")
                    .attr("tabindex", 0)
                    .append(buttonsContainer)
                    .append(_E("div")
                        .css("clear", "both")
                    );
            }

            function initSingle() {
                singleValue = $(main).find("input[type=hidden][name=" + options.name + "]");
                if ($(singleValue).size() == 0) {
                    singleValue = _E("input").attr("name", options.name).attr("type", "hidden").appendTo(main);
                }

                mainText = _E("span").addClass("mainText").appendTo(main);
                var self = this;
                $(main)
                    .addClass("searchableInput")
                    .addClass("searchableInputSingle")
                    .attr("tabindex", 0)
                    .keyup(function (e) {
                        if (e.which == 13) {
                            $(dialog).modalDialog("open");
                            e.preventDefault();
                        }
                    })
                    .append(_E('button')
                        .addClass("btn btn-xs btn-warning pull-right")
                        .click(function (e) {
                            e.preventDefault();
                            setSingleValue({
                                value: '',
                                label: 'NA'
                            });
                            return false;
                        })
                        .append(_E("i")
                            .addClass("glyphicon glyphicon-remove")
                            .addClass("pull-right")
                            .css("height", 18)
                            .css("padding-top", 1)
                            .text("")
                        )
                    )
                    .click(function () {
                        if (!main.autoloaded && options.autoloadResults) {
                            main.autoloaded = true;
                            doSearch();
                        }
                        $(dialog).modalDialog("open");
                    });

                syncSingleLabel();
            }

            function setSingleValue(value) {
                $(singleValue).val(value.value);
                $(mainText).text(value.label);
            }

            function syncSingleLabel() {
                var label = $(singleValue).attr("data-label");
                $(mainText).text(label);
            }

            var methods = {
                init: function (opts) {
                    if (!opts.serviceUrl)
                        throw "Service url not specified";
                    if (!opts.name)
                        throw "Name not specified";

                    main = this;
                    options = $.extend({
                        mode: "multi",
                        allowDuplicates: false,
                        allowCustom: false,
                        autoloadResults: true
                    }, opts);

                    createDialog();

                    if (options.mode == "multi") {
                        initMulti();
                    } else if (options.mode == "single") {
                        initSingle();
                    }
                },

                destroy: function() {
                    var dialog = $(this).data("searchableInput_dialog");
                    if(dialog) {
                        $(dialog).modalDialog("destroy");
                    }
                }
            };

            if (methods[opts]) {
                methods[opts].apply(this, Array.prototype.slice.call(arguments, 1));
            } else {
                methods.init.apply(this, arguments);
            }
        };
    }(jQuery));

    /* notifications */
    (function ($) {
        $.noty.defaults.layout = "topRight";
        $.noty.defaults.animation.speed = 350;

        function doNoty(message, type, buttons) {
            var fn = null;
            if($.isFunction(buttons)) {
                fn = buttons;
                buttons = null;
            }
            noty({text: message, type: type, dismissQueue: true, timeout: $.notify.timeout, buttons: buttons, callback: { onClick: fn }});
        }

        $.notify = function (message, buttons) {
            doNoty(message, "notification", buttons);
        };

        $.notify.error = function (message, buttons) {
            doNoty(message, "error", buttons);
        };

        $.notify.warn = function (message, buttons) {
            doNoty(message, "warning", buttons);
        };

        $.notify.alert = function (message, buttons) {
            doNoty(message, "alert", buttons);
        };

        $.notify.success = function (message, buttons) {
            doNoty(message, "success", buttons);
        };

        $.notify.info = function (message, buttons) {
            doNoty(message, "information", buttons);
        };

        $.notify.timeout = 3000;
    })(jQuery);

// municipality
    (function ($) {
        $.fn.municipality = function (options) {
            var self = this;
            (function (opts) {
                if (!opts)
                    throw "Please specify options";
                if (!opts.name)
                    throw "Name is required";

                // create hidden input with the name
                var hidden = _E("input").attr("type", "hidden").attr("name",
                    opts.name).val($(self).attr("data-initialValue"));

                $(self).after(hidden);

                $(self).autocomplete({
                    source: BASE + "/data/municipalities/search",
                    minLength: 2,
                    select: function (e, ui) {
                        hidden.val(ui.item.id);
                        if (opts && opts.postalCode) {
                            $(opts.postalCode).val(ui.item.postalCode);
                        }
                    }
                });
            })(options);
        };
    }(jQuery));

// custom tabs
    (function ($) {
        $.fn.ctabs = function (options) {
            var self = this;
            (function (opts) {
                var tabContainers = $(self).children("div");
                tabContainers.hide().filter(':first').show();
                $(self).addClass("ctabs");
                $(self).find('> ul a').click(function () {
                    tabContainers.hide();
                    tabContainers.filter(this.hash).show();
                    $(self).find('ul a').removeClass('selected');
                    $(this).addClass('selected');
                    return false;
                }).filter(':first').click();
            })(options);
        };
    }(jQuery));

// custom overlay
    (function ($) {
        var counter = 0;
        var element = null;

        function measure(options) {
            element = $(options.parent).data("modal-overlay-element");
            var bounds = null;
            if (window == options.parent || $(options.parent).get(0).tagName.toLowerCase() == "body") {
                var windowBounds = core.utils.bounds($(window));
                var bodyBounds = core.utils.bounds($("body"));
                if(bodyBounds.height > windowBounds.height) {
                    bounds = bodyBounds;
                } else {
                    bounds = windowBounds;
                }
            } else {
                bounds = core.utils.bounds(options.parent);
            }
            $(element).css({
                top: bounds.top + "px",
                left: bounds.left + "px",
                height: bounds.height + "px",
                width: bounds.width + "px"
            });
        }

        var Overlay = {
            isActive: false,

            createIfNotExists: function (options) {
                element = $(options.parent).data("modal-overlay-element");

                if (!element) {
                    (element = _E("div")).addClass("modalOverlay").appendTo(
                        $("body"));

                    if (options.zIndex) {
                        $(element).css("z-index", options.zIndex);
                    }

                    $(options.parent).data("modal-overlay-element", element);
                    $(options.parent).data("modal-overlay-counter", 1);

                    /*
                     * $(window).resize(function() { measure(options); });
                     */
                }

            },

            show: function (options) {
                var defaultParent = $(window).height() > $("body").height() ? window : $("body");

                options = $.extend({
                    animate: false,
                    opacity: 0.5,
                    click: null,
                    closeOnClick: false,
                    parent: defaultParent,
                    zIndex: false
                }, options);

                counter = $(options.parent).data("modal-overlay-counter");
                if (!counter)
                    counter = 0;

                counter++;
                $(options.parent).data("modal-overlay-counter", counter);
                if (counter == 1) {
                    Overlay.createIfNotExists(options);

                    $(element).unbind("click").bind("click", function () {
                        if (options.closeOnClick) {
                            $.overlay.hide(options);
                        }
                        if (options.click) {
                            $.proxy(options.click, this)(arguments);
                        }
                    });

                    if (options.animate) {
                        $(element).stop().fadeTo(250, options.opacity);
                    } else {
                        $(element).css("opacity", options.opacity).show();
                    }

                    Overlay.isActive = true;
                    measure(options);
                }
            },

            hide: function (options) {
                var defaultParent = $(window).height() > $("body").height() ? window : $("body");

                options = $.extend({
                    animate: false,
                    parent: defaultParent
                }, options);

                element = $(options.parent).data("modal-overlay-element");
                counter = $(options.parent).data("modal-overlay-counter");
                if (!counter)
                    counter = 0;
                counter--;
                $(options.parent).data("modal-overlay-counter", counter);
                if (counter < 0) {
                    $(options.parent).data("modal-overlay-counter", 0);
                    throw "$.overlay.hide() is called many times";
                }

                if (counter == 0) {
                    Overlay.isActive = false;

                    if (options.animate) {
                        $(element).stop().fadeOut(250);
                    } else {
                        $(element).hide();
                    }
                }
            }
        };

        $.overlay = Overlay;
    })(jQuery);

    /* remoteModalDialog */
    (function ($) {
        $.remoteModalDialog = function (opts) {

            function checkContainer() {
                if ($("#remoteModalDialogContainer").size() == 0) {
                    $("body").append(
                        _E("div").addClass("remoteModalDialogContainer").attr(
                            "id", "remoteModalDialogContainer").hide());
                }
            }

            self = this;

            var options = $.extend({
                url: null,
                method: "GET",
                data: {},
                complete: function (content) {
                },
                error: function () {
                }
            }, opts);

            if (!options.url)
                throw "remoteModalDialog.init(): Please specify an url";
            checkContainer();

            $.loader.show();
            $.ajax({
                type: options.method,
                data: options.data,
                url: opts.url,
                success: function (response) {
                    $.loader.hide();

                    var content = _E("div").html(response);
                    $("#remoteModalDialogContainer").empty().append(content);

                    $(content).modalDialog(options);

                    if (options.complete)
                        options.complete(content);
                },
                error: function () {
                    $.loader.hide();

                    if (options.error)
                        options.error();
                }
            });
        };
    })(jQuery);

    /* custom modal dialog */
    (function ($) {
        var MAX_HEIGHT_PERCENTUAL = 80;
        var MAX_WIDTH_PERCENTUAL = 80;

        var utils = core.utils;

        var STACK = [];
        var CURRENT = null;

        $.modalDialog = {
            closeAll: function() {
                if(STACK.length > 0) {
                    var previous = STACK[STACK.length - 1];
                    $(previous).on("close", function() {
                        $.modalDialog.closeAll();
                    });

                    $(previous).modalDialog("close");
                }
            }
        };

        $.fn.modalDialog = function (opts) {
            //var resizeHandler = null;
            var self = null;
            var options = null;
            var data = {};

            function storeReferences(element) {
                $(element).data("modalDialog_options", options);
                $(element).data("modalDialog_data", data);
            }

            function restoreReferences(element) {
                self = element;
                options = $(element).data("modalDialog_options");
                data = $(element).data("modalDialog_data");
            }

            function measure(animate) {
                var computedContentSize = core.utils.bounds(data.dialogBody);
                var contentHeight = computedContentSize.height;
                var contentWidth = computedContentSize.width;
                var contentHeightBorders = contentHeight - $(data.dialogBody).height();

                var wrapperHeight = core.utils.bounds(data.dialogContent).height;
                var heightDelta = wrapperHeight - contentHeight;
                var maxHeight = 0;

                var containerHeight = 0;
                if (options.height) {
                    containerHeight = options.height;
                    maxHeight = containerHeight - heightDelta;
                } else {
                    maxHeight = Math.min(($(window).height() - heightDelta) / 100 * MAX_HEIGHT_PERCENTUAL, contentHeight);
                    containerHeight = maxHeight + heightDelta;
                }
                maxHeight += 5;

                var maxWidth = 0;

                var containerWidth= 0;
                if (options.width) {
                    containerWidth = options.width;
                    maxWidth = containerWidth;
                } else {
                    maxWidth = Math.min(($(window).width() ) / 100 * MAX_WIDTH_PERCENTUAL, contentWidth);
                    containerWidth = maxWidth;
                }

                $(data.dialogBody)
                    .height((maxHeight - contentHeightBorders) + "px")
                    .width(containerWidth + "px");

                var containerSize = core.utils.bounds(data.dialog);
                var top = ($(window).height() / 2 - containerSize.height / 2);
                var left = ($(window).width() / 2 - containerSize.width / 2);

                $(data.dialog)
                    .css("top", top + "px")
                    .css("left", left + "px");

            }

            function invokeCloseEvents() {
                if($.isFunction(options.close)) {
                    options.close.call(self, data.dialogResult || "cancel");
                }

                $(self).trigger("close");

                if($.isFunction(options.hide)) {
                    options.hide.call(self, data.dialogResult || "cancel");
                }

                $(self).trigger("hide");
            }

            var methods = {
                init: function (opts) {
                    self = this;
                    var width = null;
                    var height = null;

                    if (opts.fitToScreen) {
                        var top = 10, left = 10;
                        width = $(document).width() - left * 2;
                        height = $(document).height() - top * 2;
                    }

                    options = $.extend({
                        autoOpen: false,
                        width: width,
                        height: height,
                        close: null,
                        destroyOnClose: false,
                        title: "Modal dialog",
                        buttons: {},
                        cancelButton: msg.LABEL_CLOSE,
                        cancel: null
                    }, opts);

                    var template = $(
                        '<div class="framework-modal" tabindex="-1">' +
                            '<div class="modal-dialog" style="width: auto;">' +
                            '<div class="modal-content">' +
                            '<div class="modal-header">' +
                            '<button type="button" class="close">&times;</button>' +
                            '<h4 class="modal-title">' + options.title + '</h4>' +
                            '</div>' +
                            '<div class="modal-body">' +
                            '</div>' +
                            '<div class="modal-footer">' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '</div>');

                    var close = $(template).find("button.close");
                    var dialogBody = $(template).find(".modal-body");
                    var footer = $(template).find(".modal-footer");

                    if (options.cancelButton) {
                        _E("button")
                            .text(options.cancelButton)
                            .addClass("btn btn-default")
                            .click(function () {
                                $(self).modalDialog("close");
                            }).appendTo(footer);
                    }

                    $(close).click(function() {
                        self.modalDialog("close");
                    });

                    for (k in options.buttons) {
                        (function(key) {
                            var label = key;
                            var command, action, primary, type;
                            if($.isFunction(options.buttons[key])) {
                                command = options.buttons[key];
                                action = false;
                                primary = false;
                            } else {
                                command = options.buttons[key].command;
                                action = options.buttons[key].action;
                                primary = options.buttons[key].primary;
                                label = options.buttons[key].label || label;
                                type = options.buttons[key].type;
                            }

                            var buttonElement = _E("button")
                                .addClass("btn")
                                .text(label)
                                .click(function () {
                                    command.call(self);
                                    data.dialogResult = key;
                                })
                                .appendTo(footer);

                            if(primary) {
                                buttonElement.addClass("btn-primary");
                            }

                            if(action) {
                                buttonElement.addClass("btn-link pull-left");
                            }

                            if(type) {
                                buttonElement.addClass("btn-" + type);
                            }
                        })(k);

                    }

                    dialogBody.append(self);

                    $("body").append(template);

                    storeReferences(self);

                    data.dialog = template;
                    data.dialogBody = dialogBody;
                    data.dialogContent = template.find(".modal-content");
                    data.dialogHeader = template.find(".modal-header");
                    data.dialogFooter = footer;

                    if(options.autoOpen){
                        $(self).modalDialog("open");
                    }
                },

                setTitle: function (title) {
                    restoreReferences(this);
                    if (!data)
                        return;
                    $(data.template).find(".modal-title").text(title);
                },

                is: function () {
                    restoreReferences(this);
                    return data != null && data != undefined;
                },

                updateSize : function() {
                    restoreReferences(this);
                    if (!data)
                        return;

                    measure();
                },

                open: function() {
                    restoreReferences(this);
                    if (!data)
                        return;

                    $(data.dialog).show();

                    data.isActive = true;

                    measure();

                    STACK.push(self);

                    if(STACK.length > 1) {
                        var previous = STACK[STACK.length - 2];
                        var previousDialog = $(previous).data("modalDialog_data").dialog;
                        previousDialog
                            .animate({
                                top: $(window).height()
                            }, 250);
                    } else {
                        $.overlay.show({
                            closeOnClick : false,
                            click : function() {
                                var current = STACK[STACK.length - 1];
                                current.dialogResult = "cancel";
                                $(current).modalDialog("close");
                            }
                        });
                    }

                    var top = parseInt($(data.dialog).css("top"));

                    var opacity = 1;

                    if(STACK.length == 1) {
                        opacity = 0;
                    }

                    $(data.dialog)
                        .css("opacity", opacity)
                        .css("top", -$(data.dialog).height() + "px").stop()
                        .animate({
                            top : top + "px",
                            opacity: 1
                        }, 250, function(){
                            if (options.shown)
                                options.shown.call();
                        });

                },

                close : function() {
                    restoreReferences(this);
                    var self = this;
                    if (!data)
                        return;

                    STACK.pop();

                    if(STACK.length > 0) {
                        var previous = STACK[STACK.length - 1];
                        var previousDialog = $(previous).data("modalDialog_data").dialog;
                        var top = $(window).height() / 2 - $(previousDialog).height() / 2;

                        previousDialog.animate({
                            top: top + "px"
                        }, 250);
                    }

                    var opacity = 1;
                    if(STACK.length == 0) {
                        opacity = 0;
                    }

                    $(data.dialog).stop().animate({
                        top : -$(data.dialog).height() + "px",
                        opacity: opacity
                    }, 250, function() {
                        data.isActive = false;

                        $(data.dialog).hide();

                        if(STACK.length == 0) {
                            $.overlay.hide();
                        }

                        invokeCloseEvents();

                        if (options.destroyOnClose) {
                            $(self).modalDialog("destroy");
                        }
                    });
                },

                destroy: function () {
                    restoreReferences(this);
                    var self = this;
                    if (!data)
                        return;

                    $(data.dialog).remove();
                }
            };

            if (methods[opts]) {
                return methods[opts].apply(this, Array.prototype.slice.call(
                    arguments, 1));
            } else {
                methods.init.apply(this, arguments);
            }
            return this;
        };
    })(jQuery);

    (function ($) {
        function FormTabs() {
        };

        FormTabs.prototype = {
            init: function (element, options) {
                this.element = element;
                this.options = $.extend({

                }, options);
                $(element).addClass('formTabs');
            },
            add: function (tab) {
                if (tab.visible && !tab.visible())
                    return;
                var self = this;
                var tabElement = _E('li').append(
                    _E('a').attr('href', 'javascript:;').text(tab.label).data(
                            'tab', tab).click(function () {
                            var tab = $(this).data('tab');
                            if (tab.command)
                                tab.command();
                        })).appendTo(self.element);
                if (tab.selected)
                    tabElement.addClass('selected');
            },
            clear: function () {
                $(this.element).empty();
            },
            addAll: function (tabs) {
                var self = this;
                var size = tabs.length;
                for (var i = 0; i < size; i++) {
                    var tab = tabs[i];
                    self.add(tab);
                }
            }
        };

        $.fn.formTabs = function (options) {
            var tabs = $(this).data("formTabs");
            if (!tabs) {
                tabs = new FormTabs();
                $(this).data("formTabs", tabs);
                tabs.init(this, options);
            } else {
                var method = FormTabs.prototype[options];
                if (method) {
                    method.apply(tabs, Array.prototype.slice.call(arguments, 1));
                }
            }
        };
    })(jQuery);

    /* toolbar */
    (function ($) {

        function Toolbar() {};

        Toolbar.prototype = {
            init: function (element, options) {
                this.element = element;
                this.options = $.extend({

                }, options);

                $(element).addClass("toolbar");
            },

            adds: {
                button: function(button) {
                    var liElement = _E("li")
                        .attr('group', button.group)
                        .appendTo(this.element);

                    var buttonElement = _E("a")
                        .attr("href", "javascript:;")
                        .appendTo(liElement);

                    if (button.icon) {
                        $(buttonElement).prepend(
                            _E("i")
                                .addClass("glyphicon glyphicon-" + button.icon)
                        );
                    }

                    if(button.text) {
                        $(buttonElement).append(" " + button.text);
                    }

                    $(buttonElement).click(function () {
                        if (button.command)
                            button.command();
                    });

                    if (button.hidden) {
                        liElement.hide();
                    }
                },

                menu: function(button) {
                    var liElement = _E("li")
                        .attr('group', button.group)
                        .addClass("dropdown")
                        .appendTo(this.element);

                    var buttonElement = _E("a")
                        .addClass("dropdown-toggle")
                        .attr("data-toggle", "dropdown")
                        .attr("href", "javascript:;")
                        .appendTo(liElement);

                    if (button.icon) {
                        $(buttonElement).prepend(
                            _E("i")
                                .addClass("glyphicon glyphicon-" + button.icon)
                        );
                    }

                    if(button.text) {
                        $(buttonElement).append(" " + button.text);
                    }

                    $(buttonElement).append(_E("b").addClass("caret"));

                    if(!button.items) {
                        throw "Please specify items for menu type toolbar buttons";
                    }

                    var itemsElement = _E("ul")
                        .addClass("dropdown-menu")
                        .appendTo(liElement);

                    core.utils.each(button.items, function(item) {
                        var itemElement = _E("li")
                            .append(
                                _E("a")
                                    .attr("href", "javascript:;")
                                    .text(item.label)
                                    .click(function() {
                                        if($.isFunction(item.command)) {
                                            item.command.call(liElement);
                                        }
                                    })
                            )
                            .appendTo(itemsElement);

                        if(item.important) {
                            itemElement.addClass("important");
                        }
                    });

                    if (button.hidden) {
                        liElement.hide();
                    }
                }
            },

            add: function (button) {
                if(!button.type) {
                    button.type = "button";
                }

                var addFn = this.adds[button.type];
                if(addFn) {
                    addFn.call(this, button);
                }
            },

            showGroups: function (groups) {
                var self = this;
                $(self.element).find('li[group~="' + groups + '"]').each(function () {
                    if (!$(this).is(":visible")) {
                        $(this).stop().show("slide", 250);
                    }
                });
            },

            hideGroups: function (groups) {
                var self = this;
                $(self.element).find('li[group~="' + groups + '"]').each(function () {
                    if ($(this).is(":visible")) {
                        $(this).stop().hide("slide", 250);
                    }
                });
            },

            clear: function () {
                $(this.element).empty();
            }
        };

        $.fn.toolbar = function (options) {
            var toolbar = $(this).data("toolbar");
            if (options === "isToolbar")
                return toolbar ? true : false;

            if (!toolbar) {
                toolbar = new Toolbar();
                $(this).data("toolbar", toolbar);

                toolbar.init(this, options);
            } else {
                var method = Toolbar.prototype[options];
                if (method) {
                    method.apply(toolbar, Array.prototype.slice.call(arguments, 1));
                }
            }
        };

    })(jQuery);

    /* btnbar */
    (function ($) {

        function Btnbar() {};

        Btnbar.prototype = {
            init: function (element, options) {
                this.element = element;
                this.options = $.extend({

                }, options);

                $(element).addClass("btnbar");
            },

            getGroup: function(group) {
                if(!group) {
                    group = "_default_";
                }

                var ge = $(this.element).find(core.utils.format("div[data-group='{0}']", group));
                if(ge.size() == 0) {
                    ge = _E("div")
                        .addClass("btn-group")
                        .attr("data-group", group)
                        .appendTo(this.element);
                }

                return ge;
            },

            add: function (button) {
                button = $.extend({
                    type: "button"
                }, button);

                var group = this.getGroup(button.group);

                var buttonElement = _E("button")
                    .addClass("btn")
                    .appendTo(group);

                if (button.icon) {
                    $(buttonElement).append(_E("i").addClass("glyphicon glyphicon-" + button.icon));
                }

                if(button.text) {
                    $(buttonElement).append(button.text);
                }

                if (button.type == "menu") {
                    if (!button.items)
                        throw "Button of type 'menu' require menu items";
                    $(buttonElement).btnbarSubMenu(button);
                } else {
                    $(buttonElement).click(function () {
                        if (button.command)
                            button.command();
                    });
                }

                if (button.hidden) {
                    liElement.hide();
                }
            },

            showGroups: function (groups) {
                var self = this;
                $(self.element).find('li[group~="' + groups + '"]').each(function () {
                    if (!$(this).is(":visible")) {
                        $(this).stop().show("slide", 250);
                    }
                });
            },

            hideGroups: function (groups) {
                var self = this;
                $(self.element).find('li[group~="' + groups + '"]').each(function () {
                    if ($(this).is(":visible")) {
                        $(this).stop().hide("slide", 250);
                    }
                });
            },

            clear: function () {
                $(this.element).empty();
            }
        };

        $.fn.btnbar = function (options) {
            var btnbar = $(this).data("btnbar");
            if (options === "isBtnbar")
                return btnbar ? true : false;

            if (!btnbar) {
                btnbar = new Btnbar();
                $(this).data("btnbar", btnbar);

                btnbar.init(this, options);
            } else {
                var method = Btnbar.prototype[options];
                if (method) {
                    method.apply(btnbar, Array.prototype.slice.call(arguments, 1));
                }
            }
        };

    })(jQuery);


    /*
     * Breadcrumbs
     */
    (function ($) {

        function Breadcrumbs() {};

        Breadcrumbs.prototype = {

            init: function (element, options) {
                this.element = element;
                this.options = $.extend(options, { });
            },

            add: function (breadcrumb) {
                var li = _E('li');
                var item;

                if (!breadcrumb.href || breadcrumb.selected) {
                    item = _E("span").text(breadcrumb.label);
                    li.addClass("active");
                } else if (breadcrumb.href) {
                    item = _E('a')
                        .attr('href', breadcrumb.href)
                        .text(breadcrumb.label);
                } else if (breadcrumb.command) {
                    item = _E('a')
                        .text(breadcrumb.label)
                        .attr('href', 'javascript:;')
                        .data('item', breadcrumb)
                        .click(function () {
                            $(this).data('item').command();
                        });
                }

                var size = $(this.element).find("li").size();
                if(size > 0) {
                    //li.append(_E("span").addClass("divider").text("/"));
                }

                li.append(item).appendTo(this.element);
            },

            addAll: function (breadcrumbs) {
                var size = breadcrumbs.length;
                for (var i = 0; i < size; i++) {
                    this.add(breadcrumbs[i]);
                }
            },

            clear: function () {
                $(this.element).empty();
            }
        };

        $.fn.breadcrumbs = function (options) {
            var breadcrumbs = $(this).data("breadcrumbs");
            if (options === "isBreadcrumbs")
                return breadcrumbs ? true : false;

            if (!breadcrumbs) {
                breadcrumbs = new Breadcrumbs();
                $(this).data("breadcrumbs", breadcrumbs);

                breadcrumbs.init(this, options);
            } else {
                var method = Breadcrumbs.prototype[options];
                if (method) {
                    method.apply(breadcrumbs, Array.prototype.slice.call(arguments, 1));
                }
            }
        };
    })(jQuery);

    (function ($) {
        var ThemeSwitcher = function () {
            var self = this;

            self.themes = [ 'blue', 'green', 'red' ];
        };

        ThemeSwitcher.prototype = {
            init: function (element, options) {
                var self = this;
                self.element = element;
                self.element.addClass('themeSwitcher');
                for (var i in self.themes) {
                    var theme = self.themes[i];
                    _E('a').addClass(theme).click(
                        function (e) {
                            e.preventDefault();
                            var theme = $(this).attr('class');
                            var links = $('link');
                            if (!self.currentTheme) {
                                links.each(function () {
                                    $(this).attr(
                                        'href',
                                        $(this).attr('href') + '?theme='
                                            + theme);
                                });
                            } else {
                                links.each(function () {
                                    var href = $(this).attr('href');
                                    href = href.replace('?theme='
                                        + self.currentTheme, '?theme='
                                        + theme);
                                    $(this).attr('href', href);
                                });
                            }
                            self.currentTheme = theme;
                        }).appendTo(self.element);
                }
            }
        };

        $.fn.themeSwitcher = function (options) {
            var themeSwitcher = $(this).data('themeSwitcher');
            if (!themeSwitcher) {
                themeSwitcher = new ThemeSwitcher();
                $(this).data('themeSwitcher', themeSwitcher);
                themeSwitcher.init(this, options);
            } else {
                var method = ThemeSwitcher.prototype[options];
                if (method) {
                    method.apply(themeSwitcher, Array.prototype.slice.call(
                        arguments, 1));
                }
            }
        };
    })(jQuery);

    (function ($) {
        var ProgressBar = function () {
            this.options = null;
            this.element = null;
            this.value = 0;
        };

        ProgressBar.prototype = {
            init: function (element, options) {
                var self = this;
                self.element = element;
                self.element.addClass('progressBar');
                self.options = $.extend({}, options);

                this.progressElement = _E("div").addClass("progress").appendTo(
                    self.element);

                this.textElement = _E("span").addClass("progressText").appendTo(
                    self.element);

                this.checkInitialValue();
            },

            checkInitialValue: function () {
                var initialValue = parseFloat($(this.element).attr("data-value"));
                if (isNaN(initialValue)) {
                    initialValue = 0;
                }

                this.setValue(initialValue);
            },

            setValue: function (value) {
                if (value < 0) {
                    throw "Progress bar value cannot be < 0";
                }

                if (value > 100) {
                    throw "Progress bar value cannot be > 100";
                }

                this.value = value;

                var mr = 255;
                var mg = 200;

                var redPower = 100 - value;
                var greenPower = value;
                var red = redPower * mr / 100;
                var green = greenPower * mg / 100;

                var rf = mr / red;
                var gf = mg / green;
                var f = Math.min(rf, gf);

                red *= f;
                green *= f;

                red = parseInt(red);
                green = parseInt(green);

                $(this.progressElement).width(value + "%").css("background-color", core.utils.format("rgb({0}, {1}, 0)", red, green));
                $(this.textElement).text(value + "%");
            }
        };

        $.fn.progressBar = function (options) {
            var progressBar = $(this).data('progressBar');
            if (!progressBar) {
                progressBar = new ProgressBar();
                $(this).data('progressBar', progressBar);
                progressBar.init(this, options);
            } else {
                var method = ProgressBar.prototype[options];
                if (method) {
                    method.apply(progressBar, Array.prototype.slice.call(arguments, 1));
                }
            }
        };
    })(jQuery);
    // bootbox.js
    window.bootbox=window.bootbox||function a(b,c){"use strict";function d(a){var b=s[q.locale];return b?b[a]:s.en[a]}function e(a,c,d){a.preventDefault();var e=b.isFunction(d)&&d(a)===!1;e||c.modal("hide")}function f(a){var b,c=0;for(b in a)c++;return c}function g(a,c){var d=0;b.each(a,function(a,b){c(a,b,d++)})}function h(a){var c,d;if("object"!=typeof a)throw new Error("Please supply an object of options");if(!a.message)throw new Error("Please specify a message");return a=b.extend({},q,a),a.buttons||(a.buttons={}),a.backdrop=a.backdrop?"static":!1,c=a.buttons,d=f(c),g(c,function(a,e,f){if(b.isFunction(e)&&(e=c[a]={callback:e}),"object"!==b.type(e))throw new Error("button with key "+a+" must be an object");e.label||(e.label=a),e.className||(e.className=2>=d&&f===d-1?"btn-primary":"btn-default")}),a}function i(a,b){var c=a.length,d={};if(1>c||c>2)throw new Error("Invalid argument length");return 2===c||"string"==typeof a[0]?(d[b[0]]=a[0],d[b[1]]=a[1]):d=a[0],d}function j(a,c,d){return b.extend(!0,{},a,i(c,d))}function k(a,b,c){return n(j(m.apply(null,a),b,c),a)}function l(){for(var a={},b=0,c=arguments.length;c>b;b++){var e=arguments[b],f=e.toLowerCase(),g=e.toUpperCase();a[f]={label:d(g)}}return a}function m(){return{buttons:l.apply(null,arguments)}}function n(a,b){var d={};return g(b,function(a,b){d[b]=!0}),g(a.buttons,function(a){if(d[a]===c)throw new Error("button key "+a+" is not allowed (options are "+b.join("\n")+")")}),a}var o={dialog:"<div class='bootbox modal' tabindex='-1' role='dialog'><div class='modal-dialog'><div class='modal-content'><div class='modal-body'><div class='bootbox-body'></div></div></div></div></div>",header:"<div class='modal-header'><h4 class='modal-title'></h4></div>",footer:"<div class='modal-footer'></div>",closeButton:"<button type='button' class='bootbox-close-button close'>&times;</button>",form:"<form class='bootbox-form'></form>",inputs:{text:"<input class='bootbox-input form-control' autocomplete=off type=text />"}},p=b("body"),q={locale:"en",backdrop:!0,animate:!0,className:null,closeButton:!0,show:!0},r={};r.alert=function(){var a;if(a=k(["ok"],arguments,["message","callback"]),a.callback&&!b.isFunction(a.callback))throw new Error("alert requires callback property to be a function when provided");return a.buttons.ok.callback=a.onEscape=function(){return b.isFunction(a.callback)?a.callback():!0},r.dialog(a)},r.confirm=function(){var a;if(a=k(["cancel","confirm"],arguments,["message","callback"]),a.buttons.cancel.callback=a.onEscape=function(){return a.callback(!1)},a.buttons.confirm.callback=function(){return a.callback(!0)},!b.isFunction(a.callback))throw new Error("confirm requires a callback");return r.dialog(a)},r.prompt=function(){var a,d,e,f,g,h;if(f=b(o.form),d={buttons:l("cancel","confirm"),value:""},a=n(j(d,arguments,["title","callback"]),["cancel","confirm"]),h=a.show===c?!0:a.show,a.message=f,a.buttons.cancel.callback=a.onEscape=function(){return a.callback(null)},a.buttons.confirm.callback=function(){return a.callback(g.val())},a.show=!1,!a.title)throw new Error("prompt requires a title");if(!b.isFunction(a.callback))throw new Error("prompt requires a callback");return g=b(o.inputs.text),g.val(a.value),f.append(g),f.on("submit",function(a){a.preventDefault(),e.find(".btn-primary").click()}),e=r.dialog(a),e.off("shown.bs.modal"),e.on("shown.bs.modal",function(){g.focus()}),h===!0&&e.modal("show"),e},r.dialog=function(a){a=h(a);var c=b(o.dialog),d=c.find(".modal-body"),f=a.buttons,i="",j={onEscape:a.onEscape};if(g(f,function(a,b){i+="<button data-bb-handler='"+a+"' type='button' class='btn "+b.className+"'>"+b.label+"</button>",j[a]=b.callback}),d.find(".bootbox-body").html(a.message),a.animate===!0&&c.addClass("fade"),a.className&&c.addClass(a.className),a.title&&d.before(o.header),a.closeButton){var k=b(o.closeButton);a.title?c.find(".modal-header").prepend(k):k.css("margin-top","-10px").prependTo(d)}return a.title&&c.find(".modal-title").html(a.title),i.length&&(d.after(o.footer),c.find(".modal-footer").html(i)),c.on("hidden.bs.modal",function(a){a.target===this&&c.remove()}),c.on("shown.bs.modal",function(){c.find(".btn-primary:first").focus()}),c.on("escape.close.bb",function(a){j.onEscape&&e(a,c,j.onEscape)}),c.on("click",".modal-footer button",function(a){var d=b(this).data("bb-handler");e(a,c,j[d])}),c.on("click",".bootbox-close-button",function(a){e(a,c,j.onEscape)}),c.on("keyup",function(a){27===a.which&&c.trigger("escape.close.bb")}),p.append(c),c.modal({backdrop:a.backdrop,keyboard:!1,show:!1}),a.show&&c.modal("show"),c},r.setDefaults=function(a){b.extend(q,a)},r.hideAll=function(){b(".bootbox").modal("hide")};var s={br:{OK:"OK",CANCEL:"Cancelar",CONFIRM:"Sim"},da:{OK:"OK",CANCEL:"Annuller",CONFIRM:"Accepter"},de:{OK:"OK",CANCEL:"Abbrechen",CONFIRM:"Akzeptieren"},en:{OK:"OK",CANCEL:"Cancel",CONFIRM:"OK"},es:{OK:"OK",CANCEL:"Cancelar",CONFIRM:"Aceptar"},fi:{OK:"OK",CANCEL:"Peruuta",CONFIRM:"OK"},fr:{OK:"OK",CANCEL:"Annuler",CONFIRM:"D'accord"},it:{OK:"OK",CANCEL:"Annulla",CONFIRM:"Conferma"},nl:{OK:"OK",CANCEL:"Annuleren",CONFIRM:"Accepteren"},pl:{OK:"OK",CANCEL:"Anuluj",CONFIRM:"Potwierdź"},ru:{OK:"OK",CANCEL:"Отмена",CONFIRM:"Применить"},zh_CN:{OK:"OK",CANCEL:"取消",CONFIRM:"确认"},zh_TW:{OK:"OK",CANCEL:"取消",CONFIRM:"確認"}};return r.init=function(c){window.bootbox=a(c||b)},r}(window.jQuery);
});