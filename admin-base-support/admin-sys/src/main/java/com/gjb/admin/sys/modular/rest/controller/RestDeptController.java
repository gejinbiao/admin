/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gjb.admin.sys.modular.rest.controller;

import cn.hutool.core.bean.BeanUtil;
import com.gjb.admin.base.log.BussinessLog;
import com.gjb.admin.base.pojo.node.ZTreeNode;
import com.gjb.admin.sys.core.constant.dictmap.DeptDict;
import com.gjb.admin.sys.core.constant.factory.ConstantFactory;
import com.gjb.admin.sys.modular.rest.entity.RestDept;
import com.gjb.admin.sys.modular.rest.factory.DeptFactory;
import com.gjb.admin.sys.modular.rest.model.DeptTreeNode;
import com.gjb.admin.sys.modular.rest.service.RestDeptService;
import com.gjb.admin.sys.modular.system.model.DeptDto;
import com.gjb.admin.sys.modular.system.warpper.DeptWrapper;
import com.gjb.core.base.controller.BaseController;
import com.gjb.core.reqres.response.ResponseData;
import com.gjb.core.reqres.response.SuccessResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 部门控制器
 *
 * @author fengshuonan
 * @Date 2017年2月17日20:27:22
 */
@RestController
@RequestMapping("/rest/dept")
public class RestDeptController extends BaseController {

    @Autowired
    private RestDeptService restDeptService;

    /**
     * 获取部门的tree列表，ztree格式
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:56 PM
     */
    @RequestMapping(value = "/tree")
    public List<ZTreeNode> tree() {
        List<ZTreeNode> tree = this.restDeptService.tree();
        tree.add(ZTreeNode.createParent());
        return tree;
    }

    /**
     * 新增部门
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @BussinessLog(value = "添加部门", key = "simpleName", dict = DeptDict.class)
    @RequestMapping(value = "/add")
    public ResponseData add(RestDept restDept) {
        this.restDeptService.addDept(restDept);
        return SUCCESS_TIP;
    }

    /**
     * 获取所有部门列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @RequestMapping(value = "/list")
    public Object list(@RequestParam(value = "condition", required = false) String condition,
                       @RequestParam(value = "deptId", required = false) Long deptId) {
        List<Map<String, Object>> list = this.restDeptService.list(condition, deptId);
        List<Map<String, Object>> wrap = new DeptWrapper(list).wrap();

        //创建部门树
        List<DeptTreeNode> deptTreeNodes = DeptFactory.buildTreeNodes(wrap);

        return new SuccessResponseData(deptTreeNodes);
    }

    /**
     * 部门详情
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @RequestMapping(value = "/detail/{deptId}")
    public Object detail(@PathVariable("deptId") Long deptId) {
        RestDept dept = restDeptService.getById(deptId);
        DeptDto deptDto = new DeptDto();
        BeanUtil.copyProperties(dept, deptDto);
        deptDto.setPName(ConstantFactory.me().getDeptName(deptDto.getPid()));
        return deptDto;
    }

    /**
     * 修改部门
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @BussinessLog(value = "修改部门", key = "simpleName", dict = DeptDict.class)
    @RequestMapping(value = "/update")
    public ResponseData update(RestDept restDept) {
        restDeptService.editDept(restDept);
        return SUCCESS_TIP;
    }

    /**
     * 删除部门
     *
     * @author fengshuonan
     * @Date 2018/12/23 4:57 PM
     */
    @BussinessLog(value = "删除部门", key = "deptId", dict = DeptDict.class)
    @RequestMapping(value = "/delete")
    public ResponseData delete(@RequestParam Long deptId) {
        restDeptService.deleteDept(deptId);
        return SUCCESS_TIP;
    }

}
