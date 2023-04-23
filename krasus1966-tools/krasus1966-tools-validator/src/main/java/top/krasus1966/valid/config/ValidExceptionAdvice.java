package top.krasus1966.valid.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.krasus1966.valid.result.BindingResultError;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理 处理校验失败
 *
 * @author Krasus1966
 * @date 2021/9/26 00:03
 **/
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class ValidExceptionAdvice {

    @Autowired(required = false)
    private ValidatorConfiguration validatorConfiguration;

    /**
     * 实体类字段校验异常处理
     *
     * @param e
     * @return ResponseEntity<String> {"msg":"参数信息不合法!","code":412,"data":{"age":"不能为null"}}
     */
    @ExceptionHandler(value = {
            BindException.class,
            MethodArgumentNotValidException.class
    })
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public ResponseEntity<String> errorHandler(BindException e) throws JsonProcessingException {
        //对校验错误信息进行封装，并输出到日志
        BindingResult result = e.getBindingResult();
        Map<String, String> resultMap = new HashMap<>();
        Map<String, Object> bodyMap = new HashMap<>();
        if (null != validatorConfiguration) {
            String message = "";
            FieldError fieldError = null;
            if (result.hasErrors()) {
                fieldError = BindingResultError.getErrorStr(result);
                if (null != fieldError) {
                    resultMap.put(fieldError.getField(), fieldError.getDefaultMessage());
                    message = fieldError.getDefaultMessage();
                }
            }
            bodyMap.put("code", HttpStatus.PRECONDITION_FAILED.value());
            bodyMap.put("msg", message);
            bodyMap.put("data", resultMap);
        } else {
            if (result.hasErrors()) {
                resultMap = BindingResultError.getError(result);
            }
            bodyMap.put("msg", "参数信息不合法!");
            bodyMap.put("data", resultMap);
        }
        bodyMap.put("code", HttpStatus.PRECONDITION_FAILED.value());
        bodyMap.put("isSuccessful", false);
        ObjectMapper objectMapper = JacksonEnum.INSTANCE.getObjectMapper();
        return ResponseEntity
                .status(HttpStatus.PRECONDITION_FAILED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(bodyMap));
    }
}
