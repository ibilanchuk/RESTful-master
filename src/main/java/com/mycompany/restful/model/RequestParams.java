/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.restful.model;

/**
 *
 * @author ibilanchuk
 */
public class RequestParams {
    private int length;
    private int start;
    private String column;
    private String dir;
    private String value;

    public RequestParams(int length, int start, String column, String dir,String value) {
        this.length = length;
        this.start = start;
        this.column = column;
        this.dir = dir; 
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

}