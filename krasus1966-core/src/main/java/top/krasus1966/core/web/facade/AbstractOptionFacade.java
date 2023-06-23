package top.krasus1966.core.web.facade;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import top.krasus1966.core.db.entity.AbstractPersistent;
import top.krasus1966.core.db.service.IBaseService;
import top.krasus1966.core.web.entity.R;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author Krasus1966
 * @date 2023/6/18 22:55
 **/
public abstract class AbstractOptionFacade<Service extends IBaseService<Persistent>, Persistent extends AbstractPersistent> extends BaseController {

    private final Service service;

    public AbstractOptionFacade(HttpServletRequest request, HttpServletResponse response, Service service) {
        super(request, response);
        this.service = service;
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
    public R<List<Map<String, Object>>> option(
            @RequestBody(required = false) Persistent obj,
            @RequestParam(required = false, defaultValue = "id") String key,
            @RequestParam(required = false, defaultValue = "value") String keyLabel,
            String label,
            @RequestParam(required = false, defaultValue = "label") String labelName) {
        if (!SqlInjectionUtils.check(key) || !SqlInjectionUtils.check(keyLabel) || SqlInjectionUtils.check(label) || SqlInjectionUtils.check(labelName)) {
            return R.failed("不可接受的传递参数");
        }
        key = StrUtil.toSymbolCase(key, '_');
        label = StrUtil.toSymbolCase(label, '_');
        QueryWrapper<Persistent> wrapper = new QueryWrapper<Persistent>(obj);
        wrapper.select(key + " AS " + keyLabel, label + " AS " + labelName).groupBy(key);
        List<Map<String, Object>> maps = service.getBaseMapper().selectMaps(wrapper);
        return R.success(maps);
    }
}
