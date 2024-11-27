package com.ugf360.dbdemo.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;

public interface IOService {
 
    public Iterator<Row> readFromSource();

    public void writeToSource();

    protected Map<String, Integer> getHeader();

    protected void setHeader();
    
}
