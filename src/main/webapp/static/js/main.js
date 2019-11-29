$(function () {
    if ($('.select2').get(0)) {
        $('.select2').select2();
    }
    $('form .select2,.select').on('change', function () {
        $(this).valid();
    });
    jQuery.validator.setDefaults({
        errorClass: "help-block",
        errorElement: "span",
        highlight: function (element, errorClass, validClass) {
            $(element).parents('.form-group').removeClass('has-success').addClass('has-error').removeClass("has-feedback").addClass("has-feedback");
            if ($(element).hasClass('select2')) {
                if ($(element).parents('.form-group').find('.form-control-feedback').get(0)) {
                    $(element).parents('.form-group').find('.form-control-feedback').removeClass("glyphicon-ok").addClass("glyphicon-remove")
                } else {
                    $(element).next('span').find('.select2-selection').after('<span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>');
                }
            } else {
                if ($(element).attr('type') != 'radio' && $(element).attr('type') != 'checkbox') {
                    if ($(element).parents('.form-group').find('.form-control-feedback').get(0)) {
                        $(element).parents('.form-group').find('.form-control-feedback').removeClass("glyphicon-ok").addClass("glyphicon-remove")
                    } else {
                        $(element).after('<span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>');
                    }
                }
            }
        },
        unhighlight: function (element, errorClass, validClass) {
            $(element).parents('.form-group').removeClass('has-error').addClass('has-success').removeClass("has-feedback").addClass("has-feedback");
            if ($(element).hasClass('select2')) {
                if ($(element).parents('.form-group').find('.form-control-feedback').get(0)) {
                    $(element).parents('.form-group').find('.form-control-feedback').removeClass("glyphicon-remove").addClass("glyphicon-ok")
                } else {
                    $(element).next('span').find('.select2-selection').after('<span class="glyphicon glyphicon-ok form-control-feedback" aria-hidden="true"></span>');
                }
            } else {
                if ($(element).attr('type') != 'radio' && $(element).attr('type') != 'checkbox') {
                    if ($(element).parents('.form-group').find('.form-control-feedback').get(0)) {
                        $(element).parents('.form-group').find('.form-control-feedback').removeClass("glyphicon-remove").addClass("glyphicon-ok")
                    } else {
                        $(element).after('<span class="glyphicon glyphicon-ok form-control-feedback" aria-hidden="true"></span>');
                    }
                }
            }
        },
        errorPlacement: function (error, element) {
            if (element.attr('type') == 'radio' || element.attr('type') == 'checkbox') {
                error.insertAfter(element.parents('.form-radio-group'));
            } else if (element.hasClass('select2')) {
                error.insertAfter(element.next('span'));
            } else if(element.parent().hasClass('input-group')){
                error.insertAfter(element.parent());
            }else{
                error.insertAfter(element);
            }
        }
    });
    $('.fileinput-box-list').on('change', '.fileinput-input', function () {
        var $this = $(this);
        var box = $this.parents('.fileinput-box');
        box.find('input[type=hidden]').remove();
        var boxContainer = $this.parents('.fileinput-box-list');
        var maxNumber = boxContainer.attr('data-max');
        var nowNumber = boxContainer.find('.fileinput-box').length;
        if ($this.val()) {
            var reader = new FileReader();
            reader.onload = function (e) {
                if (box.find('img').length) {
                    box.find('img').attr('src', e.target.result);
                } else {
                    var imgHtml = '<img src="' + e.target.result + '">';
                    box.find('.fileinput-button').before(imgHtml);
                    var btnHtml = '<div class="file-remove-btn"><div class="btn btn-sm btn-danger" style="font-size: 0.5rem;">删除</div></div>';
                    box.find('.fileinput-button').after(btnHtml);
                    box.find('.plus-symbol').hide();
                }
            };
            if ($this.hasClass('add-new') && maxNumber > nowNumber) {
                var newBox = box.clone();
                var max = (parseInt($this.attr('data-max')) + 1).toString();
                var keyName = $this.attr('data-name');
                box.after('<div class="fileinput-box">' + newBox.html() + '</div>');
                box.parents('.fileinput-box-list').find('.fileinput-input').last().attr('name', keyName + '[' + max + ']');
            }
            if ($this[0].files.length) {
                reader.readAsDataURL($this[0].files[0]);
                $this.removeClass('add-new');
            }
        } else {
            box.find('.plus-symbol').show();
            box.find('img').remove();
        }
    }).on('click', '.file-remove-btn', function () {
        var $this = $(this);
        var boxContainer = $this.parents('.fileinput-box-list');
        var box = $this.parents('.fileinput-box');
        var emptyBox = box.clone();
        if (!boxContainer.find('.add-new').length) {
            emptyBox.find('img').remove();
            emptyBox.find('input[type=hidden]').remove();
            emptyBox.find('.fileinput-input').addClass('add-new');
            emptyBox.find('.file-remove-btn').remove();
            emptyBox.find('.plus-symbol').show();
            boxContainer.find('.fileinput-box').last().after('<div class="fileinput-box">' + emptyBox.html() + '</div>')
        }
        box.remove();
    });
    $('.main-item').click(function () {
        $('.sub-item').collapse('hide');
        $(this).next('.sub-item').collapse('toggle')
    });
    $('.list-sub-item .list-group-item').click(function () {
        location.href = $(this).data('url');
    });
    $('.search-form').on('click', '.search-btn', function () {
        if ($('#page-size').get(0)) {
            $(this).parents('form').append('<input type="hidden" name="pageSize" value="' + $('#page-size').val() + '"/>');
        }
        $(this).parents('form').submit();
    });
    $('#page-size').change(function () {
        var form = $('.search-form');
        if (!form.find('[name=pageSize]').get(0)) {
            form.append('<input type="hidden" name="pageSize" value="' + $(this).val() + '">');
        } else {
            form.find('[name=pageSize]').val($(this).val());
        }
        form.submit();
    });
});

function POST(url, args, callback) {
    $.loading('show');
    $.post(stringTrim(url, '.html'), args, function (res) {
        $.loading('hide');
        if (res.code == 200) {
            callback(res);
        } else {
            $.error(res.message);
        }
    }, 'json');
}

Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

function stringTrim(str, element) {
    elementLength = element.length;
    beginIndexFlag = true;
    endIndexFlag = true;
    do {
        beginIndex = str.indexOf(element) == 0 ? elementLength : 0;
        endIndex = str.lastIndexOf(element);
        endIndex = endIndex + elementLength == str.length ? endIndex : str.length;
        str = str.substr(beginIndex, endIndex);
        beginIndexFlag = (str.indexOf(element) == 0);
        endIndexFlag = (str.lastIndexOf(element) + elementLength == str.length);
    } while (beginIndexFlag || endIndexFlag);
    return str;
}