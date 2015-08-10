package com.example.fahadtariq.ottawatraffic.Parse;

import com.example.fahadtariq.ottawatraffic.models.Model;

import java.util.ArrayList;

public interface StrategyParse {
    public ArrayList<Model> Parse(String parseWhichList, String stringToParse);
}
