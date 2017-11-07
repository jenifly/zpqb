package com.jenifly.zpqb.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jenifly.zpqb.R;
import com.jenifly.zpqb.cache.Cache;
import com.jenifly.zpqb.utils.ColorUtils;
import com.jenifly.zpqb.view.calenderView.CalenderView;
import com.jenifly.zpqb.view.calenderView.cons.DPMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ahmadnajar on 3/8/17.
 */

public class SigninDialog extends DialogFragment {
    public static final String TAG = SigninDialog.class.getSimpleName();
    private Builder builder;

    private CardView cardView;
    private TextView title;
    private JButton dismiss;
    private CalenderView calenderView;

    private static SigninDialog instance = new SigninDialog();

    public static SigninDialog getInstance() {
        return instance;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.setCancelable(true);

        if (savedInstanceState != null) {
            if (builder != null) {
                builder = (Builder) savedInstanceState.getParcelable(Builder.class.getSimpleName());
            }
        }
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (builder != null)
            outState.putParcelable(Builder.class.getSimpleName(), builder);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_signin, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        if (builder != null) {
            final Calendar c = Calendar.getInstance();
            calenderView.setDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
            calenderView.setFestivalDisplay(true);
            calenderView.setTodayDisplay(true);
            calenderView.setHolidayDisplay(false);
            calenderView.setDeferredDisplay(false);
            calenderView.setMode(DPMode.SINGLE);
            calenderView.setWeekBackgrougColor(Cache.BaseColor);
            calenderView.setDecorColorT(Cache.BaseColor);
            /*calenderView.setOnDatePickedListener(new CalenderView.OnDatePickedListener() {
                @Override
                public void onDatePicked(DPInfo info) {
                    Toast.makeText(getBaseContext(), info.strY+"-"+info.strM+"-"+info.strG+"-"+info.strF, Toast.LENGTH_SHORT).show();
                }
            });
            calenderView.setOnDateChangListener(new CalenderView.OnDateChangeListener() {
                @Override
                public void onDateChange(String date) {
                    String[] tmp = date.split("-");
                    mDate = calenderView.mLManager.titleMonth()[Integer.parseInt(tmp[0]) - 1] +  "   " + tmp[1];
//                main_tilte.setText(mDate);
                }
            });*/
            dismiss.setText("确定");
            dismiss.setBackColor(Cache.BaseColor, ColorUtils.getColorWithAlpha(180, Cache.BaseColor));
            dismiss.setTextColor(Color.WHITE);
            if (builder.getOnInsertClicked() != null) {
                dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.getOnInsertClicked().OnClick(v, getDialog());
                        dismiss();
                    }
                });
            }
        }
        cardView.setCardBackgroundColor(Color.WHITE);
    }

    private void initViews(View view) {
        cardView = (CardView) view.findViewById(R.id.card_view);
        title = (TextView) view.findViewById(R.id.title);
        dismiss = (JButton) view.findViewById(R.id.signin_dismiss);
        calenderView = (CalenderView)view.findViewById(R.id.signin_calenderView);
    }

    private Dialog show(Activity activity, Builder builder) {
        this.builder = builder;
        if (!isAdded())
            show(((AppCompatActivity) activity).getSupportFragmentManager(), TAG);
        return getDialog();
    }

    public static class Builder implements Parcelable {

        private OnInsertClicked onInsertClicked;
        private Context context;

        public static final Creator<Builder> CREATOR = new Creator<Builder>() {
            @Override
            public Builder createFromParcel(Parcel in) {
                return new Builder(in);
            }

            @Override
            public Builder[] newArray(int size) {
                return new Builder[size];
            }
        };

        protected Builder(Parcel in) {
        }

        public OnInsertClicked getOnInsertClicked() {
            return onInsertClicked;
        }

        public Builder setOnInsertClicked(OnInsertClicked onInsertClicked) {
            this.onInsertClicked = onInsertClicked;
            return this;
        }


        public Builder (Context context) {
            this.context = context;
        }

        public Builder build() {
            return this;
        }

        public Dialog show() {
            return SigninDialog.getInstance().show(((Activity) context), this);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public interface OnInsertClicked {
        void OnClick(View view, Dialog dialog);
    }
}
