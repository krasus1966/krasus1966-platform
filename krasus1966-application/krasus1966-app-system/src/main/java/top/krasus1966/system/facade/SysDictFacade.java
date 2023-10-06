package top.krasus1966.system.facade;

import cn.hutool.core.text.CharSequenceUtil;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.krasus1966.core.base.constant.Constants;
import top.krasus1966.core.base.util.TreeUtil;
import top.krasus1966.core.cache.constant.DictConst;
import top.krasus1966.core.rule_engine.IRuleExecuteService;
import top.krasus1966.core.web.entity.R;
import top.krasus1966.core.web.facade.AbstractAdminFacade;
import top.krasus1966.system.domain.sys.SysDict;
import top.krasus1966.system.service.SysDictService;

import java.util.Comparator;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2023/5/3 14:44
 **/
@RestController
@RequestMapping("/admin/sys-dict")
public class SysDictFacade extends AbstractAdminFacade<SysDictService, SysDict> {

    public SysDictFacade(HttpServletRequest request, HttpServletResponse response, SysDictService service, IRuleExecuteService ruleExecuteService) {
        super(request, response, service, ruleExecuteService);
    }

    /**
     * 通过dictCode查询字典
     *
     * @param code dictCode
     * @return top.krasus1966.base.result.R<top.krasus1966.base.domain.SysDict>
     * @method getDictByCode
     * @author krasus1966
     * @date 2022/1/3 21:06
     * @description 通过dictCode查询字典
     */
    @ApiOperation(value = "通过dictCode查询启用的字典组")
    @GetMapping("/getDictByCode")
    public R<List<SysDict>> getDictByCode(String code) {
        return R.success(service.lambdaQuery().eq(SysDict::getDictCode, code).eq(SysDict::getStatus, DictConst.STATUS_TYPE.STATUS_TYPE_ON).list());
    }

    /**
     * 通过dictValue查询字典
     *
     * @param value dictValue
     * @return top.krasus1966.base.result.R<top.krasus1966.base.domain.SysDict>
     * @method getDictByCode
     * @author krasus1966
     * @date 2022/1/3 21:06
     * @description 通过dictCode查询字典
     */
    @ApiOperation(value = "通过dictValue查询启用的字典")
    @GetMapping("/getDictByValue")
    public R<SysDict> getDictByValue(String value) {
        return R.success(service.lambdaQuery().eq(SysDict::getDictValue, value).eq(SysDict::getStatus, DictConst.STATUS_TYPE.STATUS_TYPE_ON).last("LIMIT 1").one());
    }

    /**
     * 通过dictValue查询字典值
     *
     * @param value dictValue
     * @return top.krasus1966.base.result.R<java.lang.String>
     * @method getDictValueByCode
     * @author krasus1966
     * @date 2022/1/3 21:10
     * @description 通过dictCode查询字典值
     */
    @ApiOperation(value = "通过dictValue查询启用的字典值")
    @GetMapping("/getDictNameByValue")
    public R<String> getDictValueByCode(String value) {
        return R.success(service.lambdaQuery().eq(SysDict::getDictValue,
                value).eq(SysDict::getStatus, DictConst.STATUS_TYPE.STATUS_TYPE_ON).last("LIMIT " +
                "1").one().getDictName());
    }

    /**
     * 通过parentId查询字典组
     *
     * @param parentId 父id
     * @return top.krasus1966.base.result.R<java.util.List < top.krasus1966.base.domain.SysDict>>
     * @method getDictByParentId
     * @author krasus1966
     * @date 2022/1/3 21:20
     * @description 通过parentId查询字典组
     */
    @ApiOperation(value = "通过parentId查询启用的字典组")
    @GetMapping("/getDictByParentId")
    public R<List<SysDict>> getDictByParentId(String parentId) {
        return R.success(service.lambdaQuery().eq(SysDict::getParentId, parentId).list());
    }

    /**
     * 获取字典树
     *
     * @param obj 字典对象
     * @return top.krasus1966.base.result.R<java.util.List < top.krasus1966.base.domain.SysDict>>
     * @method getTree
     * @author krasus1966
     * @date 2022/1/3 21:20
     * @description 获取字典树
     */
    @ApiOperation(value = "获取字典树")
    @GetMapping("/getTree")
    public R<List<SysDict>> getTree(SysDict obj) {
        if (CharSequenceUtil.isBlank(obj.getParentId())) {
            obj.setParentId(Constants.Entity.ROOT);
        }
        List<SysDict> result = service.query().list();
        return R.success(TreeUtil.parseTree(result, obj.getParentId(), SysDict::getId,
                SysDict::getParentId, SysDict::setChildren,
                Comparator.comparing(SysDict::getSort)));
    }
}
