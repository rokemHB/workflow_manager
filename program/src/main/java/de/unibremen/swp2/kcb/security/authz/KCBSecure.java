package de.unibremen.swp2.kcb.security.authz;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

/**
 * Annotation to be invoked by Interceptor
 * Nach: http://balusc.omnifaces.org/2013/01/apache-shiro-is-it-ready-for-java-ee-6.html
 * Abruf: 29.01.2020
 *
 * @author Marius
 */
@Inherited
@InterceptorBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface KCBSecure {

}
