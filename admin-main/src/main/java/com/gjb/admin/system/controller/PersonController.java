package com.gjb.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gjb.admin.base.pojo.page.LayuiPageFactory;
import com.gjb.admin.sys.core.exception.enums.BizExceptionEnum;
import com.gjb.admin.system.model.Person;
import com.gjb.admin.system.service.PersonService;
import com.gjb.core.base.controller.BaseController;
import com.gjb.core.reqres.response.ResponseData;
import com.gjb.core.util.ToolUtil;
import com.gjb.model.exception.ServiceException;
import com.gjb.model.util.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 人员管理控制器
 *
 * @author fengshuonan
 * @Date 2020-03-27 09:35:01
 */
@Controller
@RequestMapping("/person")
public class PersonController extends BaseController {

    private String PREFIX = "/person/";

    @Autowired
    private PersonService personService;

    /**
     * 跳转到人员管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "person.html";
    }

    /**
     * 跳转到添加人员管理
     */
    @RequestMapping("/person_add")
    public String personAdd() {
        return PREFIX + "person_add.html";
    }

    /**
     * 跳转到修改人员管理
     */
    @RequestMapping("/person_edit")
    public String personUpdate(String personId) {
        return PREFIX + "person_edit.html";
    }

    /**
     * 获取人员管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(Integer page, Integer limit, Person person) {
        IPage<Person> personPage = personService.pageList(page, limit, person);
        return LayuiPageFactory.createPageInfo(personPage);
    }

    /**
     * 新增人员管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Person person) {
        personService.save(person);
        return SUCCESS_TIP;
    }

    /**
     * 删除人员管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer personId) {
         if (ToolUtil.isEmpty(personId)) {
                throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
            }
        this.personService.removeById(personId);
        return SUCCESS_TIP;
    }

    /**
     * 修改人员管理
     */
    @RequestMapping(value = "/edit")
    @ResponseBody
    public Object edit(Person person) {
        personService.updateById(person);
        return SUCCESS_TIP;
    }

    /**
     * 查看人员管理
     *
     * @author gjb
     */
    @RequestMapping(value = "/view/{personId}")
    @ResponseBody
    public ResponseData view(@PathVariable Long personId) {
        if (ValidateUtil.isEmpty(personId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        Person person = this.personService.getById(personId);

        return ResponseData.success(person);
    }

    /**
     * 人员管理详情
     */
    @RequestMapping(value = "/detail/{personId}")
    @ResponseBody
    public Object detail(@PathVariable("personId") Integer personId) {
        return personService.getById(personId);
    }
}
