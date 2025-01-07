package com.wolf.minimq.domain.service.store.infra;

public interface MappedFileQueue {
    boolean load();
    void checkSelf();
    void shutdown(long interval);
    void destroy();

    boolean isEmpty();

    void setFileMode(int mode);

    /**
     * get or create available MappedFile
     * which available space > messageSize
     *
     * @param messageSize messageSize
     * @return mappedFile
     */
    MappedFile getMappedFileForSize(int messageSize);

    /**
     * get or create mappedFile contains offset
     * @param offset offset
     * @return MappedFile
     */
    MappedFile getMappedFileForOffset(long offset);

    /**
     * get the mappedFile contains the offset
     *
     * @param offset offset
     * @return mappedFile | null
     */
    MappedFile getMappedFileByOffset(long offset);

    /**
     *
     * @return mappedFile | null
     */
    MappedFile getFirstMappedFile();

    /**
     *
     * @return mappedFile | null
     */
    MappedFile getLastMappedFile();


    long getMinOffset();
    long getMaxOffset();

    long getCommitPosition();
    long getFlushPosition();
    long getStoreTimestamp();

    long getUnCommittedSize();
    long getUnFlushedSize();

    boolean flush(int minPages);
    boolean commit(int minPages);

}
