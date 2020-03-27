package com.gjb.admin.sys.code.engine.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Service模板生成的配置
 *
 * @author fengshuonan
 * @date 2017-05-07 22:12
 */
public class ServiceConfig {

    private ContextConfig contextConfig;

    private String servicePathTemplate;
    private String serviceImplPathTemplate;

    private String packageName;

    private List<String> serviceInterfaceImports;
    private List<String> serviceImplImports;

    public void init() {
       /* ArrayList<String> imports = new ArrayList<>();
        imports.add("org.springframework.stereotype.Service");
        imports.add(contextConfig.getModelPackageName() + "." + contextConfig.getEntityName());
        imports.add(contextConfig.getModelMapperPackageName() + "." + contextConfig.getEntityName() + "Mapper");
        imports.add(contextConfig.getProPackage() + ".modular." + contextConfig.getModuleName() + ".service." + contextConfig.getBizEnBigName() + "Service");
        this.serviceImplImports = imports;*/


        ArrayList<String> interfaceImports = new ArrayList<>();
        interfaceImports.add("com.baomidou.mybatisplus.core.conditions.query.QueryWrapper");
        interfaceImports.add("com.baomidou.mybatisplus.core.metadata.IPage");
        interfaceImports.add("com.baomidou.mybatisplus.extension.plugins.pagination.Page");
        interfaceImports.add("com.baomidou.mybatisplus.extension.service.impl.ServiceImpl");
        interfaceImports.add("org.springframework.stereotype.Service");
        this.serviceInterfaceImports = interfaceImports;

        this.servicePathTemplate = "\\src\\main\\java\\" + contextConfig.getProPackage().replaceAll("\\.", "\\\\") + "\\" + contextConfig.getModuleName() + "\\service\\{}Service.java";
        //this.serviceImplPathTemplate = "\\src\\main\\java\\" + contextConfig.getProPackage().replaceAll("\\.", "\\\\") + "\\modular\\" + contextConfig.getModuleName() + "\\service\\impl\\{}ServiceImpl.java";
        this.packageName = contextConfig.getProPackage() + "." + contextConfig.getModuleName() + ".service";
    }


    public String getServicePathTemplate() {
        return servicePathTemplate;
    }

    public void setServicePathTemplate(String servicePathTemplate) {
        this.servicePathTemplate = servicePathTemplate;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getServiceImplPathTemplate() {
        return serviceImplPathTemplate;
    }

    public void setServiceImplPathTemplate(String serviceImplPathTemplate) {
        this.serviceImplPathTemplate = serviceImplPathTemplate;
    }

    public List<String> getServiceImplImports() {
        return serviceImplImports;
    }

    public void setServiceImplImports(List<String> serviceImplImports) {
        this.serviceImplImports = serviceImplImports;
    }

    public ContextConfig getContextConfig() {
        return contextConfig;
    }

    public void setContextConfig(ContextConfig contextConfig) {
        this.contextConfig = contextConfig;
    }

    public List<String> getServiceInterfaceImports() {
        return serviceInterfaceImports;
    }

    public void setServiceInterfaceImports(List<String> serviceInterfaceImports) {
        this.serviceInterfaceImports = serviceInterfaceImports;
    }
}
