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
            $.post("/cardealer/delete", {"carDealerId": row.carDealerId}, function (result) {
                if (result.success) {
                    //移除行
                    $table.bootstrapTable('remove', {
                        field: 'carDealerId',
                        values: [row.carDealerId]
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
    var url = "/cardealer/listdata",
        queryParams = function queryParams(params) {   //设置查询参数
            var param = {
                pageIndex: params.pageNumber,
                pageSize: params.pageSize,
                companyName: $('#companyName').val(),
                name: $('#name').val(),
                provinceId: parseInt($('#provinceId').val()) || 0,
                cityId: parseInt($('#cityId').val()) || 0
            };
            return param;
        },
        columns = [{
            field: 'carDealerId',
            title: '编号'
        }, {
            field: 'companyName',
            title: '企业名称'
        }, {
            field: 'provinceName',
            title: '省份'
        }, {
            field: 'cityName',
            title: '城市'
        }, {
            field: 'name',
            title: '联系人'
        }, {
            field: 'phone',
            title: '联系电话'
        }, {
            field: 'address',
            title: '地址'
        }, {
            field: 'business',
            title: '业务'
        }, {
            field: 'registTime',
            title: '注册时间',
            sortable: true,
        }, {
            field: 'expireTime',
            title: '到期时间',
            sortable: true,
        }, {
            field: 'smsCount',
            title: '已用/总共'
        }, {
            field: 'op',
            title: '操作',
            events: operateEvents,
            formatter: function (value, row, index) {
                var sb = new StringBuilder();
                sb.appendFormat('<a class="btn" href="/cardealer/edit/{0}"><i class="fa fa-fw fa-edit"></i></a>', row.carDealerId);
                sb.append(' <a class="btn  remove"><i class="fa fa-fw fa-trash"></i></a>');
                return sb.toString();
            }
        },]
    $.bootstrapTable($table, url, queryParams, columns);
}