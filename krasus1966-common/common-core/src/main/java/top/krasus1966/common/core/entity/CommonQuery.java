package top.krasus1966.common.core.entity;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/***
 * 通用查询构建
 */
@Data
public class CommonQuery {

    private Integer pageNum = 1;
    private Integer pageSize = 10;

    private List<CommonQueryDTO> params = new ArrayList<>();
    private List<CommonSortableDTO> sorts = new ArrayList<>();

    public CommonQuery() {

    }

    public void addQuery(String column, String dataType, String compareType, String[] value, String dataFormat) {
        CommonQueryDTO dto = new CommonQueryDTO();
        dto.setColumn(column);
        dto.setDataType(dataType);
        dto.setCompareType(compareType);
        dto.setValue(value);
        dto.setDateFormat(dataFormat);
        params.add(dto);
    }

    public void addSort(String column, Integer num, Boolean asc) {
        CommonSortableDTO dto = new CommonSortableDTO();
        dto.setColumn(column);
        dto.setNum(num);
        dto.setAsc(asc);
        sorts.add(dto);
    }


    @Data
    public static class CommonQueryDTO {
        /**
         * 字段
         */
        private String column;
        /**
         * 数据类型
         */
        private String dataType;
        private String dateFormat;
        /**
         * 比较方式
         */
        private String compareType;
        /**
         * 值
         */
        private String[] value;
    }

    @Data
    public static class CommonSortableDTO {
        private String column;
        private Integer num;
        private Boolean asc;
    }
}
