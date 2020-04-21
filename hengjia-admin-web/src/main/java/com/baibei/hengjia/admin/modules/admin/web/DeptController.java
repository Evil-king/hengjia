package com.baibei.hengjia.admin.modules.admin.web;

import com.baibei.hengjia.admin.config.DataScope;
import com.baibei.hengjia.admin.modules.admin.bean.dto.DeptDTO;
import com.baibei.hengjia.admin.modules.admin.model.Dept;
import com.baibei.hengjia.admin.modules.admin.service.IDeptService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * @author jie
 * @date 2019-03-25
 */
@RestController
@RequestMapping("api")
public class DeptController {

    @Autowired
    private IDeptService deptService;

  /*  @Autowired
    private DeptQueryService deptQueryService;*/

    @Autowired
    private DataScope dataScope;

    private static final String ENTITY_NAME = "dept";

    //@Log("查询部门")
    @GetMapping(value = "/dept")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_SELECT','DEPT_ALL','DEPT_SELECT')")
    public ApiResult<MyPageInfo<DeptDTO>> getDepts(DeptDTO resources) {
        // 数据权限
        Set<Long> deptIds = dataScope.getDeptIds();
        List<Dept> deptList = deptService.queryAll(resources, deptIds);
        List<DeptDTO> deptDTOList = BeanUtil.copyProperties(deptList, DeptDTO.class);
        return ApiResult.success(deptService.buildTree(deptDTOList));
    }

   /* @Log("新增部门")
    @PostMapping(value = "/dept")
    @PreAuthorize("hasAnyRole('ADMIN','DEPT_ALL','DEPT_CREATE')")
    public ResponseEntity create(@Validated @RequestBody Dept resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        return new ResponseEntity(deptService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改部门")
    @PutMapping(value = "/dept")
    @PreAuthorize("hasAnyRole('ADMIN','DEPT_ALL','DEPT_EDIT')")
    public ResponseEntity update(@Validated(Dept.Update.class) @RequestBody Dept resources){
        deptService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除部门")
    @DeleteMapping(value = "/dept/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DEPT_ALL','DEPT_DELETE')")
    public ResponseEntity delete(@PathVariable Long id){
        deptService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }*/
}