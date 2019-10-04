package com.stefan.classscheduleforusc.data.bean;

import java.util.ArrayList;

/**
 * Created by stefan on 19-10-04.
 * used in network
 */

public class CourseTime {
    public ArrayList<String> years = new ArrayList<>();
    public ArrayList<String> terms = new ArrayList<>();
    public String selectYear;
    public String selectTerm;

    @Override
    public String toString() {
        return "CourseTime{" +
                "years=" + years +
                ", terms=" + terms +
                ", selectYear='" + selectYear + '\'' +
                ", selectTerm='" + selectTerm + '\'' +
                '}';
    }
}
