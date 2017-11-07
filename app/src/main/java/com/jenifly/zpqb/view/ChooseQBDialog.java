package com.jenifly.zpqb.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.TextView;

import com.jenifly.zpqb.R;
import com.jenifly.zpqb.cache.Cache;
import com.jenifly.zpqb.utils.ColorUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmadnajar on 3/8/17.
 */

public class ChooseQBDialog extends DialogFragment {

    public static final String TAG = ChooseQBDialog.class.getSimpleName();
    private Builder builder;
    private static ChooseQBDialog instance = new ChooseQBDialog();

    public static ChooseQBDialog getInstance() {
        return instance;
    }

    private CardView cardView;
    private TextView title;
    private JButton positive;
    private JButton negavitv;
    private JLinearLayout listView;

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
        return inflater.inflate(R.layout.dialog_chooseqb, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        if (builder != null) {
            title.setTextColor(Color.WHITE);
            final  List<String> list = new ArrayList<String>();
            list.add("2017年8月先考后培复习题");
          //  listView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,list ));
            if(builder.getOnListItemClicked() != null)
                listView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.getOnListItemClicked().OnClick(list.get(0));
                        listView.setState(true);
                    }
                });
            positive.setBackColor(Cache.BaseColor, ColorUtils.getColorWithAlpha(180, Cache.BaseColor));
            positive.setTextColor(Color.WHITE);
            negavitv.setBackColor(Cache.BaseColor, ColorUtils.getColorWithAlpha(180, Cache.BaseColor));
            negavitv.setTextColor(Color.WHITE);
            negavitv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.getOnNegativeClicked().OnClick();
                    dismiss();
                }
            });
            if (builder.getOnPositiveClicked() != null) {
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.getOnPositiveClicked().OnClick();
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
        positive = (JButton) view.findViewById(R.id.chooseqb_positive);
        negavitv = (JButton) view.findViewById(R.id.chooseqb_nagavive);
        listView = (JLinearLayout) view.findViewById(R.id.chooseqb_list);
    }

    private Dialog show(Activity activity, Builder builder) {
        this.builder = builder;
        if (!isAdded())
            show(((AppCompatActivity) activity).getSupportFragmentManager(), TAG);
        return getDialog();
    }

    public static class Builder implements Parcelable {

        private OnPositiveClicked onPositiveClicked;
        private OnListItemClicked onListItemClicked;
        private OnNegativeClicked onNegativeClicked;
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

        public OnPositiveClicked getOnPositiveClicked() {
            return onPositiveClicked;
        }

        public OnListItemClicked getOnListItemClicked() {
            return onListItemClicked;
        }

        public OnNegativeClicked getOnNegativeClicked() {
            return onNegativeClicked;
        }

        public Builder setOnPositiveClicked(OnPositiveClicked onInsertClicked) {
            this.onPositiveClicked = onInsertClicked;
            return this;
        }

        public Builder setOnListItemClicked(OnListItemClicked onListItemClicked) {
            this.onListItemClicked = onListItemClicked;
            return this;
        }

        public Builder setOnNegativeClicked(OnNegativeClicked onNegativeClicked) {
            this.onNegativeClicked = onNegativeClicked;
            return this;
        }

        public Builder (Context context) {
            this.context = context;
        }

        public Builder build() {
            return this;
        }

        public Dialog show() {
            return ChooseQBDialog.getInstance().show(((Activity) context), this);
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

    public interface OnPositiveClicked {
        void OnClick();
    }

    public interface OnNegativeClicked {
        void OnClick();
    }


    public interface OnListItemClicked {
        void OnClick(String ItemContext);
    }
}
