package com.ab.hicarerun.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutConsulationParentAdapterBinding;
import com.ab.hicarerun.databinding.LayoutOffersAdapterBinding;
import com.ab.hicarerun.handler.OnCheckListItemClickHandler;
import com.ab.hicarerun.handler.OnConsultationClickHandler;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.handler.OnRewardtemClickHandler;
import com.ab.hicarerun.network.models.ConsulationModel.Data;
import com.ab.hicarerun.network.models.ConsulationModel.Optionlist;
import com.ab.hicarerun.network.models.OffersModel.RewardList;
import com.ab.hicarerun.viewmodel.ConsulationViewModel;
import com.ab.hicarerun.viewmodel.RewardsViewModel;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by Arjun Bhatt on 7/18/2020.
 */
public class ConsulationParentAdapter extends RecyclerView.Adapter<ConsulationParentAdapter.ViewHolder> {
    private OnConsultationClickHandler onItemClickHandler;
    private final Context mContext;
    private List<ConsulationViewModel> items = null;
    private HashMap<Integer, String> checkItems = null;
    private String strAnswer = "";
    private OnOptionClicked onOptionClicked;
    private int selectedPos = -1;
    private boolean isInspectionDone;
    private String type = "";

    public ConsulationParentAdapter(Context context, boolean isInspectionDone, String type, OnOptionClicked onOptionClicked) {
        if (items == null) {
            items = new ArrayList<>();
        }

        if (checkItems == null) {
            checkItems = new HashMap<>();
        }
        this.onOptionClicked = onOptionClicked;
        this.mContext = context;
        this.isInspectionDone = isInspectionDone;
        this.type = type;
    }

