package com.example.customalertdialog;

import android.widget.ImageView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EarMarkImageSelector {

    private Map<String, Integer> earMarkImages;
    private boolean previousOwnerExisted;

    public EarMarkImageSelector() {
        earMarkImages = new HashMap<>();
        earMarkImages.put("Simo Siivola", R.drawable.simon_merkki);
        earMarkImages.put("Kullervo Kullanhuuhtoja", R.drawable.kullervonmerkki);
        this.previousOwnerExisted = false;
    }

    public boolean imageShouldBeChanged(String owner) {
        if(earMarkImages.containsKey(owner)) {
            return true;
        } else return this.previousOwnerExisted;
    }


    public int getEarMarkImage(String owner) {
        if(earMarkImages.containsKey(owner)){
            this.previousOwnerExisted = true;
            return earMarkImages.get(owner);
        } else {
            this.previousOwnerExisted = false;
            return randomPlaceholderImage();
        }
    }

    private int randomPlaceholderImage() {
        List<Integer> givenList = Arrays.asList(
                R.drawable.poro, R.drawable.korvamerkki
        );
        Random rand = new Random();
        return givenList.get(rand.nextInt(givenList.size()));
    }

}
