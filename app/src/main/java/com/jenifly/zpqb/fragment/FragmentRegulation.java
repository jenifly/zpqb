package com.jenifly.zpqb.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.jenifly.zpqb.R;
import com.jenifly.zpqb.activity.RegulationActivity;
import com.jenifly.zpqb.cache.Cache;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentRegulation extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_regulation, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick({R.id.regulatoin_001,R.id.regulatoin_002,R.id.regulatoin_003}) void click(View view){
        switch (view.getId()){
            case R.id.regulatoin_001:
                Cache.RegulationId = 1;
                break;
            case R.id.regulatoin_002:
                Cache.RegulationId = 2;
                break;
            case R.id.regulatoin_003:
                Cache.RegulationId = 3;
                break;
        }
        startActivity(new Intent(getActivity(), RegulationActivity.class));
    }

}
