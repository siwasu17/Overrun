package com.game.siwasu17.overrun.ecs.system;

import com.game.siwasu17.overrun.ecs.manager.EntityManager;

/**
 * Created by yasu on 16/02/14.
 */
public class BaseSystem {
    EntityManager entityManager;

    public BaseSystem(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
