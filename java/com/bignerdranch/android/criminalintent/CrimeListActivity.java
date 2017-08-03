package com.bignerdranch.android.criminalintent;


import android.support.v4.app.Fragment;

/**
 * Created by lawren on 02/08/17.
 */

public class CrimeListActivity extends SingleFragmentActivity{
    @Override
    protected Fragment createFragment(){
        return new CrimeListFragment();
    }
}
