//package com.ab.hicarerun.adapter;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.util.Base64;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.Toast;
//
//import androidx.databinding.DataBindingUtil;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.ab.hicarerun.R;
//import com.ab.hicarerun.databinding.LayoutRoutineCheckAdapterBinding;
//import com.ab.hicarerun.databinding.TechnicianRoutineAdapterBinding;
//import com.ab.hicarerun.handler.OnListItemClickHandler;
//import com.ab.hicarerun.network.NetworkCallController;
//import com.ab.hicarerun.network.NetworkResponseListner;
//import com.ab.hicarerun.network.models.RoutineModel.RoutineQuestion;
//import com.ab.hicarerun.network.models.TechnicianRoutineModel.RoutineQuestions;
//import com.ab.hicarerun.network.models.TechnicianRoutineModel.TechnicianData;
//import com.ab.hicarerun.viewmodel.RoutineViewModel;
//import com.ab.hicarerun.viewmodel.TechnicianDataViewModel;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
///**
// * Created by Arjun Bhatt on 8/13/2020.
// */
//public class RoutineCheckUpAdapter extends RecyclerView.Adapter<RoutineCheckUpAdapter.ViewHolder> {
//    private OnListItemClickHandler onItemClickHandler;
//    private final Context mContext;
//    private List<RoutineViewModel> items = null;
//    private RadioGroup lastCheckedRadioGroup = null;
//
//    public RoutineCheckUpAdapter(Context context) {
//        if (items == null) {
//            items = new ArrayList<>();
//        }
//        this.mContext = context;
//    }
//
//    @NotNull
//    @Override
//    public RoutineCheckUpAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
//        LayoutRoutineCheckAdapterBinding mLayoutRoutineCheckAdapterBinding =
//                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
//                        R.layout.layout_routine_check_adapter, parent, false);
//        return new RoutineCheckUpAdapter.ViewHolder(mLayoutRoutineCheckAdapterBinding);
//    }
//
//    @Override
//    public void onBindViewHolder(@NotNull RoutineCheckUpAdapter.ViewHolder holder, final int position) {
//        try {
//            Log.i("RoutineCheckUpAdapter", "bind");
//
//            holder.mLayoutRoutineCheckAdapterBinding.txtQuest.setText(items.get(position).getQuestionTitle());
////            int id = (position + 1) * 100;
////            for (String value : items.get(position).getValueList()) {
////                RadioButton rb = new RadioButton(mContext);
////                rb.setId(id);
////                rb.setText(value);
////                holder.mLayoutRoutineCheckAdapterBinding.routineGrp.removeAllViews();
////                holder.mLayoutRoutineCheckAdapterBinding.routineGrp.addView(rb);
////
////
////            }
//            holder.mLayoutRoutineCheckAdapterBinding.routineGrp.removeAllViews();
//            for(int i =0; i<items.get(position).getValueList().size(); i++){
//                final RadioButton rbn = new RadioButton(mContext);
//                rbn.setId(i);
//                rbn.setText(items.get(position).getValueList().get(i).getValue());
//                rbn.setTextSize(15);
//                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
//                params.setMargins(10, 10, 2, 1);
//                holder.mLayoutRoutineCheckAdapterBinding.routineGrp.addView(rbn, params);
//            }
//            holder.mLayoutRoutineCheckAdapterBinding.routineGrp.setOnCheckedChangeListener((group, checkedId) -> {
//                if (lastCheckedRadioGroup != null
//                        && lastCheckedRadioGroup.getCheckedRadioButtonId()
//                        != holder.mLayoutRoutineCheckAdapterBinding.routineGrp.getCheckedRadioButtonId()
//                        && lastCheckedRadioGroup.getCheckedRadioButtonId() != -1) {
//                    lastCheckedRadioGroup.clearCheck();
//                }
//                lastCheckedRadioGroup = holder.mLayoutRoutineCheckAdapterBinding.routineGrp;
//                if (holder.mLayoutRoutineCheckAdapterBinding.routineGrp.getCheckedRadioButtonId() == 0) {
//                    if (items.get(position).getValueYes() != null) {
//                        if (items.get(position).getValueYes().getType().equals("TextBox")) {
//                            holder.mLayoutRoutineCheckAdapterBinding.edtValue.setVisibility(View.VISIBLE);
//                            holder.mLayoutRoutineCheckAdapterBinding.lnrSpinner.setVisibility(View.GONE);
//                        }
//                    }
//                } else {
//                    if (items.get(position).getValueNo() != null) {
//                        if (items.get(position).getValueNo().getType().equals("DropDown")) {
//                            holder.mLayoutRoutineCheckAdapterBinding.lnrSpinner.setVisibility(View.VISIBLE);
//                            holder.mLayoutRoutineCheckAdapterBinding.edtValue.setVisibility(View.GONE);
//                            ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(Objects.requireNonNull(mContext),
//                                    R.layout.spinner_layout_new, items.get(position).getValueNo().getValue());
//                            statusAdapter.setDropDownViewResource(R.layout.spinner_popup);
//                            holder.mLayoutRoutineCheckAdapterBinding.spnValue.setAdapter(statusAdapter);
//
//                            holder.mLayoutRoutineCheckAdapterBinding.spnValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                @Override
//                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                    Toast.makeText(mContext, holder.mLayoutRoutineCheckAdapterBinding.spnValue.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
//                                }
//
//                                @Override
//                                public void onNothingSelected(AdapterView<?> parent) {
//
//                                }
//                            });
//                        }
//                    }
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void setOnItemClickHandler(OnListItemClickHandler onItemClickHandler) {
//        this.onItemClickHandler = onItemClickHandler;
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//    public RoutineViewModel getItem(int position) {
//        return items.get(position);
//    }
//
//    public void setData(List<RoutineQuestion> data) {
//        items.clear();
//        for (int index = 0; index < data.size(); index++) {
//            RoutineViewModel routineViewModel = new RoutineViewModel();
//            routineViewModel.clone(data.get(index));
//            items.add(routineViewModel);
//        }
//    }
//
//    public void addData(List<RoutineQuestion> data) {
//        items.clear();
//        for (int index = 0; index < data.size(); index++) {
//            RoutineViewModel routineViewModel = new RoutineViewModel();
//            routineViewModel.clone(data.get(index));
//            items.add(routineViewModel);
//        }
//        Log.i("RoutineCheckUpAdapter", "called");
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        private final LayoutRoutineCheckAdapterBinding mLayoutRoutineCheckAdapterBinding;
//
//        public ViewHolder(LayoutRoutineCheckAdapterBinding mLayoutRoutineCheckAdapterBinding) {
//            super(mLayoutRoutineCheckAdapterBinding.getRoot());
//            this.mLayoutRoutineCheckAdapterBinding = mLayoutRoutineCheckAdapterBinding;
//        }
//    }
//
//}
//
