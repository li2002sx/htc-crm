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
            $.post("/userphoto/delete", {"userPhotoId": row.userPhotoId}, function (result) {
                if (result.success) {
                    //移除行
                    $table.bootstrapTable('remove', {
                        field: 'userPhotoId',
                        values: [row.userPhotoId]
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
    var url = "/userphoto/listdata",
        queryParams = function queryParams(params) {   //设置查询参数
            var param = {
                pageIndex: params.pageNumber,
                pageSize: params.pageSize,
                userId: parseInt($('#userId').val()) || 0
            };
            return param;
        },
        columns = []
    $.bootstrapTable($table, url, queryParams, columns, function (data) {
        var sb = new StringBuilder();
        if (data != null && data.rows != null) {
            data.rows.forEach(function (item) {
                sb.append('<li>');
                sb.appendFormat('<img src="{0}" alt="User Image">', item.realPicUrl);
                sb.appendFormat('<a class="users-list-name" href="#">用户ID：{0}</a>', item.userId);
                sb.appendFormat('<span class="users-list-date">{0}</span>', item.createTime);
                sb.append('</li>');
            })
        }
        $('#rows').html(sb.toString());
    });
}
