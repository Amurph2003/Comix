package com.rit.comix.subsystems.hierarchy;

public class Publisher extends Subcategory<Series>{

    private String publisherName;

    public Publisher(){super(null);}

    public Publisher(Publisher toCopy) {
        super(null);
        this.publisherName = toCopy.getPublisherName();
    }

    public Publisher(String publisherName, ComixCollection parentNode){
        super(parentNode);
        this.publisherName = publisherName;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    @Override
    public boolean equals(Object compared){
        if (compared instanceof Publisher)
            return publisherName.equals(((Publisher)compared).publisherName);
        return false;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(getNewTabString() + "Publisher \"" + getPublisherName() + "\":");

        if (getChildren() == null) return stringBuilder.toString();

        for (Series series : getChildren()){
            stringBuilder.append("\n");
            series.setNewTabCounter(this.getNewTabCounter()+1);
            series.setNewTabString();
            stringBuilder.append(series.toString());
        }

        return stringBuilder.toString();
    }
}