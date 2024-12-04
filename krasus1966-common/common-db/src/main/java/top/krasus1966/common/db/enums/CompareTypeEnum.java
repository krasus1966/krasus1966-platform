package top.krasus1966.common.db.enums;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

public enum CompareTypeEnum {
    EQ {
        @Override
        public <T> void execute(QueryWrapper<T> wrapper, String column, Object[] values) {
            wrapper.eq(ObjectUtil.isNotEmpty(values[0]), StringUtils.camelToUnderline(column), values[0]);
        }
    },
    NE {
        @Override
        public <T> void execute(QueryWrapper<T> wrapper, String column, Object[] values) {
            wrapper.ne(ObjectUtil.isNotEmpty(values[0]), StringUtils.camelToUnderline(column), values[0]);
        }
    },
    LT {
        @Override
        public <T> void execute(QueryWrapper<T> wrapper, String column, Object[] values) {
            wrapper.lt(ObjectUtil.isNotEmpty(values[0]), StringUtils.camelToUnderline(column), values[0]);
        }
    },
    LE {
        @Override
        public <T> void execute(QueryWrapper<T> wrapper, String column, Object[] values) {
            wrapper.le(ObjectUtil.isNotEmpty(values[0]), StringUtils.camelToUnderline(column), values[0]);
        }
    },
    GT {
        @Override
        public <T> void execute(QueryWrapper<T> wrapper, String column, Object[] values) {
            wrapper.gt(ObjectUtil.isNotEmpty(values[0]), StringUtils.camelToUnderline(column), values[0]);
        }
    }, GE {
        @Override
        public <T> void execute(QueryWrapper<T> wrapper, String column, Object[] values) {
            wrapper.ge(ObjectUtil.isNotEmpty(values[0]), StringUtils.camelToUnderline(column), values[0]);
        }
    },
    LIKE {
        @Override
        public <T> void execute(QueryWrapper<T> wrapper, String column, Object[] values) {
            wrapper.like(ObjectUtil.isNotEmpty(values[0]), StringUtils.camelToUnderline(column), values[0]);
        }
    },
    NLIKE {
        @Override
        public <T> void execute(QueryWrapper<T> wrapper, String column, Object[] values) {
            wrapper.notLike(ObjectUtil.isNotEmpty(values[0]), StringUtils.camelToUnderline(column), values[0]);
        }
    },
    LIKEL {
        @Override
        public <T> void execute(QueryWrapper<T> wrapper, String column, Object[] values) {
            wrapper.likeLeft(ObjectUtil.isNotEmpty(values[0]), StringUtils.camelToUnderline(column), values[0]);
        }
    },
    LIKER {
        @Override
        public <T> void execute(QueryWrapper<T> wrapper, String column, Object[] values) {
            wrapper.likeRight(ObjectUtil.isNotEmpty(values[0]), StringUtils.camelToUnderline(column), values[0]);
        }
    },
    BT {
        @Override
        public <T> void execute(QueryWrapper<T> wrapper, String column, Object[] values) {
            wrapper.between(ObjectUtil.isNotEmpty(values[0]) && ObjectUtil.isNotEmpty(values[1]),
                    StringUtils.camelToUnderline(column), values[0], values[1]);
        }
    },
    NBT {
        @Override
        public <T> void execute(QueryWrapper<T> wrapper, String column, Object[] values) {
            wrapper.notBetween(ObjectUtil.isNotEmpty(values[0]) && ObjectUtil.isNotEmpty(values[1]),
                    StringUtils.camelToUnderline(column), values[0], values[1]);
        }
    },
    IN {
        @Override
        public <T> void execute(QueryWrapper<T> wrapper, String column, Object[] values) {
            wrapper.in(ObjectUtil.isNotEmpty(values), StringUtils.camelToUnderline(column), values);
        }
    },
    ISNULL {
        @Override
        public <T> void execute(QueryWrapper<T> wrapper, String column, Object[] values) {
            wrapper.isNull(ObjectUtil.isNotEmpty(column), StringUtils.camelToUnderline(column));
        }
    },
    NOTNULL {
        @Override
        public <T> void execute(QueryWrapper<T> wrapper, String column, Object[] values) {
            wrapper.isNotNull(ObjectUtil.isNotEmpty(column), StringUtils.camelToUnderline(column));
        }
    };

    public abstract <T> void execute(QueryWrapper<T> wrapper, String column, Object[] values);
}
