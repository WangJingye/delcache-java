$(function () {
    $('#save-form').validate({
        rules: {
            default_password: {
                required: true
            }
        },
        messages: {
            default_password: {
                required: '请输入默认密码'
            }
        },
        submitHandler: function (e) {
            saveForm($('#save-form'), function (res) {
                location.reload();
            });
            return false;
        }
    });
});