package com.game.siwasu17.overrun.ecs.factory;

import com.game.siwasu17.overrun.ecs.component.InputComponent;
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

    /**
     * 未完了のタスク
     *  Render, Input
     * @return
     */
    public Entity createTodoTask() {
        Entity entity = entityManager.createEntity();

        //初期状態
        //TODO: マジックナンバー化しているので、定数分離できそうならあとでやる
        entityManager.addComponentToEntity(new RenderComponent(-1, -1), entity);
        entityManager.addComponentToEntity(new InputComponent(),entity);
        return entity;
    }


    /**
     * 完了したタスク
     *  Render, Input, Move
     * @return
     */
    public Entity createCompleteTask(Entity todoTask) {
        Entity entity = entityManager.createEntity();

        //TODO: 未完了タスクの情報をコピーする

        return entity;
    }

    /*
    public Entity createHumanPlayer() {
        //画像の設定など

        Entity entity = entityManager.createEntity();
        //細かいパラメータはここで設定する

        entityManager.addComponentToEntity(new RenderComponent(0, 0), entity);
        entityManager.addComponentToEntity(new MoveComponent(), entity);

        return entity;
    }
    */
}
