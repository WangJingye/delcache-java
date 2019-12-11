$(function () {
    $('#save-form').validate({
        rules: {
            parent_id: {
                required: true
            },
            name: {
                required: true
            }
        },
        messages: {
            parent_id: {
                required: '请选择父级功能'
            },
            name: {
                required: '请输入标题'
            }
        },
        submitHandler: function (e) {
            saveForm($('#save-form'), function (res) {
                if ($('#index-url').get(0)) {
                    location.href = $("#index-url").val();
                }
            });
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

});
