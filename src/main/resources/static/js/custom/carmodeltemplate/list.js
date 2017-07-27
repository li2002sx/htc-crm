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
            $.post("/cartemplate/delete", {templateId: row.templateId}, function (result) {
                if (result.success) {
                    //移除行
                    $table.bootstrapTable('remove', {
                        field: 'templateId',
                        values: [row.templateId]
                    });
                    dlg.close();
                } else {
                    $alert("提示", result.error);
                }
            });

        })
    },
    'click .updateacquiesce': function (e, value, row, index) {
        $confirm("提示", "确定要设置为默认吗？", function (dlg) {
            var param = {
                templateId: row.templateId,
                carModelId: carModelId
            }
            $.post("/cartemplate/acquiesce", param, function (result) {
                if (result.success) {
                    dlg.close();
                    location.href = '/cartemplate/list?modelid=' + carModelId;
                } else {
                    $alert("提示", result.error);
                }
            });

        })
    }
};

//初始化表格
function initTable() {
    var url = "/cartemplate/listdata",
        queryParams = function queryParams(params) {   //设置查询参数
            var param = {
                pageIndex: params.pageNumber,
                pageSize: params.pageSize,
                carModelId: carModelId,
                title: $('#title').val()
            };
            return param;
        },
        columns = [{
            field: 'templateId',
            title: '编号'
        }, {
            field: 'title',
            title: '标题'
        }, {
            field: 'picUrl',
            title: '背景图',
            formatter: function (value, row, index) {
                if (value != undefined) {
                    return '<img src=' + value + ' width="50" height="50" />';
                }
            }
        }, {
            field: 'acquiesce',
            title: '是否默认',
            events: operateEvents,
            formatter: function (value, row, index) {
                var sb = new StringBuilder();
                sb.appendFormat('<a class="btn updateacquiesce">{0}<i class="fa fa-fw fa-edit"></i></a>', value == 1 ? '是' : '否');
                return sb.toString();
            }
        }, {
            field: 'op',
            title: '操作',
            events: operateEvents,
            formatter: function (value, row, index) {
                var sb = new StringBuilder();
                sb.appendFormat('<a class="btn" href="/cartemplate/edit/{0}?modelid={1}"><i class="fa fa-fw fa-edit"></i></a>', row.templateId, carModelId);
                sb.append(' <a class="btn remove"><i class="fa fa-fw fa-trash"></i></a>');
                return sb.toString();
            }
        },]
    $.bootstrapTable($table, url, queryParams, columns);
}
