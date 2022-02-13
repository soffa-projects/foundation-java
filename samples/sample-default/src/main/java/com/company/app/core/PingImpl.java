package com.company.app.core;

import io.soffa.foundation.context.RequestContext;
import io.soffa.foundation.exceptions.FakeException;
import io.soffa.foundation.model.TenantId;
import org.jetbrains.annotations.NotNull;

import javax.inject.Named;


@Named
public class PingImpl implements Ping {

    public static final TenantId T2 = new TenantId("T2");

    @Override
    public PingResponse handle(Void arg, @NotNull RequestContext context) {
        if (T2.equals(context.getTenantId())) {
            throw new FakeException("Controlled error triggered (%s)", context.getTenantId());
        } else {
            return new PingResponse("PONG");
        }
    }

}