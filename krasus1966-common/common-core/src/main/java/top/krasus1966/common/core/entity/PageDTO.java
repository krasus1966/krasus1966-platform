package top.krasus1966.common.core.entity;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class PageDTO<T> {

    private long total = 0;

    private List<T> records = Collections.emptyList();
}
