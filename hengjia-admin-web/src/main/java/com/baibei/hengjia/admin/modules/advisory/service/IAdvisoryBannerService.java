package com.baibei.hengjia.admin.modules.advisory.service;

import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryBannerDto;
import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryBannerListDto;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryBannerVo;
import com.baibei.hengjia.admin.modules.advisory.model.AdvisoryBanner;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;


/**
* @author: wenq
* @date: 2019/09/11 10:09:00
* @description: AdvisoryBanner服务接口
*/
public interface IAdvisoryBannerService extends Service<AdvisoryBanner> {

    /**
     * 添加对象
     * @param advisoryBannerDto
     * @return
     */
    ApiResult addObject(AdvisoryBannerDto advisoryBannerDto);

    /**
     * 列表页数据
     *  @param advisoryBannerListDto
     * @return
     */
    MyPageInfo<AdvisoryBannerVo> ObjList(AdvisoryBannerListDto advisoryBannerListDto);

    /**
     * 批量操作
     * @param id
     * @param type
     * @return
     */
    ApiResult batchOperator(String id, String type);

    /**
     * 编辑和查看
     * @param id
     * @return
     */
    AdvisoryBannerVo lookObj(long id);

}
