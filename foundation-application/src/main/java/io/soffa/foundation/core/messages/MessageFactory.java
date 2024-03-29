package io.soffa.foundation.core.messages;

import io.soffa.foundation.commons.IdGenerator;
import io.soffa.foundation.commons.Mappers;
import io.soffa.foundation.commons.ObjectUtil;
import io.soffa.foundation.core.RequestContext;
import io.soffa.foundation.core.context.RequestContextHolder;
import lombok.SneakyThrows;

import java.util.Map;

public final class MessageFactory {

    private MessageFactory() {
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public static <T> T getPayload(final Message message) {
        if (message.getPayload() == null) {
            return null;
        }
        return (T) Mappers.JSON.deserialize(message.getPayload(), Class.forName(message.getPayloadType()));
    }

    @SneakyThrows
    public static <T> T getPayload(final Message message, Class<T> type) {
        if (type == Void.class) {
            return null;
        }
        return Mappers.JSON.deserialize(message.getPayload(), type);
    }

    public static Message create(String operation, Object payload) {
        byte[] lPayload = null;
        String payloadType = null;
        if (payload != null) {
            lPayload = ObjectUtil.serialize(payload);
            payloadType = payload.getClass().getName();
        }
        RequestContext context = RequestContextHolder.inheritOrCreate();
        context.sync();
        Map<String, String> headers = context.getHeaders();
        return new Message(IdGenerator.shortUUID("msg"), operation, lPayload, payloadType, headers);
    }

    public static Message create(String operation) {
        return create(operation, null);
    }

}
