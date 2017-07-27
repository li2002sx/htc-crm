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
            $.post("/carmodel/delete", {"carModelId": row.carModelId}, function (result) {
                if (result.success) {
                    //移除行
                    $table.bootstrapTable('remove', {
                        field: 'carModelId',
                        values: [row.carModelId]
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
    var url = "/carmodel/listdata",
        queryParams = function queryParams(params) {   //设置查询参数
            var param = {
                pageIndex: params.pageNumber,
                pageSize: params.pageSize,
                specId: parseInt($('#specId').val()) || 0,
                brandId: parseInt($('#brandId').val()) || 0
            };
            return param;
        },
        columns = [{
            field: 'carModelId',
            title: '编号'
        }, {
            field: 'brandName',
            title: '品牌'
        }, {
            field: 'seriesName',
            title: '车系'
        }, {
            field: 'modelsName',
            title: '车型'
        }, {
            field: 'specName',
            title: '规格'
        }, {
            field: 'op',
            title: '操作',
            events: operateEvents,
            formatter: function (value, row, index) {
                var sb = new StringBuilder();
                sb.appendFormat('<a class="btn" href="/carmodel/edit/{0}"><i class="fa fa-fw fa-edit"></i></a>', row.carModelId);
                sb.appendFormat('<a class="btn" title="车型模块" href="/carmodule/list?modelid={0}"><i class="fa fa-fw fa-cubes"></i></a>', row.carModelId);
                sb.appendFormat('<a class="btn" title="车型模板" href="/cartemplate/list?modelid={0}"><i class="fa fa-fw fa-building-o"></i></a>', row.carModelId);
                sb.append(' <a class="btn  remove"><i class="fa fa-fw fa-trash"></i></a>');
                return sb.toString();
            }
        },]
    $.bootstrapTable($table, url, queryParams, columns);
}
