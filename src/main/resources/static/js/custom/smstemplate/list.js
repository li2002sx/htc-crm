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
    'click .updatestatus': function (e, value, row, index) {
        var status = row.status;
        var tips = status == 1 ? '删除到草稿' : '通过审核';
        var newStatus = status == 1 ? 10 : 1;
        $confirm("提示", "确定要" + tips + "吗？", function (dlg) {
            //执行远程“删除操作”，修改状态为10
            $.post("/smstemplate/updatestatus", {"smsTemplateId": row.templateId, status: newStatus}, function (result) {
                if (result.success) {
                    initTable();
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
    var url = "/smstemplate/listdata",
        queryParams = function queryParams(params) {   //设置查询参数
            var param = {
                pageIndex: params.pageNumber,
                pageSize: params.pageSize
            };
            return param;
        },
        columns = [{
            field: 'templateId',
            title: '编号'
        }, {
            field: 'content',
            title: '内容'
        }, {
            field: 'templateTypeName',
            title: '类型'
        }, {
            field: 'status',
            title: '状态名称',
            formatter: function (value, row, index) {
                if (row.status == 1) {
                    return "正常";
                } else {
                    return "草稿"
                }
            }
        }, {
            field: 'op',
            title: '操作',
            events: operateEvents,
            formatter: function (value, row, index) {
                var sb = new StringBuilder();
                sb.appendFormat('<a class="btn" href="/smstemplate/edit/{0}"><i class="fa fa-fw fa-edit"></i></a>', row.templateId);
                if (row.status == 1) {
                    sb.append(' <a class="btn updatestatus"><i class="fa fa-fw fa-trash"></i></a>');
                } else {
                    sb.append(' <a class="btn updatestatus"><i class="fa fa-fw fa-sign-in"></i></a>');
                }
                return sb.toString();
            }
        },]
    $.bootstrapTable($table, url, queryParams, columns);
}
