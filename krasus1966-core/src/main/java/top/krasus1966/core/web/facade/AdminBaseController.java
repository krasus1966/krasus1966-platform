package top.krasus1966.core.web.facade;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.krasus1966.core.anno.sensitive.Crypto;
import top.krasus1966.core.constant.Constants;
import top.krasus1966.core.entity.db.BaseEntity;
import top.krasus1966.core.entity.web.R;
import top.krasus1966.core.exception.NotFoundException;
import top.krasus1966.core.service.IService2;
import top.krasus1966.valid.anno.group.Insert;
import top.krasus1966.valid.anno.group.Update;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 公用后台Controller
 *
 * @author Krasus1966
 * @date 2022/1/3 17:00
 **/
@RestController
public abstract class AdminBaseController<S extends IService2<T>, T extends BaseEntity> extends BaseController {

    protected final S service;

    public AdminBaseController(HttpServletRequest request, HttpServletResponse response,
                               S service) {
        super(request, response);
        this.service = service;
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
    public R<T> insert(@Validated(Insert.class) @RequestBody T obj) {
        service.checkInsertValidity(obj);
        if (service.save(obj)) {
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
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "id", paramType = "form", dataTypeClass
                    = String.class, required = true),
    })
    @RequestMapping(value = "/update", method = {RequestMethod.POST, RequestMethod.PUT})
    public R<T> update(@Validated(Update.class) @RequestBody T obj) {
        service.checkUpdateValidity(obj);
        if (service.updateById(obj)) {
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
    public R<Boolean> deleteByIds(@RequestParam("ids") String ids) {
        if (CharSequenceUtil.isBlank(ids)) {
            return R.failed("ids不能为空！");
        }
        service.checkDeleteValidity(ids);
        if (service.removeByIds(Arrays.asList(ids.split(Constants.Entity.SPLIT)))) {
            return R.success();
        }
        throw new NotFoundException();
    }

    /**
     * 列表查询接口
     *
     * @param obj 查询对象
     * @return top.krasus1966.base.result.R<java.util.List < T>>
     * @method query
     * @author krasus1966
     * @date 2022/1/3 17:12
     * @description 列表查询接口
     */
    @ApiOperation(value = "列表查询", notes = "返回实体对应的列表", httpMethod = "GET")
    @GetMapping("/query")
    public R<List<T>> query(T obj) {
        return R.success(service.lambdaQuery().setEntity(obj).list());
    }

    /**
     * 分页查询接口
     *
     * @param obj  查询对象
     * @param page 分页封装对象 包含current页码，size每页条数
     * @return top.krasus1966.base.result.R<com.baomidou.mybatisplus.extension.plugins.pagination.PageResult < T>>
     * @method queryPage
     * @author krasus1966
     * @date 2022/1/3 17:13
     * @description 分页查询接口
     */
    @ApiOperation(value = "分页查询", notes = "分页查询返回，默认页码1，默认每页条数10", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "页码", paramType = "query", dataTypeClass
                    = Integer.class, defaultValue = "1", required = true),
            @ApiImplicitParam(name = "size", value = "每页条数", paramType = "query", dataTypeClass =
                    Integer.class, defaultValue = "10", required = true)
    })
    @GetMapping("/queryPage")
    @Crypto
    public R<Page<T>> queryPage(T obj, @ApiIgnore Page<T> page) {
        return R.success(service.lambdaQuery().setEntity(obj).page(page));
    }

    /**
     * 单条查询接口
     *
     * @param id 数据id
     * @return top.krasus1966.base.result.R<T>
     * @method get
     * @author krasus1966
     * @date 2022/1/3 17:14
     * @description 单条查询接口
     */
    @ApiOperation(value = "通过id查询数据", notes = "通过id查询数据", httpMethod = "GET")
    @GetMapping("/get")
    public R<T> get(@RequestParam("id") String id) {
        if (CharSequenceUtil.isBlank(id)) {
            return R.failed("id不能为空！");
        }
        return R.success(Optional.ofNullable(service.getById(id)).<NotFoundException>orElseThrow(() -> {
            throw new NotFoundException();
        }));
    }

    /**
     * 单条查询接口
     *
     * @param id 数据id
     * @return top.krasus1966.base.result.R<T>
     * @method getByPathVariable
     * @author krasus1966
     * @date 2022/1/3 17:15
     * @description 单条查询接口
     */

    @ApiOperation(value = "通过id查询数据", notes = "通过id查询数据，参数id拼接在请求路径中", httpMethod = "GET")
    @GetMapping("/get/{id}")
    public R<T> getByPathVariable(@PathVariable String id) {
        return get(id);
    }
}
