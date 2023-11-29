package com.rit.comix.subsystems.hierarchy;

public class SearchEntry {
    private ComicBook comicBook;
    private Publisher publisher;
    private Series series;
    private Volume volume;

    /**
     * @param comicBook The comicbook searched
     * @param publisher The publisher of the comicbook
     * @param series Thes eries of the comicbook
     * @param volume The volume of the comicbook
     */
    public SearchEntry() {}

    public SearchEntry(ComicBook comicBook, Publisher publisher, Series series, Volume volume) {
        this.comicBook = comicBook;
        this.publisher = publisher;
        this.series = series;
        this.volume = volume;
    }

    public SearchEntry(SearchEntry searchEntry){
        this.publisher = new Publisher(searchEntry.getPublisher().getPublisherName(), null);
        this.series = new Series(searchEntry.getSeries().getSeriesNumber(), null);
        this.volume = new Volume(searchEntry.getVolume().getVolumeNumber(), null);
        this.comicBook = new ComicBook(searchEntry.getComicBook().getTitle(), searchEntry.getComicBook().getIssueNumber(), searchEntry.getComicBook().getPublicationDate(), searchEntry.getComicBook().getCreators(), searchEntry.getComicBook().getPrincipleCharacters(), searchEntry.getComicBook().getDescription(), searchEntry.getComicBook().getBaseValue(), searchEntry.getComicBook().getGrade(), searchEntry.getComicBook().getIsSlabbed(), null, searchEntry.getComicBook().getSignatures(), searchEntry.getComicBook().getIsAuthenticated());
    }

    public ComicBook getComicBook() {
        return comicBook;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public Series getSeries() {
        return series;
    }

    public Volume getVolume() {
        return volume;
    }

    public void setComicBook(ComicBook comicBook) {
        this.comicBook = comicBook;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public void setVolume(Volume volume) {
        this.volume = volume;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SearchEntry) {
            return this.comicBook.equals(((SearchEntry) obj).comicBook)
                    && this.volume.equals(((SearchEntry) obj).volume)
                    && this.publisher.equals(((SearchEntry) obj).publisher)
                    && this.series.equals(((SearchEntry) obj).series);
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        publisher.setNewTabCounter(0);
        publisher.setNewTabString();
        
        stringBuilder.append("\n" + publisher.toString());

        series.setNewTabCounter(1);
        series.setNewTabString();

        stringBuilder.append("\n" + series.toString());

        volume.setNewTabCounter(2);
        volume.setNewTabString();

        stringBuilder.append("\n" + volume.toString());

        comicBook.setNewTabCounter(3);
        comicBook.setNewTabString();

        stringBuilder.append("\n" + comicBook.toString());

        return stringBuilder.toString();
    }
}
