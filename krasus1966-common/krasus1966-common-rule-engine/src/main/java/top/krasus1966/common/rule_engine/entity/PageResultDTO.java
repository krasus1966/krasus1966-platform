package top.krasus1966.common.rule_engine.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author Krasus1966
 * {@code @date} 2022/11/2 22:18
 **/
public class PageResultDTO<T> implements Serializable {
    private List<T> rows = Collections.emptyList();
    private long total;

    public PageResultDTO() {
    }

    public PageResultDTO(List<T> rows, long total) {
        this.rows = rows;
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public PageResultDTO<T> setRows(List<T> rows) {
        this.rows = rows;
        return this;
    }

    public long getTotal() {
        return total;
    }

    public PageResultDTO<T> setTotal(long total) {
        this.total = total;
        return this;
    }
}
