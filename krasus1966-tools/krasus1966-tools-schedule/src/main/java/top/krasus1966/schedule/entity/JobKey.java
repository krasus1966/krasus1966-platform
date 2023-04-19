package top.krasus1966.schedule.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Krasus1966
 * @date 2023/4/19 22:44
 **/
@Data
@EqualsAndHashCode
public class JobKey {
    private String name;
    private String group;

    public JobKey(String name, String group) {
        this.name = name;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public JobKey setName(String name) {
        this.name = name;
        return this;
    }

    public String getGroup() {
        return group;
    }

    public JobKey setGroup(String group) {
        this.group = group;
        return this;
    }
}
