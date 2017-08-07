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
            $.post("/userrecharge/delete", {"userRechargeId": row.userRechargeId}, function (result) {
                if (result.success) {
                    //移除行
                    $table.bootstrapTable('remove', {
                        field: 'userRechargeId',
                        values: [row.userRechargeId]
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
    var url = "/userrecharge/listdata",
        queryParams = function queryParams(params) {   //设置查询参数
            var param = {
                pageIndex: params.pageNumber,
                pageSize: params.pageSize,
                userId: parseInt($('#userId').val()) || 0
            };
            return param;
        },
        columns = [{
            field: 'userRechargeId',
            title: '编号'
        }, {
            field: 'userId',
            title: '用户ID'
        }, {
            field: 'orderNumber',
            title: '订单号'
        }, {
            field: 'amount',
            title: '金额'
        }, {
            field: 'statusName',
            title: '状态'
        }, {
            field: 'createTime',
            title: '创建时间'
        }, {
            field: 'finishTime',
            title: '完成时间'
        }]
    $.bootstrapTable($table, url, queryParams, columns);
}
