package com.baibei.hengjia.admin.modules.settlement.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.admin.modules.settlement.bean.dto.WithDrawDepositDiffDto;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.BankOrderVo;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.WithDrawDepositDiffVo;
import com.baibei.hengjia.admin.modules.settlement.dao.WithDrawDepositDiffMapper;
import com.baibei.hengjia.admin.modules.settlement.model.WithDrawDepositDiff;
import com.baibei.hengjia.admin.modules.settlement.service.IWithDrawDepositDiffService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.utils.HttpClientUtils;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
* @author: Longer
* @date: 2019/07/12 18:10:38
* @description: WithDrawDepositDiff服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class WithDrawDepositDiffServiceImpl extends AbstractService<WithDrawDepositDiff> implements IWithDrawDepositDiffService {

    @Autowired
    private WithDrawDepositDiffMapper withdrawDepositDiffMapper;
    @Value("${api.dealdiff.url}")
    private String dealDiffUrl;

    @Override
    public MyPageInfo<WithDrawDepositDiffVo> pageList(WithDrawDepositDiffDto withDrawDepositDiffDto) {
        PageHelper.startPage(withDrawDepositDiffDto.getCurrentPage(), withDrawDepositDiffDto.getPageSize());
        List<WithDrawDepositDiffVo> list = withdrawDepositDiffMapper.myList(withDrawDepositDiffDto);
        MyPageInfo<WithDrawDepositDiffVo> myPageInfo = new MyPageInfo<>(list);
        return myPageInfo;
    }

    @Override
    public ApiResult dealDiff(Long diffId) {
        Map paramMap = new HashMap<>();
        paramMap.put("diffId",diffId);
        String param = JSONObject.toJSONString(paramMap);
        String result = HttpClientUtils.doPostJson(dealDiffUrl, param);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if(!jsonObject.get("code").toString().equals("200")){
            throw new ServiceException("冲正失败，系统异常");
        }
        return ApiResult.success();
    }

    @Override
    public List<WithDrawDepositDiffVo> WithDrawDepositDiffVoList(WithDrawDepositDiffDto withDrawDepositDiffDto) {
        List<WithDrawDepositDiffVo> list = withdrawDepositDiffMapper.myList(withDrawDepositDiffDto);
        return list;
    }
}
