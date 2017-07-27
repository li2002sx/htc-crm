//layer
$.err = function (msg) {
    layer.msg(msg, {time: 1500, icon: 2});
};
/**
 * 发送 GET AJAX 请求
 */
$.request = function (url, data, callback, options) {
    $.getx(url, data, callback, options);
};
/**
 * 发送 GET AJAX 请求
 */
$.getx = function (url, data, callback, options) {
    // url, data, callback, btn, dataType, async, timeout, type
    options = $.extend({
        async: true,
        type: "GET",
        dataType: "json",
        timeout: 30000
    }, options);

    var $btn = $(options.btn);
    var success;
    if ($btn) {
        $btn.button('loading');
        success = function (data, textStatus) {
            if (callback) {
                callback(data, textStatus);
            }
            if ($btn) {
                $btn.button('reset');
            }
        };
    } else {
        success = callback;
    }

    $.ajax({
        type: options.type,
        url: url,
        data: data,
        success: success,
        error: function (xmlHttpRequest, textStatus, errorThrown) {
            var errors = [];
            if (xmlHttpRequest.responseText) {
                errors.push(xmlHttpRequest.responseText);
            }
            if (textStatus) {
                errors.push(textStatus);
            }
            if (errorThrown) {
                errors.push(errorThrown);
            }
            var errBody = ""
            if (xmlHttpRequest.responseJSON) {
                var err = xmlHttpRequest.responseJSON;
                errBody = '<p id="pErr" style="color:red">' +
                    '错误信息：' + err.message + '<br>' +
                    '请求路径：' + err.path + '<br>' +
                    '异常类型：' + err.exception + '<br/>' +
                    '堆栈详情：' +
                    '<div style="padding:8px">' +
                    '<pre id="preTrace" style="border:0px;background:#ffffcc;font-family: \'Consolas\',\'Lucida Console\',Monospace;font-size:9pt;">' +
                    err.exceptionDetail + '</pre>' +
                    '</div>' + new Date() + '</p>'
            }

            $alert("错误", errBody);
            if ($btn) {
                $btn.button('reset');
            }
        },
        dataType: options.dataType,
        timeout: options.timeout,
        async: options.async
    });
};
function $errorAjax(err) {

}
$.loadScript = function (url, callback) {
    if (url.length < 4 || url.substr(0, 4).toLowerCase() !== "http") {
        if (typeof $GS$ != "undefined") url = $GS$.ScriptServer + url + "?v=" + $GS$.Version;
    }
    $.getScript(url, callback);
};
$.loadStyle = function (url, id) {
    if (url.length < 4 || url.substr(0, 4).toLowerCase() != "http") {
        if (typeof $GS$ != "undefined") url = $GS$.StyleServer + url + "?v=" + $GS$.Version;
    }
    var elem = $('<link rel="stylesheet" href="' + url + '" type="text/css" />');
    if (id != null) elem.attr("id", id);
    return elem.appendTo('head');
};
$.getJSONP = function (url, data, callback, options) {
    if (options) {
        options.dataType = "jsonp";
    }
    url += (url.indexOf("?") > 0) ? "&jsonp=?" : "?jsonp=?";
    $.request(url, data, callback, options);
};
/***Ajax End***/

/***Action***/
// todo: 扩展成EventDispatcher类
$.action = {
    attribute: "data-action",
    list: {},
    add: function (actionName, callback) {
        this.list[actionName] = callback;
    },
    dispatch: function (e) {
        var actionName = $(e.target).attr($.action.attribute);
        if (actionName) {
            var callback = $.action.list[actionName];
            if (callback) callback(e);
        }
    }
};
/***Action End***/

/***Function***/
Function.prototype.bind = function (obj) {
    return $.proxy(this, obj);
};
/***Function End***/

/*
 * Dialog
 */
