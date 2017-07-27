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
            $.post("/notice/delete", {"noticeId": row.noticeId}, function (result) {
                if (result.success) {
                    //移除行
                    $table.bootstrapTable('remove', {
                        field: 'noticeId',
                        values: [row.noticeId]
                    });
                    dlg.close();
                } else {
                    $alert("提示", result.error);
                }
            });

        })
    }
};

//初始化表格
function initTable() {
    var url = "/notice/listdata",
        queryParams = function queryParams(params) {   //设置查询参数
            var param = {
                pageIndex: params.pageNumber,
                pageSize: params.pageSize,
                title: $('#title').val()
            };
            return param;
        },
        columns = [{
            field: 'noticeId',
            title: '编号'
        }, {
            field: 'title',
            title: '标题'
        }, {
            field: 'createTime',
            title: '创建时间'
        }, {
            field: 'op',
            title: '操作',
            events: operateEvents,
            formatter: function (value, row, index) {
                var sb = new StringBuilder();
                sb.appendFormat('<a class="btn" href="/notice/edit/{0}"><i class="fa fa-fw fa-edit"></i></a>', row.noticeId);
                sb.append(' <a class="btn remove"><i class="fa fa-fw fa-trash"></i></a>');
                return sb.toString();
            }
        },]
    $.bootstrapTable($table, url, queryParams, columns);
}
