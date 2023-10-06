package top.krasus1966.core.web.facade;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.krasus1966.core.crypto.anno.Crypto;
import top.krasus1966.core.db.entity.AbstractPersistent;
import top.krasus1966.core.db.service.IBaseService;
import top.krasus1966.core.rule_engine.IRuleExecuteService;
import top.krasus1966.core.web.entity.R;
import top.krasus1966.core.web.exception.NotFoundException;
import top.krasus1966.valid.anno.group.Insert;
import top.krasus1966.valid.anno.group.Update;

import java.util.List;
import java.util.Optional;

/**
 * @author Krasus1966
 * @date 2023/6/16 23:46
 **/
public abstract class AbstractAdminFacade<Service extends IBaseService<Persistent>,
        Persistent extends AbstractPersistent> extends AbstractOptionFacade<Service, Persistent> {

    protected final IRuleExecuteService ruleExecuteService;

    public AbstractAdminFacade(
            HttpServletRequest request, HttpServletResponse response,
            Service service,
            IRuleExecuteService ruleExecuteService) {
        super(request, response, service);
        this.ruleExecuteService = ruleExecuteService;
    }

    /**
     * 新增接口
     *
     * @param obj 新增对象
     * @return top.krasus1966.base.result.R<java.lang.Boolean>
     * @method insert
     * @author krasus1966
     * @date 2022/1/3 17:18
     * @description 新增接口
     */

    @ApiOperation(value = "新增", notes = "新增并返回对应的对象", httpMethod = "POST")
    @PostMapping(value = "/insert")
    public R<Persistent> insert(@Validated(Insert.class) @RequestBody Persistent obj) throws Exception {
        Class<Persistent> persistentClass = service.entityClass();
        String ruleKey = "INSERT-" + persistentClass.getSimpleName().replace("Persistent", "");
        ruleExecuteService.doExec(ruleKey, obj, service);
        if (service.insert(obj)) {
            return R.success(obj);
        }
        return R.failed("新增失败，数据无改变");
    }

    /**
     * 修改接口
     *
     * @param obj 修改对象
     * @return top.krasus1966.base.result.R<java.lang.Boolean>
     * @method update
     * @author krasus1966
     * @date 2022/1/3 17:18
     * @description 修改接口
     */
    @ApiOperation(value = "修改", notes = "修改，无变化数据判定为修改失败", httpMethod = "POST")
    @ApiImplicitParams(value = {@ApiImplicitParam(name = "id", value = "id", paramType = "form", dataTypeClass = String.class, required = true),})
    @RequestMapping(value = "/update", method = {RequestMethod.POST, RequestMethod.PUT})
    public R<Persistent> update(@Validated(Update.class) @RequestBody Persistent obj) throws Exception {
        Class<Persistent> persistentClass = service.entityClass();
        String ruleKey = "UPDATE-" + persistentClass.getSimpleName().replace("Persistent", "");
        ruleExecuteService.doExec(ruleKey, obj, service);
        if (service.update(obj)) {
            return R.success(obj);
        }
        return R.failed("更新失败，数据无改变");
    }

    /**
     * 批量删除接口
     *
     * @param ids 删除的数据ids
     * @return top.krasus1966.base.result.R<java.lang.Boolean>
     * @method deleteByIds
     * @author krasus1966
     * @date 2022/1/3 17:12
     * @description 批量删除接口
     */
    @ApiOperation(value = "批量删除", notes = "删除，ids为id逗号分隔传递", httpMethod = "POST")
    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.POST, RequestMethod.DELETE})
    public R<Boolean> deleteByIds(@RequestParam("ids") String ids) throws Exception {
        if (CharSequenceUtil.isBlank(ids)) {
            return R.failed("ids不能为空！");
        }
        Class<Persistent> persistentClass = service.entityClass();
        String ruleKey = "DELETE-" + persistentClass.getSimpleName().replace("Persistent", "");
        ruleExecuteService.doExec(ruleKey, ids, service);
        if (service.delete(ids)) {
            return R.success();
        }
        throw new NotFoundException();
    }

    /**
     * 列表查询接口
     *
     * @param obj 查询对象
     * @return top.krasus1966.base.result.R<java.util.List < Persistent>>
     * @method query
     * @author krasus1966
     * @date 2022/1/3 17:12
     * @description 列表查询接口
     */
    @ApiOperation(value = "列表查询", notes = "返回实体对应的列表", httpMethod = "GET")
    @GetMapping("/query")
    public R<List<Persistent>> query(Persistent obj, @RequestBody(required = false) List<OrderItem> orderItems) throws Exception {
        Class<Persistent> persistentClass = service.entityClass();
        String ruleKey = "QUERY-" + persistentClass.getSimpleName().replace("Persistent", "");
        ruleExecuteService.doExec(ruleKey, obj, service);
        List<Persistent> persistentList = service.query(obj, orderItems);
        return R.success(persistentList);
    }


    /**
     * 分页查询接口
     *
     * @param obj  查询对象
     * @param page 分页封装对象 包含current页码，size每页条数
     * @return top.krasus1966.base.result.R<com.baomidou.mybatisplus.extension.plugins.pagination.PageResult < Persistent>>
     * @method queryPage
     * @author krasus1966
     * @date 2022/1/3 17:13
     * @description 分页查询接口
     */
    @ApiOperation(value = "分页查询", notes = "分页查询返回，默认页码1，默认每页条数10", httpMethod = "GET")
    @ApiImplicitParams(value = {@ApiImplicitParam(name = "current", value = "页码", paramType = "query", dataTypeClass = Integer.class, defaultValue = "1", required = true), @ApiImplicitParam(name = "size", value = "每页条数", paramType = "query", dataTypeClass = Integer.class, defaultValue = "10", required = true)})
    @GetMapping("/queryPage")
    @Crypto
    public R<Page<Persistent>> queryPage(Persistent obj, @ApiIgnore PageDTO<Persistent> page) throws Exception {
        Class<Persistent> persistentClass = service.entityClass();
        String ruleKey = "QUERYPAGE-" + persistentClass.getSimpleName().replace("Persistent", "");
        ruleExecuteService.doExec(ruleKey, obj, service);
        Page<Persistent> persistentPage = service.queryPage(obj, page);
        return R.success(persistentPage);
    }


    /**
     * 单条查询接口
     *
     * @param id 数据id
     * @return top.krasus1966.base.result.R<Persistent>
     * @method get
     * @author krasus1966
     * @date 2022/1/3 17:14
     * @description 单条查询接口
     */
    @ApiOperation(value = "通过id查询数据", notes = "通过id查询数据", httpMethod = "GET")
    @GetMapping("/get")
    public R<Persistent> get(@RequestParam("id") String id) throws Exception {
        if (CharSequenceUtil.isBlank(id)) {
            return R.failed("id不能为空！");
        }
        Class<Persistent> persistentClass = service.entityClass();
        String ruleKey = "GET-" + persistentClass.getSimpleName().replace("Persistent", "");
        ruleExecuteService.doExec(ruleKey, id, service);
        return R.success(Optional.ofNullable(service.getById(id)).<NotFoundException>orElseThrow(NotFoundException::new));
    }
}
