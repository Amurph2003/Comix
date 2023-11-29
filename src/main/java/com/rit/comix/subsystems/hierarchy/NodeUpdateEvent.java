package com.rit.comix.subsystems.hierarchy;

public class NodeUpdateEvent {
    private CollectionNode node;

    public NodeUpdateEvent() {this.node = null;}

    public NodeUpdateEvent(CollectionNode node) {this.node = node;}

    public CollectionNode getNode() {
        return node;
    }
}
