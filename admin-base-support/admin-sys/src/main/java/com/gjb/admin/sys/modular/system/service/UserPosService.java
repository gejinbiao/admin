package com.gjb.admin.sys.modular.system.service;

import com.gjb.admin.base.pojo.page.LayuiPageInfo;
import com.gjb.admin.sys.modular.system.entity.UserPos;
import com.gjb.admin.sys.modular.system.model.params.UserPosParam;
import com.gjb.admin.sys.modular.system.model.result.UserPosResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户职位关联表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2019-06-28
 */
public interface UserPosService extends IService<UserPos> {

    /**
     * 新增
     *
     * @author stylefeng
     * @Date 2019-06-28
     */
    void add(UserPosParam param);

    /**
     * 删除
     *
     * @author stylefeng
     * @Date 2019-06-28
     */
    void delete(UserPosParam param);

    /**
     * 更新
     *
     * @author stylefeng
     * @Date 2019-06-28
     */
    void update(UserPosParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author stylefeng
     * @Date 2019-06-28
     */
    UserPosResult findBySpec(UserPosParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author stylefeng
     * @Date 2019-06-28
     */
    List<UserPosResult> findListBySpec(UserPosParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author stylefeng
     * @Date 2019-06-28
     */
     LayuiPageInfo findPageBySpec(UserPosParam param);

}
