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
            $.post("/moduleinfo/delete", {"moduleInfoId": row.moduleInfoId}, function (result) {
                if (result.success) {
                    //移除行
                    $table.bootstrapTable('remove', {
                        field: 'moduleInfoId',
                        values: [row.moduleInfoId]
                    });
                    dlg.close();
                } else {
                    $alert("提示", result.error);
                }
            });

        })
    },
    'click .updateorderno': function (e, value, row, index) {
        var sb = new StringBuilder();
        sb.append('<div class="input-group">');
        sb.append('<span class="input-group-addon">序号：</span>');
        sb.appendFormat('<input class="form-control" id="orderNo" value="{0}" />', row.orderNo);
        sb.append('</div>');

        $confirm("修改序号", sb.toString(), function (dlg) {
            var param = {
                moduleInfoId: row.moduleInfoId,
                orderNo: parseInt($('#orderNo').val().trim()) || 0
            }
            $.post("/moduleinfo/updateorderno", param, function (result) {
                if (result.success) {
                    dlg.close();
                    initTable();
                } else {
                    $alert("提示", result.error);
                }
            });

        })
    }
};

//初始化表格
function initTable() {
    var url = "/moduleinfo/listdata",
        queryParams = function queryParams(params) {   //设置查询参数
            var param = {
                pageIndex: params.pageNumber,
                pageSize: params.pageSize,
                moduleId: moduleId,
                title: $('#title').val()
            };
            return param;
        },
        columns = [{
            field: 'moduleInfoId',
            title: '编号'
        }, {
            field: 'title',
            title: '标题'
        }, {
            field: 'color',
            title: '颜色'
        }, {
            field: 'picUrl',
            title: '配图',
            formatter: function (value, row, index) {
                if (value != undefined) {
                    return '<img src=' + value + ' width="50" height="50" />';
                }
            }
        }, {
            field: 'orderNo',
            title: '排序',
            events: operateEvents,
            formatter: function (value, row, index) {
                var sb = new StringBuilder();
                sb.appendFormat('<a class="btn updateorderno">{0}<i class="fa fa-fw fa-edit"></i></a>', value);
                return sb.toString();
            }
        }, {
            field: 'op',
            title: '操作',
            events: operateEvents,
            formatter: function (value, row, index) {
                var sb = new StringBuilder();
                sb.appendFormat('<a class="btn" href="/moduleinfo/edit/{0}?moduleid={1}&modelid={2}"><i class="fa fa-fw fa-edit"></i></a>', row.moduleInfoId, row.moduleId, carModelId);
                sb.append(' <a class="btn remove"><i class="fa fa-fw fa-trash"></i></a>');
                return sb.toString();
            }
        },]
    $.bootstrapTable($table, url, queryParams, columns);
}
