package top.krasus1966.core.web.facade;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import top.krasus1966.core.db.entity.AbstractPersistent;
import top.krasus1966.core.web.entity.AbstractResponse;
import top.krasus1966.core.web.entity.AbstractSearchForm;
import top.krasus1966.core.web.entity.AbstractUpdateForm;
import top.krasus1966.core.web.entity.R;

import java.util.List;

/**
 * @author Krasus1966
 * @date 2023/6/16 23:35
 **/
public interface ICrudFacade<Persistent extends AbstractPersistent, Response extends AbstractResponse,
        UpdateForm extends AbstractUpdateForm, SearchForm extends AbstractSearchForm> {

    R<Response> insert(UpdateForm obj);

    R<Response> update(UpdateForm obj);

    R<Boolean> deleteByIds(String ids);


    R<List<Response>> query(SearchForm obj, List<OrderItem> orderItems);


    R<Page<Response>> queryPage(SearchForm obj,  PageDTO<Persistent> page);


    R<Response> get(String id);
}