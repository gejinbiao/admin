package com.gjb.admin.sys.modular.rest.controller;

import com.gjb.admin.base.enums.CommonStatus;
import com.gjb.admin.base.pojo.page.LayuiPageInfo;
import com.gjb.admin.sys.modular.rest.entity.RestPosition;
import com.gjb.admin.sys.modular.rest.service.RestPositionService;
import com.gjb.admin.sys.modular.system.model.params.PositionParam;
import com.gjb.core.base.controller.BaseController;
import com.gjb.core.reqres.response.ResponseData;
import com.gjb.core.reqres.response.SuccessResponseData;
import com.gjb.core.util.ToolUtil;
import com.gjb.model.exception.RequestEmptyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 职位表控制器
 *
 * @author stylefeng
 * @Date 2019-06-27 2:47
 */
@RestController
@RequestMapping("/rest/position")
public class RestPositionController extends BaseController {

    @Autowired
    private RestPositionService restPositionService;

    /**
     * 新增接口
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    @RequestMapping("/addItem")
    public ResponseData addItem(PositionParam positionParam) {
        this.restPositionService.add(positionParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    @RequestMapping("/editItem")
    public ResponseData editItem(PositionParam positionParam) {
        this.restPositionService.update(positionParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    @RequestMapping("/delete")
    public ResponseData delete(PositionParam positionParam) {
        this.restPositionService.delete(positionParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    @RequestMapping("/detail")
    public ResponseData detail(PositionParam positionParam) {
        RestPosition detail = this.restPositionService.getById(positionParam.getPositionId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    @RequestMapping("/list")
    public LayuiPageInfo list(@RequestParam(value = "condition", required = false) String condition) {

        PositionParam positionParam = new PositionParam();
        if (ToolUtil.isNotEmpty(condition)) {
            positionParam.setCode(condition);
            positionParam.setName(condition);
        }

        return this.restPositionService.findPageBySpec(positionParam);
    }

    /**
     * 修改状态
     *
     * @author stylefeng
     * @Date 2019-06-27
     */
    @RequestMapping("/changeStatus")
    public ResponseData changeStatus(@RequestParam("positionId") String positionId,
                                     @RequestParam("status") Boolean status) {

        RestPosition position = this.restPositionService.getById(positionId);
        if (position == null) {
            throw new RequestEmptyException();
        }

        if (status) {
            position.setStatus(CommonStatus.ENABLE.getCode());
        } else {
            position.setStatus(CommonStatus.DISABLE.getCode());
        }

        this.restPositionService.updateById(position);

        return new SuccessResponseData();
    }

    /**
     * 查询所有职位
     *
     * @author stylefeng
     * @Date 2019-03-13
     */
    @RequestMapping("/listPositions")
    public LayuiPageInfo listlistPositionsTypes(@RequestParam(value = "userId", required = false) Long userId) {
        return this.restPositionService.listPositions(userId);
    }


}


