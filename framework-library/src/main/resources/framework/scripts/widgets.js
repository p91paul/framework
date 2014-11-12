/**
 * Applica (www.applicadoit.com).
 * User: bimbobruno
 * Date: 2/21/13
 * Time: 2:52 PM
 */

define(["framework/core"], function(core) {

    var exports = {};

    var Widget = core.Disposable.extend({
        ctor: function(container) {
            Widget.super.ctor.call(this);

            this.container = container;
        },

        setContainer: function(container) {
            this.container = container;
        },

        getContainer: function() {
            return this.container;
        },

        render: function() {
            $(this.container).empty().text("widget");
        }
    });

    var Form = Widget.extend({
        ctor: function(container, element) {
            Form.super.ctor.call(this, container);

            this.element = element;
            this.componentInitializers = {};
            this.inDialog = false;
        },

        initializeComponents: function() {
            var self = this;
            var components = __crud_getComponents(this.element);
            $(components).each(function(i, component) {
                var handler = Form.__componentInitializers[component.type] || self.componentInitializers[component.type];
                if(handler) {
                    handler.call(self, component.element);
                }
            });
        },

        registerComponent: function(component, handler) {
            this.componentInitializers[component] = handler;
        },

        get_defaultTitle: function() {
            return $(this.element).find("[data-component=form_title]").text();
        },

        render: function() {
            if(!this.element) {
                throw "Element not loaded. Use FormService to load element from server and set the result using Form.setElement() method";
            }

            this.initializeComponents();

            $(this.container).append(this.element);

            this.callback("after-render");
        },

        submit: function() {
            if(this.element) {
                $(this.element).find("[data-component=form]").submit();
            }
        },

        serialize: function() {
            return $(this.element).find("[data-component=form]").serialize();
        },

        serializeArray: function() {
            return $(this.element).find("[data-component=form]").serializeArray();
        },

        serializeObject: function() {
            var arr = this.serializeArray();
            var obj = {};

            for(var i = 0; i < arr.length; i++) {
                var e = arr[i];
                obj[e.name] = e.value;
            }

            return obj;
        },

        handleValidationErrors: function(result) {
            $(result.errors).each($.proxy(function(i, error) {

                $(this.element)
                    .find("[data-component=field][data-property=" + error.property + "]")
                        .addClass("has-error")
                        .end()
                    .find("[data-component=error][data-error-property=" + error.property + "]")
                        .html(error.message)
                        .removeClass("hidden")
                        .show();
            }, this));
        },

        resetValidation: function(result) {
            $(this.element)
                .find("[data-component=field]")
                    .removeClass("has-error")
                    .end()
                .find("[data-component=error]")
                    .hide();
        }
    });


    Form.__componentInitializers = {
        form: function(element) {
            $(element).submit($.proxy(function(e) {
                core.events.invoke(this, "beforeSubmit", e);
                core.events.invoke(this, "submit", e);
                core.events.invoke(this, "afterSubmit", e);
            }, this));
        },

        date: function(element) {
            var input = $(element).find("[data-component=date_input]");
            var button = $(element).find("[data-component=date_button]");
            $(input).datepicker();
            $(button).click(function() {
                $(input).datepicker("show");
            });
        },

        number: function(element) {
            $(element).numeric();
        },

        time: function(element) {
            var property = $(element).attr('data-property');
            var target = 'input[name=' + property + '_container]';

            $(target)
                .bind('click, focus', function() {
                    var e = $(this);
                    if(!e.data('time-init')) {
                        e.timepicker({ ampm: false });
                        e.data('time-init', true);
                        e.focus();
                    }
                })
                .bind('change', function() {
                    var e = $(this);
                    var input = 'input[name=' + e.attr('name').replace('_container','') + ']';
                    var minValue = $(input).attr('data-property-format-date-min-value');
                    $(input).val(minValue + ' ' + e.val());
                });
        },

        file: function(element) {
            var uploadButton = $(element).find("[data-component=upload_button]");
            var downloadButton = $(element).find("[data-component=download_button]");
            var filenameInput = $(element).find("[data-component=file_name_input]");
            var progress = $(element).find("[data-component=progress]");
            var upload_value = $(element).find("[data-component=upload_value]");
            var action = $(element).attr("data-action");
            var path = $(element).attr("data-path");

            var fileUploader = new FileUploader();

            fileUploader.set({
                url: BASE + action,
                button : uploadButton,
                progressBar: progress,
                data: { path: path },
                input: upload_value,
                filenameInput: filenameInput,
                downloadButton: downloadButton
            });

            if(this.get("inDialog")) {
                this.callback("form-dialog-show", function() {
                    fileUploader.init();
                });
            } else {
                this.callback("after-render", function() {
                    fileUploader.init();
                });
            }

        },

        image: function(element) {
            var container = $(element).find("[data-sub-component=image_container]");
            var button = $(element).find("[data-sub-component=image_button]");
            var image = $(element).find("[data-sub-component=image_img]");
            var input = $(element).find("[data-sub-component=image_input]");
            var path = $(element).attr("data-path");
            var action = $(element).attr("data-action");
            var link = $(element).find("[data-sub-component=image_link]");

            var imageUploader = new ImageUploader();

            imageUploader.set({
                url: BASE + action,
                button : button,
                container: container,
                data: { path: path },
                input: input,
                image: image
            });

            imageUploader.on("complete", function(data) {
                $(link).attr("href", FILES_BASE + data.value);
            });

            if(this.get("inDialog")) {
                this.callback("form-dialog-show", function() {
                    imageUploader.init();
                });
            } else {
                this.callback("after-render", function() {
                    imageUploader.init();
                });
            }
        },

        check: function(element) {
            var checkbox = $(element).find("[data-sub-component='checkbox']");
            var value = $(element).find("[data-sub-component='value']");
            $(checkbox).change(function() {
                if(checkbox.prop("checked")) {
                    value.val(1);
                } else {
                    value.val(0);
                }
            });
        },

        searchable_input_single: function(element) {
            $(element).searchableInput({
                name: $(element).attr("data-property"),
                mode: "single",
                serviceUrl: BASE + $(element).attr("data-serviceUrl"),
                linkParameters: $(element).attr('data-link-parameters'),
                linkHandler: $(element).attr('data-link-handler')
            });

            this.pushDispose(function() {
                $(element).searchableInput("destroy");
            });
        },

        searchable_input_multi: function(element) {
            $(element).searchableInput({
                name: $(element).attr("data-property"),
                mode: "multi",
                serviceUrl: BASE + $(element).attr("data-serviceUrl")
            });

            this.pushDispose(function() {
                $(element).searchableInput("destroy");
            });
        },

        color: function(element) {
            var colorPicker = $.farbtastic($(element).find("[data-component=color_picker]"));
            colorPicker.linkTo($(element).find("[data-component=color_input]"));
        },

        link_button: function(element) {
            $(element)
                .css('width', '100px')
                .css('margin-left', '2px')
                .click(function(e) {
                    e.preventDefault();
                    location.href = $(this).attr('data-url');
                });
        },

        percentage: function(element) {
            var value = $(element).attr('data-value');
            var property = $(element).attr('data-property-target');
            var valueSelector = 'input[name="' + property + '"]';
            if(!value || utils.stringIsNullOrEmpty(value)) {
                value = 50;
                $(valueSelector).val(value);
            }
            $(element).find('.display').text(value + '%');
            $(element).find('.slider').slider({
                min: 0,
                value: value,
                max: 100,
                slide: function(event, ui) {
                    var v = $(this).slider('value');
                    $(this)
                        .parent()
                        .find('.display')
                        .text(v + '%');
                },
                change: function(event, ui) {
                    var v = $(this).slider('value');
                    $(valueSelector).val(v);
                    $(this)
                        .parent()
                        .find('.display')
                        .text(v + '%');
                }
            });
        },

        html: function (element) {
            $(element).summernote({
                height: "250"
            });

            //override form method
            this.on("beforeSubmit", function() {
                $(element).val($(element).code())
            });
        }
    };


    Form.registerComponent = function(component, handler) {
        Form.__componentInitializers[component] = handler;
    };


    var Grid = Widget.extend({
        ctor: function(container, element) {
            Grid.super.ctor.call(this, container);

            this.element = element;
            this.componentInitializers = {};
            this.sortDescending = {};


        },

        initializeComponents: function() {
            var self = this;
            var components = __crud_getComponents(this.element);
            $(components).each(function(i, component) {
                var handler = Grid.__componentInitializers[component.type] || self.componentInitializers[component.type];
                if(handler) {
                    handler.call(self, component.element);
                }
            });
        },

        registerComponent: function(component, handler) {
            this.componentInitializers[component] = handler;
        },

        render: function() {
            if(!this.element) {
                throw "Element not loaded. Use GridLoader to load element from server and set the result using Grid.setElement() method";
            }

            this.initializeComponents();

            $(this.container).append(this.element);
        },

        reload: function() {
            this.load(this.options);
        },

        selectAll: function() {
            var self = this;
            self.die("select");
            $(self.element).find("input[type=checkbox][data-component=select_checkbox]").each(function(i, check) {
                var id = $(check).attr("data-entity-id");
                self.select(id);
            });
            self.live("select");
            self.invoke("select");
        },

        set_title: function(title) {
            $(this.element).find("[data-component=title]").text(title);
        },

        unselectAll: function() {
            var self = this;
            self.die("select");
            $(self.element).find("input[type=checkbox][data-component=select_checkbox]").each(function(i, check) {
                var id = $(check).attr("data-entity-id");
                self.unselect(id);
            });
            self.live("select");
            self.invoke("select");
        },

        select: function(id) {
            var self = this;
            var removed = $(self.element).find("[data-component=row]").attr("data-removed");
            if(removed) return;

            $(self.element).find("input[type=checkbox][data-component=select_checkbox][data-entity-id=" + id + "]").prop("checked", true);
            $(self.element).find("[data-component=row][data-entity-id=" + id + "]").addClass(Grid.__selectClass);

            self.invoke("select");
        },

        unselect: function(id) {
            var self = this;
            $(self.element).find("input[type=checkbox][data-component=select_checkbox][data-entity-id=" + id + "]").prop("checked", false);
            $(self.element).find("[data-component=row][data-entity-id=" + id + "]").removeClass(Grid.__selectClass);

            self.invoke("select");
        },

        markAsRemoved: function(id) {
            this.unselect(id);
            $(this.element)
                .find("[data-component=row][data-entity-id=" + id + "]")
                .fadeOut(250);
        },

        getSelection: function() {
            var self = this;
            var ids = [];
            $(self.element).find("input[type=checkbox][data-component=select_checkbox]:checked").each(function(i, c) {
                var id = $(c).attr("data-entity-id");
                if (id) {
                    ids.push($(c).attr("data-entity-id"));
                }
            });
            return ids;
        },

        get_selection: function() {
            return this.getSelection();
        },

        getRowProperties: function(id) {
            var self = this;
            var row = $(self.element).find('[data-component=row][data-entity-id="' + id + '"]');
            if( row.size() != 1) {
                row = $(self.element).find('[data-component=row][data-rowid="' + id + '"]');
            }
            var properties = [];
            row.find('*[data-property]').each(function() {
                var propertyName = $(this).attr('data-property');
                var propertyValue = $(this).attr('data-property-value');
                properties[propertyName] = propertyValue;
            });
            properties.id = row.find('[data-entity-id]').first().attr('data-entity-id');
            return properties;
        },

        hideSearchForm: function(cb) {
            $(this.element).find("[data-component=search_form_container]").slideUp(cb);
        },

        showSearchForm: function(cb) {
            $(this.element).find("[data-component=search_form_container]").slideDown(cb);
        },

        edit: function(id) {
            core.events.invoke(this, "edit", id);
        },

        changePage: function(page) {
            core.events.invoke(this, "page", page);
        },

        sort: function(sort) {
            core.events.invoke(this, "sort", sort);
        },

        nextPage: function() {
            core.events.invoke(this, "nextPage");
        },

        previousPage: function() {
            core.events.invoke(this, "previousPage");
        },

        search: function(filters) {
            core.events.invoke(this, "search", filters);
        }
    });

    Grid.__selectClass = "selected";

    Grid.__componentInitializers = {
        select_all_checkbox: function(element) {
            var self = this;
            $(element).click(function() {
                var checked = $(element).prop("checked");
                if(checked) self.selectAll();
                else self.unselectAll();
            });
        },

        select_checkbox: function(element) {
            var self = this;
            $(element).click(function(e) {
                var id = $(element).attr("data-entity-id");
                var checked = $(element).prop("checked");
                if(checked) self.select(id);
                else self.unselect(id);
                e.stopPropagation();
            });
        },

        row: function(element) {
            var self = this;
            var id = $(element).attr("data-entity-id");
            $(element).click(function() {
                self.live('selectRow');
                self.die("select");
                self.unselectAll();
                self.live("select");
                self.select(id);
            });

            $(element).find('.dbClickSensitive').dblclick(function() {
                self.edit(id);
            });
        },

        checkbox_readonly: function(element) {
            $(element).click(function(e) { e.preventDefault(); });
        },

        progress_bar: function(element) {
            $(element).progressBar();
        },

        linked_cell: function(element) {
            var self = this;
            $(element).click(function(e) {
                var id = $(element).parent().parent().attr("data-entity-id");
                self.die("select");
                self.unselectAll();
                self.edit(id);
                self.live("select");
                self.select(id);
                e.stopPropagation();
            });
        },

        text_link: function(element) {
            var self = this;
            $(element).click(function(e) {
                var id = $(element).parent().parent().attr("data-entity-id");
                var row = $(element).parent().parent();
                var rowid = row.attr('data-rowid');
                if(!rowid) {
                    rowid = 'row_' + utils.GUID();
                    row.attr('data-rowid', rowid);
                }
                self.die("select");
                self.unselectAll();
                self.edit(self.getRowProperties(rowid));
                e.stopPropagation();
            });
        },

        search_form: function(element) {
            var self = this;
            $(element).submit(function() {
                var formData = $(element).serializeArray();
                var filters = [];
                var criterias = {};
                try {
                    criterias = $.parseJSON($(element).find("[data-component='criterias']").val());
                } catch(e) {}

                var size = formData.length;
                for(var i = 0; i < size; i++) {
                    filters.push({
                        property: formData[i].name,
                        value: formData[i].value,
                        type: criterias[formData[i].name] || "eq"
                    });
                }

                self.search(filters);

                return false;
            });
        },

        search_form_container: function(element) {

        },

        cancel_search: function(element) {
            var self = this;
            $(element).click(function() {
                filters = [];
                self.hideSearchForm(function() { self.search(filters); });
            });
        },

        open_search_form: function(element) {
            var self = this;
            $(element).click(function() {
                self.showSearchForm();
            });
        },

        page_button: function(element) {
            var self = this;
            $(element).click(function() {
                var page = $(element).attr("data-page");
                self.changePage(page);
            });
        },

        sort_button: function(element) {
            var self = this;

            $(element).click(function() {
                var property = $(this).attr('data-property');
                var descending = self.sortDescending[property] ? true : false;

                var sortBy = {
                    property: property,
                    descending: descending
                };

                self.sortDescending[property] = !descending;
                self.sort(sortBy);

                return false;
            });
        },

        next_button: function(element) {
            var self = this;
            $(element).click(function() {
                self.nextPage();
            });
        },
        prev_button: function(element) {
            var self = this;
            $(element).click(function() {
                self.previousPage();
            });
        }
    };

    Grid.registerComponent = function(component, handler) {
        Grid.__componentInitializers[component] = handler;
    };

    var FileUploader = core.AObject.extend({
        ctor: function() {
            FileUploader.super.ctor.call(this);

            this.url = null;
            this.button = null;
            this.downloadButton = null;
            this.data = null;
            this.paramName = "file";
            this.filters = [];
            this.maxFileSize = "10mb";
            this.input = null;
            this.progressBar = null;
            this.filenameInput = null;
        },

        init: function() {
            var self = this;

            if(!this.button) {
                throw "Please specify a browse button to click "
            }

            if(!$(this.button).attr("id")) {
                $(this.button).attr("id", core.utils.format("file-uploader-browse-button-{0}", ++FileUploader.__buttonCount))
            }

            if(!this.url) {
                throw "Please specify an url to upload the file";
            }

            if(!this.input) {
                throw "Please specify an input to store uploaded file path";
            }

            if(this.progressBar) {
                $(this.progressBar).hide();
            }

            if(this.downloadButton) {
                $(this.downloadButton).click(function() {
                    var url = $(self.input).val();
                    if(url) {
                        window.open(FILES_BASE + url);
                    }
                });
            }

            this.uploaderError = false;
            var uploader = new plupload.Uploader({
                url: this.url,
                runtimes : 'html5,flash,html4',
                browse_button : $(this.button).attr("id"),
                max_file_size : this.maxFileSize,
                multi_selection: false,
                multipart_params: this.data,
                file_data_name: this.paramName,
                flash_swf_url: STATIC_BASE + 'commons/scripts/lib/plupload/plupload.flash.swf',
                filters: this.filters
            });

            uploader.init();

            uploader.bind('FilesAdded', function(up, files) {
                self.uploaderError = false;

                uploader.start();

                var data = {
                    filename: files[0].name,
                    size: files[0].size
                }

                if(self.progressBar) {
                    $(self.progressBar)
                        .removeClass("progress-succes")
                        .removeClass("progress-danger")
                        .show()
                        .find(".bar").width("0%");
                }

                if(self.filenameInput) {
                    $(self.filenameInput).val(data.filename);
                }

                self.invoke("start", data);
            });

            uploader.bind('UploadProgress', function(up, file) {
                if(self.progressBar) {
                    $(self.progressBar).find(".bar").width(file.percent +"%");
                }

                self.invoke("progress", file.percent);
            });

            uploader.bind("Error", function(up, error) {
                self.uploaderError = true;
                var errorMessage = msg.MSG_UPLOAD_ERROR_GENERIC;
                switch(error.code) {
                    case -601:
                        errorMessage = msg.MSG_UPLOAD_ERROR_FORMAT;
                        break;
                    case -600:
                        errorMessage = msg.MSG_UPLOAD_ERROR_SIZE;
                        break;
                }

                self.invoke("error", errorMessage);
            });

            uploader.bind("FileUploaded", function(up, file, data) {
                if(!self.uploaderError) {
                    self.handleUploadComplete(data.response);
                } else {
                    if(this.progressBar) {
                        $(this.progressBar).addClass("progress-danger");
                    }
                }
            });
        },

        handleUploadComplete: function(json) {
            try {
                var data = $.parseJSON(json);

                if(data.error) {
                    if(this.progressBar) {
                        $(this.progressBar).addClass("progress-danger");
                    }

                    self.invoke("error", data.message);
                    return;
                }

                if(this.progressBar) {
                    $(this.progressBar).addClass("progress-success");
                    $(this.progressBar).find(".bar").width("100%");
                }

                if(this.input) {
                    $(this.input).val(data.value);
                }

                this.invoke("complete", data);
            } catch(e) {
                if(this.progressBar) {
                    $(this.progressBar).addClass("progress-danger");
                }

                this.invoke("error", data.message);
            }
        }
    });

    FileUploader.__buttonCount = 0;

    var ImageUploader = core.AObject.extend({
        ctor: function() {
            ImageUploader.super.ctor.call(this);

            this.url = null;
            this.size = "150x150";
            this.container = null;
            this.button = null;
            this.image = null;
            this.input = null;
            this.data = null;
            this.paramName = "image";
        },

        init: function() {
            var self = this;

            $(this.container).addClass("image-uploader-container");

            if(!this.button) {
                this.button = _E("button")
                    .attr("type", "button")
                    .attr("id", "upload_button_" + new Date().getTime())
                    .addClass("btn")
                    .addClass("image-uploader-button")
                    .addClass("hide")
                    .text(msg.LABEL_CHANGE)
                    .appendTo($(this.container));

                $(this.container).hover(
                    function() { $(self.button).fadeIn(150); },
                    function() { $(self.button).fadeOut(150); }
                );
            }

            this.uploaderError = false;
            var uploader = new plupload.Uploader({
                url: this.url,
                runtimes : 'html5,flash,html4',
                browse_button : $(this.button).attr("id"),
                max_file_size : '10mb',
                multi_selection: false,
                multipart_params: this.data,
                file_data_name: this.paramName,
                flash_swf_url: BASE + "framework-resources/lib/plupload/Moxie.swf",
                filters : [
                    {title : "Image files", extensions : "jpg,png"}
                ]
            });

            uploader.init();

            uploader.bind('FilesAdded', function(up, files) {
                $.loader.show({ parent: self.container });
                self.uploaderError = false;
                uploader.start();

                self.invoke("start");
            });

            uploader.bind('UploadProgress', function(up, file) {
                self.invoke("progress", file);
            });

            uploader.bind("Error", function(up, error) {
                $.loader.hide({ parent: self.container });
                self.uploaderError = true;
                var errorMessage = msg.MSG_IMAGE_UPLOAD_ERROR_GENERIC;
                switch(error.code) {
                    case -601:
                        errorMessage = msg.MSG_IMAGE_UPLOAD_ERROR_FORMAT;
                        break;
                    case -600:
                        errorMessage = msg.MSG_IMAGE_UPLOAD_ERROR_SIZE;
                        break;
                }
                $.notify.error(errorMessage);

                self.invoke("error", errorMessage);
            });

            uploader.bind("FileUploaded", function(up, file, data) {
                $.loader.hide({ parent: self.container });
                if(!self.uploaderError) {
                    self.handleUploadComplete(data.response);
                }
            });
        },

        handleUploadComplete: function(json) {
            try {
                var data = $.parseJSON(json);

                if(data.error) {
                    $.notify.error(msg.MSG_IMAGE_UPLOAD_ERROR_GENERIC);
                    return;
                }

                if(!this.image) {
                    this.image = _E("img").appendTo(this.container);
                }

                $(this.image).attr("src", FILES_BASE + data.value + "?size=" + this.size);
                $(this.input).val(data.value);

                this.invoke("complete", data);
            } catch(e) {
                $.notify.error(msg.MSG_IMAGE_UPLOAD_ERROR_GENERIC);
            }
        }
    });

    var ScrollEventsInvoker = core.AObject.extend({
        ctor: function(element) {
            ScrollEventsInvoker.super.ctor.call(this);

            this.element = element;
            this.content = null;
            this.strategies = ["top", "bottom"]; //or bottom
            this.topInvokeable = false;
            this.bottomInvokeable = false;
            this.tolerance = 10;
        },

        init: function() {
            var self = this;
            if(!this.element) {
                throw "Please specify element";
            }

            if(!this.content) {
                this.content = $(this.element).children();
            }


            $(this.element).scroll(function(e) {
                core.utils.each(self.strategies, function(s) {
                    self["scroll_" + s](e);
                });
            });
        },

        scroll_top: function(e) {
            var scrollTop = $(this.element).scrollTop();
            if(!this.topInvokeable) {
                if(scrollTop > this.tolerance) {
                    this.topInvokeable = true;
                }
            }

            if(this.topInvokeable) {
                if(scrollTop <= this.tolerance) {
                    this.topInvokeable = false;
                    this.invoke("top");
                }
            }
        },

        scroll_bottom: function(e) {
            var contentHeight = $(this.content).outerHeight();
            var elementHeight = $(this.element).height();
            if(contentHeight <= elementHeight) {
                return;
            }
            var scrollBottomLimit = contentHeight - elementHeight - this.tolerance;

            if(!this.bottomInvokeable) {
                if($(this.element).scrollTop() < scrollBottomLimit) {
                    this.bottomInvokeable = true;
                }
            }

            if(this.bottomInvokeable) {
                if($(this.element).scrollTop() >= scrollBottomLimit) {
                    this.bottomInvokeable = false;
                    this.invoke("bottom");
                }
            }
        },

        invokeIfNotScroll: function() {
            var contentHeight = $(this.content).outerHeight();
            var elementHeight = $(this.element).height();
            if(contentHeight < (elementHeight - this.tolerance)) {
                this.invoke("top");
                this.invoke("bottom");
            }
        }
    });

    var FileUploader = core.AObject.extend({
        ctor: function() {
            FileUploader.super.ctor.call(this);

            this.url = null;
            this.button = null;
            this.downloadButton = null;
            this.data = null;
            this.paramName = "file";
            this.filters = [];
            this.maxFileSize = "10mb";
            this.input = null;
            this.progressBar = null;
            this.filenameInput = null;
        },

        init: function() {
            var self = this;

            if(!this.button) {
                throw "Please specify a browse button to click "
            }

            if(!$(this.button).attr("id")) {
                $(this.button).attr("id", core.utils.format("file-uploader-browse-button-{0}", ++FileUploader.__buttonCount))
            }

            if(!this.url) {
                throw "Please specify an url to upload the file";
            }

            if(!this.input) {
                throw "Please specify an input to store uploaded file path";
            }

            if(this.progressBar) {
                $(this.progressBar).hide();
            }

            if(this.downloadButton) {
                $(this.downloadButton).click(function() {
                    var url = $(self.input).val();
                    if(url) {
                        window.open(FILES_BASE + url);
                    }
                });
            }

            this.uploaderError = false;
            var uploader = new plupload.Uploader({
                url: this.url,
                runtimes : 'html5,flash,html4',
                browse_button : $(this.button).attr("id"),
                max_file_size : this.maxFileSize,
                multi_selection: false,
                multipart_params: this.data,
                file_data_name: this.paramName,
                flash_swf_url: BASE + "framework-resources/lib/plupload/Moxie.swf",
                filters: this.filters
            });

            uploader.init();

            uploader.bind('FilesAdded', function(up, files) {
                self.uploaderError = false;

                uploader.start();

                var data = {
                    filename: files[0].name,
                    size: files[0].size
                }

                if(self.progressBar) {
                    $(self.progressBar)
                        .show()
                            .find(".progress-bar")
                            .removeClass("progress-bar-success")
                            .removeClass("progress-bar-danger")
                            .width("0%");
                }

                if(self.filenameInput) {
                    $(self.filenameInput).val(data.filename);
                }

                self.invoke("start", data);
            });

            uploader.bind('UploadProgress', function(up, file) {
                if(self.progressBar) {
                    $(self.progressBar).find(".progress-bar").width(file.percent +"%");
                }

                self.invoke("progress", file.percent);
            });

            uploader.bind("Error", function(up, error) {
                self.uploaderError = true;
                var errorMessage = msg.MSG_UPLOAD_ERROR_GENERIC;
                switch(error.code) {
                    case -601:
                        errorMessage = msg.MSG_UPLOAD_ERROR_FORMAT;
                        break;
                    case -600:
                        errorMessage = msg.MSG_UPLOAD_ERROR_SIZE;
                        break;
                }

                self.invoke("error", errorMessage);
            });

            uploader.bind("FileUploaded", function(up, file, data) {
                if(!self.uploaderError) {
                    self.handleUploadComplete(data.response);
                } else {
                    if(this.progressBar) {
                        $(this.progressBar).find(".progress-bar").addClass("progress-bar-danger");
                    }
                }
            });
        },

        handleUploadComplete: function(json) {
            try {
                var data = $.parseJSON(json);

                if(data.error) {
                    if(this.progressBar) {
                        $(this.progressBar).find(".progress-bar").addClass("progress-bar-danger");
                    }

                    self.invoke("error", data.message);
                    return;
                }

                if(this.progressBar) {
                    $(this.progressBar).find(".progress-bar").addClass("progress-bar-success");
                    $(this.progressBar).find(".progress-bar").width("100%");
                }

                if(this.input) {
                    $(this.input).val(data.value);
                }

                this.invoke("complete", data);
            } catch(e) {
                if(this.progressBar) {
                    $(this.progressBar).find(".progress-bar").addClass("progress-bar-danger");
                }

                this.invoke("error", data.message);
            }
        }
    });

    FileUploader.__buttonCount = 0;

    exports.Widget = Widget;
    exports.Form = Form;
    exports.Grid = Grid;
    exports.FileUploader = FileUploader;
    exports.ImageUploader = ImageUploader;
    exports.ScrollEventsInvoker = ScrollEventsInvoker;
    exports.FileUploader = FileUploader;
    exports.ImageUploader = ImageUploader;

    var __crud_getComponents = function(element) {
        var components = [];
        $(element).find("*[data-component]").each(function(i, item) {
            var type = $(item).attr("data-component");
            components.push({ type: type, element: item });
        });

        return components;
    };


    return exports;

});