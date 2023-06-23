package top.krasus1966.core.web.facade;


import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.krasus1966.core.db.entity.AbstractPersistent;
import top.krasus1966.core.web.entity.R;
import top.krasus1966.core.web.exception.NotFoundException;
import top.krasus1966.core.db.service.IBaseService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 公共前台Controller
 *
 * @author Krasus1966
 * @date 2022/10/30 22:26
 **/
@RestController
public abstract class FrontBaseController<S extends IBaseService<T>, T extends AbstractPersistent> extends BaseController {

    protected final S service;

    public FrontBaseController(HttpServletRequest request, HttpServletResponse response,
                               S service) {
        super(request, response);
        this.service = service;
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

    /**
     * 查询特殊内容
     *
     * @param obj       查询条件对象
     * @param key       key字段
     * @param keyLabel  key名称
     * @param label     标签字段
     * @param labelName 标签名称
     * @return top.krasus1966.core.web.entity.R<java.util.List < java.util.Map < java.lang.String, java.lang.Object>>>
     * @throws
     * @method option
     * @author krasus1966
     * @date 2023/5/3 15:47
     * @description 查询特殊内容
     */
    @GetMapping("/option")
    public R<List<Map<String, Object>>> option(@RequestBody(required = false) T obj,
                                               @RequestParam(defaultValue = "id") String key,
                                               @RequestParam(defaultValue = "value") String keyLabel,
                                               String label,
                                               @RequestParam(defaultValue = "label") String labelName) {
        key = StrUtil.toSymbolCase(key, '_');
        label = StrUtil.toSymbolCase(label, '_');
        QueryWrapper<T> wrapper = new QueryWrapper<T>(obj);
        wrapper.select(key + " AS " + keyLabel, label + " AS " + labelName).groupBy(key);
        List<Map<String, Object>> maps = service.getBaseMapper().selectMaps(wrapper);
        return R.success(maps);
    }
}
