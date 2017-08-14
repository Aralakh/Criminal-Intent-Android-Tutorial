package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.UUID;

/**
 * Created by lawren on 04/08/17.
 */

public class CrimePagerActivity extends AppCompatActivity {
    private static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";

    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private Button mButtonFirstPage;
    private Button mButtonLastPage;

    public static Intent newIntent(Context packageContext, UUID crimeId){
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        mViewPager = (ViewPager) findViewById(R.id.crime_view_pager);
        mCrimes = CrimeLab.get(this).getCrimes();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        for(int i = 0; i < mCrimes.size(); i++){
            if(mCrimes.get(i).getId().equals(crimeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        //disable first/last buttons if you're on that current page
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){
                if(mViewPager.getCurrentItem() == 0){
                    mButtonFirstPage.setVisibility(View.INVISIBLE);
                }else{
                    mButtonFirstPage.setVisibility(View.VISIBLE);
                }

                if(mViewPager.getCurrentItem() == (mCrimes.size()-1)){
                    mButtonLastPage.setVisibility(View.INVISIBLE);
                }else{
                    mButtonLastPage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {
                //do nothing
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //do nothing
            }
        });

        mButtonFirstPage = (Button) findViewById(R.id.first_page);
        mButtonFirstPage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mViewPager.setCurrentItem(0);
            }
        });

        mButtonLastPage = (Button) findViewById(R.id.last_page);
        mButtonLastPage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mViewPager.setCurrentItem(mCrimes.size()-1);
            }
        });

    }



}
