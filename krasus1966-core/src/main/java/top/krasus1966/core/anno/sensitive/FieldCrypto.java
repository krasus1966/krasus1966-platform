package top.krasus1966.core.anno.sensitive;

import java.lang.annotation.*;

/**
 * @author Krasus1966
 * @date 2022/12/4 21:06
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface FieldCrypto {
}
