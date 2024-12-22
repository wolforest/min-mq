package com.wolf.minimq.store.domain.message;

import com.wolf.minimq.domain.enums.EnqueueStatus;
import com.wolf.minimq.domain.lock.TopicQueueLock;
import com.wolf.minimq.domain.model.Message;
import com.wolf.minimq.domain.service.store.domain.CommitLog;
import com.wolf.minimq.domain.service.store.domain.ConsumeQueue;
import com.wolf.minimq.domain.service.store.domain.MessageStore;
import com.wolf.minimq.domain.vo.EnqueueResult;
import com.wolf.minimq.domain.vo.MessageContext;
import com.wolf.minimq.store.server.StoreContext;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultMessageStore implements MessageStore {
    private final TopicQueueLock topicQueueLock;

    public DefaultMessageStore() {
        this.topicQueueLock = new TopicQueueLock();
    }

    /**
     * enqueue single/multi message
     *  - assign consumeQueue offset
     *  - append commitLog
     *  - increase consumeQueue offset
     *
     * @param context messageContext
     * @return EnqueueResult
     */
    @Override
    public EnqueueResult enqueue(MessageContext context) {
        return null;
    }

    @Override
    public CompletableFuture<EnqueueResult> enqueueAsync(MessageContext context) {
        String topicKey = getTopicKey(context);

        topicQueueLock.lock(topicKey);
        try {
            ConsumeQueue consumeQueue = StoreContext.getBean(ConsumeQueue.class);
            consumeQueue.assignOffset(context);

            CommitLog commitLog = StoreContext.getBean(CommitLog.class);
            EnqueueResult result = commitLog.append(context);

            if (result.isSuccess()) {
                consumeQueue.increaseOffset(context);
            }
        } catch (Exception e) {
            return CompletableFuture.completedFuture(new EnqueueResult(EnqueueStatus.UNKNOWN_ERROR));
        } finally {
            topicQueueLock.unlock(topicKey);
        }

        return null;
    }

    private String getTopicKey(MessageContext context) {
        Message message = context.getMessageList().getFirst();
        return message.getTopic() + '-' + context.getQueueId();
    }
}
