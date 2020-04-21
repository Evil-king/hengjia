package com.baibei.hengjia.api.modules.advisory.service.impl;

import com.baibei.hengjia.api.modules.advisory.bean.vo.AdvisoryPublicNoticeVo;
import com.baibei.hengjia.api.modules.advisory.dao.PublicNoticeMapper;
import com.baibei.hengjia.api.modules.advisory.model.PublicNotice;
import com.baibei.hengjia.api.modules.advisory.service.IPublicNoticeService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.bean.CustomerBaseAndPageDto;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.page.PageParam;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/09/20 13:54:34
 * @description: PublicNotice服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PublicNoticeServiceImpl extends AbstractService<PublicNotice> implements IPublicNoticeService {

    @Autowired
    private PublicNoticeMapper publicNoticeMapper;

    @Override
    public MyPageInfo<AdvisoryPublicNoticeVo> apiPageList(PageParam pageParam) {
        PageHelper.startPage(pageParam.getCurrentPage(), pageParam.getPageSize());
        List<AdvisoryPublicNoticeVo> advisoryPublicNoticeVos = publicNoticeMapper.findByApiPublicNoticeNoContent(pageParam);
        MyPageInfo<AdvisoryPublicNoticeVo> pageInfo = new MyPageInfo<>(advisoryPublicNoticeVos);
        return pageInfo;
    }

    @Override
    public List<AdvisoryPublicNoticeVo> findByPublicNotice() {
        Condition condition = new Condition(PublicNotice.class);
        condition.orderBy("createTime").desc();
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("flag", new Byte(Constants.Flag.VALID));
        criteria.andEqualTo("hidden",new Byte(Constants.Flag.UNVALID));
        List<PublicNotice> publicNoticeList = this.findByCondition(condition);
        List<AdvisoryPublicNoticeVo> advisoryPublicNoticeVos = new ArrayList<>();
        if (!publicNoticeList.isEmpty()) {
            PublicNotice result = publicNoticeList.get(0);
            AdvisoryPublicNoticeVo publicNotice = BeanUtil.copyProperties(result, AdvisoryPublicNoticeVo.class);
            advisoryPublicNoticeVos.add(publicNotice);
        }
        return advisoryPublicNoticeVos;
    }
}
