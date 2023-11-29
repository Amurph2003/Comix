package com.rit.comix.subsystems.history;

import com.rit.comix.subsystems.hierarchy.SearchEntry;

import ch.qos.logback.core.joran.conditional.ElseAction;

public class EntryRevision {
    private SearchEntry oldEntry;
    private SearchEntry newEntry;
    
    public EntryRevision(SearchEntry oldEntry, SearchEntry newEntry) {
        this.oldEntry = oldEntry;
        this.newEntry = newEntry;
    }

    public SearchEntry getOldEntry() {
        return oldEntry;
    }

    public SearchEntry getNewEntry() {
        return newEntry;
    }

    // public void updateRevisionListsWhenRestoring(EntryRevision entryRevision) {
    // }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Old Entry:");
        if(oldEntry == null){
            stringBuilder.append("null");
        } else {
            stringBuilder.append(oldEntry.toString());
        }
        stringBuilder.append("\n\nNew Entry:\n");
        if(newEntry == null){
            stringBuilder.append("null");
        } else {
            stringBuilder.append(newEntry.toString());
        }
        return stringBuilder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((oldEntry == null) ? 0 : oldEntry.hashCode());
        result = prime * result + ((newEntry == null) ? 0 : newEntry.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EntryRevision other = (EntryRevision) obj;
        if (oldEntry == null) {
            if (other.oldEntry != null)
                return false;
        } else if (!oldEntry.equals(other.oldEntry))
            return false;
        if (newEntry == null) {
            if (other.newEntry != null)
                return false;
        } else if (!newEntry.equals(other.newEntry))
            return false;
        return true;
    }
}