package com.game.siwasu17.overrun.ecs.system;

import com.game.siwasu17.overrun.ecs.component.BaseComponent;
import com.game.siwasu17.overrun.ecs.component.HealthComponent;
import com.game.siwasu17.overrun.ecs.component.RenderComponent;
import com.game.siwasu17.overrun.ecs.entity.BaseEntity;
import com.game.siwasu17.overrun.ecs.manager.EntityManager;

import java.util.Map;
import java.util.Set;

/**
 * Created by yasu on 16/02/14.
 */
public class HealthSystem extends BaseSystem {
    public HealthSystem(EntityManager entityManager) {
        super(entityManager);
    }


    void update() {
        Set<BaseEntity> targetEntities = entityManager.getAllEntitiesPosessingComponentOfClass(HealthComponent.class);
        for (BaseEntity entity : targetEntities) {
            //動作に関連するコンポーネントを取り出す
            HealthComponent health = (HealthComponent) entityManager.getComponentOfClassByEntity(HealthComponent.class, entity);
            RenderComponent render = (RenderComponent) entityManager.getComponentOfClassByEntity(RenderComponent.class, entity);

            //TODO: WIP...

        }
    }
}