    @NotNull
    @Override
    public ConsulationParentAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutConsulationParentAdapterBinding mLayoutConsulationParentAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_consulation_parent_adapter, parent, false);
        return new ConsulationParentAdapter.ViewHolder(mLayoutConsulationParentAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull ConsulationParentAdapter.ViewHolder holder, final int position) {
        try {
            if (items.get(position).getPictureRequired()) {
                holder.mLayoutConsulationParentAdapterBinding.relPhoto.setVisibility(View.VISIBLE);
            } else {
                holder.mLayoutConsulationParentAdapterBinding.relPhoto.setVisibility(View.GONE);
            }

            if (items.get(position).getShowQuestion()) {
                holder.itemView.setVisibility(View.VISIBLE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            } else {
                holder.itemView.setVisibility(GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
//                ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
//                params.height = 0;
//                params.width = 0;
//                holder.itemView.setLayoutParams(params);
            }

            if (isInspectionDone) {
                holder.mLayoutConsulationParentAdapterBinding.imageCancel.setVisibility(View.GONE);
                if (items.get(position).getPictureURL() == null || items.get(position).getPictureURL().equals("")) {
                    holder.mLayoutConsulationParentAdapterBinding.relPhoto.setVisibility(GONE);
                }
            } else {
                holder.mLayoutConsulationParentAdapterBinding.imageCancel.setVisibility(View.VISIBLE);
            }


            holder.mLayoutConsulationParentAdapterBinding.imageCancel.setOnClickListener(v -> {
                items.get(position).setPictureURL(null);
                holder.mLayoutConsulationParentAdapterBinding.lnrImage.setVisibility(View.GONE);
                holder.mLayoutConsulationParentAdapterBinding.lnrUpload.setVisibility(View.VISIBLE);
//                notifyDataSetChanged();
                onItemClickHandler.onCancelImageClicked(position);
            });

            if (items.get(position).getPictureURL() != null && !items.get(position).getPictureURL().equals("")) {
                Picasso.get().load(items.get(position).getPictureURL()).into(holder.mLayoutConsulationParentAdapterBinding.imgUploadedCheque);
                holder.mLayoutConsulationParentAdapterBinding.lnrUpload.setVisibility(View.GONE);
                holder.mLayoutConsulationParentAdapterBinding.lnrImage.setVisibility(View.VISIBLE);
            } else {
                holder.mLayoutConsulationParentAdapterBinding.lnrUpload.setVisibility(View.VISIBLE);
                holder.mLayoutConsulationParentAdapterBinding.lnrImage.setVisibility(View.GONE);
            }


            holder.mLayoutConsulationParentAdapterBinding.lnrUpload.setOnClickListener(v -> onItemClickHandler.onCameraClicked(position));
//            if (type.equals("Termite Inspection")) {
//                holder.mLayoutConsulationParentAdapterBinding.txtQuest.setText(String.valueOf(position + 1) + ") " + items.get(position).getQuestionDisplayTitle());
//            } else {
            holder.mLayoutConsulationParentAdapterBinding.txtQuest.setText(items.get(position).getQuestionDisplayTitle());
//            }
            holder.mLayoutConsulationParentAdapterBinding.txtQuest.setTypeface(holder.mLayoutConsulationParentAdapterBinding.txtQuest.getTypeface(), Typeface.BOLD);
            ConsulationChildAdapter childAdapter = new ConsulationChildAdapter(mContext, isInspectionDone, (position1, isChecked, isSelectedAndDisabled) -> {

                String optionValue = items.get(position).getOptionlists().get(position1).getOptionvalue();

                if (isChecked) {
                    String newAppendValue = (checkItems.get(position) != null) ? checkItems.get(position) + "," + optionValue : optionValue;
                    checkItems.put(position, newAppendValue);
                    Log.i("Parent_Position", String.valueOf(position));
                    Log.i("Parent_Position", newAppendValue);
                } else {
                    String newAppendValue = checkItems.get(position);
                    newAppendValue = newAppendValue.replace("," + optionValue, "");
                    newAppendValue = newAppendValue.replace(optionValue, "");
                    checkItems.put(position, newAppendValue);
                    Log.i("Parent_Position", String.valueOf(position));
                    Log.i("Parent_Position", newAppendValue);
                }

                strAnswer = (checkItems.get(position) == null) ? "" : checkItems.get(position);
                Log.i("Parent_Position", "Final Value : " + strAnswer);

                items.get(position).setAnswerText(strAnswer);
                onOptionClicked.onClicked(position, strAnswer);


            });

            holder.mLayoutConsulationParentAdapterBinding.recycleChild.setLayoutManager(new LinearLayoutManager(mContext));
            holder.mLayoutConsulationParentAdapterBinding.recycleChild.setHasFixedSize(true);
            holder.mLayoutConsulationParentAdapterBinding.recycleChild.setClipToPadding(false);
            holder.mLayoutConsulationParentAdapterBinding.recycleChild.setAdapter(childAdapter);
            if (items.get(position).getOptionlists() != null && items.get(position).getOptionlists().size() > 0) {
                childAdapter.addData(items.get(position).getOptionlists(), items.get(position).getQuestionType(), items.get(position).getAnswerSelected(), type);
                childAdapter.setOnItemClickHandler(positionChild -> {
                    if (items.get(position).getQuestionType().equals("Single Select")) {
                        onOptionClicked.onClicked(position, childAdapter.getItem(positionChild).getOptionvalue());
                        childAdapter.getItem(positionChild).setIsselected(true);
                        childAdapter.notifyItemChanged(positionChild);
                    } else {
                        onOptionClicked.onClicked(position, strAnswer);
                        childAdapter.getItem(positionChild).setIsselected(true);
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnItemClickHandler(OnConsultationClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ConsulationViewModel getItem(int position) {
        return items.get(position);
    }

    public void setData(List<Data> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            ConsulationViewModel consulationViewModel = new ConsulationViewModel();
            consulationViewModel.clone(data.get(index));
            items.add(consulationViewModel);
        }
    }


    //
    public void addData(List<Data> data) {
        items.clear();
        for (int index = 0; index < data.size(); index++) {
            ConsulationViewModel consulationViewModel = new ConsulationViewModel();
            consulationViewModel.clone(data.get(index));
            items.add(consulationViewModel);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final LayoutConsulationParentAdapterBinding mLayoutConsulationParentAdapterBinding;

        public ViewHolder(LayoutConsulationParentAdapterBinding mLayoutConsulationParentAdapterBinding) {
            super(mLayoutConsulationParentAdapterBinding.getRoot());
            this.mLayoutConsulationParentAdapterBinding = mLayoutConsulationParentAdapterBinding;
        }
    }

    public interface OnOptionClicked {
        void onClicked(int position, String option);
    }
}
