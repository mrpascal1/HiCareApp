package com.ab.hicarerun.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutOptionAdapterBinding;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.handler.OnOptionClickListener;
import com.ab.hicarerun.network.models.quizmodel.QuizAnswer;
import com.ab.hicarerun.network.models.quizmodel.QuizOption;
import com.ab.hicarerun.utils.ImageOverlayStfalcon;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Arjun Bhatt on 2/11/2021.
 * Later modified by Shahid Raza on 7/15/2021
 */
public class QuizOptionAdapter extends RecyclerView.Adapter<QuizOptionAdapter.ViewHolder> {
    private OnListItemClickHandler onItemClickHandler;
    private OnOptionClickListener onOptionClickListener;
    private final Context mContext;
    private List<QuizOption> items = null;
    private ArrayList<String> givenAnswers = null;
    private String questionType = "";
    private String optionType = "";
    int correctAnswers = 0;
    int correctAnsPos = -1;
    String whichType = "";
    String dataType = "";
    boolean isWrongSelected = false;

    boolean isRadioAndText = false;
    boolean isRadioAndImage = false;
    boolean isCheckboxAndText = false;
    boolean isCheckboxAndImage = false;
    boolean fragmentCall = false;
    boolean radioOptions = false;
    int radioPos = -1;

    /**
     * <b>isRadioOptionSelected</b> is used as a flag for selected radio button
     * which is later used in code to disable the rest of the answers
     * so the user can't select it
     */
    int isRadioOptionSelected = 0;
    boolean isCheckOptionSelected = false;
    private int selectedPos = -1;
    private String correctAns = "";
    private List<QuizAnswer> answerList = null;
    private List<String> answers = null;
    private boolean isNextPressed = false;


    public QuizOptionAdapter(Context context, String QuestionType, String OptionType, List<QuizAnswer> optionValue, boolean isNextPressed) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
        this.isNextPressed = isNextPressed;
        this.questionType = QuestionType;
        this.optionType = OptionType;
        if (answerList == null) {
            answerList = new ArrayList<>();
        }
        givenAnswers = new ArrayList<>();
        answers = new ArrayList<>();
        this.onOptionClickListener = null;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutOptionAdapterBinding mLayoutOptionAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_option_adapter, parent, false);
        return new ViewHolder(mLayoutOptionAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, final int position) {

        try {
            if (fragmentCall && (isCheckOptionSelected || isCheckboxAndImage || isCheckboxAndText)){
                Collections.sort(givenAnswers);
                for (int i = 0; i < items.size(); i++){
                    holder.mLayoutOptionAdapterBinding.imgOption.setEnabled(false);
                    holder.mLayoutOptionAdapterBinding.checkOption.setEnabled(false);
                    holder.itemView.setEnabled(false);
                }
                //Log.d("TAG", "Before "+givenAnswers.toString());
                //Log.d("TAG", "After "+givenAnswers.toString());
            }
            if (isRadioOptionSelected == 1){
                for (int i = 0; i < items.size(); i++){
                    if (i != selectedPos) {
                        holder.mLayoutOptionAdapterBinding.radioOption.setEnabled(false);
                        holder.itemView.setEnabled(false);
                    }
                }
            }

            if (isWrongSelected && isRadioOptionSelected == 1){
                if (isRadioAndImage){
                    for (int i = 0; i < items.size(); i++){
                        holder.mLayoutOptionAdapterBinding.imgOption.setEnabled(false);
                        holder.itemView.setEnabled(false);
                    }
                    for (QuizAnswer ans : answerList) {
                        if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                            holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                            fadeOut(holder.mLayoutOptionAdapterBinding.lnrImgOption);
                        }else {
                            if (givenAnswers.contains(items.get(position).getOptionValue())){
                                holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                                arrayOperations(items.get(position).getOptionValue(), "radio");
                            }
                        }
                    }
                }else {
                    for (QuizAnswer ans : answerList) {
                        if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                            holder.mLayoutOptionAdapterBinding.lnrRadio.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                            fadeOut(holder.mLayoutOptionAdapterBinding.lnrRadio);
                        }else {
                            if (givenAnswers.contains(items.get(position).getOptionValue())){
                                holder.mLayoutOptionAdapterBinding.lnrRadio.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                                arrayOperations(items.get(position).getOptionValue(), "radio");
                            }
                        }/* else {
                            holder.mLayoutOptionAdapterBinding.lnrRadio.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                        }*/
                    }
                }

                //isRadioOptionSelected = 0;
                //isWrongSelected = false;
                notifyItemChanged(position);
            }


            if (isCheckOptionSelected && isWrongSelected){
                for (int i = 0; i < items.size(); i++){
                    holder.mLayoutOptionAdapterBinding.checkOption.setEnabled(false);
                    holder.itemView.setEnabled(false);
                }

                for (QuizAnswer ans : answerList) {
                    if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                        holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                        fadeOut(holder.mLayoutOptionAdapterBinding.lnrCheck);
                        if (!givenAnswers.isEmpty() && givenAnswers.contains(items.get(position).getOptionValue())){
                            arrayOperations(items.get(position).getOptionValue(), "checkbox");
                        }
                    }else {
                        if (givenAnswers.contains(items.get(position).getOptionValue())){
                            holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                            holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                            arrayOperations(items.get(position).getOptionValue(), "checkbox");
                        }

                    }
                }
            }

            if (isCheckboxAndImage || isRadioAndImage){
                if (isWrongSelected){
                    Log.d("TAG", "Came here");
                    for (int i = 0; i < items.size(); i++){
                        holder.mLayoutOptionAdapterBinding.imgOption.setEnabled(false);
                        holder.mLayoutOptionAdapterBinding.checkOption.setEnabled(false);
                        holder.itemView.setEnabled(false);
                    }

                    for (QuizAnswer ans : answerList) {
                        if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                            holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                            fadeOut(holder.mLayoutOptionAdapterBinding.lnrImgOption);
                            if (!givenAnswers.isEmpty() && givenAnswers.contains(items.get(position).getOptionValue())){
                                arrayOperations(items.get(position).getOptionValue(), "checkbox");
                            }
                        }else {
                            if (givenAnswers.contains(items.get(position).getOptionValue())){
                                holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                                holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                                arrayOperations(items.get(position).getOptionValue(), "checkbox");
                            }
                        }
                    }
                }
            }

            holder.mLayoutOptionAdapterBinding.zoomIv.setVisibility(View.VISIBLE);
