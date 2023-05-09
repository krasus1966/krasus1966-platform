package top.krasus1966.common.preview.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Krasus1966
 * @date 2023/5/6 23:25
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultMsg {

    public static final Integer SUCCESS = 1;
    public static final Integer FAILED = 2;

    private Integer code;
    private String msg;

    public ResultMsg(Integer code) {
        this.code = code;
    }
}
