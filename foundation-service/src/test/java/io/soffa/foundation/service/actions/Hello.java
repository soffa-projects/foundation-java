package io.soffa.foundation.service.actions;

import io.soffa.foundation.annotations.Handle;
import io.soffa.foundation.core.Operation;
import io.soffa.foundation.core.RequestContext;
import io.soffa.foundation.core.models.Ack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@Handle("HELLO")
public class Hello implements Operation<Void, Ack> {

    public static final AtomicInteger TICK = new AtomicInteger(0);

    @Override
    public Ack handle(Void input, @NonNull RequestContext context) {
        TICK.incrementAndGet();
        return Ack.OK;
    }
}
