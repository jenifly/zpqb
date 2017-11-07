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

public class ChooseTSDialog extends DialogFragment {

    public static final String TAG = ChooseTSDialog.class.getSimpleName();
    private Builder builder;
    private static ChooseTSDialog instance = new ChooseTSDialog();

    public static ChooseTSDialog getInstance() {
        return instance;
    }

    private CardView cardView;
    private JButton positive;
    private JButton negavitv;
    private JLinearLayout model1;
    private JLinearLayout model2;
    private JLinearLayout model3;
    private JLinearLayout model4;
    private JLinearLayout model5;

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
        return inflater.inflate(R.layout.dialog_choosets, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        if (builder != null) {
            final  List<String> list = new ArrayList<String>();
            model1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sss();
                    model1.setState(true);
                    builder.getOnListItemClicked().OnClick(20);
                }
            });
            model2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sss();
                    model2.setState(true);
                    builder.getOnListItemClicked().OnClick(25);
                }
            });
            model3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sss();
                    model3.setState(true);
                    builder.getOnListItemClicked().OnClick(30);
                }
            });
            model4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sss();
                    model4.setState(true);
                    builder.getOnListItemClicked().OnClick(40);
                }
            });
            model5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sss();
                    model5.setState(true);
                    builder.getOnListItemClicked().OnClick(50);
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


    private void sss(){
        model1.Restore();
        model2.Restore();
        model3.Restore();
        model4.Restore();
        model5.Restore();
    }

    private void initViews(View view) {
        cardView = (CardView) view.findViewById(R.id.card_view);
        positive = (JButton) view.findViewById(R.id.choosets_positive);
        negavitv = (JButton) view.findViewById(R.id.choosets_nagavive);
        model1 = (JLinearLayout) view.findViewById(R.id.choosets_model1);
        model2 = (JLinearLayout) view.findViewById(R.id.choosets_model2);
        model3 = (JLinearLayout) view.findViewById(R.id.choosets_model3);
        model4 = (JLinearLayout) view.findViewById(R.id.choosets_model4);
        model5 = (JLinearLayout) view.findViewById(R.id.choosets_model5);
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
            return ChooseTSDialog.getInstance().show(((Activity) context), this);
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
        void OnClick(int count);
    }
}
