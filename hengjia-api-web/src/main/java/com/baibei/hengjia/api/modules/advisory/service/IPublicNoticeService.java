package com.baibei.hengjia.api.modules.advisory.service;

import com.baibei.hengjia.api.modules.advisory.bean.vo.AdvisoryPublicNoticeVo;
import com.baibei.hengjia.api.modules.advisory.model.PublicNotice;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.bean.CustomerBaseAndPageDto;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.page.PageParam;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/09/20 13:54:34
 * @description: PublicNotice服务接口
 */
public interface IPublicNoticeService extends Service<PublicNotice> {

    MyPageInfo<AdvisoryPublicNoticeVo> apiPageList(PageParam pageParam);

    List<AdvisoryPublicNoticeVo> findByPublicNotice();
}
