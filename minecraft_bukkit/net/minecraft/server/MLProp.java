// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode

package net.minecraft.server;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.FIELD})
public @interface MLProp {
    /**
     * Overrides the field name for property key.
     */
    String name() default "";

    /**
     * Adds additional help to top of configuration file.
     */
    String info() default "";

    /**
     * Minimum value allowed if field is a number.
     */
    double min() default Double.NEGATIVE_INFINITY;

    /**
     * Maximum value allowed if field is a number.
     */
    double max() default Double.POSITIVE_INFINITY;
}
