package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.Format;
import java.util.List;

/**
 * Created by lawren on 02/08/17.
 */

public class CrimeListFragment extends Fragment{

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean mSubtitleVisible;
    private int mCurrentPosition = -1;
    private TextView mNoCrimesTextView;
    private Button mAddCrimeButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
       View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

       mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
       mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

       mNoCrimesTextView = (TextView) view.findViewById(R.id.no_crimes);

       mAddCrimeButton = (Button) view.findViewById(R.id.add_crime);
       mAddCrimeButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               Crime crime = new Crime();
               CrimeLab.get(getActivity()).addCrime(crime);
               Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
               startActivity(intent);
           }
       });

       if(savedInstanceState != null){
           mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
       }

       updateUI();

       return view;
   }

   private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       private TextView mTitleTextView;
       private TextView mDateTextView;
       private TextView mTimeTextView;
       private Crime mCrime;
       private Button mRequiresPoliceButton;
       private ImageView mSolvedImageView;

       public CrimeHolder(LayoutInflater inflater, ViewGroup parent, int viewType){
           super(inflater.inflate(viewType, parent, false));
           itemView.setOnClickListener(this);

           mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
           mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
           mTimeTextView = (TextView) itemView.findViewById(R.id.crime_time);
           mSolvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved);
       }

       public void bind(Crime crime){
           mCrime = crime;
           mTitleTextView.setText(mCrime.getTitle());

           java.text.DateFormat dateFormat = android.text.format.DateFormat.getMediumDateFormat(getActivity().getApplicationContext());
           String formatDate = android.text.format.DateFormat.format("EEEE", mCrime.getDate()) + ", " + dateFormat.format(mCrime.getDate());
           mDateTextView.setText(formatDate);
           mTimeTextView.setText(android.text.format.DateFormat.format("hh:mm a", mCrime.getTime()));

           mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);

           //only displays + activates the police button if a crime requires it
           if(crime.doesRequirePolice()){
               mRequiresPoliceButton = (Button) itemView.findViewById(R.id.crime_requires_police);
               mRequiresPoliceButton.setOnClickListener(new View.OnClickListener(){
                   @Override
                   public void onClick(View view){
                       Toast.makeText(getActivity(), "Contacting police for " + mCrime.getTitle() + "!", Toast.LENGTH_SHORT).show();
                   }
               });
           }

       }

       @Override
       public void onClick(View view){
           mCurrentPosition = getAdapterPosition();
           Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
           startActivity(intent);
       }
   }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

   private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
       private List<Crime> mCrimes;

       public CrimeAdapter(List<Crime> crimes){
           mCrimes = crimes;
       }

       public void replaceList(List<Crime> crimes){ mCrimes = crimes; }

       @Override
       public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

           return new CrimeHolder(layoutInflater, parent, viewType);
       }

       @Override
       public void onBindViewHolder(CrimeHolder holder, int position){
           Crime crime = mCrimes.get(position);
           holder.bind(crime);
       }

       @Override
       public int getItemCount() {
           return mCrimes.size();
       }

       @Override
       public int getItemViewType(int position){
           if(mCrimes.get(position).doesRequirePolice()){
               return R.layout.list_item_serious_crime;
           }else{
               return R.layout.list_item_crime;
           }
       }
   }

   private void updateUI(){
       CrimeLab crimeLab = CrimeLab.get(getActivity());
       List<Crime> crimes = crimeLab.getCrimes();


       mNoCrimesTextView.setVisibility((crimes.size() > 0? View.GONE : View.VISIBLE));
       mAddCrimeButton.setVisibility((crimes.size() > 0? View.GONE : View.VISIBLE));


       if(mAdapter == null) {
           mAdapter = new CrimeAdapter(crimes);
           mCrimeRecyclerView.setAdapter(mAdapter);
       }else{
           mAdapter.replaceList(crimes);
           mAdapter.notifyDataSetChanged();
           updateSubtitle();
       }
   }

   private void updateSubtitle(){
       CrimeLab crimeLab = CrimeLab.get(getActivity());
       int crimeSize = crimeLab.getCrimes().size();
       String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural, crimeSize, crimeSize);

       if(!mSubtitleVisible){
           subtitle = null;
       }

       AppCompatActivity activity = (AppCompatActivity) getActivity();
       activity.getSupportActionBar().setSubtitle(subtitle);
   }
}
