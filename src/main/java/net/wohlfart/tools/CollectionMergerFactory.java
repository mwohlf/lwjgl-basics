package net.wohlfart.tools;

import java.util.Collection;
import java.util.HashSet;

import net.wohlfart.basic.elements.IsRenderable;

import org.springframework.beans.factory.FactoryBean;

public class CollectionMergerFactory implements FactoryBean<Collection<IsRenderable>> {
    private final Collection<IsRenderable> set = new HashSet<IsRenderable>();

    public CollectionMergerFactory(Collection<Collection<IsRenderable>> items) {
        for (final Collection<IsRenderable> item : items) {
            if (item != null) {
                set.addAll(item);
            }
        }
    }

    @Override
    public Collection<IsRenderable> getObject() throws Exception {
        return set;
    }

    @Override
    public Class getObjectType() {
        return set.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
