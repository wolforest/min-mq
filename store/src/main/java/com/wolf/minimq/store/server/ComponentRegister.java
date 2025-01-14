package com.wolf.minimq.store.server;

import com.wolf.common.convention.service.LifecycleManager;
import com.wolf.minimq.domain.config.StoreConfig;
import com.wolf.minimq.domain.service.store.manager.CommitLogManager;
import com.wolf.minimq.domain.service.store.manager.CommitLogDispatcherManager;
import com.wolf.minimq.domain.service.store.manager.ConsumeQueueManager;
import com.wolf.minimq.domain.service.store.manager.IndexManager;
import com.wolf.minimq.domain.service.store.manager.MessageQueueManager;
import com.wolf.minimq.domain.service.store.manager.MetaManager;
import com.wolf.minimq.domain.service.store.manager.TimerManager;
import com.wolf.minimq.store.domain.commitlog.DefaultCommitLogManager;
import com.wolf.minimq.store.domain.consumequeue.DefaultConsumeQueueManager;
import com.wolf.minimq.store.domain.dispatcher.DefaultCommitLogDispatcherManager;
import com.wolf.minimq.store.domain.index.DefaultIndexManager;
import com.wolf.minimq.store.domain.mq.DefaultMessageQueueManager;
import com.wolf.minimq.store.domain.meta.DefaultMetaManager;
import com.wolf.minimq.store.domain.timer.DefaultTimerManager;
import com.wolf.minimq.store.infra.file.AllocateMappedFileService;
import com.wolf.minimq.store.infra.memory.TransientPool;

public class ComponentRegister {
    private final LifecycleManager manager = new LifecycleManager();

    public static LifecycleManager register() {
        ComponentRegister register = new ComponentRegister();
        StoreContext.register(register);

        return register.execute();
    }

    public LifecycleManager execute() {
        registerMappedFileService();

        registerMeta();
        registerCommitLog();
        registerDispatcher();

        registerConsumeQueue();
        registerIndexService();

        registerMessageQueue();
        registerTimer();

        return this.manager;
    }

    private void registerMeta() {
        MetaManager component = new DefaultMetaManager();
        manager.register(component);
    }

    private void registerCommitLog() {
        CommitLogManager component = new DefaultCommitLogManager();
        manager.register(component);
    }

    private void registerDispatcher() {
        CommitLogDispatcherManager component = new DefaultCommitLogDispatcherManager();
        manager.register(component);
    }

    private void registerConsumeQueue() {
        ConsumeQueueManager component = new DefaultConsumeQueueManager();
        manager.register(component);
    }

    private void registerMessageQueue() {
        MessageQueueManager component = new DefaultMessageQueueManager();
        manager.register(component);
    }

    private void registerIndexService() {
        IndexManager component = new DefaultIndexManager();
        manager.register(component);
    }

    private void registerTimer() {
        TimerManager component = new DefaultTimerManager();
        manager.register(component);
    }

    private void registerMappedFileService() {
        StoreConfig storeConfig = StoreContext.getBean(StoreConfig.class);
        TransientPool transientPool = null;
        if (storeConfig.isEnableTransientPool()) {
            transientPool = new TransientPool(storeConfig.getTransientPoolSize(), storeConfig.getTransientFileSize());
            manager.register(transientPool);
            StoreContext.register(transientPool);
        }

        AllocateMappedFileService component = new AllocateMappedFileService(storeConfig, transientPool);
        manager.register(component);
        StoreContext.register(component);
    }
}
