package com.glovoapp.effortlessfaker;

public interface DataProvider<FieldType> {

    FieldType provideData();

}
