package com.game.siwasu17.overrun.ecs.system;

import com.game.siwasu17.overrun.ecs.component.MoveComponent;
import com.game.siwasu17.overrun.ecs.component.RenderComponent;
import com.game.siwasu17.overrun.ecs.entity.Entity;
import com.game.siwasu17.overrun.ecs.manager.EntityManager;

import java.util.Set;

/**
 * Created by yasu on 16/03/24.
 */
public class MoveSystem extends BaseSystem{

    public MoveSystem(EntityManager entityManager) {
        super(entityManager);
    }

    void update() {
        Set<Entity> targetEntities = entityManager.getAllEntitiesPosessingComponentOfClass(MoveComponent.class);
        for (Entity entity : targetEntities) {
            //動作に関連するコンポーネントを取り出す
            MoveComponent move = (MoveComponent) entityManager.getComponentOfClassByEntity(MoveComponent.class,entity);
            RenderComponent render = (RenderComponent) entityManager.getComponentOfClassByEntity(RenderComponent.class, entity);
            if(move == null || render == null){
                return;
            }



        }
    }
}
