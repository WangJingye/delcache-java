$(function () {
    $('#save-form').validate({
        rules: {
            name: {
                required: true
            }
        },
        messages: {
            name: {
                required: '请输入角色名称'
            }
        },
        submitHandler: function (e) {
            saveForm($('#save-form'));
            return false;
        }
    });
    var setting = {
        check: {
            enable: true,
            chkboxType: {'Y': 'p' + 's', 'N': 'p' + 's'}
        },
        view: {
            dblClickExpand: false
        },
        data: {
            simpleData: {
                enable: true
            }
        }
    };
    $('#save-role-menu-form').validate({
        submitHandler: function (e) {
            saveRoleMenuForm();
            return false;
        }
    });
    $('#save-role-admin-form').validate({
        submitHandler: function (e) {
            saveForm($('#save-role-admin-form'));
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
    if (typeof menuList != 'undefined') {
        $.fn.zTree.init($('#menuTree'), setting, menuList);
    }
});
var nodesArr = [];

function getCheckTreeNodes(nodes) {
    $.each(nodes, function (i, obj) {
        if (obj.checked) {
            nodesArr.push(obj.id);
            if (obj.children) {
                getCheckTreeNodes(obj.children);
            }
        }
    });
}

function saveRoleMenuForm() {
    nodesArr = [];
    var zTree = $.fn.zTree.getZTreeObj('menuTree');
    getCheckTreeNodes(zTree.getNodes());
    var form = $('#save-role-menu-form');
    $('input[name=menu_ids]').val(nodesArr.join(','));
    var data = form.serialize();
    POST(form.attr('action'), data);
}