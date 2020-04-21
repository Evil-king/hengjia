package com.baibei.hengjia.api.modules.advisory.dao;

import com.baibei.hengjia.api.modules.advisory.bean.vo.AdvisoryPublicNoticeVo;
import com.baibei.hengjia.api.modules.advisory.model.PublicNotice;
import com.baibei.hengjia.common.core.mybatis.MyMapper;
import com.baibei.hengjia.common.tool.page.PageParam;

import java.util.List;

public interface PublicNoticeMapper extends MyMapper<PublicNotice> {

    List<AdvisoryPublicNoticeVo> findByApiPublicNoticeNoContent(PageParam pageParam);
}