package top.krasus1966.common.db.enums;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import lombok.extern.slf4j.Slf4j;
import top.krasus1966.common.core.util.DateTimeUtil;

import java.math.BigDecimal;

@Slf4j
public enum DataTypeEnum {
    S {
        @Override
        public Object[] execute(String[] valueStrs, String dateFormat) {
            Object[] values = new Object[valueStrs.length];
            for (int i = 0; i < valueStrs.length; i++) {
                if (SqlInjectionUtils.check(valueStrs[i])) {
                    continue;
                }
                try {
                    values[i] = String.valueOf(valueStrs[i]);
                } catch (Exception e) {
                    log.error("INPUT DATA_TYPE ERROR", e);
                }
            }
            return values;
        }
    },
    I {
        @Override
        public Object[] execute(String[] valueStrs, String dateFormat) {

            Object[] values = new Object[valueStrs.length];
            for (int i = 0; i < valueStrs.length; i++) {
                if (SqlInjectionUtils.check(valueStrs[i])) {
                    continue;
                }
                try {
                    values[i] = Integer.valueOf(valueStrs[i]);
                } catch (Exception e) {
                    log.error("INPUT DATA_TYPE ERROR", e);
                }
            }
            return values;
        }
    },
    L {
        @Override
        public Object[] execute(String[] valueStrs, String dateFormat) {
            Object[] values = new Object[valueStrs.length];
            for (int i = 0; i < valueStrs.length; i++) {
                if (SqlInjectionUtils.check(valueStrs[i])) {
                    continue;
                }
                try {
                    values[i] = Long.valueOf(valueStrs[i]);
                } catch (Exception e) {
                    log.error("INPUT DATA_TYPE ERROR", e);
                }
            }
            return values;
        }
    },
    D {
        @Override
        public Object[] execute(String[] valueStrs, String dateFormat) {
            Object[] values = new Object[valueStrs.length];
            for (int i = 0; i < valueStrs.length; i++) {
                if (SqlInjectionUtils.check(valueStrs[i])) {
                    continue;
                }
                try {
                    values[i] = new BigDecimal(valueStrs[i]);
                } catch (Exception e) {
                    log.error("INPUT DATA_TYPE ERROR", e);
                }
            }
            return values;
        }
    },
    B {
        @Override
        public Object[] execute(String[] valueStrs, String dateFormat) {
            Object[] values = new Object[valueStrs.length];
            for (int i = 0; i < valueStrs.length; i++) {
                if (SqlInjectionUtils.check(valueStrs[i])) {
                    continue;
                }
                try {
                    values[i] = Boolean.valueOf(valueStrs[i]);
                } catch (Exception e) {
                    log.error("INPUT DATA_TYPE ERROR", e);
                }
            }
            return values;

        }
    },
    T {
        @Override
        public Object[] execute(String[] valueStrs, String dateFormat) {
            Object[] values = new Object[valueStrs.length];
            for (int i = 0; i < valueStrs.length; i++) {
                if (SqlInjectionUtils.check(valueStrs[i])) {
                    continue;
                }
                try {
                    values[i] = DateTimeUtil.toDate(valueStrs[i], CharSequenceUtil.isEmpty(dateFormat) ? "yyyy" +
                            "-MM-dd HH:mm:ss" : dateFormat);
                } catch (Exception e) {
                    log.error("INPUT DATA_TYPE ERROR", e);
                }
            }
            return values;
        }
    };

    public abstract Object[] execute(String[] valueStrs, String dateFormat);
}
