package com.rit.comix.subsystems.hierarchy;

public class Volume extends Subcategory<ComicBook>{

    private int volumeNumber;

    public Volume(){super(null);}

    public Volume(Volume toCopy){
        super(null);
        this.volumeNumber = toCopy.getVolumeNumber();
    }

    public Volume(int volumeNumber, Series series){
        super(series);
        this.volumeNumber = volumeNumber;
    }

    public int getVolumeNumber() {
        return volumeNumber;
    }

    public void setVolumeNumber(int volumeNumber) {
        this.volumeNumber = volumeNumber;
    }

    @Override
    public boolean equals(Object compared){
        if (compared instanceof Volume)
            return volumeNumber == ((Volume)compared).volumeNumber;
        return false;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(getNewTabString() + "Volume \"" + getVolumeNumber() + "\":");

        if (getChildren() == null) return stringBuilder.toString();

        for (ComicBook comicBook : getChildren()){
            stringBuilder.append("\n");
            comicBook.setNewTabCounter(this.getNewTabCounter()+1);
            comicBook.setNewTabString();
            stringBuilder.append(comicBook.toString());
        }

        return stringBuilder.toString();
    }
}
