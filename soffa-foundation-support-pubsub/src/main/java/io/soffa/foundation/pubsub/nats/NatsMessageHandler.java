package io.soffa.foundation.pubsub.nats;

import io.nats.client.Message;
import io.nats.client.MessageHandler;
import io.soffa.foundation.commons.Logger;
import io.soffa.foundation.commons.ObjectUtil;
import io.soffa.foundation.commons.TextUtil;
import io.soffa.foundation.errors.ManagedException;
import io.soffa.foundation.model.OperationResult;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NatsMessageHandler implements MessageHandler {

    private static final Logger LOG = Logger.get(NatsMessageHandler.class);

    private final io.soffa.foundation.pubsub.MessageHandler handler;

    @Override
    public void onMessage(Message msg) {
        boolean hasReply = TextUtil.isNotEmpty(msg.getReplyTo());
        try {
            io.soffa.foundation.model.Message message = ObjectUtil.deserialize(msg.getData(), io.soffa.foundation.model.Message.class);
            OperationResult response = OperationResult.create(handler.handle(message).orElse(null), null);
            if (hasReply) {
                msg.getConnection().publish(msg.getReplyTo(), ObjectUtil.serialize(response));
            }
        } catch (Exception e) {
            LOG.error("Nats event handling failed with error", e);
            if (e instanceof ManagedException) {
                if (hasReply) {
                    msg.getConnection().publish(msg.getReplyTo(), ObjectUtil.serialize(OperationResult.create(null, e)));
                }
            } else {
                throw e;
            }
        }
    }

}
