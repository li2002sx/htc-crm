/*
 jq:http://jquery.cuishifeng.cn/index.html
 layer:http://layer.layui.com/mobile/
 */
var setting = {
    view: {
        selectedMulti: false
    },
    edit: {
        enable: true,
        showRemoveBtn: false,
        showRenameBtn: false
    },
    data: {
        keep: {
            parent: true,
            leaf: true
        },
        simpleData: {
            enable: true
        }
    },
    callback: {
        beforeDrag: beforeDrag,
        beforeRemove: beforeRemove,
        beforeRename: beforeRename,
        onRemove: onRemove,
        onRename: onRename
    }
};

var zNodes = carTrees;

var log, className = "dark";
function beforeDrag(treeId, treeNodes) {
    return false;
}
function beforeRemove(treeId, treeNode) {
    className = (className === "dark" ? "" : "dark");
    showLog("[ " + getTime() + " beforeRemove ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
    return confirm("确认删除 节点 -- " + treeNode.name + " 吗？");
}
function onRemove(e, treeId, treeNode) {
    showLog("[ " + getTime() + " onRemove ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
}
function beforeRename(treeId, treeNode, newName) {
    if (newName.length == 0) {
        alert("节点名称不能为空.");
        var zTree = $.fn.zTree.getZTreeObj("treeDemo");
        setTimeout(function () {
            zTree.editName(treeNode)
        }, 10);
        return false;
    }
    return true;
}

function onRename(e, treeId, treeNode, isCancel) {
    // showLog((isCancel ? "<span style='color:red'>":"") + "[ "+getTime()+" onRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name + (isCancel ? "</span>":""));
    $.post("/cartree/save", treeNode);
}

function showLog(str) {
    if (!log) log = $("#log");
    log.append("<li class='" + className + "'>" + str + "</li>");
    if (log.children("li").length > 8) {
        log.get(0).removeChild(log.children("li")[0]);
    }
}
function getTime() {
    var now = new Date(),
        h = now.getHours(),
        m = now.getMinutes(),
        s = now.getSeconds(),
        ms = now.getMilliseconds();
    return (h + ":" + m + ":" + s + " " + ms);
}

var newCount = startCount;
function add(e) {
    var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
        isParent = e.data.isParent,
        nodes = zTree.getSelectedNodes(),
        treeNode = nodes[0];
    if (treeNode) {
        treeNode = zTree.addNodes(treeNode, {
            id: (newCount),
            pId: treeNode.id,
            isParent: isParent,
            name: "new node" + (newCount++)
        });
    } else {
        treeNode = zTree.addNodes(null, {id: (newCount), pId: 0, isParent: isParent, name: "new node" + (newCount++)});
    }
    if (treeNode) {
        zTree.editName(treeNode[0]);
        $.post("/cartree/save", treeNode[0]);
    } else {
        $.err('叶子节点被锁定，无法增加子节点');
    }
};
function edit() {
    var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
        nodes = zTree.getSelectedNodes(),
        treeNode = nodes[0];
    if (nodes.length == 0) {
        $.err('请先选择一个节点');
        return;
    }
    zTree.editName(treeNode);
};
function remove(e) {
    $confirm("确认", "确定要删除吗？,请谨慎操作？", function (dlg) {
        dlg.close();
        var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
            nodes = zTree.getSelectedNodes(),
            treeNode = nodes[0];
        if (nodes.length == 0) {
            $.err('请先选择一个节点');
            return;
        }
        var callbackFlag = $("#callbackTrigger").attr("checked");
        zTree.removeNode(treeNode, callbackFlag);
        $.post("/cartree/delete", {id: treeNode.id});
    })
};
function clearChildren(e) {
    var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
        nodes = zTree.getSelectedNodes(),
        treeNode = nodes[0];
    if (nodes.length == 0 || !nodes[0].isParent) {
        $.err('请先选择一个父节点');
        return;
    }
    zTree.removeChildNodes(treeNode);
};

$(document).ready(function () {
    $.fn.zTree.init($("#treeDemo"), setting, zNodes);
    $("#addParent").bind("click", {isParent: true}, add);
    $("#addLeaf").bind("click", {isParent: false}, add);
    $("#edit").bind("click", edit);
    $("#remove").bind("click", remove);
    $("#clearChildren").bind("click", clearChildren);
});