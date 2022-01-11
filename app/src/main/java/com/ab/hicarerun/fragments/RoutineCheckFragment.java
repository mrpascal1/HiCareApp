package com.ab.hicarerun.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.RoutineCheckUpParentAdapter;
import com.ab.hicarerun.databinding.FragmentRoutineCheckBinding;
import com.ab.hicarerun.handler.UserRoutineCheckClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.routinemodel.RoutineQuestion;
import com.ab.hicarerun.network.models.routinemodel.SaveRoutineResponse;
import com.ab.hicarerun.network.models.routinemodel.TechRoutineData;
import com.ab.hicarerun.utils.LocaleHelper;

import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoutineCheckFragment extends DialogFragment implements UserRoutineCheckClickHandler {
    FragmentRoutineCheckBinding mFragmentRoutineCheckBinding;
    public static final String ARGS_RES = "ARGS_RES";
    private static final int ROUTINE_REQ = 1000;
    private static final int ROUTINE_SAVE_REQ = 2000;
    private String resourceId = "";
    private RoutineCheckUpParentAdapter mAdapter;
    private TechRoutineData routineData;
    private ProgressDialog progressD;
    private Context mContext;
    private static RoutineDialogInterface mCallback;

    //  @Override
    //   public void onAttach(@NotNull Context context) {
    //     super.onAttach(context);
    //    try {
    //      mCallback = (RoutineCheckFragment.RoutineDialogInterface) context;
    //  } catch (ClassCastException e) {
    //       throw new ClassCastException(context.toString()
    //              + " must implement FragmentToActivity");
    //   }
    //  }

    public static RoutineCheckFragment newInstance(String technicianId, RoutineDialogInterface mCallbackk) {
        Bundle args = new Bundle();
        args.putString(ARGS_RES, technicianId);
        RoutineCheckFragment fragment = new RoutineCheckFragment();
        mCallback = mCallbackk;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            resourceId = getArguments().getString(ARGS_RES);
        }
    }

    public void onResume() {
        Window window = getDialog().getWindow();
        Point size = new Point();


        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.9), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentRoutineCheckBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_routine_check, container, false);
        mFragmentRoutineCheckBinding.setHandler(this);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return mFragmentRoutineCheckBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressD = new ProgressDialog(getActivity(), R.style.TransparentProgressDialog);
        progressD.setCancelable(false);
        progressD.show();
        mAdapter = new RoutineCheckUpParentAdapter(getActivity(), (position, primary, secondary) -> {
            routineData.getRoutineQuestions().get(position).setPrimarySelection(primary);
            routineData.getRoutineQuestions().get(position).setSecondarySelection(secondary);
        });
        mFragmentRoutineCheckBinding.recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFragmentRoutineCheckBinding.recycleView.setHasFixedSize(true);
        mFragmentRoutineCheckBinding.recycleView.setClipToPadding(false);
        mFragmentRoutineCheckBinding.recycleView.setAdapter(mAdapter);
        getRoutine();
    }

    private void getRoutine() {
        try {
            NetworkCallController controller = new NetworkCallController();
            controller.setListner(new NetworkResponseListner<TechRoutineData>() {
                @Override
                public void onResponse(int requestCode, TechRoutineData response) {
                    routineData = new TechRoutineData();
                    routineData.setResourceId(response.getResourceId());
                    routineData.setRoutineQuestions(response.getRoutineQuestions());
                    progressD.dismiss();
                    if (response != null) {
                        if (response.getRoutineQuestions() != null && response.getRoutineQuestions().size() > 0) {
                            mAdapter.addData(response.getRoutineQuestions());
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(int requestCode) {
                    progressD.dismiss();
                }
            });
            controller.getRoutineResponse(ROUTINE_REQ, resourceId, LocaleHelper.getLanguage(getActivity()));
        } catch (Exception e) {
            progressD.dismiss();
            Log.i("RoutineCheckUp", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveButtonClicked(View view) {
        try {
            progressD.show();
            if (isPrimaryChecked(routineData.getRoutineQuestions())) {
                if (isSecondaryChecked(routineData.getRoutineQuestions())) {
                    NetworkCallController controller = new NetworkCallController();
                    controller.setListner(new NetworkResponseListner<SaveRoutineResponse>() {
                        @Override
                        public void onResponse(int requestCode, SaveRoutineResponse response) {
                            if (response.getIsSuccess()) {
                                progressD.dismiss();
                                dismiss();
                            } else {
                                progressD.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(int requestCode) {

                        }
                    });
                    controller.saveRoutineCheckList(ROUTINE_SAVE_REQ, routineData);
                } else {
                    progressD.dismiss();
                    Toasty.error(getActivity(), "All fields are required!", Toasty.LENGTH_SHORT).show();
                }
            } else {
                progressD.dismiss();
                Toasty.error(getActivity(), "All fields are required!", Toasty.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isPrimaryChecked(List<RoutineQuestion> routineQuestions) {
        for (RoutineQuestion data : routineQuestions) {
            Log.i("RoutineCheck", "Primary : " + data.getPrimarySelection());
            Log.i("RoutineCheck", "Secondary : " + data.getSecondarySelection());
            if (data.getPrimarySelection().equals("")) {
                return false;
            }
        }
        return true;
    }

    private boolean isSecondaryChecked(List<RoutineQuestion> routineQuestions) {
        for (RoutineQuestion data : routineQuestions) {
            Log.i("RoutineCheck", "Primary : " + data.getPrimarySelection());
            Log.i("RoutineCheck", "Secondary : " + data.getSecondarySelection());
            if (data.getSecondarySelection().equals("")) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        try {
            mCallback.onDialogDismissed();
        } catch (ClassCastException e) {
            e.getStackTrace();
        }
    }

    public interface RoutineDialogInterface {
        void onDialogDismissed();
    }

}