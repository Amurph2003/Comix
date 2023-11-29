package com.rit.comix.subsystems.hierarchy;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "totalValue", "numIssues", "parentNode", "value"})
public class ComicBook extends CollectionNode {

    private String title;
    private String issueNumber;
    private LocalDate publicationDate;
    private String[] creators;
    private String[] principleCharacters;
    private String description;
    private double baseValue;
    private int grade;
    private boolean isSlabbed;
    private String[] signatures;
    private boolean isAuthenticated;

    public ComicBook(){super(null);}

    public ComicBook(ComicBook toCopy) {
        super(toCopy.getParentNode());
        this.title = toCopy.title;
        this.issueNumber = toCopy.issueNumber;
        this.publicationDate = toCopy.publicationDate;
        this.creators = toCopy.creators;
        this.principleCharacters = toCopy.principleCharacters;
        this.description = toCopy.description;
        this.baseValue = toCopy.baseValue;
        this.grade = toCopy.grade;
        this.isSlabbed = toCopy.isSlabbed;
        this.signatures = toCopy.signatures;
        this.isAuthenticated = toCopy.isAuthenticated;
    }

    /**
     * @param title
     * @param issueNumber
     * @param publicationDate
     * @param creators
     * @param principleCharacters
     * @param description
     * @param baseValue
     * @param grade
     * @param isSlabbed
     */
    
    public ComicBook(String title, String issueNumber, LocalDate publicationDate, String[] creators, String[] principleCharacters, String description, double baseValue, int grade, boolean isSlabbed, String[] signatures, boolean isAuthenticated){
        super(null);
        this.title = title;
        this.issueNumber = issueNumber;
        this.publicationDate = publicationDate;
        this.creators = creators;
        this.principleCharacters = principleCharacters;
        this.description = description;
        this.baseValue = baseValue;
        this.grade = grade;
        this.isSlabbed = isSlabbed;
        this.signatures = signatures;
        this.isAuthenticated = isAuthenticated;
    }

    /**
     * @param title
     * @param issueNumber
     * @param publicationDate
     * @param creators
     * @param principleCharacters
     * @param description
     * @param baseValue
     * @param grade
     * @param isSlabbed
     * @param volume
     */
    public ComicBook(String title, String issueNumber, LocalDate publicationDate, String[] creators, String[] principleCharacters, String description, double baseValue, int grade, boolean isSlabbed, Volume volume, String[] signatures, boolean isAuthenticated){
        super(volume);
        this.title = title;
        this.issueNumber = issueNumber;
        this.publicationDate = publicationDate;
        this.creators = creators;
        this.principleCharacters = principleCharacters;
        this.description = description;
        this.baseValue = baseValue;
        this.grade = grade;
        this.isSlabbed = isSlabbed;
        this.signatures = signatures;
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public void notify(NodeUpdateEvent collectionNode) {

    }

    public String getTitle() {
        return title;
    }

    public String getIssueNumber() {
        return issueNumber;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public String[] getCreators() {
        return creators;
    }

    public String[] getPrincipleCharacters() {
        return principleCharacters;
    }

    public String getDescription() {
        return description;
    }

    public double getBaseValue() {
        return baseValue;
    }

    public int getGrade() {
        return grade;
    }

    public boolean getIsSlabbed() {
        return isSlabbed;
    }

    public String[] getSignatures() {
        return signatures;
    }

    public boolean getIsAuthenticated() {
        return isAuthenticated;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIssueNumber(String issueNumber) {
        this.issueNumber = issueNumber;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setCreators(String[] creators) {
        this.creators = creators;
    }

    public void setPrincipleCharacters(String[] principleCharacters) {
        this.principleCharacters = principleCharacters;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBaseValue(double baseValue) {
        this.baseValue = baseValue;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setIsSlabbed(boolean isSlabbed) {
        this.isSlabbed = isSlabbed;
    }

    @Override
    public int getNumIssues() {
        return 1;
    }

    @Override
    public double getTotalValue() {
        return getValue();
    }
    
    @Override
    public String toString() {
        return  getNewTabString() + "Title: " + getTitle() + "\n" +
                getNewTabString() + "Issue Number: " + getIssueNumber() + "\n" +
                getNewTabString() + "Publication Date: " + getPublicationDate() + "\n" +
                getNewTabString() + "Creators: " + Arrays.stream(getCreators()).toList() + "\n" +
                getNewTabString() + "Principle Characters: " + Arrays.stream(getPrincipleCharacters()).toList() + "\n" +
                getNewTabString() + "Description: " + getDescription() + "\n" +
                getNewTabString() + "Base Value: " + getBaseValue() + "\n" +
                getNewTabString() + "Grade: " + getGrade() + "\n" +
                getNewTabString() + "Is Slabbed: " + getIsSlabbed();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ComicBook comicBook = (ComicBook) obj;
        return grade == comicBook.grade && title.equals(comicBook.title) && issueNumber.equals(comicBook.issueNumber) && publicationDate.equals(comicBook.publicationDate) && Arrays.equals(creators, comicBook.creators) && Arrays.equals(principleCharacters, comicBook.principleCharacters) && description.equals(comicBook.description);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(title, issueNumber, publicationDate, description, baseValue, grade, isSlabbed);
        result = 31 * result + Arrays.hashCode(creators);
        result = 31 * result + Arrays.hashCode(principleCharacters);
        return result;
    }

    public double getValue() {
        double value = getBaseValue();
        
        switch (grade) {
            case 0: {
                break;
            }
            case 1: {
                value *= .1;
                break;
            }
            default: {
                value = Math.log10(grade) * value;
                break;
            }
        }

        if (isSlabbed) {
            value *= 2;
        }

        if (signatures != null) {
            for (String signature : signatures) {
                value += value*.05;
            }
        }

        if (isAuthenticated) {
            value += value*.2;
        }

        return value;
    }
}