package com.wolf.minimq.domain.model.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetRequest implements Serializable {
    private String topic;
    private int queueId;
    private long offset;

    @Builder.Default
    private int num = 1;
}
