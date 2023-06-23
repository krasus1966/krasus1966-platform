package top.krasus1966.valid.anno.valid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import top.krasus1966.valid.validation.StrictEmailValidation;

import java.lang.annotation.*;

/**
 * @author Krasus1966
 * @date 2022/11/15 20:47
 **/
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StrictEmailValidation.class)
@Documented
public @interface StrictEmail {

    String message() default "Invalid Email.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String regexp() default "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
}
