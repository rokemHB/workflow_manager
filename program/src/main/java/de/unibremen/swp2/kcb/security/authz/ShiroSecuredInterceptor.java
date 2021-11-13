package de.unibremen.swp2.kcb.security.authz;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 *  Interceptor to invoke Shiro Annotations
 *
 *  See also: http://balusc.omnifaces.org/2013/01/apache-shiro-is-it-ready-for-java-ee-6.html
 *  Access: 29.01.2020
 *
 * @author Marius
 */
@Interceptor
@KCBSecure
public class ShiroSecuredInterceptor implements Serializable {

    /**
     * Logger object of the KCBRealm class
     */
    private static final Logger logger = LogManager.getLogger(ShiroSecuredInterceptor.class);

    /**
     * SerialVersion
     */
    private static final long serialVersionUID = 1L;

    /**
     * Check for Shiro Annotations RequiresAuthentication and RequiresRoles before function invokation
     *
     * @param context of invokation
     * @return context.proceed()
     * @throws Exception if annotation failed
     */
    @AroundInvoke
    public Object interceptShiroSecurity(InvocationContext context) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        Class<?> c = context.getTarget().getClass();
        Method m = context.getMethod();

        // Check if subject tries to access @RequiresAuthentication protected method without being logged in.
        if (!subject.isAuthenticated() && hasAnnotation(c, m, RequiresAuthentication.class)) {
            logger.debug("Unauthorized method call. Method {} requires authentication.", m.getName());
            throw new UnauthenticatedException("Unauthorized. Method: " + m.getName() + " requires authentication.");
        }

        if (hasAnnotation(c, m, RequiresRoles.class)) {
            final RequiresRoles annotation = getAnnotation(c, m, RequiresRoles.class);

            // Roles given to the annotation. Should not be empty.
            final String[] roles = annotation.value();

            // Logical the roles are linked with. Default is AND
            final Logical logical = annotation.logical();

            if (roles.length == 0) {
                logger.warn("RequiresRole Annotation without roles found for method: {}. Will allow execution.", m.getName());
                return context.proceed();
            }

            try {
                if (logical == Logical.OR)
                    checkOneRole(subject, roles);
                else
                    checkAllRoles(subject, roles);
            } catch (AuthorizationException e) {
                logger.debug("Subject {} is missing a required role for method: {}", subject.getPrincipal(), m.getName());
                // Only works with servlet response. May cause problems with REST API
                HttpServletResponse response = WebUtils.getHttpResponse(subject);
                if (response == null) {
                    logger.debug("Couldn't get request for subject " + subject.getPrincipal()
                            + ". Execution will be stopped for method: " + m.getName());
                    return null;
                }
                logger.debug("Subject " + subject.getPrincipal() + " unauthorized for method: " + m.getName()
                    + ". Unauthorized Error will be send to subject and execution will be stopped.");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return null;
            }
        }
        return context.proceed();
    }

    /**
     * Assert all given roles are owned by the subject. Will throw AuthorizationException if not.
     *
     * @param subject to check rules of
     * @param roles to be checked
     * @throws AuthorizationException if subject doesn't own all roles.
     */
    private static void checkAllRoles(Subject subject, String[] roles) throws AuthorizationException {
        subject.checkRoles(Arrays.asList(roles));
    }

    /**
     * Assert subject owns one of the given roles. Will throw AuthorziationException if not.
     *
     * @param subject to check rules of
     * @param roles to be checked
     * @throws AuthorizationException if subject doesn't own one of the given roles.
     */
    private static void checkOneRole(Subject subject, String[] roles) throws AuthorizationException {
        for (String role : roles) {
            if (subject.hasRole(role))
                return;
        }
        throw new AuthorizationException("Subject owns non of the given roles.");
    }

    /**
     * Check if the given annotation is present on class, method or superclass
     *
     * @param c to be checked for annotation
     * @param m to be checked for annotation
     * @param a to check for
     * @return is the annotation present?
     */
    private static boolean hasAnnotation(Class<?> c, Method m, Class<? extends Annotation> a) {
        return m.isAnnotationPresent(a)
                || c.isAnnotationPresent(a)
                || c.getSuperclass().isAnnotationPresent(a);
    }

    /**
     * Return annotation of method, class or superclass, if present
     *
     * @param c class to get annotation of
     * @param m method to get annotation of
     * @param a class of annotation to be returned
     * @param <A> Annotation of given class on c, m or superclass of c
     * @return
     */
    private static <A extends Annotation> A getAnnotation(Class<?> c, Method m, Class<A> a) {
        return m.isAnnotationPresent(a) ? m.getAnnotation(a)
                : c.isAnnotationPresent(a) ? c.getAnnotation(a)
                : c.getSuperclass().getAnnotation(a);
    }

}