//            Picasso.get().load(items.get(position).getPuzzleUrl()).into(holder.mQuizCategoryAdapterBinding.imgCategory);
            if (optionType.equalsIgnoreCase("Radio") && items.get(position).getOptionType().equalsIgnoreCase("Text")) {
                Log.d("TAG-Which", "1");
                whichType = "radio";
                dataType = "text";
                //holder.mLayoutOptionAdapterBinding.radioOption.setEnabled(false);
                holder.mLayoutOptionAdapterBinding.lnrRadio.setVisibility(View.VISIBLE);
                holder.mLayoutOptionAdapterBinding.lnrCheck.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.lnrImgOption.setVisibility(View.GONE);
                if (items.get(position).getOptionTitleDisplay() != null) {
                    holder.mLayoutOptionAdapterBinding.txtRadioOption.setText(items.get(position).getOptionTitleDisplay());
                }else {
                    holder.mLayoutOptionAdapterBinding.txtRadioOption.setText(items.get(position).getOptionTitle());
                }
                //holder.mLayoutOptionAdapterBinding.radioOption.setText(items.get(position).getOptionValue());
                isRadioAndText = true;
                isRadioAndImage = false;
                isCheckboxAndText = false;
                isCheckboxAndImage = false;
            } else if (optionType.equalsIgnoreCase("Radio") && items.get(position).getOptionType().equalsIgnoreCase("Image")) {
                Log.d("TAG-Which", "2");
                whichType = "radio";
                dataType = "image";
                holder.mLayoutOptionAdapterBinding.lnrRadio.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.lnrCheck.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.lnrImgOption.setVisibility(View.VISIBLE);
                Picasso.get().load(items.get(position).getOptionUrl()).into(holder.mLayoutOptionAdapterBinding.imgOption);
                isRadioAndImage = true;
                isRadioAndText = false;
                isCheckboxAndText = false;
                isCheckboxAndImage = false;
            } else if (optionType.equalsIgnoreCase("Checkbox") && items.get(position).getOptionType().equalsIgnoreCase("Image")) {
                Log.d("TAG-Which", "3");
                whichType = "checkbox";
                dataType = "image";
                isCheckboxAndImage = true;
                isRadioAndText = false;
                isRadioAndImage = false;
                isCheckboxAndText = false;
                holder.mLayoutOptionAdapterBinding.lnrRadio.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.lnrCheck.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.lnrImgOption.setVisibility(View.VISIBLE);
                Picasso.get().load(items.get(position).getOptionUrl()).into(holder.mLayoutOptionAdapterBinding.imgOption);
            } else if (optionType.equalsIgnoreCase("Checkbox") && items.get(position).getOptionType().equalsIgnoreCase("Text")) {
                Log.d("TAG-Which", "4");
                whichType = "checkbox";
                dataType = "text";
                holder.mLayoutOptionAdapterBinding.lnrRadio.setVisibility(View.GONE);
                holder.mLayoutOptionAdapterBinding.lnrCheck.setVisibility(View.VISIBLE);
                holder.mLayoutOptionAdapterBinding.lnrImgOption.setVisibility(View.GONE);
                if (items.get(position).getOptionTitleDisplay() != null) {
                    holder.mLayoutOptionAdapterBinding.txtCheckOption.setText(items.get(position).getOptionTitleDisplay());
                }else {
                    holder.mLayoutOptionAdapterBinding.txtCheckOption.setText(items.get(position).getOptionTitle());
                }
                isCheckboxAndText = true;
                isRadioAndText = false;
                isRadioAndImage = false;
                isCheckboxAndImage = false;
            }