function Dialog() {
}
Dialog.__instanceCount = 0;
Dialog.prototype = {
    initialize: function (setting) {
        this.setting = $.extend({
            data: {},
            width: 400,
            fixed: false,
            initialShow: true,
            title: '提示',
            content: ''
        }, setting);
        this.id = "dlg_" + (++Dialog.__instanceCount);
        this.createContainer();
        if (this.setting.initialShow) this.show();
    },
    // just shorthand for the long initialize
    init: function (setting) {
        this.initialize(setting);
    },
// <div class="modal fade" id="dlg_bootstrap" tabindex="-1" role="dialog" aria-labelledby="ModalLabel" aria-hidden="true">
//   <div class="modal-dialog">
//     <div class="modal-content">
//       <div class="modal-header">
//         <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
//         <h4 class="modal-title" id="myModalLabel">Modal title</h4>
//       </div>
//       <div class="modal-body">
//         ...
//       </div>
//       <div class="modal-footer">
//         <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
//         <button type="button" class="btn btn-primary">Save changes</button>
//       </div>
//     </div>
//   </div>
// </div>

    // 创建容器
    createContainer: function () {
        this.dlg = $(this.id);
        //this.dlg = $('#dlg_bootstrap');
        if (this.dlg.length == 0) {
            var html = '<div id="' + this.id + '" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="Modal Dialog" aria-hidden="true">' +
                '<div class="modal-dialog ';
            if (this.setting.width > 400) {
                html += 'modal-lg';
            }
            html += '">' +
                '<div class="modal-content">' +
                '<div class="modal-header">' +
                '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only"></span></button>' +
                '<h4 class="modal-title" id="myModalLabel">Title</h4>' +
                '</div>' +
                '<div class="modal-body">' +
                '<div class="modal-body-content"></div>' +
                '<div class="modal-body-error" style="color:red"></div>' +
                '</div>' +
                '<div class="modal-footer">' +
                '<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>' +
                '<button type="button" class="btn btn-primary">Save changes</button>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>';
            this.dlg = $(html).appendTo($(document.body));

            this.dlg.find(".modal-header").bind("mousedown", function (e) {
                this.drag = true;
                this.dragX = e.clientX;
                this.dragY = e.clientY;
                e.target.style.cursor = "move";
                $(document).bind({
                    mousemove: this._drag.bind(this),
                    mouseup: this._dragEnd.bind(this),
                    selectstart: this._disableSelect.bind(this)
                });
            }.bind(this));
            // this.dlg.find('.jmodal-close').click(this.close.bind(this));
        }
        // this.dlg.css({ opacity: 0, 'display': 'block', width: this.setting.width, position: this.setting.fixed ? 'fixed' : 'absolute' });
    },
    _drag: function (e) {
        if (this.drag) {
            var d = this.dlg.find("div.modal-content")
            var left = d.prop("offsetLeft") + e.clientX - this.dragX;
            var top = d.prop("offsetTop") + e.clientY - this.dragY;
            d.css({left: left + "px", top: top + "px"})
            // var left = this.dlg.prop("offsetLeft") + e.clientX - this.dragX;
            // var top = this.dlg.prop("offsetTop") + e.clientY - this.dragY;
            // this.dlg.css({ left: left + "px", top: top + "px" })
            this.dragX = e.clientX;
            this.dragY = e.clientY;
        }
    },
    _dragEnd: function (e) {
        $(document).unbind({
            mousemove: this._drag.bind(this),
            mouseup: this._dragEnd.bind(this),
            selectstart: this._disableSelect.bind(this)
        });
        e.target.style.cursor = "";
        this.drag = false;
    },
    _disableSelect: function (e) {
        return false;
    },
    // 子类可通过此方法动态构造对话框内容
    createContent: function () {
        return this.setting.content;
    },
    // 查找对话框内元素
    find: function (selector) {
        return this.dlg ? this.dlg.find(selector) : null;
    },
    // 显示对话框
    show: function () {
        this.dlg.find("h4.modal-title").text(this.setting.title);
        this.dlg.find("div.modal-error").hide();
        this.dlg.find("div.modal-body-content").empty().append(this.createContent());

        // 创建按钮
        var dialog = this;
        var div_button = this.dlg.find('div.modal-footer').empty();

        if (this.setting.buttons.length == 0) div_button.hide();
        else {
            $.each(this.setting.buttons, function (i, btn) {
                var event = btn.callback ? function (that, e) {
                        $.proxy(btn.callback, that)(dialog, e)
                    } : function (that, e) {
                        $.proxy(dialog.close, dialog)()
                    };
                var style = "btn btn-sm " + (btn.style ? btn.style : "btn-default");
                $('<button type="button" class="' + style + '">' + btn.text + '</button>').appendTo(div_button).click(function (e) {
                    event(btn.btnThis === true ? $(this) : btn, e)
                });
            });
            div_button.show();
        }

        this.onload();

        // var d = this.dlg.find("div.modal-content")
        // var doc = $(document);
        // var win = $(window);
        // var left = (win.width() - d.width()) / 2;
        // var top = (win.height() - d.height()) / 2;
        // if (!this.setting.fixed) { left += doc.scrollLeft(); top += doc.scrollTop(); }
        // d.css({ left: left + "px", top: top + "px" });
        // if (this.setting.autoClose) window.setTimeout(this.close.bind(this), 3000);

        // var doc = $(document);
        // var win = $(window);
        // var left = (win.width() - this.dlg.width()) / 2;
        // var top = (win.height() - this.dlg.height()) / 2;
        // var top = (win.height() - this.dlg.height()) / 2;
        // if (!this.setting.fixed) { left += doc.scrollLeft(); top += doc.scrollTop(); }
        // this.dlg.css({ left: left + "px", top: top + "px" }).animate({ opacity: 1 });
        // if (this.setting.autoClose) window.setTimeout(this.close.bind(this), 3000);

        this.dlg.modal({backdrop: "static"});
    },
    // 设置错误信息
    setError: function (error) {
        this.find(".modal-body-error").text(error).show();
    },
    // 清除错误信息
    clearError: function () {
        this.find(".modal-body-error").text("").hide();
    },
    // 关闭对话框
    close: function (e) {
        this.dlg.modal("hide");
        this.find('div.modal-footer > button').each(function (i, btn) {
            $(btn).unbind();
        });

        this.onclose();
    },
    // 初始化
    onload: function () {
        if (typeof this.setting.load == 'function') {
            this.setting.load(this)
        }
    },
    // 清理资源
    onclose: function () {
        this.dlg.remove();
        $(".modal-backdrop").remove();
        $("body").removeClass("modal-open");
    }
};
function $info(content, load, btnThis) {
    var dlg = new Dialog();
    dlg.initialize({
        title: "提示",
        content: content,
        buttons: [{text: "确定", style: "btn-primary", btnThis: btnThis}],
        load: load
    });
    return dlg;
}
function $alert(title, content, callback, load, btnThis) {
    var dlg = new Dialog();
    dlg.initialize({
        title: title || "提示",
        content: content,
        buttons: [{text: "确定", style: "btn-primary", callback: callback, btnThis: btnThis}],
        load: load,
    });
    return dlg;
}
function $confirm(title, content, callback, load, btnThis) {
    var dlg = new Dialog();
    dlg.initialize({
        title: title || "提示",
        content: content,
        buttons: [{text: '确定', style: "btn-primary", callback: callback, btnThis: btnThis}, {
            text: '取消',
            btnThis: btnThis
        }],
        load: load
    });
    return dlg;
}

