package net.wohlfart.basic.elements;


/**
 * implemented by objects that need some cleanup to do after they are no longer used
 */
public interface HasResouces {

    /**
     * <p>
     * Called after this Object is no longer in use, frees all resources needed by this renderable.
     * </p>
     */
    void destroy();
}
