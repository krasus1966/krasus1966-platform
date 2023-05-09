package top.krasus1966.core.event.entity.db;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import top.krasus1966.core.db.entity.BaseEntity;
import top.krasus1966.core.event.enums.BaseEventEnum;
import top.krasus1966.core.event.enums.EventStatusEnum;
import top.krasus1966.core.event.entity.BaseEvent;
import top.krasus1966.core.json.util.JsonUtils;

/**
 * @author Krasus1966
 * @date 2022/4/17 16:58
 **/
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_event_log")
@ApiModel(value = "EventPO对象", description = "记录事件信息")
@Slf4j
public class EventPO extends BaseEntity {

    @ApiModelProperty("事件内容")
    @TableField("data")
    private String data;

    @ApiModelProperty("状态")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("事件类型")
    @TableField("type")
    private String type;

    public EventPO(BaseEvent obj) {
        this.setId(obj.getId());
        try {
            this.data = JsonUtils.objectToJson(obj.getData());
        } catch (JsonProcessingException e) {
            log.error("json生成异常", e);
        }
        this.status = obj.getStatus().getCode();
        this.type = obj.getType().getKey();
    }

    public BaseEvent toBaseEvent() {
        return new BaseEvent(this.getId(), this.data, EventStatusEnum.valueOf(this.status),
                BaseEventEnum.findEnumByKey(this.type));
    }
}
