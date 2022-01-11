package com.ab.hicarerun.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.CovidCheckListParentAdapter;
import com.ab.hicarerun.databinding.FragmentCovidCheckBinding;
import com.ab.hicarerun.handler.CovidCheckListHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.selfassessmodel.ResourceCheckList;
import com.ab.hicarerun.network.models.selfassessmodel.SelfAssessmentRequest;
import com.ab.hicarerun.network.models.selfassessmodel.SelfAssessmentResponse;
import com.ab.hicarerun.utils.LocaleHelper;
import com.ab.hicarerun.utils.SharedPreferencesUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import io.realm.RealmResults;

public class CovidCheckFragment extends DialogFragment implements CovidCheckListHandler {
    FragmentCovidCheckBinding mFragmentCovidCheckBinding;
    CovidCheckListParentAdapter mAdapter;
    private static final int COVID_REQ = 1000;
    private static final int COVID_SAVE_REQ = 2000;
    private List<SelfAssessmentRequest> checkList = null;
    private String resourceId = "";
    private HashMap<Integer, Boolean> checkMap = new HashMap<>();
    private HashMap<Integer, String> tempMap = new HashMap<>();
    private List<Boolean> isCheckList = null;

    public CovidCheckFragment() {
        // Required empty public constructor
    }

    public static CovidCheckFragment newInstance() {
        CovidCheckFragment fragment = new CovidCheckFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentCovidCheckBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_covid_check, container, false);
        mFragmentCovidCheckBinding.setHandler(this);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        setCancelable(false);
        return mFragmentCovidCheckBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            RealmResults<LoginResponse> LoginRealmModels =
                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                resourceId = LoginRealmModels.get(0).getUserID();
            }
            mFragmentCovidCheckBinding.txtTitle.setTypeface(mFragmentCovidCheckBinding.txtTitle.getTypeface(), Typeface.BOLD);
            mAdapter = new CovidCheckListParentAdapter(getActivity(), (position, option, isChecked) -> {
                if (checkMap != null && tempMap != null) {
                    checkMap.put(position, isChecked);
                    tempMap.put(position, option);
                    checkList = new ArrayList<>();
                    isCheckList = new ArrayList<>();
                    for (int i = 0; i < mAdapter.getItemCount(); i++) {
                        SelfAssessmentRequest checkModel = new SelfAssessmentRequest();
                        checkModel.setOptionId(mAdapter.getItem(i).getId());
                        checkModel.setOptionTitle(mAdapter.getItem(i).getTitle());
                        checkModel.setCreatedBy(resourceId);
                        checkModel.setDisplayOptionTitle(mAdapter.getItem(i).getDisplayTitle());
                        checkModel.setType(mAdapter.getItem(i).getOptionType());
                        checkModel.setMax(mAdapter.getItem(i).getMaxValue());
                        checkModel.setMin(mAdapter.getItem(i).getMinValue());
                        if (checkMap.containsKey(i)) {
                            checkModel.setIsSelected(checkMap.get(i));
                        } else {
                            checkModel.setIsSelected(false);
                        }
                        if (tempMap.containsKey(i)) {
                            checkModel.setOptionText(tempMap.get(i));
                        } else {
                            checkModel.setOptionText("");
                        }
                        checkList.add(checkModel);
                        isCheckList.add(checkList.get(i).getIsSelected());
                    }
                }
            });
            mFragmentCovidCheckBinding.recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mFragmentCovidCheckBinding.recycleView.setHasFixedSize(true);
            mFragmentCovidCheckBinding.recycleView.setClipToPadding(false);
            mFragmentCovidCheckBinding.recycleView.setAdapter(mAdapter);
            getCovidCheckList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isListChecked(List<Boolean> isCheckList) {
        for (Boolean isChecked : isCheckList) {
            if (!isChecked) {
                return false;
            }
        }
        return true;
    }

    private boolean isRangeChecked(List<SelfAssessmentRequest> checkList) {
        for (SelfAssessmentRequest range : checkList) {
            if (!range.getType().equals("TextBox") && !(range.getMin() >= Integer.valueOf(range.getOptionText()) && range.getMax() <= Integer.valueOf(range.getOptionText()))) {
                return false;
            }
        }
        return true;
    }


    private boolean isUnderRange(List<SelfAssessmentRequest> checkList) {
        boolean isInRange = true;
        for (SelfAssessmentRequest data : checkList) {
            if (data.getType()!=null && data.getType().equals("TextBox")) {
                if ((!data.getOptionText().equals("")) && (Double.parseDouble(data.getOptionText())>=data.getMin() && Double.parseDouble(data.getOptionText())<=data.getMax())) {
                    isInRange = true;
                } else {
                    isInRange = false;
                    break;
                }
            }
        }
        return isInRange;
    }

    private boolean isOptThere(List<SelfAssessmentRequest> checkList) {
        boolean isThere = true;
        for (SelfAssessmentRequest data : checkList) {
            if (data.getType()!=null && data.getType().equals("TextBox")) {
                if ((!data.getOptionText().equals(""))) {
                    isThere = true;
                } else {
                    isThere = false;
                    break;
                }
            }
        }
        return isThere;
    }


    private boolean isOptionThere(List<SelfAssessmentRequest> checkList) {
        for (SelfAssessmentRequest option : checkList) {
            if (option.getType()!=null && (option.getType().equals("ToolBox") || option.getType().equals("CheckBox")) && option.getOptionText().equals("")) {
                return false;
            }
        }
        return true;
    }

    private void getCovidCheckList() {
        try {
            if (getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        BaseApplication.getRealm().where(LoginResponse.class).findAll();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                    String resourceId = LoginRealmModels.get(0).getUserID();

                    NetworkCallController controller = new NetworkCallController();
                    controller.setListner(new NetworkResponseListner<List<ResourceCheckList>>() {
                        @Override
                        public void onResponse(int requestCode, List<ResourceCheckList> items) {
                            if (items != null && items.size() > 0) {
                                mAdapter.addData(items);
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(int requestCode) {

                        }
                    });
                    controller.getResourceCheckList(COVID_REQ, resourceId, LocaleHelper.getLanguage(getActivity()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveButtonClicked(View view) {
        try {
            if (isCheckList != null && isListChecked(isCheckList) && checkList != null) {
                if (isOptThere(checkList)) {
                    if (isUnderRange(checkList)) {
                        NetworkCallController controller = new NetworkCallController();
                        controller.setListner(new NetworkResponseListner<SelfAssessmentResponse>() {
                            @Override
                            public void onResponse(int requestCode, SelfAssessmentResponse response) {
                                if (response.getIsSuccess()) {
                                    try {
                                        dismiss();
                                        SharedPreferencesUtility.savePrefBoolean(Objects.requireNonNull(getActivity()), SharedPreferencesUtility.PREF_RESOURCE_SAVED, false);
                                        Toasty.success(getActivity(), response.getData(), Toast.LENGTH_LONG).show();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toasty.error(Objects.requireNonNull(getActivity()), response.getErrorMessage(), Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(int requestCode) {
                            }
                        });
                        controller.saveSelfAssessment(COVID_SAVE_REQ, checkList);
                    } else {
                        Toasty.error(getActivity(), "Invalid Entry.", Toasty.LENGTH_SHORT).show();
                    }
                } else {
                    Toasty.error(getActivity(), "All fields are mandatory.", Toasty.LENGTH_SHORT).show();
                }
            } else {
                Toasty.error(getActivity(), "All fields are mandatory.", Toasty.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}