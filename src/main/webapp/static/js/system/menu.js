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
            saveForm();
            return false;
        }
    });

    $('.set-status-btn').click(function () {
        let args = {
            id: $(this).data('id'),
            status: $(this).data('status')
        };
        POST($(this).data('url'), args, function (res) {
            $.success(res.message, function () {
                location.reload();
            });
        });
    });

});

function saveForm() {
    var form = $('#save-form');
    var formData = new FormData();
    var data = form.serializeArray();
    var args = {};
    for (var i in data) {
        args[data[i].name] = data[i].value;
    }
    formData.append('data', JSON.stringify(args));
    $.loading('show');
    $.ajax({
        url: stringTrim(form.attr('action'), '.html'),
        type: 'POST',
        data: formData,
        dataType: 'json',
        contentType: false,
        processData: false,
        success: function (res) {
            if (res.code == 200) {
                $.success(res.message, function () {
                    location.href = $("#index-url").val();
                });
            } else {
                $.error(res.message);
            }
        },
        complete: function () {
            $.loading('hide');
        }
    });
}