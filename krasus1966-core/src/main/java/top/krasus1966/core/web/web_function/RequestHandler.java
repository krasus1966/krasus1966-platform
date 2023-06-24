/*
package top.krasus1966.core.web.web_function;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

*/
/**
 * @author Krasus1966
 * @date 2023/6/23 17:22
 **//*

@Component
public class RequestHandler{

    public ServerResponse get(ServerRequest request) {
        String module = request.pathVariable("module");
        if (StrUtil.isBlank(module)) {
            return ServerResponse.notFound().build();
        }


        return null;
    }

    public ServerResponse save(ServerRequest request) {

        return null;
    }

    public ServerResponse update(ServerRequest request) {
        return null;
    }

    public ServerResponse delete(ServerRequest request) {
        return null;
    }

    public ServerResponse queryPage(ServerRequest request) {
        return null;
    }



    public ServerResponse query(ServerRequest request) {
        return null;
    }

    public ServerResponse options(ServerRequest request) {
        return null;
    }
}
*/
