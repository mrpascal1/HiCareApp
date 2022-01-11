package com.ab.hicarerun.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutNewCompletionListBinding;
import com.ab.hicarerun.handler.OnCheckListClickHandler;
import com.ab.hicarerun.network.models.generalmodel.TaskCheckList;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by Arjun Bhatt on 8/18/2020.
 */
public class CheckListParentAdapter extends RecyclerView.Adapter<CheckListParentAdapter.ViewHolder> {
    private OnCheckListClickHandler onItemClickHandler;
    private final Context mContext;
    private List<TaskCheckList> items = null;
    private OnOptionClicked onOptionClicked;
    private ArrayList<String> multileItems = null;
    private String strAnswer = "";

    public CheckListParentAdapter(Context context, List<TaskCheckList> items, OnOptionClicked onOptionClicked) {
        this.items = items;
        this.onOptionClicked = onOptionClicked;
        this.mContext = context;
        if (multileItems == null) {
            multileItems = new ArrayList<>();
        }
    }

    @NotNull
    @Override
    public CheckListParentAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutNewCompletionListBinding mLayoutNewCompletionListBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_new_completion_list, parent, false);
        return new CheckListParentAdapter.ViewHolder(mLayoutNewCompletionListBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull CheckListParentAdapter.ViewHolder holder, final int position) {
        try {
            if (items.get(position).getTakePicture()) {
                 items.get(position).setImageRequired(true);
                holder.mLayoutNewCompletionListBinding.relPhoto.setVisibility(View.VISIBLE);
            } else {
                   items.get(position).setImageRequired(false);
                holder.mLayoutNewCompletionListBinding.relPhoto.setVisibility(View.GONE);
            }
            if (items.get(position).getOptionType().equalsIgnoreCase("TextBox")) {
                holder.mLayoutNewCompletionListBinding.edtAnswers.setText(items.get(position).getSelectedAnswer());
                holder.mLayoutNewCompletionListBinding.edtAnswers.setVisibility(View.VISIBLE);
                holder.mLayoutNewCompletionListBinding.edtAnswers.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        onOptionClicked.onClicked(position, s.toString().trim());
                        items.get(position).setSelectedAnswer(s.toString().trim());
                    }
                });
            } else {
                holder.mLayoutNewCompletionListBinding.edtAnswers.setVisibility(GONE);
            }
            if (items.get(position).getOptions() != null) {
                holder.mLayoutNewCompletionListBinding.txtQuest.setTextColor(mContext.getResources().getColor(R.color.black));
                holder.mLayoutNewCompletionListBinding.txtQuest.setTextSize(17f);
            } else {
                if (items.get(position).getOptionType().equalsIgnoreCase("TextBox")) {
                    holder.mLayoutNewCompletionListBinding.txtQuest.setTextColor(mContext.getResources().getColor(R.color.black));
                    holder.mLayoutNewCompletionListBinding.txtQuest.setTextSize(17f);
                } else {
                    holder.mLayoutNewCompletionListBinding.txtQuest.setTextColor(mContext.getResources().getColor(R.color.orange));
                    holder.mLayoutNewCompletionListBinding.txtQuest.setTextSize(15f);
                    items.get(position).setSelectedAnswer("NA");
                }
            }
            holder.mLayoutNewCompletionListBinding.imageCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    items.get(position).setImagePath(null);
                    items.get(position).setIconUrl(null);
                    holder.mLayoutNewCompletionListBinding.lnrImage.setVisibility(GONE);
                    holder.mLayoutNewCompletionListBinding.lnrUpload.setVisibility(View.VISIBLE);
                    notifyItemChanged(position);
                }
            });

            if (items.get(position).getImagePath() != null && !items.get(position).getImagePath().equals("")) {
                Picasso.get().load(items.get(position).getImagePath()).into(holder.mLayoutNewCompletionListBinding.imgUploadedCheque);
                holder.mLayoutNewCompletionListBinding.lnrUpload.setVisibility(View.GONE);
                holder.mLayoutNewCompletionListBinding.lnrImage.setVisibility(View.VISIBLE);
            } else {
                holder.mLayoutNewCompletionListBinding.lnrUpload.setVisibility(View.VISIBLE);
                holder.mLayoutNewCompletionListBinding.lnrImage.setVisibility(GONE);
            }

            holder.mLayoutNewCompletionListBinding.lnrUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickHandler.onCameraClicked(position);
                }
            });

            holder.mLayoutNewCompletionListBinding.txtQuest.setText(items.get(position).getDisplay_Title());
            holder.mLayoutNewCompletionListBinding.txtQuest.setTypeface(holder.mLayoutNewCompletionListBinding.txtQuest.getTypeface(), Typeface.BOLD);
            CheckListChildAdapter childAdapter = new CheckListChildAdapter(mContext, (position1, isChecked, value) -> {

                if (isChecked) {
                    multileItems.add(items.get(position).getOptions().get(position1).getValue());
                } else {
                    multileItems.remove(items.get(position).getOptions().get(position1).getValue());
                }

                if (multileItems.size() > 0) {
                    StringBuilder sbString = new StringBuilder("");
                    //iterate through ArrayList
                    for (String service : multileItems) {
                        //append ArrayList element followed by comma
                        sbString.append(service).append(",");
                    }
                    //convert StringBuffer to String
                    strAnswer = sbString.toString();
                    //remove last comma from String if you want
                    if (strAnswer.length() > 0) {
                        strAnswer = strAnswer.substring(0, strAnswer.length() - 1);
                    }
                    onOptionClicked.onClicked(position, strAnswer);
                } else {
                    strAnswer = "";
                }

            });
            holder.mLayoutNewCompletionListBinding.recycleChild.setLayoutManager(new LinearLayoutManager(mContext));
            holder.mLayoutNewCompletionListBinding.recycleChild.setHasFixedSize(true);
            holder.mLayoutNewCompletionListBinding.recycleChild.setClipToPadding(false);
            holder.mLayoutNewCompletionListBinding.recycleChild.setAdapter(childAdapter);
            if (items.get(position).getOptions() != null) {
                childAdapter.addData(items.get(position).getOptions(), items.get(position).getOptionType());
                childAdapter.setOnItemClickHandler(positionChild -> {
                    if (items.get(position).getOptionType().equalsIgnoreCase("Single Select")) {
                        items.get(position).setSelectedAnswer(childAdapter.getItem(positionChild).getValue());
                        childAdapter.getItem(positionChild).setIsSelected(true);
                        onOptionClicked.onClicked(position, childAdapter.getItem(positionChild).getValue());
                        childAdapter.notifyItemChanged(positionChild);
                    } else if (items.get(position).getOptionType().equalsIgnoreCase("Multi Select")) {
                        items.get(position).setSelectedAnswer(strAnswer);
                        childAdapter.getItem(positionChild).setIsSelected(true);
                        onOptionClicked.onClicked(position, strAnswer);
                    } else {
                        childAdapter.getItem(positionChild).setIsSelected(true);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnItemClickHandler(OnCheckListClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public TaskCheckList getItem(int position) {
        return items.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final LayoutNewCompletionListBinding mLayoutNewCompletionListBinding;

        public ViewHolder(LayoutNewCompletionListBinding mLayoutNewCompletionListBinding) {
            super(mLayoutNewCompletionListBinding.getRoot());
            this.mLayoutNewCompletionListBinding = mLayoutNewCompletionListBinding;
        }
    }

    public interface OnOptionClicked {
        void onClicked(int position, String option);
    }
}
