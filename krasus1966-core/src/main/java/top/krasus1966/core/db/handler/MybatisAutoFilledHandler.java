package top.krasus1966.core.db.handler;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import top.krasus1966.core.base.constant.DictConst;
import top.krasus1966.core.web.auth.entity.UserLoginInfo;
import top.krasus1966.core.web.util.login.LoginUtils;

import java.time.LocalDateTime;

/**
 * 自动填充配置
 *
 * @author Krasus1966
 * @date 2021/4/18 23:07
 **/
@Slf4j
@Component
public class MybatisAutoFilledHandler implements MetaObjectHandler {

    /**
     * 新增时自动填充，需要在 @TableField 中增加 fill = FieldFill.INSERT 或 FieldFill.INSERT_UPDATE
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            // 填充前判断是否已经存在值
            UserLoginInfo info = LoginUtils.getUserLoginInfo();
            if (ObjectUtil.isEmpty(metaObject.getValue("crtIp")) && null != info) {
                this.strictInsertFill(metaObject, "crtIp", String.class, info.getLoginIp());
            }
            if (ObjectUtil.isEmpty(metaObject.getValue("crtId")) && null != info) {
                this.strictInsertFill(metaObject, "crtId", String.class, info.getId());
            }
            this.strictInsertFill(metaObject, "crtTime", LocalDateTime.class, LocalDateTime.now());
            this.strictUpdateFill(metaObject, "version", Integer.class, 0);
            this.strictInsertFill(metaObject, "deleted", String.class,
                    DictConst.DELETE_TYPE.DELETE_TYPE_NO);
        } catch (Exception e) {
            log.error("自动填充失败:insert", e);
        }
    }

    /**
     * 修改时自动填充，需要在 @TableField 中增加 fill = FieldFill.UPDATE 或 FieldFill.INSERT_UPDATE
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            UserLoginInfo info = LoginUtils.getUserLoginInfo();
            if (null != info) {
                this.strictUpdateFill(metaObject, "updIp", String.class, info.getLoginIp());
                this.strictUpdateFill(metaObject, "updId", String.class, info.getId());
            }
            this.strictUpdateFill(metaObject, "updTime", LocalDateTime.class, LocalDateTime.now());
        } catch (Exception e) {
            log.error("自动填充失败:update", e);
        }
    }
}
