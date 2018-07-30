package com.graduate.zl.sd2Lts.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Vincent on 2018/7/30.
 */
public class Guard {

    @Getter @Setter
    private String id;

    @Getter @Setter
    private String body;

    public Guard(String id, String body) {
        this.id = id;
        this.body = body;
    }
}
