package io.kikirikou.modules.common.markers;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(
        {ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
@Retention(RUNTIME)
@Documented
public @interface Common {

}
