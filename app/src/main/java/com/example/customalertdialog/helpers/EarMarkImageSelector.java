package com.example.customalertdialog.helpers;

import com.example.customalertdialog.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EarMarkImageSelector {

    private Map<String, Integer> earMarkImages;
    private List<Integer> placeholderImageIds;

    public EarMarkImageSelector() {
        this.placeholderImageIds = Arrays.asList(R.drawable.jalanjaljet);
        earMarkImages = new HashMap<>();
        earMarkImages.put("Simo Siivola", R.drawable.simon_merkki);
        earMarkImages.put("Kullervo Kullanhuuhtoja", R.drawable.kullervonmerkki);
    }

    public EarMarkImageSelector(List<Integer> placeholderImageIds) {
        this.placeholderImageIds = placeholderImageIds;
        earMarkImages = new HashMap<>();
        earMarkImages.put("Simo Siivola", R.drawable.simon_merkki);
        earMarkImages.put("Kullervo Kullanhuuhtoja", R.drawable.kullervonmerkki);
    }


    public int getEarMarkImage(String owner) {
        if(owner.length() == 0) {
            return randomPlaceholderImage();
        } else if(earMarkImages.containsKey(owner)){
            return earMarkImages.get(owner);
        } else {
            return R.drawable.jalanjaljet;
        }
    }

    public int randomPlaceholderImage() {
        Random rand = new Random();
        return placeholderImageIds.get(rand.nextInt(placeholderImageIds.size()));
    }

}
