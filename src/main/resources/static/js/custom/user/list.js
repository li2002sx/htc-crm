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
            $.post("/user/delete", {"userId": row.userId}, function (result) {
                if (result.success) {
                    //移除行
                    $table.bootstrapTable('remove', {
                        field: 'userId',
                        values: [row.userId]
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
    var url = "/user/listdata",
        queryParams = function queryParams(params) {   //设置查询参数
            var param = {
                pageIndex: params.pageNumber,
                pageSize: params.pageSize,
                realName: $('#realName').val(),
                mobile: $('#mobile').val(),
            };
            return param;
        },
        columns = [{
            field: 'userId',
            title: '编号'
        }, {
            field: 'realName',
            title: '真实姓名'
        }, {
            field: 'mobile',
            title: '手机号'
        }, {
            field: 'sex',
            title: '性别'
        }, {
            field: 'industryName',
            title: '行业名称'
        }, {
            field: 'company',
            title: '公司'
        }, {
            field: 'position',
            title: '职位'
        }, {
            field: 'wxCode',
            title: '微信号'
        }, {
            field: 'provinceName',
            title: '省份'
        }, {
            field: 'cityName',
            title: '城市'
        }, {
            field: 'expireTime',
            title: '过期时间'
        }, {
            field: 'createTime',
            title: '创建时间'
        }, {
            field: 'op',
            title: '操作',
            events: operateEvents,
            formatter: function (value, row, index) {
                var sb = new StringBuilder();
                // sb.appendFormat('<a class="btn" href="/user/edit/{0}"><i class="fa fa-fw fa-edit"></i></a>', row.userId);
                sb.appendFormat('<a class="btn" title="用户车型" href="/usercar/list?userid={0}"><i class="fa fa-fw fa-car"></i></a>', row.userId);
                sb.appendFormat('<a class="btn" title="用户照片" href="/userphoto/list?userid={0}"><i class="fa fa-fw fa-file-image-o"></i></a>', row.userId);
                // sb.append(' <a class="btn remove"><i class="fa fa-fw fa-trash"></i></a>');
                return sb.toString();
            }
        },]
    $.bootstrapTable($table, url, queryParams, columns);
}
