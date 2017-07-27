/*
 jq:http://jquery.cuishifeng.cn/index.html
 layer:http://layer.layui.com/mobile/
 */

$(function () {
    //调用函数，初始化表格
    initTable();

    //当点击查询按钮的时候执行
    $("#search").bind("click", initTable);
});

//操作事件
var $table = $('#table');
window.operateEvents = {
    'click .remove': function (e, value, row, index) {
        $confirm("提示", "确定要删除吗？", function (dlg) {
            //执行远程“删除操作”，修改状态为10
            $.post("/adminuser/delete", {"adminUserId": row.adminUserId}, function (result) {
                if (result.success) {
                    //移除行
                    $table.bootstrapTable('remove', {
                        field: 'adminUserId',
                        values: [row.adminUserId]
                    });
                    dlg.close();
                } else {
                    $alert("提示", result.error);
                }
            });

        })
    },
    'click .resetpass': function (e, value, row, index) {
        $confirm("提示", "确定要重置密码吗？", function (dlg) {
            //执行远程“删除操作”，修改状态为10
            $.post("/adminuser/resetpassword", {"adminUserId": row.adminUserId}, function (result) {
                if (result.success) {
                    $alert("提示", "重置成功！账户密码为:" + result.value + "，请手工记录", function () {
                        location.href = "/adminuser/list";
                    });
                } else {
                    $alert("提示", result.error);
                }
            });
        })
    }
};

//初始化表格
function initTable() {
    var url = "/adminuser/listdata",
        queryParams = function queryParams(params) {   //设置查询参数
            var param = {
                pageIndex: params.pageNumber,
                pageSize: params.pageSize,
                companyName: $('#companyName').val(),
                name: $('#name').val()
            };
            return param;
        },
        columns = [{
            field: 'adminUserId',
            title: '编号'
        }, {
            field: 'userName',
            title: '名称'
        }, {
            field: 'roleName',
            title: '角色'
        }, {
            field: 'remark',
            title: '备注'
        }, {
            field: 'lastLoginTime',
            title: '最后登录时间'
        }, {
            field: 'createTime',
            title: '创建时间'
        }, {
            field: 'op',
            title: '操作',
            events: operateEvents,
            formatter: function (value, row, index) {
                var sb = new StringBuilder();
                sb.appendFormat('<a class="btn" href="/adminuser/edit/{0}"><i class="fa fa-fw fa-edit"></i></a>', row.adminUserId);
                sb.append(' <a class="btn resetpass"><i class="fa fa-fw fa-lock"></i></a>');
                sb.append(' <a class="btn remove"><i class="fa fa-fw fa-trash"></i></a>');
                return sb.toString();
            }
        },]
    $.bootstrapTable($table, url, queryParams, columns);
}
