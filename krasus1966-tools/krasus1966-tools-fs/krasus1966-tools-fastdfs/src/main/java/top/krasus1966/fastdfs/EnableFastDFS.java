package top.krasus1966.fastdfs;

import java.lang.annotation.*;

/**
 *
 *
 * @author Krasus1966
 * @date 2020/8/18 21:37
 **/
@Target(value = ElementType.TYPE)
//@Import(value = {AutoConfigFastDFS.class})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface EnableFastDFS {
}
