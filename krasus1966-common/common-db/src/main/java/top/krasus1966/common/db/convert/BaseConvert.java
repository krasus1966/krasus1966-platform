package top.krasus1966.common.db.convert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import top.krasus1966.common.core.entity.PageDTO;

import java.util.ArrayList;
import java.util.List;

public class BaseConvert<Source, Target> {

    private final Class<Source> sourceClass;
    private final Class<Target> targetClass;

    public BaseConvert(Class<Source> sourceClass, Class<Target> targetClass) {
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
    }

    /**
     * 源转目标
     *
     * @param source
     * @return
     */
    public Target source2Target(Source source) {
        Target target = null;
        try {
            target = targetClass.getDeclaredConstructor().newInstance();
            if (source != null) {
                BeanUtils.copyProperties(source, target);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return target;
    }

    /**
     * 源转目标
     *
     * @param target
     * @return
     */
    public Source target2Source(Target target) {
        Source source = null;
        try {
            source = sourceClass.getDeclaredConstructor().newInstance();
            if (target != null) {
                BeanUtils.copyProperties(target, source);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return source;
    }


    public List<Target> sources2Targets(List<Source> sourceList) {
        List<Target> targetList = new ArrayList<>();
        for (Source source : sourceList) {
            Target target = source2Target(source);
            targetList.add(target);
        }
        return targetList;
    }

    public List<Source> targets2Sources(List<Target> targetList) {
        List<Source> sourceList = new ArrayList<>();
        for (Target target : targetList) {
            Source source = target2Source(target);
            sourceList.add(source);
        }
        return sourceList;
    }

    public PageDTO<Target> sourcePage2TargetPage(Page<Source> page1) {
        PageDTO<Target> pr = new PageDTO<>();
        pr.setTotal(page1.getTotal());
        List<Target> targetList = new ArrayList<>();
        for (Source source : page1.getRecords()) {
            Target target = source2Target(source);
            targetList.add(target);
        }
        pr.setRecords(targetList);
        return pr;
    }

    public Page<Source> targetPage2SourcePage(PageDTO<Target> page1) {
        Page<Source> pr = new Page<>();
        pr.setTotal(page1.getTotal());
        List<Source> sourceList = new ArrayList<>();
        for (Target target : page1.getRecords()) {
            Source source = target2Source(target);
            sourceList.add(source);
        }
        pr.setRecords(sourceList);
        return pr;
    }
}
