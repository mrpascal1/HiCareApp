//package com.ab.hicarerun.adapter;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.graphics.Typeface;
//import android.text.Spannable;
//import android.text.SpannableStringBuilder;
//import android.text.style.ForegroundColorSpan;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CompoundButton;
//
//import androidx.databinding.DataBindingUtil;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.ab.hicarerun.R;
//import com.ab.hicarerun.databinding.LayoutChemicalsDialogAdapterBinding;
//import com.ab.hicarerun.databinding.LayoutCompletionListAdapterBinding;
//import com.ab.hicarerun.databinding.LayoutNewCompletionListBinding;
//import com.ab.hicarerun.handler.OnCheckListItemClickHandler;
//import com.ab.hicarerun.handler.OnListItemClickHandler;
//import com.ab.hicarerun.network.models.ChemicalModel.Chemicals;
//import com.ab.hicarerun.network.models.GeneralModel.TaskCheckList;
//import com.ab.hicarerun.viewmodel.ChemicalViewModel;
//import com.squareup.picasso.Picasso;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static android.view.View.GONE;
//
///**
// * Created by Arjun Bhatt on 4/6/2020.
// */
//public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.ViewHolder> {
//
//    private OnCheckListItemClickHandler onItemClickHandler;
//    private final Context mContext;
//    private static List<TaskCheckList> items = new ArrayList<>();
//    private int selectedPos = 0;
//
//    public CheckListAdapter(Context context, List<TaskCheckList> list) {
//        items = list;
//        this.mContext = context;
//    }
//
//
//    @NotNull
//    @Override
//    public CheckListAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
//        LayoutNewCompletionListBinding mLayoutCompletionListAdapterBinding =
//                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
//                        R.layout.layout_new_completion_list, parent, false);
//        return new CheckListAdapter.ViewHolder(mLayoutCompletionListAdapterBinding);
//    }
//
//    @Override
//    public void onBindViewHolder(@NotNull final CheckListAdapter.ViewHolder holder, final int position) {
//        try {
//
//            holder.mLayoutCompletionListAdapterBinding.radioYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (isChecked) {
//                        onItemClickHandler.onPositiveButtonClicked(position);
//                        holder.mLayoutCompletionListAdapterBinding.radioNo.setChecked(false);
//                        holder.mLayoutCompletionListAdapterBinding.radioNotReq.setChecked(false);
//                    }
//                }
//            });
//
//            holder.mLayoutCompletionListAdapterBinding.radioNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (isChecked) {
//                        onItemClickHandler.onNegativeButtonClicked(position);
//                        holder.mLayoutCompletionListAdapterBinding.radioYes.setChecked(false);
//                        holder.mLayoutCompletionListAdapterBinding.radioNotReq.setChecked(false);
//                    }
//                }
//            });
//
//            holder.mLayoutCompletionListAdapterBinding.radioNotReq.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (isChecked) {
//                        onItemClickHandler.onNotRequiredButtonClicked(position);
//                        holder.mLayoutCompletionListAdapterBinding.radioYes.setChecked(false);
//                        holder.mLayoutCompletionListAdapterBinding.radioNo.setChecked(false);
//                    }
//                }
//            });
//
//            if (items.get(position).getTakePicture()) {
//                holder.mLayoutCompletionListAdapterBinding.relPhoto.setVisibility(View.VISIBLE);
//            } else {
//                holder.mLayoutCompletionListAdapterBinding.relPhoto.setVisibility(GONE);
//            }
//
//            holder.mLayoutCompletionListAdapterBinding.lnrUpload.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onItemClickHandler.onCameraClicked(position);
//                }
//            });
//
//            holder.mLayoutCompletionListAdapterBinding.imageCancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    items.get(position).setIconUrl(null);
//                    holder.mLayoutCompletionListAdapterBinding.lnrImage.setVisibility(GONE);
//                    holder.mLayoutCompletionListAdapterBinding.lnrUpload.setVisibility(View.VISIBLE);
//                }
//            });
//
//            if (items.get(position).getIconUrl() != null && !items.get(position).getIconUrl().equals("")) {
//                Picasso.get().load(items.get(position).getIconUrl()).into(holder.mLayoutCompletionListAdapterBinding.imgUploadedCheque);
//                holder.mLayoutCompletionListAdapterBinding.lnrUpload.setVisibility(View.GONE);
//                holder.mLayoutCompletionListAdapterBinding.lnrImage.setVisibility(View.VISIBLE);
//            } else {
//                holder.mLayoutCompletionListAdapterBinding.lnrUpload.setVisibility(View.VISIBLE);
//                holder.mLayoutCompletionListAdapterBinding.lnrImage.setVisibility(GONE);
//            }
//
//            SpannableStringBuilder builder = new SpannableStringBuilder();
//
//            builder.append(items.get(position).getDetailDescription());
//            int start = builder.length();
//            builder.append("*");
//            int end = builder.length();
//
//            builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            holder.mLayoutCompletionListAdapterBinding.txtQuery.setText(builder);
//
//
////            holder.mLayoutCompletionListAdapterBinding.txtQuery.setText(items.get(position).getDetailDescription());
////            holder.mLayoutCompletionListAdapterBinding.txtQuery.setTypeface(holder.mLayoutCompletionListAdapterBinding.txtQuery.getTypeface(), Typeface.BOLD);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//
//    }
//
//    public void setOnItemClickHandler(OnCheckListItemClickHandler onItemClickHandler) {
//        this.onItemClickHandler = onItemClickHandler;
//    }
//
//
//    public void setData(List<Chemicals> data) {
//        items.clear();
//
//
//    }
//
//    public void addData(List<Chemicals> data) {
//        items.clear();
//
//    }
//
//    public TaskCheckList getItem(int position) {
//        return items.get(position);
//    }
//
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        private final LayoutNewCompletionListBinding mLayoutCompletionListAdapterBinding;
//
//        public ViewHolder(LayoutNewCompletionListBinding mLayoutCompletionListAdapterBinding) {
//            super(mLayoutCompletionListAdapterBinding.getRoot());
//            this.mLayoutCompletionListAdapterBinding = mLayoutCompletionListAdapterBinding;
//        }
//    }
//
//}
//
