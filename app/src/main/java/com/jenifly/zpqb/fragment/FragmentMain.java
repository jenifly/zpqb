package com.jenifly.zpqb.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jenifly.zpqb.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentMain extends Fragment {

    @BindView(R.id.mian_search_tv) EditText search_tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init(){
    }


    private void initView(){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {

                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
            }

            @Override
            protected Void doInBackground(Void... params) {

                return null;
            }
        }.execute();
    }

}
