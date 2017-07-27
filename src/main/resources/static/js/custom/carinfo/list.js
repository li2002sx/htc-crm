/*
 jq:http://jquery.cuishifeng.cn/index.html
 layer:http://layer.layui.com/mobile/
 */

$(function () {
    //Date picker
    $("#datepicker").datepicker({
        //minView: "month", //选择日期后，不会再跳转去选择时分秒
        format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式
        language: 'zh-CN', //汉化
        autoclose: true //选择日期后自动关闭
    });

    $('#models').cxSelect({
        url: '/cartree/listdata',
        //url: 'js/cityData.min.json',
        selects: ['brand'],
        nodata: 'none'
    });

    $('#region').cxSelect({
        url: '/region/listdata',
        //url: 'js/cityData.min.json',
        selects: ['province', 'city'],
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
    var url = "/carinfo/listdata",
        queryParams = function queryParams(params) {   //设置查询参数
            var param = {
                pageIndex: params.pageNumber,
                pageSize: params.pageSize,
                year: parseInt($('#year').val()) || 0,
                month: parseInt($('#month').val()) || 0,
                specId: parseInt($('#specId').val()) || 0,
                brandId: parseInt($('#brandId').val()) || 0,
                provinceId: parseInt($('#provinceId').val()) || 0,
                cityId: parseInt($('#cityId').val()) || 0
            };
            return param;
        },
        columns = [{
            field: 'carInfoId',
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
            field: 'configure',
            title: '配置'
        }, {
            field: 'frame',
            title: '车架号'
        }, {
            field: 'outColor',
            title: '外饰'
        }, {
            field: 'inColor',
            title: '内饰'
        }, {
            field: 'sourceName',
            title: '来源状态'
        }, {
            field: 'procedureName',
            title: '手续状态'
        }, {
            field: 'provinceName',
            title: '省份'
        }, {
            field: 'cityName',
            title: '城市'
        }, {
            field: 'time',
            title: '日期'
        }, {
            field: 'price',
            title: '价格',
            formatter: function (value, row, index) {
                return value + '万';
            }
        }, {
            field: 'priceRatio',
            title: '占比',
            formatter: function (value, row, index) {
                return value + '%';
            }
        }, {
            field: 'op',
            title: '操作',
            events: operateEvents,
            formatter: function (value, row, index) {
                var sb = new StringBuilder();
                sb.appendFormat('<a class="btn" href="/carinfo/edit/{0}"><i class="fa fa-fw fa-edit"></i></a>', row.carInfoId);
                sb.append(' <a class="btn  remove"><i class="fa fa-fw fa-trash"></i></a>');
                return sb.toString();
            }
        },]
    $.bootstrapTable($table, url, queryParams, columns);
}
