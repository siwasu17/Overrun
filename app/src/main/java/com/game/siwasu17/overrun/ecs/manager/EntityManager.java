package com.game.siwasu17.overrun.ecs.manager;

import com.game.siwasu17.overrun.ecs.component.BaseComponent;
import com.game.siwasu17.overrun.ecs.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by yasu on 16/02/14.
 */
public class EntityManager {

    Map<Long, Entity> entityRepository;
    //そのコンポーネントを持つEntityのリストを返すためのデータ構造
    Map<Class, Map<Entity, BaseComponent>> componentsByClass;
    long lowestUnassignedEid;

    public EntityManager() {
        this.entityRepository = new HashMap<>();
        this.componentsByClass = new HashMap<>();
        this.lowestUnassignedEid = 1;
    }

    long generateNewEid() throws ArrayIndexOutOfBoundsException {
        if (lowestUnassignedEid < Long.MAX_VALUE) {
            return lowestUnassignedEid++;
        } else {
            //溢れたら空いているところを探す
            for (long i = 1; i < Long.MAX_VALUE; ++i) {
                if (!entityRepository.containsKey(i)) {
                    return i;
                }
            }

            //通常無いはずだが最大値までオブジェクト生成してしまったら例外とする
            throw new ArrayIndexOutOfBoundsException("Eid exhaustion!!");
        }
    }

    public Entity createEntity() {
        long eid = generateNewEid();
        Entity entity = new Entity(eid);
        entityRepository.put(eid, entity);
        return entity;
    }

    public void removeEntity(Entity entity) {

        //該当するEntityの情報を消しているだけ
        for (Map.Entry<Class, Map<Entity, BaseComponent>> entry : componentsByClass.entrySet()) {
            BaseComponent c = getComponentOfClassByEntity(entry.getKey(), entity);
            if (c != null) {
                entry.getValue().remove(entity);
            }
        }

        entityRepository.remove(entity.eid);
    }

    public void addComponentToEntity(BaseComponent component, Entity entity) {
        Class clazz = component.getClass();

        Map<Entity, BaseComponent> componentMap;
        if (componentsByClass.containsKey(clazz)) {
            componentMap = componentsByClass.get(clazz);
        } else {
            //初めて見るクラスの場合
            componentMap = new HashMap<>();
        }

        componentMap.put(entity, component);
        componentsByClass.put(clazz, componentMap);
    }

    //あるコンポーネントを保持しているエンティティがいたら、保持しているコンポーネントを返す
    public BaseComponent getComponentOfClassByEntity(Class clazz, Entity entity) {
        Map<Entity, BaseComponent> e2c = componentsByClass.get(clazz);
        if (e2c != null) {
            return e2c.get(entity);
        }
        return null;
    }

    //特定のコンポーネントを持つエンティティ群を返す
    public Set<Entity> getAllEntitiesPosessingComponentOfClass(Class clazz) {
        return componentsByClass.get(clazz).keySet();
    }
}
