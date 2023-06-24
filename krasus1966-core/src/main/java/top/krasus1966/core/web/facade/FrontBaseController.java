package top.krasus1966.core.web.facade;


import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import top.krasus1966.core.db.entity.AbstractPersistent;
import top.krasus1966.core.db.service.IBaseService;
import top.krasus1966.core.web.entity.R;
import top.krasus1966.core.web.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * 公共前台Controller
 *
 * @author Krasus1966
 * @date 2022/10/30 22:26
 **/
@RestController
public abstract class FrontBaseController<Service extends IBaseService<Persistent>, Persistent extends AbstractPersistent> extends AbstractOptionFacade<Service,Persistent> {

    public FrontBaseController(HttpServletRequest request, HttpServletResponse response,
                               Service service) {
        super(request, response,service);
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
    public R<List<Persistent>> query(Persistent obj) {
        return R.success(service.query(obj));
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
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "页码", paramType = "query", dataTypeClass
                    = Integer.class, defaultValue = "1", required = true),
            @ApiImplicitParam(name = "size", value = "每页条数", paramType = "query", dataTypeClass =
                    Integer.class, defaultValue = "10", required = true)
    })
    @GetMapping("/queryPage")
    public R<Page<Persistent>> queryPage(Persistent obj, @ApiIgnore PageDTO<Persistent> page) {
        return R.success(service.queryPage(obj, page));
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
    public R<Persistent> get(@RequestParam("id") String id) {
        if (CharSequenceUtil.isBlank(id)) {
            return R.failed("id不能为空！");
        }
        return R.success(Optional.ofNullable(service.getById(id)).<NotFoundException>orElseThrow(NotFoundException::new));
    }

    /**
     * 单条查询接口
     *
     * @param id 数据id
     * @return top.krasus1966.base.result.R<Persistent>
     * @method getByPathVariable
     * @author krasus1966
     * @date 2022/1/3 17:15
     * @description 单条查询接口
     */
    @ApiOperation(value = "通过id查询数据", notes = "通过id查询数据，参数id拼接在请求路径中", httpMethod = "GET")
    @GetMapping("/get/{id}")
    public R<Persistent> getByPathVariable(@PathVariable String id) {
        return get(id);
    }
}
