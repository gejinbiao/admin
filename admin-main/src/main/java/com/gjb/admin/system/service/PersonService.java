package com.gjb.admin.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjb.admin.system.mapper.PersonMapper;
import com.gjb.admin.system.model.Person;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 人员管理 服务
 * </p>
 *
 * @author gjb
 * @Date 2020-03-27 09:35:01
 */
@Service
public class PersonService extends ServiceImpl<PersonMapper, Person> {


    /**
     * 分页查询
     *
     * @param pageIndex
     * @param limit
     * @param person
     * @return
     */
    public IPage<Person> pageList(Integer pageIndex, Integer limit, Person person) {
        Page page = new Page<Person>(pageIndex, limit);
        QueryWrapper<Person> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return this.page(page, queryWrapper);
    }
}
