/*
 jq:http://jquery.cuishifeng.cn/index.html
 layer:http://layer.layui.com/mobile/
 */

$(function () {

    $('#models').cxSelect({
        url: '/cartree/listdata',
        //url: 'js/cityData.min.json',
        selects: ['brand'],
        nodata: 'none'
    });

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
            $.post("/carinfo/delete", {"carInfoId": row.carInfoId}, function (result) {
                if (result.success) {
                    //移除行
                    $table.bootstrapTable('remove', {
                        field: 'carInfoId',
                        values: [row.carInfoId]
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
    var url = "/usercar/listdata",
        queryParams = function queryParams(params) {   //设置查询参数
            var param = {
                pageIndex: params.pageNumber,
                pageSize: params.pageSize,
                specId: parseInt($('#specId').val()) || 0
            };
            return param;
        },
        columns = [{
            field: 'userCarInfoId',
            title: '编号'
        }, {
            field: 'modelsName',
            title: '车型'
        }, {
            field: 'specName',
            title: '规格'
        }, {
            field: 'configure',
            title: '配置'
        }, {
            field: 'frame',
            title: '车架号'
        }, {
            field: 'price',
            title: '价格',
            formatter: function (value, row, index) {
                return value + '万';
            }
        }]
    $.bootstrapTable($table, url, queryParams, columns);
}
