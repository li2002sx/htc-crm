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
            $.post("/carowner/delete", {"carOwnerId": row.carOwnerId}, function (result) {
                if (result.success) {
                    //移除行
                    $table.bootstrapTable('remove', {
                        field: 'carOwnerId',
                        values: [row.carOwnerId]
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
    var url = "/carowner/listdata",
        queryParams = function queryParams(params) {   //设置查询参数
            var param = {
                pageIndex: params.pageNumber,
                pageSize: params.pageSize,
                month: parseInt($('#month').val()) || 0,
                name: $('#name').val()
            };
            return param;
        },
        columns = [{
            field: 'carOwnerId',
            title: '编号'
        }, {
            field: 'provinceName',
            title: '省份'
        }, {
            field: 'cityName',
            title: '城市'
        }, {
            field: 'name',
            title: '车主姓名'
        }, {
            field: 'phone',
            title: '联系电话'
        }, {
            field: 'address',
            title: '地址'
        }, {
            field: 'cardNo',
            title: '身份证号'
        }, {
            field: 'birthday',
            title: '生日'
        }, {
            field: 'buyTime',
            title: '购买时间'
        }, {
            field: 'buyCarModels',
            title: '购买车型'
        }, {
            field: 'op',
            title: '操作',
            events: operateEvents,
            formatter: function (value, row, index) {
                var sb = new StringBuilder();
                sb.appendFormat('<a class="btn" href="/carowner/edit/{0}"><i class="fa fa-fw fa-edit"></i></a>', row.carOwnerId);
                sb.append(' <a class="btn remove"><i class="fa fa-fw fa-trash-o"></i></a>');
                return sb.toString();
            }
        },]
    $.bootstrapTable($table, url, queryParams, columns);
}
