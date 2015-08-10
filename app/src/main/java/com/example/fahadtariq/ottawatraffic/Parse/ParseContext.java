package com.example.fahadtariq.ottawatraffic.Parse;

import com.example.fahadtariq.ottawatraffic.models.Model;

import java.util.ArrayList;

public class ParseContext {
    private StrategyParse strategy;

    public void setStrategyParse(StrategyParse strategy) {
        this.strategy = strategy;
    }

    public StrategyParse getStrategy() {
        return strategy;
    }

    public ParseContext(StrategyParse strategy) {
        this.strategy = strategy;
    }

    public ArrayList<Model> ParseString(String parseWhichList, String stringToParse) {
        return strategy.Parse(parseWhichList, stringToParse);
    }
}
