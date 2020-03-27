
    var $ ;
    var $ax;

    /**
     * 初始化
     */
    var Code = {
        ztreeInstance: null,
        tableName: "",
        submitData: {},
        switchs: {}
    };

    layui.use(['table','form', 'admin', 'ax', 'ztree'], function () {
    $ = layui.$;
    $ax = layui.ax;
    var form = layui.form;

/**
 * 选择table的事件
 */
Code.selectTable = function (tableName) {

    SelectList.clearSelect("templateList");
    Code.switchs = {};

    if (SelectList.singelSelect("tableList", "tableName", tableName) == true) {
        Code.tableName = tableName;
        Code.setTableName(tableName);
    } else {
        Code.tableName = "";
    }
};

/**
 * 选择模板的事件
 */
Code.selectTemplate = function (templateKey) {
    if (Code.tableName != "") {
        if (SelectList.mutiSelect("templateList", "key", templateKey) == true) {
            Code.switchs[templateKey] = true;
        } else {
            Code.switchs[templateKey] = false;
        }
    } else {
        Feng.info("请先选择表");
    }
};

/**
 * 点击生成
 */
Code.generate = function () {
    Code.submitData = {};
    Code.submitData.tableName = Code.tableName;
    this.set('projectPath').set('author').set('projectPackage').set('corePackage').set('bizName').set('moduleName').set('parentMenuName');
   /* var baseAjax = Feng.baseAjax("/code/generate", "生成代码");

    for (var item in Code.switchs) {
        Code.submitData[item] = Code.switchs[item];
    }

    baseAjax.setData(Code.submitData);
    baseAjax.start();*/
    for (var item in Code.switchs) {
        Code.submitData[item] = Code.switchs[item];
    }
    var ajax = new $ax(Feng.ctxPath + "/code/generate", function () {
        Feng.success("生成成功!");
    }, function (data) {
        Feng.error("生成代码失败!" + data.responseJSON.message + "!");
    });

    ajax.set(Code.submitData);
    ajax.start();
};

/**
 * 设置表名称
 */
Code.setTableName = function (tableName) {
    $("#tableName").val(tableName);
    $("#className").val(Feng.underLineToCamel(tableName));
};

/**
 * 点击父级编号input框时
 */
Code.onClickDept = function (e, treeId, treeNode) {
    $("#parentMenuName").attr("value", Code.ztreeInstance.getSelectedVal());
};

/**
 * 显示父级菜单选择的树
 */
Code.showMenuSelectTree = function () {
    Feng.showInputTree("parentMenuName", "pcodeTreeDiv", 15, 34);
};

/*$(function () {
    var ztree = new $ZTree("pcodeTree", "/menu/selectMenuTreeList");
    ztree.bindOnClick(Code.onClickDept);
    ztree.init();
    Code.ztreeInstance = ztree;
    $("#pcodeTree").css('width',$("#parentMenuName").css('width'));
});*/

Code.set = function (key, value) {
    Code.submitData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
};


// 表单提交事件
form.on('submit(btnSubmit)', function (data) {
    Code.generate();
});

});