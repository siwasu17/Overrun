package com.game.siwasu17.overrun.ecs.entity;

import com.game.siwasu17.overrun.ecs.component.BaseComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yasu on 16/02/14.
 */
public class BaseEntity {
    public long eid;
    public BaseEntity(long eid) {
        this.eid = eid;
    }

}
