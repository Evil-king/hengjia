package com.baibei.hengjia.admin.modules.advisory.service.impl;

import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryPublicNoticeDto;
import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryPublicNoticePageDto;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryPublicNoticeVo;
import com.baibei.hengjia.admin.modules.advisory.dao.PublicNoticeMapper;
import com.baibei.hengjia.admin.modules.advisory.model.PublicNotice;
import com.baibei.hengjia.admin.modules.advisory.service.IPublicNoticeService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/09/16 09:45:48
 * @description: PublicNotice服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PublicNoticeServiceImpl extends AbstractService<PublicNotice> implements IPublicNoticeService {

    @Autowired
    private PublicNoticeMapper publicNoticeMapper;

    @Override
    public MyPageInfo<AdvisoryPublicNoticeVo> pageList(@Validated @RequestBody AdvisoryPublicNoticePageDto customerBaseAndPageDto) {
        PageHelper.startPage(customerBaseAndPageDto.getCurrentPage(), customerBaseAndPageDto.getPageSize());
        List<AdvisoryPublicNoticeVo> publicNoticeVoList = publicNoticeMapper.findByPublicNoticeNoContent(customerBaseAndPageDto);
        MyPageInfo<AdvisoryPublicNoticeVo> pageInfo = new MyPageInfo<>(publicNoticeVoList);
        return pageInfo;
    }

    @Override
    public void batchDelete(AdvisoryPublicNoticeDto publicNoticeDto) {
        if (publicNoticeDto.getIds().isEmpty()) {
            throw new ServiceException("批量删除id不能为空");
        }
        this.batchDelete(publicNoticeDto.getIds());
    }

    @Override
    public void batchHidden(AdvisoryPublicNoticeDto publicNoticeDto) {
        if (publicNoticeDto.getIds().isEmpty()) {
            throw new ServiceException("批量修改id不能为空");
        }
        if (publicNoticeDto.getHidden() == null) {
            throw new ServiceException("隐藏字段不能为空");
        }
        PublicNotice publicNotice = new PublicNotice();
        publicNotice.setHidden(publicNoticeDto.getHidden());
        publicNotice.setModifyTime(new Date());
        this.batchUpdate(publicNoticeDto.getIds(), publicNotice);
    }

    @Override
    public void updatePublicNotice(AdvisoryPublicNoticeDto advisoryPublicNoticeDto) {
        PublicNotice publicNotice = this.findById(advisoryPublicNoticeDto.getId());
        publicNotice.setContent(advisoryPublicNoticeDto.getContent());
        publicNotice.setTitle(advisoryPublicNoticeDto.getTitle());
        publicNotice.setModifyTime(new Date());
        publicNotice.setSeq(advisoryPublicNoticeDto.getSeq());
        publicNotice.setLink(advisoryPublicNoticeDto.getLink());
        publicNotice.setHidden(advisoryPublicNoticeDto.getHidden());
        publicNotice.setImage(advisoryPublicNoticeDto.getImage());
        this.publicNoticeMapper.updateByPrimaryKey(publicNotice);
    }

}
