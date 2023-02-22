package com.example.customalertdialog.dialogs;

import com.example.customalertdialog.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EarMarkImageSelector {

    private Map<String, Integer> earMarkImages;

    public EarMarkImageSelector() {
        earMarkImages = new HashMap<>();
        earMarkImages.put("Simo Siivola", R.drawable.simon_merkki);
        earMarkImages.put("Kullervo Kullanhuuhtoja", R.drawable.kullervonmerkki);
    }


    public int getEarMarkImage(String owner) {
        if(earMarkImages.containsKey(owner)){
            return earMarkImages.get(owner);
        } else if(owner.length() > 0) {
            return R.drawable.jalanjaljet;
        } else {
            return randomPlaceholderImage();
        }
    }

    public int randomPlaceholderImage() {
        List<Integer> givenList = Arrays.asList(
                R.drawable.poro, R.drawable.korvamerkki, R.drawable.poronummella,
                R.drawable.luontoa, R.drawable.hyttynen
        );
        Random rand = new Random();
        return givenList.get(rand.nextInt(givenList.size()));
    }

}
