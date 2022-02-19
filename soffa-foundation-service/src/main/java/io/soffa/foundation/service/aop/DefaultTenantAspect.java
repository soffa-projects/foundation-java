package io.soffa.foundation.service.aop;

import io.soffa.foundation.annotations.DefaultTenant;
import io.soffa.foundation.context.TenantHolder;
import io.soffa.foundation.model.TenantId;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
@Order(DefaultTenantAspect.ORDER)
public class DefaultTenantAspect {

    public static final int ORDER = SecurityAspect.ORDER + 100;

    @SneakyThrows
    @Around("@annotation(arg)")
    public Object handle(ProceedingJoinPoint pjp, DefaultTenant arg) {
        final String currentTenant = TenantHolder.get().orElse(null);
        try {
            TenantHolder.set(TenantId.DEFAULT);
            return pjp.proceed(pjp.getArgs());
        } finally {
            TenantHolder.set(currentTenant);
        }
    }

}
