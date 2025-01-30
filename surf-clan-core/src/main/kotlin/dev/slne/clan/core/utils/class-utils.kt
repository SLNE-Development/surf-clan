package dev.slne.clan.core.utils

import org.springframework.aop.framework.AopProxyUtils
import org.springframework.core.MethodIntrospector
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.core.annotation.AnnotationUtils
import java.lang.reflect.Method
import kotlin.reflect.KClass

fun Any.ultimateTargetClass() = AopProxyUtils.ultimateTargetClass(this).kotlin

inline fun <reified A : Annotation> KClass<*>.isCandidateFor() =
    AnnotationUtils.isCandidateClass(this.java, A::class.java)

inline fun <reified A : Annotation> KClass<*>.containsMethodWithAnnotation() =
    selectFunctions { it.isAnnotated<A>() }.isNotEmpty()

fun KClass<*>.selectFunctions(predicate: (Method) -> Boolean): MutableSet<Method> =
    MethodIntrospector.selectMethods(java, predicate)

inline fun <reified A : Annotation> Method.isAnnotated() =
    AnnotatedElementUtils.isAnnotated(this, A::class.java)