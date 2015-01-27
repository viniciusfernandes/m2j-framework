package br.com.innovatium.mumps2java.todo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(value = { ElementType.LOCAL_VARIABLE, ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD })
public @interface REVIEW {
	String author() default "";

	String date() default "";

	String description() default "";

	String issue() default "";
}
