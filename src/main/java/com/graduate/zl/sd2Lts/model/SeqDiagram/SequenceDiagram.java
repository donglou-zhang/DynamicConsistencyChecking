package com.graduate.zl.sd2Lts.model.SeqDiagram;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class SequenceDiagram {

    @Getter @Setter
    private Map<String, Lifeline> lifelines;

    @Getter @Setter
    private Map<String,  OccurrenceSpecificationFragment> osFragments;

    @Getter @Setter
    private Map<String, CombinedFragment> combinedFragments;

    @Getter @Setter
    private Map<String, Message> messages;

    public SequenceDiagram() {

    }


}