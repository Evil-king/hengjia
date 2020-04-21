package com.baibei.hengjia.api.modules.settlement.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.api.modules.cash.base.AbstractCashFunction;
import com.baibei.hengjia.api.modules.cash.base.BaseResponse;
import com.baibei.hengjia.api.modules.cash.bean.dto.ViewFileDto;
import com.baibei.hengjia.api.modules.cash.component.PABDocumentUtils;
import com.baibei.hengjia.api.modules.cash.component.SerialNumberComponent;
import com.baibei.hengjia.api.modules.cash.enumeration.CashFunctionType;
import com.baibei.hengjia.common.core.redis.RedisUtil;
import com.baibei.hengjia.common.tool.constants.RedisConstant;
import com.baibei.hengjia.api.modules.utils.SFTPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 通知交易网查看文件，由银行发起
 * 1、去下载文件执行自带ftp文件下载的方法，把文件下载下来,解密
 * 2、然后传文件到指定的路径下面
 * 3、通过redis 发布和订阅通知文件的不同的解析
 */
@Service
public class CashFunctionViewFileServiceImpl extends AbstractCashFunction<ViewFileDto, BaseResponse> {

    /**
     * ftp工具类
     */
    @Autowired
    private SFTPUtils sftpUtils;

    /**
     * 文件加密和解密类
     */
    @Autowired
    private PABDocumentUtils pabDocumentUtils;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SerialNumberComponent serialNumberComponent;

    private String FILE_NOT_EXIST = "none.txt";

    /**
     * 本地服务器存储文件路径
     */
    @Value("${ftp.path}")
    private String localFile;

    @Override
    public Map<String, String> spiltMessage(Map<String, String> retKeyDict) {
        bankBackMessageAnalysis.spiltMessage_1005(retKeyDict);
        return retKeyDict;
    }

    @Override
    public String responseResult(Map<String, String> map) {
        return interfaceMessage.getSignMessageBody_1005(map);
    }

    /**
     * 执行业务逻辑
     * 1、接受银行文件名,文件密码
     * 2、{"fileName":"2019043423.txt","funcFlag":"1"}
     *
     * @param request
     * @param parmaKeyDict
     * @return
     */
    @Override
    public Map<String, String> doResponse(ViewFileDto request, Map<String, String> parmaKeyDict) {
        logger.info("文件标识 1：清算失败文件2：会员余额文件 3：出入金流水文件4：会员开销户文件 5：对账不平记录文件");
        String downloadFile = localFile + "/" + request.getFileName() + ".enc";
        String decryptionFile = localFile + "/" + request.getFileName();
        logger.info("银行通知交易网查看文件DTO:{},下载文件为{},解密文件为{}", JSONObject.toJSONString(request), downloadFile, decryptionFile);
        parmaKeyDict.put("ThirdLogNo", serialNumberComponent.generateThiredLogNo());//交易网流水号
        if (FILE_NOT_EXIST.equals(request.getFileName())) {
            logger.info("当日标识文件没有相关流水,文件标识为:{}", request.getFuncFlag());
            return parmaKeyDict;
        }
        if (sftpUtils.download(downloadFile)) { //下载文件需要加.enc
            try {
                pabDocumentUtils.FileDecryption(decryptionFile, request.getReserve());//文件解密,生成解密文件
            } catch (Exception e) {
                logger.info("文件加密和解密失败");
                e.printStackTrace();
                return parmaKeyDict;
            }
            Map<String, String> result = new HashMap<>();
            result.put("fileName", request.getFileName());
            result.put("funcFlag", request.getFuncFlag());
            redisUtil.leftPush(RedisConstant.SET_BANK_FILE_LIST, JSON.toJSONString(result));
            redisUtil.pub(RedisConstant.SET_BANK_FILE, RedisConstant.SET_BANK_FILE);
        }else{
            logger.error("________________文件下载失败_________________");
        }
        return parmaKeyDict;
    }

    @Override
    protected ViewFileDto toEntityByHashMapRequest(Map<String, String> params) {
        ViewFileDto viewFileDto = new ViewFileDto();
        viewFileDto.setFileName(params.get("FileName"));
        viewFileDto.setFuncFlag(params.get("FuncFlag"));
        viewFileDto.setSupAcctId(supAcctId);
        viewFileDto.setReserve(params.get("Reserve"));
        return viewFileDto;
    }

    /**
     * 交易码1005
     *
     * @return
     */
    @Override
    public CashFunctionType getType() {
        return CashFunctionType.VIEW_FILE;
    }
}