//            holder.itemView.setOnClickListener(view -> onItemClickHandler.onItemClick(position));

            final String[] arrayList = {items.get(position).getOptionUrl()};
            holder.mLayoutOptionAdapterBinding.zoomIv.setOnClickListener(v -> {
                new ImageOverlayStfalcon(mContext, arrayList);
            });
            holder.mLayoutOptionAdapterBinding.cardImage.setOnClickListener(view -> {
                onOptionClickListener.onItemClick(position, items.get(position), "", "", whichType, holder);
                /*for (QuizAnswer ans : answerList) {
                    if (items.get(position).getOptionTitle().equalsIgnoreCase(ans.getOptionTitle())) {
                        holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                        break;
                    } else {
                        for (int i = 0; i < answerList.size(); i++) {
                            if (items.get(position).getOptionTitle().equalsIgnoreCase(answerList.get(i).getOptionTitle())) {
                                holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                            }
                        }
                        holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                    }
                }*/
                //notifyDataSetChanged();
            });


            holder.mLayoutOptionAdapterBinding.checkOption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Log.d("TAG", position+" checkbox pos");
                    isCheckOptionSelected = true;
                    onOptionClickListener.onItemClick(position, items.get(position), "", "", whichType, holder);
                    arrayOperations(items.get(position).getOptionValue(), "checkbox");
                    if (b){
                        holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_selected_border));
                    }else {
                        holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_unselected_border));
                    }
                    //holder.mLayoutOptionAdapterBinding.checkOption.setEnabled(false);
                    //holder.itemView.setEnabled(false);
                /*for (QuizAnswer ans : answerList) {
                    if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                        holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                        isWrongSelected = false;
                        return;
                    } else {
                        isWrongSelected = true;
                        holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                    }
                }*/
                    //notifyDataSetChanged();
                }
            });
            holder.mLayoutOptionAdapterBinding.checkOption.setOnClickListener(v -> {
                /*Log.d("TAG", position+" checkbox pos");
                isCheckOptionSelected = true;
                if (holder.mLayoutOptionAdapterBinding.checkOption.isChecked()){
                    holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_unselected_border));
                    holder.mLayoutOptionAdapterBinding.checkOption.setChecked(false);
                }else {
                    holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_selected_border));
                    holder.mLayoutOptionAdapterBinding.checkOption.setChecked(true);
                }*/
                //holder.mLayoutOptionAdapterBinding.checkOption.setEnabled(false);
                //holder.itemView.setEnabled(false);
                //onOptionClickListener.onItemClick(position, items.get(position), "", whichType);
                /*for (QuizAnswer ans : answerList) {
                    if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                        holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                        isWrongSelected = false;
                        return;
                    } else {
                        isWrongSelected = true;
                        holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                    }
                }*/
                //notifyDataSetChanged();
            });

            if (radioOptions) {
                if (radioPos == position) {
                    holder.mLayoutOptionAdapterBinding.lnrRadio.setBackground(mContext.getResources().getDrawable(R.drawable.option_selected_border));
                    holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_selected_border));
                    holder.mLayoutOptionAdapterBinding.radioOption.setChecked(true);
                } else {
                    holder.mLayoutOptionAdapterBinding.lnrRadio.setBackground(mContext.getResources().getDrawable(R.drawable.option_unselected_border));
                    holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_unselected_border));
                    holder.mLayoutOptionAdapterBinding.radioOption.setChecked(false);
                }
            }

            holder.mLayoutOptionAdapterBinding.radioOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    radioPos = position;
                    radioOptions = true;
                    onOptionClickListener.onItemClick(position, items.get(position), "", "", whichType, holder);
                    arrayOperations(items.get(position).getOptionValue(), "radio");
                    notifyDataSetChanged();
                }
            });

            /*holder.mLayoutOptionAdapterBinding.radioOption.setOnClickListener(v -> {
                Log.d("TAG", "Full Image2");
                isRadioOptionSelected = 1;
                selectedPos = position;
                holder.mLayoutOptionAdapterBinding.radioOption.setChecked(true);
                onOptionClickListener.onItemClick(position, items.get(position), "", whichType, holder);
                for (QuizAnswer ans : answerList) {
                    if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                        correctAnswers++;
                        isWrongSelected = false;
                        holder.mLayoutOptionAdapterBinding.lnrRadio.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                    } else {
                        isWrongSelected = true;
                        holder.mLayoutOptionAdapterBinding.lnrRadio.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                    }
                }
                notifyDataSetChanged();
            });*/

            holder.mLayoutOptionAdapterBinding.imgOption.setOnClickListener(v -> {
                selectedPos = position;
                onOptionClickListener.onItemClick(position, items.get(position), "", "", whichType, holder);
                if (optionType.equalsIgnoreCase("Radio") && items.get(position).getOptionType().equalsIgnoreCase("Image")){
                    Log.d("TAG", "Full Image");
                    //isRadioOptionSelected = 1;
                    /*holder.mLayoutOptionAdapterBinding.radioOption.setChecked(true);
                    holder.mLayoutOptionAdapterBinding.radioOption.setEnabled(false);
                    holder.mLayoutOptionAdapterBinding.imgOption.setEnabled(false);
                    holder.itemView.setEnabled(false);*/
                    arrayOperations(items.get(position).getOptionValue(), "radio");
                    radioPos = position;
                    radioOptions = true;
                    notifyDataSetChanged();
                    /*for (QuizAnswer ans : answerList) {
                        if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                            holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                            isWrongSelected = false;
                        } else {
                            holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                            isWrongSelected = true;
                        }
                    }*/
                }else {
                    Log.d("TAG", position+" checkbox pos");
                    isCheckOptionSelected = true;
                    arrayOperations(items.get(position).getOptionValue(), "checkbox");
                    if (holder.mLayoutOptionAdapterBinding.imgOption.isSelected()){
                        holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_unselected_border));
                        holder.mLayoutOptionAdapterBinding.imgOption.setSelected(false);
                    }else {
                        holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_selected_border));
                        holder.mLayoutOptionAdapterBinding.imgOption.setSelected(true);
                    }
                    //holder.mLayoutOptionAdapterBinding.imgOption.setEnabled(false);
                    //holder.itemView.setEnabled(false);
                    /*for (QuizAnswer ans : answerList) {
                        if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                            holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                            isWrongSelected = false;
                            return;
                        } else {
                            holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                            isWrongSelected = true;
                        }
                    }*/
                }
            });

            holder.itemView.setOnClickListener(view -> {
                selectedPos = position;
                if (optionType.equalsIgnoreCase("Radio") && items.get(position).getOptionType().equalsIgnoreCase("Text")) {
                    onOptionClickListener.onItemClick(position, items.get(position), "", "", whichType, holder);
                    arrayOperations(items.get(position).getOptionValue(), "radio");
                    radioPos = position;
                    radioOptions = true;
                    notifyDataSetChanged();
                    /*for (QuizAnswer ans : answerList) {
                        if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                            correctAnswers++;
                            isWrongSelected = false;
                            holder.mLayoutOptionAdapterBinding.lnrRadio.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                            //fadeOut(holder.mLayoutOptionAdapterBinding.lnrRadio);
                        } else {
                            isWrongSelected = true;
                            holder.mLayoutOptionAdapterBinding.lnrRadio.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                        }
                    }
                    notifyDataSetChanged();*/
                } else if (optionType.equalsIgnoreCase("Radio") && items.get(position).getOptionType().equalsIgnoreCase("Image")) {
                    onOptionClickListener.onItemClick(position, items.get(position), "", "", whichType, holder);
                    isRadioOptionSelected = 1;
                    arrayOperations(items.get(position).getOptionValue(), "radio");
                    notifyDataSetChanged();
                    /*holder.mLayoutOptionAdapterBinding.radioOption.setChecked(true);
                    holder.mLayoutOptionAdapterBinding.imgOption.setEnabled(false);
                    holder.itemView.setEnabled(false);
                    for (QuizAnswer ans : answerList) {
                        if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                            holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                            isWrongSelected = false;
                        } else {
                            holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                            isWrongSelected = true;
                        }
                    }
                    notifyDataSetChanged();*/
                }else if (optionType.equalsIgnoreCase("Checkbox") && items.get(position).getOptionType().equalsIgnoreCase("Text")) {
                    isCheckOptionSelected = true;
                    Log.d("TAG", position+" checkbox pos");
                    //onOptionClickListener.onItemClick(position, items.get(position), "", whichType);

                    /*holder.mLayoutOptionAdapterBinding.checkOption.setChecked(true);
                    holder.mLayoutOptionAdapterBinding.checkOption.setEnabled(false);
                    holder.itemView.setEnabled(false);*/
                    //givenAnswers.add(position);
                    if (holder.mLayoutOptionAdapterBinding.checkOption.isChecked()){
                        holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_unselected_border));
                        holder.mLayoutOptionAdapterBinding.checkOption.setChecked(false);
                    }else {
                        holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_selected_border));
                        holder.mLayoutOptionAdapterBinding.checkOption.setChecked(true);
                    }
                    /*for (QuizAnswer ans : answerList) {
                        if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                            holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                            isWrongSelected = false;
                            return;
                        } else {
                            holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                            isWrongSelected = true;
                        }
                    }*/
                    //notifyDataSetChanged();
                } else if (optionType.equalsIgnoreCase("Checkbox") && items.get(position).getOptionType().equalsIgnoreCase("Image")) {
                    //isCheckOptionSelected = true;
                    /*holder.mLayoutOptionAdapterBinding.imgOption.setEnabled(false);
                    holder.itemView.setEnabled(false);*/
                    onOptionClickListener.onItemClick(position, items.get(position), "", "", whichType, holder);
                    arrayOperations(items.get(position).getOptionValue(), "checkbox");
                    if (holder.mLayoutOptionAdapterBinding.imgOption.isSelected()){
                        holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_unselected_border));
                        holder.mLayoutOptionAdapterBinding.imgOption.setSelected(false);
                    }else {
                        holder.mLayoutOptionAdapterBinding.lnrCheck.setBackground(mContext.getResources().getDrawable(R.drawable.option_selected_border));
                        holder.mLayoutOptionAdapterBinding.imgOption.setSelected(true);
                    }
                    /*for (QuizAnswer ans : answerList) {
                        if (items.get(position).getOptionValue().equalsIgnoreCase(ans.getOptionValue())) {
                            holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_right_border));
                            isWrongSelected = false;
                            return;
                        } else {
                            holder.mLayoutOptionAdapterBinding.lnrImgOption.setBackground(mContext.getResources().getDrawable(R.drawable.option_wrong_border));
                            isWrongSelected = true;
                        }
                    }
                    notifyDataSetChanged();*/
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void arrayOperations(String optionValue, String type){
        if (type.equalsIgnoreCase("checkbox")) {
            if (givenAnswers.size() > 0 && givenAnswers.contains(optionValue)) {
                givenAnswers.remove(optionValue);
            } else {
                givenAnswers.add(optionValue);
            }
        }
        if (type.equalsIgnoreCase("radio")){
            if (!givenAnswers.isEmpty()) {
                givenAnswers.clear();
                Log.d("TAG", "Array cleared");
            }
            Log.d("TAG", "Adding " + optionValue);
            givenAnswers.add(optionValue);
            Log.d("TAG", "Array now " + givenAnswers.toString());
        }
        Collections.sort(givenAnswers);
    }
    public void showCorrect(int position, QuizOption quizOption, String title, String optionType, ViewHolder holder){
        String checkArray = "";
        ArrayList<Integer> optionId = new ArrayList<>();
        fragmentCall = true;
        if (whichType.equalsIgnoreCase("checkbox")){
            isCheckOptionSelected = true;
            isWrongSelected = true;
            isRadioOptionSelected = 0;
            radioPos = -1;
            radioOptions = false;
            for (String g: givenAnswers){
                for (int i = 0; i < items.size(); i++){
                    if (g.equalsIgnoreCase(items.get(i).getOptionValue())){
                        optionId.add(items.get(i).getOptionId());
                    }
                }
            }
            if (!givenAnswers.isEmpty()){
                Collections.sort(givenAnswers);
                Collections.sort(optionId);
                StringBuilder sbf = new StringBuilder();
                for (int opt: optionId){
                    if (sbf.length() > 0){
                        sbf.append(",");
                    }
                    sbf.append(opt);
                }
                onOptionClickListener.onItemClick(position, quizOption, sbf.toString(), title, "checkboxConfirmed", holder);
                Log.d("TAG", "Check: "+sbf);
            }
        }
        if (whichType.equalsIgnoreCase("radio")){
            isRadioOptionSelected = 1;
            isWrongSelected = true;
            isCheckOptionSelected = false;
            radioPos = -1;
            radioOptions = false;
        }
        notifyDataSetChanged();
    }

    private void fadeOut(View view){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.2f);
        objectAnimator.setDuration(400L);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fadeIn(view);
            }
        });
        objectAnimator.start();
    }

    private void fadeIn(View view){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 0.2f, 1f);
        objectAnimator.setDuration(400L);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fadeOut(view);
            }
        });
        objectAnimator.start();
    }

    public void setOnItemClickHandler(OnListItemClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }

    public void setOnOptionClickListener(OnOptionClickListener onOptionClickListener){
        this.onOptionClickListener = onOptionClickListener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<QuizOption> data, List<QuizAnswer> correctAnswers) {
        items.clear();
        items.addAll(data);
        answerList.addAll(correctAnswers);
        for (QuizAnswer answer: answerList){
            if (answer != null) {
                answers.add(answer.getOptionValue());
            }
        }
        //        for (int index = 0; index < data.size(); index++) {
//            QuizCategoryViewModel quizCategoryViewModel = new QuizCategoryViewModel();
//            quizCategoryViewModel.clone(data.get(index));
//            items.add(quizCategoryViewModel);
//        }
    }

    public QuizOption getItem(int position) {
        return items.get(position);
    }
//
//    public void removeAll() {
//        items.removeAll(items);
//        notifyDataSetChanged();
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LayoutOptionAdapterBinding mLayoutOptionAdapterBinding;

        public ViewHolder(LayoutOptionAdapterBinding mLayoutOptionAdapterBinding) {
            super(mLayoutOptionAdapterBinding.getRoot());
            this.mLayoutOptionAdapterBinding = mLayoutOptionAdapterBinding;
        }
    }
}
