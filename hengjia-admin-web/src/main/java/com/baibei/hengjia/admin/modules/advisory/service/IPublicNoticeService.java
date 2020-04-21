package com.baibei.hengjia.admin.modules.advisory.service;

import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryPublicNoticeDto;
import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryPublicNoticePageDto;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryPublicNoticeVo;
import com.baibei.hengjia.admin.modules.advisory.model.PublicNotice;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.page.MyPageInfo;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/09/16 09:45:48
 * @description: PublicNotice服务接口
 */
public interface IPublicNoticeService extends Service<PublicNotice> {

    MyPageInfo<AdvisoryPublicNoticeVo> pageList(AdvisoryPublicNoticePageDto customerBaseAndPageDto);


    void batchDelete(AdvisoryPublicNoticeDto publicNoticeDto);


    void batchHidden(AdvisoryPublicNoticeDto publicNoticeDto);

    void updatePublicNotice(AdvisoryPublicNoticeDto advisoryPublicNoticeDto);
}
