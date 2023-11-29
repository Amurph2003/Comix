package com.rit.comix.subsystems.hierarchy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "parentNode" })
public abstract class CollectionNode {

    CollectionNode parentNode;
    int newTabCounter;
    String newTabString;

    CollectionNode(CollectionNode parentNode){
        this.parentNode = parentNode;
        this.newTabCounter = 0;
        this.newTabString = "";
    }

    public void notify(NodeUpdateEvent event) {
        ((Subcategory<CollectionNode>) parentNode).handleNodeUpdate(event);
    }

    public CollectionNode getParentNode(){
        return parentNode;
    }

    public void setParentNode(CollectionNode parentNode){
        this.parentNode = parentNode;
    }

    public abstract double getTotalValue();

    public abstract int getNumIssues();

    public int getNewTabCounter() {
        return newTabCounter;
    }

    public void setNewTabCounter(int newTabCounter) {
        this.newTabCounter = newTabCounter;
    }

    public void setNewTabString(){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < newTabCounter; i++) stringBuilder.append("\t");
        newTabString = stringBuilder.toString();
    }

    public String getNewTabString() {
        return newTabString;
    }
}
