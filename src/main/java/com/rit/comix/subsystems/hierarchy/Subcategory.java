package com.rit.comix.subsystems.hierarchy;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "parentNode" })
public abstract class Subcategory<N extends CollectionNode> extends CollectionNode{

    private List<N> children;
    private int numIssues;
    private double totalValue;

    public Subcategory(){
        super(null);
        this.children = new ArrayList<N>();
        numIssues = 0;
        totalValue = 0;
    }

    public Subcategory(CollectionNode parentNode){
        super(parentNode);
        children = new ArrayList<N>();
        numIssues = 0;
        totalValue = 0;
    }

    public void add(N node){
        if(children != null){
            children.add(node);
        } else {
            this.children = new ArrayList<N>();
            children.add(node);
        }
        handleNodeUpdate(null);
    }

    public void remove(int nodeIndex){
        children.remove(nodeIndex);
        handleNodeUpdate(null);
    }

    public void remove(N node){
        children.remove(node);
        handleNodeUpdate(null);
    }

    //Depending on the UI setup, we may be able to remove update methods in favor of the ComicBook setters

    public void update(int nodeIndex, N newNode){
        children.set(nodeIndex, newNode);
        handleNodeUpdate(null);
    }

    public N getChildNode(int nodeIndex){
        return children.get(nodeIndex);
    }

    public List<N> getChildren(){
        return children;
    }

    @JsonIgnore
    public List<ComicBook> getComics() {
        List<ComicBook> comics = new ArrayList<>();
        if(this.children != null){
            for (CollectionNode node : this.children) {
                if (node instanceof ComicBook) {
                    comics.add((ComicBook)node);
                }
                else {
                    //Implicit Nested for loop to get all comics below this node
                    comics.addAll(((Subcategory<CollectionNode>) node).getComics());
                }
            }
        }
        return comics;
    }

    public CollectionNode getParentNode(){
        return super.getParentNode();
    }

    protected void handleNodeUpdate(NodeUpdateEvent event) {
        refreshNumIssues();
        refreshTotalValue();
        if (parentNode != null) {
            ((Subcategory<CollectionNode>) parentNode).handleNodeUpdate(new NodeUpdateEvent(this));
        }
        else if (this instanceof ComixCollection) {
            //Use ComixCollection's method to handle SearchList overwrite
            //I understand how horrible this is
            ((ComixCollection) this).repopulateSearchList();
        }
        return;
    }

    private void refreshNumIssues() {
        numIssues = this.getComics().size();
    }

    private void refreshTotalValue() {
        totalValue = 0;
        for (N child : children) {
            totalValue += child.getTotalValue();
        }
    }

    public void setChildren(List<N> children) {
        this.children = children;
    }

    @Override
    public int getNumIssues() {
        return numIssues;
    }

    @Override
    public double getTotalValue() {
        return totalValue;
    }

    
}
