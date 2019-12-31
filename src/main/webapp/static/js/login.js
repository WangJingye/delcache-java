$(function () {
    $('#login-form').validate({
        rules: {
            username: {
                required: true
            },
            password: {
                required: true
            },
            captcha: {
                required: true
            },
        },
        messages: {
            username: {
                required: '请输入用户名'
            },
            password: {
                required: '请输入密码'
            },
            captcha: {
                required: '请输入验证码'
            },
        },
        highlight: function (element, errorClass, validClass) {
            $(element).parents('.form-group').removeClass('has-success').addClass('has-error').removeClass("has-feedback").addClass("has-feedback");
            if (!$(element).hasClass("captcha")) {
                if ($(element).parents('.form-group').find('.form-control-feedback').get(0)) {
                    $(element).parents('.form-group').find('.form-control-feedback').removeClass("glyphicon-ok").addClass("glyphicon-remove")
                } else {
                    $(element).after('<span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>');
                }
            }
        },
        unhighlight: function (element, errorClass, validClass) {
            $(element).parents('.form-group').removeClass('has-error').addClass('has-success').removeClass("has-feedback").addClass("has-feedback");
            if (!$(element).hasClass("captcha")) {
                if ($(element).parents('.form-group').find('.form-control-feedback').get(0)) {
                    $(element).parents('.form-group').find('.form-control-feedback').removeClass("glyphicon-remove").addClass("glyphicon-ok")
                } else {
                    $(element).after('<span class="glyphicon glyphicon-ok form-control-feedback" aria-hidden="true"></span>');
                }
            }
        },
        submitHandler: function (e) {
            loginForm();
            return false;
        }
    });

    $('.captcha-box').on('click', 'img', function () {
        $(this).attr('src', $(this).data('src') + '?' + new Date().getTime());
    });
});

function loginForm() {
    var form = $('#login-form');
    var data = form.serialize();
    $.loading('show');
    $.post(stringTrim(form.attr('action'), '.html'), data, function (res) {
        $.loading('hide');
        if (res.code == 200) {
            $.success(res.message);
            setTimeout(function () {
                location.href = $('#index-url').val();
            }, 2000)
        } else {
            $.error(res.message);
            $('.captcha-box').find('img').click();
        }
    }, 'json');
}