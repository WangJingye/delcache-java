$(function () {
    //编辑用户
    $('#save-form').validate({
        rules: {
            username: {
                required: true
            },
            realname: {
                required: true
            },
            email: {
                required: true
            }
        },
        messages: {
            username: {
                required: '请输入用户名称'
            },
            realname: {
                required: '请输入真实姓名'
            },
            email: {
                required: '请输入邮箱'
            }
        },
        submitHandler: function (e) {
            saveForm($('#save-form'));
            return false;
        }
    });
    //修改个人信息
    $('#change-user-info-form').validate({
        rules: {
            realname: {
                required: true
            },
            email: {
                required: true
            }
        },
        messages: {
            realname: {
                required: '请输入真实姓名'
            },
            email: {
                required: '请输入邮箱'
            }
        },
        submitHandler: function (e) {
            saveForm($('#change-user-info-form'), function () {
                location.reload();
            });
            return false;
        }
    });
    //修改密码
    $('#change-password-form').validate({
        rules: {
            password: {
                required: true
            },
            newPassword: {
                minlength: 6,
                required: true
            },
            rePassword: {
                minlength: 6,
                required: true,
                equalTo: "#newPassword"
            }
        },
        messages: {
            password: {
                required: '请输入当前登录密码'
            },
            newPassword: {
                minlength: '新登录密码不能少于6位',
                required: '请输入新登录密码'
            },
            rePassword: {
                minlength: '确认密码不能少于6位',
                required: '请输入确认新登录密码',
                equalTo: '确认密码和新登录密码不一致'
            }
        },
        submitHandler: function (e) {
            changePasswordForm();
            return false;
        }
    });

    $('.set-status-btn').click(function () {
        let args = {
            id: $(this).data('id'),
            status: $(this).data('status')
        };
        POST($(this).data('url'), args, function (res) {
            location.reload();
        });
    });
    $('.reset-password-btn').click(function () {
        if (!confirm('是否重置密码为' + $(this).data('default') + '?')) {
            return false;
        }
        POST($(this).data('url'), {admin_id: $(this).data('id')});
    });

    if (window.location.hash && $(window.location.hash).get(0)) {
        $('.profile-nav a[href="' + window.location.hash + '"]').tab('show');
    }
});

function changePasswordForm() {
    let form = $('#change-password-form');
    POST(form.attr('action'), form.serialize(), function (res) {
        location.reload();
    }, 'json');
}