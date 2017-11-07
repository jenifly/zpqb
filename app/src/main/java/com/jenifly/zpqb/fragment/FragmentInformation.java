package com.jenifly.zpqb.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jenifly.zpqb.R;
import com.jenifly.zpqb.activity.ZJgushiActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentInformation extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init(){

    }

    @OnClick({R.id.infomation_zjgushi})void click(View view){
        startActivity(new Intent(getActivity(), ZJgushiActivity.class));
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
