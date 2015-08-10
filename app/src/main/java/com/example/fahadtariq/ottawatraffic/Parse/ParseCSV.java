package com.example.fahadtariq.ottawatraffic.Parse;

import com.example.fahadtariq.ottawatraffic.models.Model;
import com.example.fahadtariq.ottawatraffic.models.RingPost;

import java.util.ArrayList;

public class ParseCSV implements StrategyParse {

    @Override
    public ArrayList<Model> Parse(String parseWhichList, String stringToParse) {
        ArrayList<Model> list = new ArrayList<Model>();

        // Skip the first row since it only contains the column headings
        int i = 1;

        // Separate all the rows from file
        String[] lines = stringToParse.split("\n");

        // Loop through all rows and place markers
        while(lines.length > i)
        {
            RingPost rp = new RingPost();
            String[] line = lines[i].split(",");

            if (line.length > 8) {
                if (line[0] != null) rp.setId(Integer.parseInt(line[0]));
                if (line[1] != null) rp.setMidBlockId(Integer.parseInt(line[1]));
                if (line[2] != null) rp.setStreet1((String) (line[2]));
                if (line[3] != null) rp.setStreet2((String) (line[3]));
                if (line[4] != null) rp.setStreet3((String) (line[4]));
                if (line[5] != null) rp.setSide((String) (line[5]));
                if (line[6] != null) rp.setAdjacentTo((String) (line[6]));
                if (line[7] != null) rp.setLatitude(Double.parseDouble(line[7]));
                if (line[8] != null) rp.setLongitude(Double.parseDouble(line[8]));
                if (line.length > 9 && line[9] != null) rp.setCore((String) (line[9]));
                if (line.length > 10 && line[10] != null) rp.setNotes((String) (line[10]));

                list.add(rp);
            }

            i++;
        }
        return list;
    }
}
