package ${basePackage}.controller;

import com.baibei.common.tools.api.ApiResult;
import com.baibei.common.tools.api.ApiResultGenerator;
import ${basePackage}.model.${modelNameUpperCamel};
import ${basePackage}.service.I${modelNameUpperCamel}Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
* @author: ${author}
* @date: ${date}
* @description: ${modelNameUpperCamel}控制器
*/
@Api(value = "${modelNameUpperCamel} web api", description = "operation about ${modelNameUpperCamel}")
@RestController
@RequestMapping("${baseRequestMapping}")
public class ${modelNameUpperCamel}Controller {

    @Autowired
    private I${modelNameUpperCamel}Service ${modelNameLowerCamel}Service;


    /**
     * 新增${modelNameUpperCamel}
     */
    @ApiOperation(value = "add ${modelNameUpperCamel}")
    @PostMapping("/add")
    public ApiResult add(@RequestBody ${modelNameUpperCamel} ${modelNameLowerCamel}) {
        boolean flag = ${modelNameLowerCamel}Service.save(${modelNameLowerCamel});
        if (!flag) {
            return ApiResultGenerator.badParameter("新增失败！");
        }
        return ApiResultGenerator.success();
    }

    /**
     * 删除${modelNameUpperCamel}
     */
    @ApiOperation(value = "delete ${modelNameUpperCamel}")
    @DeleteMapping("/delete")
    public ApiResult delete(@RequestParam Long id) {
        boolean flag = ${modelNameLowerCamel}Service.deleteById(id);
        if (!flag) {
            return ApiResultGenerator.badParameter("被删除对象不存在！");
        }
        return ApiResultGenerator.success();
    }

    /**
     * 更新${modelNameUpperCamel}
     */
    @ApiOperation(value = "update ${modelNameUpperCamel}")
    @PutMapping("/update")
    public ApiResult update(@RequestBody ${modelNameUpperCamel} ${modelNameLowerCamel}) {
        boolean flag = ${modelNameLowerCamel}Service.update(${modelNameLowerCamel});
        if (!flag) {
            return ApiResultGenerator.badParameter("更新失败！");
        }
        return ApiResultGenerator.success();
    }

    /**
     * ${modelNameUpperCamel}详情
     */
    @ApiOperation(value = "get ${modelNameUpperCamel} detail")
    @GetMapping("/detail")
    public ApiResult detail(@RequestParam Long id) {
${modelNameUpperCamel} ${modelNameLowerCamel} = ${modelNameLowerCamel}Service.findById(id);
        if (${modelNameLowerCamel} == null) {
            return ApiResultGenerator.badParameter("查找对象不存在！");
        }
        return ApiResultGenerator.success(${modelNameLowerCamel});
    }

    /**
     * ${modelNameUpperCamel}分页列表
     *
     * @param pageNum  页码
     * @param pageSize 每页记录数
     */
    @ApiOperation(value = "get ${modelNameUpperCamel} pageList")
    @GetMapping("/pageList")
    public ApiResult pageList(${modelNameUpperCamel} ${modelNameLowerCamel}, @RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "0") Integer pageSize) {
        // 分页
        PageHelper.startPage(pageNum, pageSize);
        // 增加查询条件，criteria.andCondition("xxxx");
        Condition condition = new Condition(${modelNameUpperCamel}.class);
        Example.Criteria criteria = condition.createCriteria();

<#--criteria.andNotEqualTo("nickName", umUser.getNickName());-->

        /* List<String> agentType = new ArrayList<>();
    agentType.add("COMPREHENSIVE");
    agentType.add("SETTLEMENT");
    criteria.andIn("agentType", agentType); */

    List<${modelNameUpperCamel}> list = ${modelNameLowerCamel}Service.findByCondition(condition);
    PageInfo pageInfo = new PageInfo(list);
    return ApiResultGenerator.success(pageInfo);
    }
}
