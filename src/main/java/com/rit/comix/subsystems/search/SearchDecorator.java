package com.rit.comix.subsystems.search;

public abstract class SearchDecorator implements Search{
    protected boolean firstDecorator;
    protected boolean lastDecorator;
    protected String fieldOpt;
    public Search searchOption;

}
