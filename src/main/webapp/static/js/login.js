$(function () {
    $('#login-form').validate({
        rules: {
            username: {
                required: true
            },
            password: {
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
        },
        submitHandler: function (e) {
            submitForm();
            return false;
        }
    });

    $('.captcha-box').on('click', 'img', function () {
        $(this).attr('src', $(this).data('src') + '?' + new Date().getTime());
    });
});

function submitForm() {
    var form = $('#login-form');
    var data = form.serialize();
    $.loading('show');
    POST(form.attr('action'), data, function (res) {
        $.loading('hide');
        if (res.code == 200) {
            $.success(res.message);
            setTimeout(function () {
                location.href = $('#index-url').val();
            }, 2000)
        } else {
            $.error(res.message);
        }
    }, 'json');
}