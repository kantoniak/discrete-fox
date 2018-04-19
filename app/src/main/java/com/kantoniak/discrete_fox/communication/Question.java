package com.kantoniak.discrete_fox.communication;

import com.kantoniak.discrete_fox.scene.Country;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Question {
    private final String mlink;
    private HashMap<String, Integer> ans;
    private HashMap<String, Double> ansDouble;
    public static final int NUMBEROFCOUNTRIES = 5;

    double midThres;
    double highThres;
    String mdesc;
    List<String> mcountries;
    String munit;
    QuestionCategory mcategory;

    Question(String link, HashMap<String, Double> data, String desc, List<String> countries, String unit, QuestionCategory category) {
        mcategory = category;
        mcountries = countries;
        mdesc = desc;
        mlink = link;
        munit = unit;
        ansDouble = new HashMap<>();
        ArrayList<Double> valueList = new ArrayList<>();
        Iterator it = data.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Double val = (Double)pair.getValue();
            valueList.add(val);
            ansDouble.put((String)pair.getKey(), val);
        }
        createThresholds(valueList);
        createAnswers(ansDouble);
    }

    void createThresholds(ArrayList<Double> valueList) {
        double mid = 0;
        double high = 0;

        double mini = Collections.min(valueList);
        double maxi = Collections.max(valueList);

        mid = mini + (maxi - mini) / 3;
        high = mid + (maxi - mini) / 3;

        setThreshold(mid, high);
    }

    public String getDesc() {
        return mdesc;
    }

    void createAnswers(HashMap<String, Double> data) {
        ans = new HashMap<>();
        Iterator it = data.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Double val = (Double)pair.getValue();
            int l = 1;
            if (val > highThres) {
                l = 3;
            } else if (val > midThres) {
                l = 2;
            }
            ans.put((String)pair.getKey(), l);
        }
    }

    void setThreshold(double mid, double high) {
        midThres = Math.round(mid * 100.0) / 100.0;
        highThres = Math.round(high * 100.0) / 100.0;
    }

    public Integer getCorrectAnswer(String country) {
        return ans.get(country);
    }

    public int getMminColor() {
        return mcategory.getMinColor();
    }

    public int getMmaxColor() {
        return mcategory.getMaxColor();
    }

    public List<String> getCountries() {
        return mcountries;
    }

    public String getMminLabel() { return "<" + midThres + munit; }
    public String getMmidLabel() { return midThres + munit + " - " + highThres + munit; }
    public String getMmaxLabel() { return ">" + highThres + munit; }
    public HashMap<String, Double> getAnsDouble() {
        return ansDouble;
    }

    public String getUnit() {
        return munit;
    }
}
