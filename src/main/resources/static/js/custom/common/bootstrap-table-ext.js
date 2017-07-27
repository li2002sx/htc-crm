/*
 jq:http://jquery.cuishifeng.cn/index.html
 layer:http://layer.layui.com/mobile/
 */

function rowStyle(row, index) {
    var classes = ['active', 'success', 'info', 'warning', 'danger'];

    if (index % 2 === 0 && index / 2 < classes.length) {
        return {
            classes: classes[index / 2]
        };
    }
    return {};
}
