package com.rit.comix.subsystems.hierarchy;

public class Series extends Subcategory<Volume>{

    private String seriesNumber;

    public Series(){super();}

    public Series(Series toCopy) {
        super(null);
        this.seriesNumber = toCopy.getSeriesNumber();
    }

    public Series(String seriesNumber, Publisher publisher){
        super(publisher);
        this.seriesNumber = seriesNumber;
    }

    public String getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(String seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    @Override
    public boolean equals(Object compared){
        if (compared instanceof Series)
            return seriesNumber.equals(((Series)compared).seriesNumber);
        return false;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(getNewTabString() + "Series \"" + getSeriesNumber() + "\":");

        if (getChildren() == null) return stringBuilder.toString();

        for (Volume volume : getChildren()){
            stringBuilder.append("\n");
            volume.setNewTabCounter(this.getNewTabCounter()+1);
            volume.setNewTabString();
            stringBuilder.append(volume.toString());
        }

        return stringBuilder.toString();
    }
}
