package com.example.dms.service;

import com.example.dms.domain.Document;

import java.util.Comparator;

public class FinishComparator implements Comparator<Document> {
 
    public int compare(Document a, Document b){
        //if (a != null || b != null) {
        return a.getFinish().compareTo(b.getFinish()); }
        //return 0;
  //  }
}