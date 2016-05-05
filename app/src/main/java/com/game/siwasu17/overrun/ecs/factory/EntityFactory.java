package com.game.siwasu17.overrun.ecs.factory;

import com.game.siwasu17.overrun.ecs.component.MoveComponent;
import com.game.siwasu17.overrun.ecs.component.RenderComponent;
import com.game.siwasu17.overrun.ecs.entity.Entity;
import com.game.siwasu17.overrun.ecs.manager.EntityManager;

/**
 * Created by yasu on 16/03/16.
 */
public class EntityFactory {
    EntityManager entityManager;

    public EntityFactory(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Entity createHumanPlayer() {
        //画像の設定など

        Entity entity = entityManager.createEntity();
        //細かいパラメータはここで設定する

        entityManager.addComponentToEntity(new RenderComponent(0,0), entity);
        entityManager.addComponentToEntity(new MoveComponent(), entity);

        return entity;
    }
}
