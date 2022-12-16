package com.example.myattendence2;

public class classitem {
    long cid;
    String classname;
    String subjectname;

    public classitem(long cid, String classname, String subjectname) {
        this.cid = cid;
        this.classname = classname;
        this.subjectname = subjectname;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public classitem(String classname, String subjectname) {
        this.classname = classname;
        this.subjectname = subjectname;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }
}
