package com.taskmanager.model;


/**
 * A class that represents an action that can be undone and redone
 */
public class Action {
    /**
     * The description of the action
     */
    private String desc;
    /**
     * The id of the user that performed the action
     */
    private long userid;


    /**
     * The undo and redo runnables
     */
    private Runnable undo;
    /**
     * The redo runnable
     */
    private Runnable  redo;


    /**
     * Creates a new action
     * @param desc the description of the action
     * @param user the id of the user that performed the action
     * @param undo the undo runnable
     * @param redo the redo runnable
     */
    public Action (String desc, long user,Runnable undo,Runnable redo )
    {
        setUndo(undo);
        setRedo(redo);
        setUser(user);
        setDesc(desc);
    }

    /**
     * Undoes the action
     */
    public void undo(){

        this.undo.run();
    }

    /**
     * Redoes the action
     */
    public void redo(){
        this.redo.run();
    }

    /**
     * Gets the id of the user that performed the action
     * @return the id of the user that performed the action
     */
    public long getUserId() {
        return userid;
    }

    /**
     * Gets the description of the action
     * @return the description of the action
     */
    public String getDescription() {
        return desc;
    }

    /**
     * Sets the description of the action
     * @param desc the new description of the action
     */
    private void setDesc(String desc) {
        if (desc == null || desc.trim().isEmpty()){
            throw new IllegalArgumentException("description cant be empty");
        }
        this.desc = desc;
    }

    /**
     * Sets the redo runnable
     * @param runnable the new redo runnable
     */
    public void setRedo(Runnable runnable) {
        if (runnable == null){throw new IllegalArgumentException("runnable cant be null");}
        this.redo = runnable;
    }

    /**
     * Sets the undo runnable
     * @param runnable the new undo runnable
     */
    public void setUndo(Runnable runnable) {
        if (runnable == null){throw new IllegalArgumentException("runnable cant be null");}
        this.undo = runnable;
    }

    /**
     * Sets the id of the user that performed the action
     * @param userid the new id of the user that performed the action
     */
    private void setUser(long userid) {
        this.userid = userid;
    }

}
