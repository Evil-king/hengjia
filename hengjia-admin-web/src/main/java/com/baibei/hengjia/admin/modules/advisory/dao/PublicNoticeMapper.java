package com.baibei.hengjia.admin.modules.advisory.dao;

import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryPublicNoticePageDto;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryPublicNoticeVo;
import com.baibei.hengjia.admin.modules.advisory.model.PublicNotice;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface PublicNoticeMapper extends MyMapper<PublicNotice> {

    List<AdvisoryPublicNoticeVo> findByPublicNoticeNoContent(AdvisoryPublicNoticePageDto customerBaseAndPageDto);
}