function $largeModal(title, content, callback, load, btnThis) {
    var dlg = new Dialog();
    dlg.initialize({
        width: 500,
        title: title || "提示",
        content: content,
        buttons: [{text: '确定', style: "btn-primary", callback: callback, btnThis: btnThis}, {
            text: '取消',
            btnThis: btnThis
        }],
        load: load
    });
    return dlg;
}
/***Dialog End***/

/***JSON Start***/
/**
 * jQuery JSON plugin v2.5.1
 * https://github.com/Krinkle/jquery-json
 *
 * @author Brantley Harris, 2009-2011
 * @author Timo Tijhof, 2011-2014
 * @source This plugin is heavily influenced by MochiKit's serializeJSON, which is
 *         copyrighted 2005 by Bob Ippolito.
 * @source Brantley Harris wrote this plugin. It is based somewhat on the JSON.org
 *         website's http://www.json.org/json2.js, which proclaims:
 *         "NO WARRANTY EXPRESSED OR IMPLIED. USE AT YOUR OWN RISK.", a sentiment that
 *         I uphold.
 * @license MIT License <http://opensource.org/licenses/MIT>
 */
(function ($) {
    'use strict';

    var escape = /["\\\x00-\x1f\x7f-\x9f]/g,
        meta = {
            '\b': '\\b',
            '\t': '\\t',
            '\n': '\\n',
            '\f': '\\f',
            '\r': '\\r',
            '"': '\\"',
            '\\': '\\\\'
        },
        hasOwn = Object.prototype.hasOwnProperty;

    /**
     * jQuery.toJSON
     * Converts the given argument into a JSON representation.
     *
     * @param o {Mixed} The json-serializable *thing* to be converted
     *
     * If an object has a toJSON prototype, that will be used to get the representation.
     * Non-integer/string keys are skipped in the object, as are keys that point to a
     * function.
     *
     */
    $.toJSON = typeof JSON === 'object' && JSON.stringify ? JSON.stringify : function (o) {
            if (o === null) {
                return 'null';
            }

            var pairs, k, name, val,
                type = $.type(o);

            if (type === 'undefined') {
                return undefined;
            }

            // Also covers instantiated Number and Boolean objects,
            // which are typeof 'object' but thanks to $.type, we
            // catch them here. I don't know whether it is right
            // or wrong that instantiated primitives are not
            // exported to JSON as an {"object":..}.
            // We choose this path because that's what the browsers did.
            if (type === 'number' || type === 'boolean') {
                return String(o);
            }
            if (type === 'string') {
                return $.quoteString(o);
            }
            if (typeof o.toJSON === 'function') {
                return $.toJSON(o.toJSON());
            }
            if (type === 'date') {
                var month = o.getUTCMonth() + 1,
                    day = o.getUTCDate(),
                    year = o.getUTCFullYear(),
                    hours = o.getUTCHours(),
                    minutes = o.getUTCMinutes(),
                    seconds = o.getUTCSeconds(),
                    milli = o.getUTCMilliseconds();

                if (month < 10) {
                    month = '0' + month;
                }
                if (day < 10) {
                    day = '0' + day;
                }
                if (hours < 10) {
                    hours = '0' + hours;
                }
                if (minutes < 10) {
                    minutes = '0' + minutes;
                }
                if (seconds < 10) {
                    seconds = '0' + seconds;
                }
                if (milli < 100) {
                    milli = '0' + milli;
                }
                if (milli < 10) {
                    milli = '0' + milli;
                }
                return '"' + year + '-' + month + '-' + day + 'T' +
                    hours + ':' + minutes + ':' + seconds +
                    '.' + milli + 'Z"';
            }

            pairs = [];

            if ($.isArray(o)) {
                for (k = 0; k < o.length; k++) {
                    pairs.push($.toJSON(o[k]) || 'null');
                }
                return '[' + pairs.join(',') + ']';
            }

            // Any other object (plain object, RegExp, ..)
            // Need to do typeof instead of $.type, because we also
            // want to catch non-plain objects.
            if (typeof o === 'object') {
                for (k in o) {
                    // Only include own properties,
                    // Filter out inherited prototypes
                    if (hasOwn.call(o, k)) {
                        // Keys must be numerical or string. Skip others
                        type = typeof k;
                        if (type === 'number') {
                            name = '"' + k + '"';
                        } else if (type === 'string') {
                            name = $.quoteString(k);
                        } else {
                            continue;
                        }
                        type = typeof o[k];

                        // Invalid values like these return undefined
                        // from toJSON, however those object members
                        // shouldn't be included in the JSON string at all.
                        if (type !== 'function' && type !== 'undefined') {
                            val = $.toJSON(o[k]);
                            pairs.push(name + ':' + val);
                        }
                    }
                }
                return '{' + pairs.join(',') + '}';
            }
        };

    /**
     * jQuery.evalJSON
     * Evaluates a given json string.
     *
     * @param str {String}
     */
    $.evalJSON = typeof JSON === 'object' && JSON.parse ? JSON.parse : function (str) {
            /*jshint evil: true */
            return eval('(' + str + ')');
        };

    /**
     * jQuery.secureEvalJSON
     * Evals JSON in a way that is *more* secure.
     *
     * @param str {String}
     */
    $.secureEvalJSON = typeof JSON === 'object' && JSON.parse ? JSON.parse : function (str) {
            var filtered =
                str
                    .replace(/\\["\\\/bfnrtu]/g, '@')
                    .replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, ']')
                    .replace(/(?:^|:|,)(?:\s*\[)+/g, '');

            if (/^[\],:{}\s]*$/.test(filtered)) {
                /*jshint evil: true */
                return eval('(' + str + ')');
            }
            throw new SyntaxError('Error parsing JSON, source is not valid.');
        };

    /**
     * jQuery.quoteString
     * Returns a string-repr of a string, escaping quotes intelligently.
     * Mostly a support function for toJSON.
     * Examples:
     * >>> jQuery.quoteString('apple')
     * "apple"
     *
     * >>> jQuery.quoteString('"Where are we going?", she asked.')
     * "\"Where are we going?\", she asked."
     */
    $.quoteString = function (str) {
        if (str.match(escape)) {
            return '"' + str.replace(escape, function (a) {
                    var c = meta[a];
                    if (typeof c === 'string') {
                        return c;
                    }
                    c = a.charCodeAt();
                    return '\\u00' + Math.floor(c / 16).toString(16) + (c % 16).toString(16);
                }) + '"';
        }
        return '"' + str + '"';
    };

}(jQuery));
/***JSON End***/

/***JQuery Extension***/
// 返回选中的 checkbox/radio
function $checked(name, find) {
    if (find == undefined) {
        find = $;
    }
    return find('input[name="' + name + '"]:checked');
}
// 返回指定 name 的 input
function $input(name, find) {
    if (find == undefined) {
        find = $;
    }
    return find('input[name="' + name + '"]');
}
// 返回指定 name 的 select
function $select(name, find) {
    if (find == undefined) {
        find = $;
    }
    return find('select[name="' + name + '"]');
}
// 返回 $obj 中所有名为 key 的 data 值，
// 结果：数组
function $dataArray($obj, key) {
    var arr = [];
    $obj.each(function () {
        $row = $(this);
        arr.push($row.data(key));
    });
    return arr;
}
// 返回 $obj 中所有名为 key 的 data 值，
// 结果：使用分割符号连接的字符串
//function $dataLine($obj, key, separator) {
//    var arr = $dataArray($obj, key);
//    return arr;
//}


/**
 * json序列化表单控件
 * @author Sun Wang
 * @returns {{}}
 */
$.fn.serializeToJson = function () {
    var json = {}
    $(this).each(function () {
        $(this).find(":text,:password,:file,input[type=hidden],:radio,:checkbox,select,textarea").each(function () {
            if (!$(this).is("[name]") || $(this).is(":disabled")) return;
            if ($(this).is(":radio,:checkbox") && !$(this).is(":checked")) return;
            var name = $(this).attr("name"), value = $(this).val(), index
            if (/([$_a-z0-9]+)\[([0-9]+)\]/i.test(name)) {
                name = RegExp.$1, index = RegExp.$2
                json[name] = json[name] || []
                if (!$.isArray(json[name])) throw "the value of element attribute [name] is invalid"
            }
            if ($.isArray(json[name])) {
                json[name][index] = value
            } else {
                if ($(this).is(":checkbox")) {
                    json[name] = (json[name] || "") + (json[name] ? "," : "") + value
                } else {
                    json[name] = value;
                }
            }
        })
    })
    return json;
}

/**
 * 清空表单控件
 * @returns {*}
 */
if (!$.fn.reset) $.fn.reset = function () {
    return $(this).each(function () {
        $(this).find(":text,:password,:file,input[type=hidden],select,:checkbox,:radio,textarea").each(function () {
            if ($(this).is(":checkbox,:radio")) {
                $(this).prop("checked", false)
            } else if ($(this).is("select")) {
                $(this).val(0)
            } else {
                $(this).val("")
            }
        })
    })
}

/**
 * 动态加载资源文件
 * @param url 加载路径，string|Array
 * @param success 全部成功后回调
 * @param error 失败后回调
 * @returns {jQuery}
 */
$.loadJS = function (urls, success, error) {
    var cache = $.loadJS.cache = $.loadJS.cache || {}
    var getDeferred = function (url) {
        if (url in cache) return cache[url]
        return cache[url] = $.getScript(url)
    }
    urls = $.isArray(urls) ? urls : [urls]
    var ajax = []
    for (var i = 0; i < urls.length; i++) {
        if (!$("script[src='" + urls[i] + "']")[0]) ajax.push(getDeferred(urls[i]))
    }
    return $.when.apply(this, ajax).done(success).fail(error)
}

/**
 * 模态抓取
 * @returns {*}
 */
$.fn.modalDraggable = function () {
    return $(this).each(function () {
        $(this).find(".modal-header").on("mousedown", function (e) {
            var modal = $(this).closest(".modal"),
                _drag = function (e) {
                    if (this.drag) {
                        var d = this.find("div.modal-content")
                        var left = d.prop("offsetLeft") + e.clientX - this.dragX;
                        var top = d.prop("offsetTop") + e.clientY - this.dragY;
                        d.css({left: left + "px", top: top + "px"})
                        this.dragX = e.clientX;
                        this.dragY = e.clientY;
                    }
                },
                _dragEnd = function (e) {
                    $(document).unbind({
                        mousemove: _drag.bind(modal),
                        mouseup: _dragEnd.bind(modal),
                        selectstart: _disableSelect.bind(modal)
                    });
                    e.target.style.cursor = "";
                    this.drag = false;
                },
                _disableSelect = function (e) {
                    return false;
                }
            modal.drag = true;
            modal.dragX = e.clientX;
            modal.dragY = e.clientY;
            e.target.style.cursor = "move";
            $(document).bind({
                mousemove: _drag.bind(modal),
                mouseup: _dragEnd.bind(modal),
                selectstart: _disableSelect.bind(modal)
            });
        });
    });
}
jQuery(function () {
    $(".modal[mx_draggable=true]").modalDraggable();
})
/***JQuery Extension End***/
/* String */
String.format = function () {
    var args = arguments;
    if (args.length == 0) return "";
    if (args.length == 1) return arguments[0];

    var regex = /{(\d+)?}/g, arg, result;
    if (args[1] instanceof Array) {
        arg = args[1];
        result = args[0].replace(regex, function ($0, $1) {
            return arg[parseInt($1)];
        });
    }
    else {
        arg = args;
        result = args[0].replace(regex, function ($0, $1) {
            return arg[parseInt($1) + 1];
        });
    }
    return result;
};
String.transform = function (template, value) {
    template = template || '';
    value = value || {};

    // 匹配参数的正则表达式，参数形式为: ${my-param}
    var reg = /\$\{([^{}]*)}/g;

    // 替换函数
    var replace = function (str, match) {
        return typeof value[match] === 'string' || typeof value[match] === 'number' ? value[match] : str;
    };

    return template.replace(reg, replace);
};

String.prototype.trim = function () {
    return this.replace(/(^[\s　]*)|([\s　]*$)/g, "");
};
String.prototype.ltrim = function () {
    return this.replace(/(^[\s　]*)/g, "");
};
String.prototype.rtrim = function () {
    return this.replace(/([\s　]*$)/g, "");
};
String.prototype.bytelength = function () {
    var doubleByteChars = this.match(/[^\x00-\xff]/ig);
    return this.length + (doubleByteChars == null ? 0 : doubleByteChars.length);
};
String.prototype.isEmpty = function () {
    return this.trim().length == 0;
};
String.prototype.toDate = function () {
    var dateString = this.replace(/\/Date\((\d+)(\+?\d+\))\//g, 'new Date($1)');
    return eval(dateString);
};
String.prototype.htmlEncode = function () {
    if (String._tempDom == null) String._tempDom = document.createElement("div");
    else String._tempDom.innerHTML = "";

    var text = document.createTextNode(this);
    String._tempDom.appendChild(text);
    return String._tempDom.innerHTML;
};
String.prototype.htmlDecode = function () {
    if (String._tempDom == null) String._tempDom = document.createElement("div");
    else String._tempDom.innerHTML = "";

    String._tempDom.innerHTML = this;
    return String._tempDom.innerText || String._tempDom.textContent;
    ;
};
String.prototype.startWith = function (s) {
    if (s == null || s == "" || this.length == 0 || s.length > this.length)
        return false;
    if (this.substr(0, s.length) == s)
        return true;
    else
        return false;
    return true;
}
/*
 * StringBuilder
 */
function StringBuilder() {
    this.strings = [];
}
StringBuilder.prototype = {
    append: function (text) {
        this.strings[this.strings.length] = String(text);
    },
    appendFormat: function () {
        this.strings[this.strings.length] = String.format.apply(String, arguments);
    },
    appendTransform: function (template, value) {
        this.strings[this.strings.length] = String.transform(template, value);
    },
    clear: function () {
        this.strings.length = 0;
    },
    backspace: function () {
        this.strings.pop();
    },
    toString: function () {
        return this.strings.join(arguments.length == 0 ? "" : arguments[0]);
    }
}
/***StringBuilder End***/
/*** 统计指定字符出现的次数 ***/
String.prototype.Occurs = function (ch) {
    return this.split(ch).length - 1;
}
/*** 返回数字 ***/
String.prototype.Digit = function () {
    return this.replace(/\D/g, "");
}
/*** 检查是否由数字组成 ***/
String.prototype.isDigit = function () {
    var s = this.Trim();
    return (s.replace(/\d/g, "").length == 0);
}

/*** 检查是否由数字字母和下划线组成 ***/
String.prototype.isAlpha = function () {
    return (this.replace(/\w/g, "").length == 0);
}
/*** 检查是否由数字字母和下划线组成 字母开头 ***/
String.prototype.isZAlpha = function () {
    var reg = /^[a-zA-Z][a-zA-Z0-9_]*$/;
    if (reg.test(this))
        return true;
    else
        return false;
}

/*** 验证是否合法的颜色 ***/
String.prototype.checkColor = function () {
    var pattern = /^#[0-9a-fA-F]{6}$/
    if (this.match(pattern) == null)
        return false;
    else
        return true;
}


/*** 检查是否为数 ***/
String.prototype.isNumber = function () {
    var s = this.Trim();
    return (s.search(/^[+-]?[0-9.]*$/) >= 0);
}

/*** 返回字节数 一个汉字二个字符 ***/
String.prototype.Lenb = function () {
    return this.replace(/[^\x00-\xff]/g, "**").length;
}

/**取汉字及字符**/
String.prototype.MidStr = function (len) {
    var strlen = 0;
    var s = "";
    var ss = 1;
    var str = this;
    for (var i = 0; i < str.length; i++) {
        if (str.charCodeAt(i) > 255)
            ss = 2;
        else
            ss = 1;
        if (strlen + ss <= len) {
            s += str.charAt(i);
            strlen += ss
        } else {
            return s;
        }
    }
    return s;
}

/*** 检查是否包含汉字 ***/
String.prototype.isInChinese = function () {
    return (this.length != this.replace(/[^\x00-\xff]/g, "**").length);
}
/*** 是否为汉字 ***/
String.prototype.existChinese = function () {
    //[\u4E00-\u9FA5]为汉字，[\uFE30-\uFFA0]为全角符号
    return /^[\x00-\xff]*$/.test(this);
}

/***是否是有效的电话号码***/
String.prototype.isPhoneCall = function () {
    var reg = /^(((\()?\d{2,4}(\))?[-(\s)*]){0,2})?(\d{7,8})$/;
    if (reg.test(this))// 电话号码格式正确
        return true;
    else //号码格式错误
        return false;
}

/***是否是数字***/
String.prototype.isNumeric = function (flag) {
    //验证是否是数字
    if (isNaN(this)) {
        return false;
    }
    switch (flag) {
        case null:        //数字
        case "":
            return true;
        case "+":        //正数
            return /(^\+?|^\d?)\d*\.?\d+$/.test(this);
        case "-":        //负数
            return /^-\d*\.?\d+$/.test(this);
        case "i":        //整数
            return /(^-?|^\+?|\d)\d+$/.test(this);
        case "+i":        //正整数
            return /(^\d+$)|(^\+?\d+$)/.test(this);
        case "-i":        //负整数
            return /^[-]\d+$/.test(this);
        case "f":        //浮点数
            return /(^-?|^\+?|^\d?)\d*\.\d+$/.test(this);
        case "+f":        //正浮点数
            return /(^\+?|^\d?)\d*\.\d+$/.test(this);
        case "-f":        //负浮点数
            return /^[-]\d*\.\d$/.test(this);
        default:        //缺省
            return true;
    }
}

/***清除所有的HTML代码***/
String.prototype.ClearHtml = function () {
    if (this != null) {
        return this.replace(/<.+?>/gm, '');
    }
}

String.prototype.isCardNo = function () {
    // 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X
    var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
    var s = this.trim();
    return reg.test(s);
}

String.prototype.isMobile = function () {
    // 手机号码验证
    var reg = /(^1\d{10}$)/;
    var s = this.trim();
    return reg.test(s);
}

$.bootstrapTable = function (table, url, queryParams, columns, callback) {
//    var $table = $('#table');
    //先销毁表格
    table.bootstrapTable('destroy');
    //初始化表格,动态从服务器加载数据
    table.bootstrapTable({
        method: "get",  //使用get请求到服务器获取数据
        url: url, //获取数据的Servlet地址
        striped: true,  //表格显示条纹
        pagination: true, //启动分页
        pageSize: 25,  //每页显示的记录数
        pageNumber: 1, //当前第几页
        pageList: [25, 50, 100, 200, 1000],  //记录数可选列表
        search: false,  //是否启用查询
        showColumns: false,  //显示下拉框勾选要显示的列
        showRefresh: false,  //显示刷新按钮
        showToggle: false,
        sidePagination: "server", //表示服务端请求
        //设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder
        //设置为limit可以获取limit, offset, search, sort, order
        queryParamsType: "undefined",
        queryParams: queryParams,
        onLoadSuccess: function (data) {  //加载成功时执行
            if (callback) {
                callback(data)
//          layer.msg("加载成功",{time : 1000});
            }
        },
        onLoadError: function () {  //加载失败时执行
            $.err('加载数据失败');
        },
        columns: columns
    });
}