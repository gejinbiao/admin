package com.gjb.admin.sys.code.controller;

import com.gjb.admin.sys.code.action.config.WebGeneratorConfig;
import com.gjb.admin.sys.code.action.model.GenQo;
import com.gjb.admin.sys.code.factory.DefaultTemplateFactory;
import com.gjb.admin.sys.code.service.TableService;
import com.gjb.core.base.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 代码生成控制器
 *
 * @author fengshuonan
 * @Date 2017年11月30日16:39:19
 */
@Controller
@RequestMapping("/code")
public class CodeController extends BaseController {

    private static String PREFIX = "/code";

    @Autowired
    private TableService tableService;

   /* @Autowired
    private DruidProperties druidProperties;*/

   @Value("${spring.datasource.url}")
   private String url;

    @Value("${spring.datasource.username}")
   private String username;

    @Value("${spring.datasource.password}")
   private String password;

    /**
     * 跳转到代码生成主页
     */
    @RequestMapping("")
    public String blackboard(Model model) {
        model.addAttribute("tables", tableService.getAllTables());
        model.addAttribute("params", DefaultTemplateFactory.getDefaultParams());
        model.addAttribute("templates", DefaultTemplateFactory.getDefaultTemplates());
        return PREFIX + "/code.html";
    }

    /**
     * 生成代码
     */
    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    @ResponseBody
    public Object generate(GenQo genQo) {
        genQo.setUrl(url);
        genQo.setUserName(username);
        genQo.setPassword(password);
        WebGeneratorConfig webGeneratorConfig = new WebGeneratorConfig(genQo);
        webGeneratorConfig.doMpGeneration();
        webGeneratorConfig.doGunsGeneration();
        return SUCCESS_TIP;
    }
}
