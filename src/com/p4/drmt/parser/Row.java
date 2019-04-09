package com.p4.drmt.parser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Row {
	RowType type();
	String output();
	boolean alwaysByte() default false; 
	boolean reverse() default false;
	boolean noByte() default false;
}
