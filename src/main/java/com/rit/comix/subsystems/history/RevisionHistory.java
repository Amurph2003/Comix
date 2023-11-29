package com.rit.comix.subsystems.history;

import java.util.LinkedList;

import org.springframework.stereotype.Component;

import com.rit.comix.subsystems.hierarchy.SearchEntry;

@Component
public class RevisionHistory {
    private static RevisionHistory instance = null;
    private LinkedList<EntryRevision> undoableChanges = null;
    private LinkedList<EntryRevision> undoneChanges = null;
    
    private RevisionHistory() {
        // private constructor
        undoableChanges = new LinkedList<>();
        undoneChanges = new LinkedList<>();
    }

    public static RevisionHistory getInstance() {
        if (instance == null){
            instance = new RevisionHistory();
        }
        return instance;
    }

    public void storeRevision(SearchEntry oldEntry, SearchEntry newEntry){
        if(oldEntry != null){
            oldEntry.getComicBook().setParentNode(null);
            oldEntry.getVolume().setParentNode(null);
            oldEntry.getSeries().setParentNode(null);
            oldEntry.getPublisher().setParentNode(null);
            // oldEntry.getVolume().setChildren(null);
            // oldEntry.getSeries().setChildren(null);
            // oldEntry.getPublisher().setChildren(null);
        }

        if(newEntry != null){
            newEntry.getComicBook().setParentNode(null);
            newEntry.getVolume().setParentNode(null);
            newEntry.getSeries().setParentNode(null);
            newEntry.getPublisher().setParentNode(null);
            // newEntry.getVolume().setChildren(null);
            // newEntry.getSeries().setChildren(null);
            // newEntry.getPublisher().setChildren(null);
        }

        undoableChanges.add(new EntryRevision(oldEntry, newEntry));
    }

    public void updateRevisionListsWhenRestoringUndoable(EntryRevision entryRevision){
        undoableChanges.remove(entryRevision);
        undoneChanges.add(entryRevision);
    }

    public void updateRevisionListsWhenRestoringUndone(EntryRevision entryRevision){
        undoneChanges.remove(entryRevision);
        undoableChanges.add(entryRevision);
    }

    public LinkedList<EntryRevision> getUndoableChanges(){
        return undoableChanges;
    }

    public LinkedList<EntryRevision> getUndoneChanges(){
        return undoneChanges;
    }
}
