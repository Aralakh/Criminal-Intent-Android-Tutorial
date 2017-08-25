package com.bignerdranch.android.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by lawren on 25/08/17.
 */

public class PhotoViewFragment extends DialogFragment {
    private static final String ARG_IMAGE = "image";

    private ImageView mImageZoomed;

    public static PhotoViewFragment newInstance(String imageFile){
        Bundle args = new Bundle();
        args.putSerializable(ARG_IMAGE, imageFile);

        PhotoViewFragment fragment = new PhotoViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
       String imageFile = (String) getArguments().getSerializable(ARG_IMAGE);

       View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_photo, null);

        Bitmap bitmap = PictureUtils.getScaledBitmap(imageFile, getActivity());

        mImageZoomed = (ImageView) v.findViewById(R.id.dialog_photo);
        mImageZoomed.setImageBitmap(bitmap);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
    }
}
