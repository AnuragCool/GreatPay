package com.example.greatpay;

import android.graphics.pdf.PdfDocument;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PageAdapter extends FragmentStatePagerAdapter {

    int countTab;

    public PageAdapter (FragmentManager fm,int countTab){
        super(fm);
        this.countTab=countTab;

    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                TabOne tabOne= new TabOne();
                return tabOne;
            case 1 :
                TabTwo tabtwo= new TabTwo();
                return tabtwo;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return countTab;
    }
}
