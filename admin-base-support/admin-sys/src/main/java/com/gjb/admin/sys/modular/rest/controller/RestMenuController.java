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
import com.gjb.admin.base.pojo.page.LayuiPageFactory;
import com.gjb.admin.base.pojo.page.LayuiPageInfo;
import com.gjb.admin.sys.core.constant.dictmap.DeleteDict;
import com.gjb.admin.sys.core.constant.dictmap.MenuDict;
import com.gjb.admin.sys.core.constant.factory.ConstantFactory;
import com.gjb.admin.sys.core.exception.enums.BizExceptionEnum;
import com.gjb.admin.sys.core.log.LogObjectHolder;
import com.gjb.admin.sys.modular.rest.entity.RestMenu;
import com.gjb.admin.sys.modular.rest.factory.MenuFactory;
import com.gjb.admin.sys.modular.rest.model.MenuTreeNode;
import com.gjb.admin.sys.modular.rest.service.RestMenuService;
import com.gjb.admin.sys.modular.rest.service.RestUserService;
import com.gjb.admin.sys.modular.system.model.MenuDto;
import com.gjb.admin.sys.modular.system.warpper.MenuWrapper;
import com.gjb.core.base.controller.BaseController;
import com.gjb.core.reqres.response.ResponseData;
import com.gjb.core.util.ToolUtil;
import com.gjb.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 菜单控制器
 *
 * @author fengshuonan
 * @Date 2017年2月12日21:59:14
 */
@RestController
@RequestMapping("/rest/menu")
public class RestMenuController extends BaseController {

    @Autowired
    private RestMenuService restMenuService;

    @Autowired
    private RestUserService restUserService;

    /**
     * 修该菜单
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:53 PM
     */
    @RequestMapping(value = "/edit")
    @BussinessLog(value = "修改菜单", key = "name", dict = MenuDict.class)
    public ResponseData edit(MenuDto menu) {

        //如果修改了编号，则该菜单的子菜单也要修改对应编号
        this.restMenuService.updateMenu(menu);

        //刷新当前用户菜单
        this.restUserService.refreshCurrentUser();

        return SUCCESS_TIP;
    }

    /**
     * 获取菜单列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:53 PM
     */
    @RequestMapping(value = "/list")
    public LayuiPageInfo list(@RequestParam(required = false) String menuName,
                              @RequestParam(required = false) String level,
                              @RequestParam(required = false) Long menuId) {
        Page<Map<String, Object>> menus = this.restMenuService.selectMenus(menuName, level, menuId);
        Page<Map<String, Object>> wrap = new MenuWrapper(menus).wrap();
        return LayuiPageFactory.createPageInfo(wrap);
    }

    /**
     * 获取菜单列表（树形）
     *
     * @author fengshuonan
     * @Date 2019年2月23日22:01:47
     */
    @RequestMapping(value = "/listTree")
    public LayuiPageInfo listTree(@RequestParam(required = false) String menuName,
                                  @RequestParam(required = false) String level) {
        List<Map<String, Object>> menus = this.restMenuService.selectMenuTree(menuName, level);
        List<Map<String, Object>> menusWrap = new MenuWrapper(menus).wrap();

        //构建树
        List<MenuTreeNode> menuTreeNodes = MenuFactory.buildTreeNodes(menusWrap);

        LayuiPageInfo result = new LayuiPageInfo();
        result.setData(menuTreeNodes);
        return result;
    }

    /**
     * 新增菜单
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:53 PM
     */
    @RequestMapping(value = "/add")
    @BussinessLog(value = "菜单新增", key = "name", dict = MenuDict.class)
    public ResponseData add(MenuDto menu) {
        this.restMenuService.addMenu(menu);
        return SUCCESS_TIP;
    }

    /**
     * 删除菜单
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:53 PM
     */
    @RequestMapping(value = "/remove")
    @BussinessLog(value = "删除菜单", key = "menuId", dict = DeleteDict.class)
    public ResponseData remove(@RequestParam Long menuId) {
        if (ToolUtil.isEmpty(menuId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        //缓存菜单的名称
        LogObjectHolder.me().set(ConstantFactory.me().getMenuName(menuId));

        this.restMenuService.delMenuContainSubMenus(menuId);

        return SUCCESS_TIP;
    }

    /**
     * 查看菜单
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:53 PM
     */
    @RequestMapping(value = "/view/{menuId}")
    public ResponseData view(@PathVariable Long menuId) {
        if (ToolUtil.isEmpty(menuId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        RestMenu menu = this.restMenuService.getById(menuId);
        return ResponseData.success(menu);
    }

    /**
     * 获取菜单信息
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:53 PM
     */
    @RequestMapping(value = "/getMenuInfo")
    public ResponseData getMenuInfo(@RequestParam Long menuId) {
        if (ToolUtil.isEmpty(menuId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        RestMenu menu = this.restMenuService.getById(menuId);

        MenuDto menuDto = new MenuDto();
        BeanUtil.copyProperties(menu, menuDto);

        //设置pid和父级名称
        menuDto.setPid(ConstantFactory.me().getMenuIdByCode(menuDto.getPcode()));
        menuDto.setPcodeName(ConstantFactory.me().getMenuNameByCode(menuDto.getPcode()));

        return ResponseData.success(menuDto);
    }

    /**
     * 获取菜单列表(首页用)
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:54 PM
     */
    @RequestMapping(value = "/menuTreeList")
    public List<ZTreeNode> menuTreeList() {
        return this.restMenuService.menuTreeList();
    }

    /**
     * 获取菜单列表(选择父级菜单用)
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:54 PM
     */
    @RequestMapping(value = "/selectMenuTreeList")
    public List<ZTreeNode> selectMenuTreeList() {
        List<ZTreeNode> roleTreeList = this.restMenuService.menuTreeList();
        roleTreeList.add(ZTreeNode.createParent());
        return roleTreeList;
    }

    /**
     * 获取角色的菜单列表
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:54 PM
     */
    @RequestMapping(value = "/menuTreeListByRoleId/{roleId}")
    public List<ZTreeNode> menuTreeListByRoleId(@PathVariable Long roleId) {
        List<Long> menuIds = this.restMenuService.getMenuIdsByRoleId(roleId);
        if (ToolUtil.isEmpty(menuIds)) {
            return this.restMenuService.menuTreeList();
        } else {
            return this.restMenuService.menuTreeListByMenuIds(menuIds);
        }
    }

}
