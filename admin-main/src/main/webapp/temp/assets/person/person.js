layui.use(['layer', 'form', 'table', 'admin', 'ax'], function () {
    var $ = layui.$;
    var layer = layui.layer;
    var form = layui.form;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;


    /**
     * 人员管理管理初始化
     */
    var Person = {
        tableId: "PersonTable",	//表格id
        condition:{}
    };

    /**
     * 初始化表格的列
     */
    Person.initColumn = function () {
        return [[
           {type: 'checkbox'},
            {title: '姓名', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '年龄', field: 'age', visible: true, align: 'center', valign: 'middle'},
            {title: '地址', field: 'address', visible: true, align: 'center', valign: 'middle'},
            {title: '备注', field: 'remark', visible: true, align: 'center', valign: 'middle'},
            {title: '创建日期', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '更新日期', field: 'updateTime', visible: true, align: 'center', valign: 'middle'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };

    /**
     * 检查是否选中
     */
    Person.check = function () {
        var selected = $('#' + this.tableId).bootstrapTable('getSelections');
        if(selected.length == 0){
            Feng.info("请先选中表格中的某一记录！");
            return false;
        }else{
            Person.seItem = selected[0];
            return true;
        }
    };

    /**
     * 点击添加人员管理
     */
    Person.openAddPerson = function () {
        admin.putTempData('formOk', false);
          top.layui.admin.open({
              type: 2,
              title: '人员管理',
              content: Feng.ctxPath + '/person/person_add',
              end: function () {
                  admin.getTempData('formOk') && table.reload(Person.tableId);
              }
          });
    };

    /**
     * 点击修改
     * @param data
     */
    Person.onEditPerson = function (data) {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '修改',
            content: Feng.ctxPath + '/person/person_edit?personId=' + data.id,
            end: function () {
                admin.getTempData('formOk') && table.reload(Person.tableId);
            }
        });
    };

    /**
     * 打开查看人员管理详情
     */
    Person.openPersonDetail = function () {
        if (this.check()) {
            var index = layer.open({
                account: 2,
                title: '人员管理详情',
                area: ['800px', '420px'], //宽高
                fix: false, //不固定
                maxmin: true,
                content: Feng.ctxPath + '/person/person_update/' + Person.seItem.id
            });
            this.layerIndex = index;
        }
    };

    /**
     * 删除人员管理
     */
    Person.delete = function (data) {
    var operation = function () {
        var ajax = new $ax(Feng.ctxPath + "/person/delete", function (data) {
            Feng.success("删除成功!");
            table.reload(Person.tableId);
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("personId", data.id);
        ajax.start();
        }
        Feng.confirm("是否删除人员管理" + data.name + "?", operation);
    };

    /**
     * 查询人员管理列表
     */
    Person.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        table.reload(Person.tableId, {where: queryData});
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Person.tableId,
        url: Feng.ctxPath + '/person/list',
        page: true,
        height: "full-98",
        cellMinWidth: 100,
        cols: Person.initColumn()
    });

        // 搜索按钮点击事件
        $('#btnSearch').click(function () {
            Person.search();
        });

        // 添加按钮点击事件
        $('#btnAdd').click(function () {
            Person.openAddPerson();
        });

         // 工具条点击事件
            table.on('tool(' + Person.tableId + ')', function (obj) {
                var data = obj.data;
                var layEvent = obj.event;
                if (layEvent === 'edit') {
                    Person.onEditPerson(data);
                } else if (layEvent === 'delete') {
                    Person.delete(data);
                }
            });
});

