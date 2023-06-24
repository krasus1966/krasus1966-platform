/*
package top.krasus1966.core.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;
import top.krasus1966.core.web.web_function.RequestHandler;


*/
/**
 * @author Krasus1966
 * @date 2023/6/23 12:02
 **//*

@Configuration
public class WebFunctionConfig {

    @Bean
    public RouterFunction<ServerResponse> usesRoute(RequestHandler requestHandler){
        return RouterFunctions.route()
                .GET("/{module}/common/{id}", RequestPredicates.all(),requestHandler::get)
                .GET("/{module}/common/query", RequestPredicates.all(),requestHandler::query)
                .GET("/{module}/common/queryPage", RequestPredicates.all(),requestHandler::queryPage)
                .POST("/{module}/common/save", RequestPredicates.all(),requestHandler::save)
                .PUT("/{module}/common/update", RequestPredicates.all(),requestHandler::update)
                .DELETE("/{module}/common/{id}", RequestPredicates.all(),requestHandler::delete)
                .OPTIONS("/{module}/common/options", RequestPredicates.all(),requestHandler::options)
                .build();
    }
}
*/
