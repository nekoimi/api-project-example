package com.nekoimi.boot.common.annotaction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * nekoimi  2020/7/2 下午2:39
 *
 *   注解作用目标:
 *     TYPE,                类、接口（包括注释类型）或枚举声明
 *     FIELD,               字段声明（包括枚举常量）
 *     METHOD,              方法声明
 *     PARAMETER,           方法参数声明
 *     CONSTRUCTOR,         构造方法声明
 *     LOCAL_VARIABLE,      局部变量声明
 *     ANNOTATION_TYPE,     注释类型声明
 *     PACKAGE,             包声明
 *     TYPE_PARAMETER,      标明注解可以用于类型参数声明
 *     TYPE_USE,            类型使用声明
 *     MODULE;              包
 *
 * 注入当前请求用户注解 用于方法参数
 * 自动注入HttpContext中用户
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Operator {
}
