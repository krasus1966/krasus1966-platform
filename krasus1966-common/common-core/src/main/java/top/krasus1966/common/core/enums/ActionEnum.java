package top.krasus1966.common.core.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import top.krasus1966.common.core.entity.CommonQuery;

import java.util.Set;

@Getter
@AllArgsConstructor
public enum ActionEnum {
    WHERE {
        @Override
        public void execute(CommonQuery query, String[] queryKeys, String[] params) {
            // WHERE.S.EQ.column.(yyyy-MM-dd)
            // WHERE.T.EQ.column.yyyy-MM-dd 时间格式才需要
            if (queryKeys.length < 4 || !DATA_TYPE_SET.contains(queryKeys[1]) || !COMPARE_TYPE_SET.contains(queryKeys[2]) || ObjectUtil.isEmpty(queryKeys[3])) {
                return;
            }
            String dataFormat = null;
            if ("T".equals(queryKeys[1])) {
                if ((queryKeys.length == 5 && ObjectUtil.isNotEmpty(queryKeys[4]))) {
                    dataFormat = queryKeys[4];
                }
            }
            String compareType = queryKeys[2];
            if (!"ISNULL".equals(compareType) && !"NOTNULL".equals(compareType) && (params.length == 0 || ObjectUtil.isEmpty(params[0]))) {
                return;
            }
            if (("BT".equals(compareType) || "NBT".equals(compareType)) && params.length < 2) {
                return;
            }
            String dataType = queryKeys[1];
            String column = queryKeys[3];
            query.addQuery(column, dataType, compareType, params, dataFormat);
        }
    }, ORDER {
        @Override
        public void execute(CommonQuery query, String[] queryKeys, String[] params) {
            // ORDER.ASC.num:column
            if (queryKeys.length < 3 || (!"ASC".equals(queryKeys[1]) && !"DESC".equals(queryKeys[1])) || params.length == 0) {
                return;
            }
            boolean isAsc = "ASC".equals(queryKeys[1]);
            Integer num = ObjectUtil.isEmpty(queryKeys[2]) ? 0 : Integer.parseInt(queryKeys[2]);
            String orderColumn = params[0];
            for (String column : orderColumn.split(",")) {
                query.addSort(column, num, isAsc);
            }
        }
    }, PAGE {
        @Override
        public void execute(CommonQuery query, String[] queryKeys, String[] params) {
            // PAGE.pageNum
            if ("pageNum".equals(queryKeys[1]) && params.length > 0 && ObjectUtil.isNotEmpty(params[0])) {
                query.setPageNum(Integer.valueOf(params[0]));
            }
            if ("pageSize".equals(queryKeys[1]) && params.length > 0 && ObjectUtil.isNotEmpty(params[0])) {
                query.setPageSize(Integer.parseInt(params[0]));
            }
        }
    };
    private static final Set<String> DATA_TYPE_SET = Set.of("S", "I", "L", "D", "B", "T");
    private static final Set<String> COMPARE_TYPE_SET = Set.of("EQ", "NE", "LT", "LE", "GT", "GE", "LIKE", "NLIKE",
            "LIKEL", "LIKER", "BT", "NBT", "IN", "ISNULL", "NOTNULL");


    public abstract void execute(CommonQuery query, String[] queryKeys, String[] params);
}
