package com.ab.hicarerun.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.QuizActivity;
import com.ab.hicarerun.activities.QuizLeaderBoardActivity;
import com.ab.hicarerun.adapter.QuizCategoryAdapter;
import com.ab.hicarerun.adapter.QuizLevelMatrixAdapter;
import com.ab.hicarerun.databinding.FragmentQuizCategoryBinding;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.generalmodel.GeneralData;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.quizlevelmodel.QuizLevelData;
import com.ab.hicarerun.network.models.quizlevelmodel.QuizLevelModelBase;
import com.ab.hicarerun.network.models.quizmodel.QuizCategoryData;
import com.ab.hicarerun.network.models.quizmodel.QuizPuzzleStats;
import com.ab.hicarerun.utils.LocaleHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.RealmResults;

public class FragmentQuizCategory extends BaseFragment {
    FragmentQuizCategoryBinding mFragmentQuizCategoryBinding;
    private static final int QUIZ_CATEGORY = 1000;
    RecyclerView.LayoutManager layoutManager;
    private QuizCategoryAdapter mAdapter;
    RealmResults<GeneralData> mGeneralRealmModel;
    String resourceId = "";
    QuizLevelMatrixAdapter quizLevelMatrixAdapter;
    ArrayList<QuizLevelData> quizLevelDataList;

    public FragmentQuizCategory() {
        // Required empty public constructor
    }

    public static FragmentQuizCategory newInstance() {
        FragmentQuizCategory fragment = new FragmentQuizCategory();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentQuizCategoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_quiz_category, container, false);
        getActivity().setTitle("");
        quizLevelDataList = new ArrayList<>();
        quizLevelMatrixAdapter = new QuizLevelMatrixAdapter(requireActivity(), quizLevelDataList);
        return mFragmentQuizCategoryBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentQuizCategoryBinding.recycleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mFragmentQuizCategoryBinding.recycleView.setLayoutManager(layoutManager);
        mAdapter = new QuizCategoryAdapter(getActivity());
        mFragmentQuizCategoryBinding.recycleView.setAdapter(mAdapter);
        getQuizCategory();
        mFragmentQuizCategoryBinding.gameNameTv.setTypeface(null, Typeface.BOLD);
        mFragmentQuizCategoryBinding.infoIv.setOnClickListener(v -> {
            showInstructionDialog();
            //showInstructionsDialog();
            //showDialog();
        });
        mFragmentQuizCategoryBinding.levelTv.setOnClickListener(v -> {
            //showDialog();
        });
        mFragmentQuizCategoryBinding.leaderBoardIv.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), QuizLeaderBoardActivity.class);
            startActivity(intent);
        });
        mFragmentQuizCategoryBinding.backIv.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
        mFragmentQuizCategoryBinding.lnrWheel.setOnClickListener(view1 -> replaceFragment(SpinWheelFragment.newInstance(), "QuizFragmentCategory - SpinFragment"));
    }

    private void showInstructionDialog() {
        try {

            LayoutInflater li = LayoutInflater.from(getActivity());

            View promptsView = li.inflate(R.layout.layout_instruction_info_dialog, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext());

            alertDialogBuilder.setView(promptsView);
            final AlertDialog alertDialog = alertDialogBuilder.create();

            final TextView popupTitleTv =
                    promptsView.findViewById(R.id.popupTitleTv);
            final TextView txtInfo =
                    promptsView.findViewById(R.id.txtInfo);
            final AppCompatButton btnOk =
                    promptsView.findViewById(R.id.btnOk);

            popupTitleTv.setText(R.string.shiksha_instructions);
            txtInfo.setText(R.string.shiksha_instructions_data);
            /*txtInfo.setText("1. Choose the correct answer to earn points.\n" +
                    "2. Complete all the given categories.\n" +
                    "3. Proceed to the next level and buy yourself new items from bazaar.\n" +
                    "4. You can play only 3 times in a day.");*/
            txtInfo.setTypeface(txtInfo.getTypeface(), Typeface.BOLD);

            btnOk.setOnClickListener(v -> alertDialog.dismiss());
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getQuizCategory() {
        try {
            RealmResults<LoginResponse> LoginRealmModels =
                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                resourceId = LoginRealmModels.get(0).getUserID();
                NetworkCallController controller = new NetworkCallController(this);
                controller.setListner(new NetworkResponseListner<List<QuizCategoryData>>() {

                    @Override
                    public void onResponse(int requestCode, List<QuizCategoryData> items) {
                        if (items != null && items.size() > 0) {
                            mFragmentQuizCategoryBinding.recycleView.setVisibility(View.VISIBLE);
                            mFragmentQuizCategoryBinding.gamesNotAvlTv.setVisibility(View.GONE);
                            mAdapter.setData(items);
                            mAdapter.setOnItemClickHandler(position -> {
                                Intent intent = new Intent(getContext(), QuizActivity.class);
                                intent.putExtra("puzzleId", mAdapter.getItem(position).getPuzzleId());
                                intent.putExtra("puzzleTitle", mAdapter.getItem(position).getPuzzleTitleDisplay());
                                startActivity(intent);
                                //replaceFragment(QuizFragment.newInstance(mAdapter.getItem(position).getPuzzleId()), "QuizFragmentCategory - QuizFragment");
                                //Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
                            });
                        }else {
                            //mAdapter.setData(null);
                            mFragmentQuizCategoryBinding.recycleView.setVisibility(View.GONE);
                            mFragmentQuizCategoryBinding.gamesNotAvlTv.setVisibility(View.VISIBLE);
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(int requestCode) {

                    }
                });
                controller.getQuizCategory(QUIZ_CATEGORY, resourceId, LocaleHelper.getLanguage(requireContext()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showInstructionsDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Shiksha Instructions");
        builder.setMessage("1. Choose the correct answer to earn points.\n" +
                "2. Complete all the given categories.\n" +
                "3. Proceed to the next level and buy yourself new items from bazaar.\n" +
                "4. You can only play 3 times in a day.");
        builder.setPositiveButton("Ok", (dialog, which) -> {
            dialog.cancel();
        });
        builder.show();
    }

    private void showDialog(){
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.layout_quiz_level_matrix_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        final AppCompatButton btnOk = promptsView.findViewById(R.id.btnOk);
        final RecyclerView recyclerView = promptsView.findViewById(R.id.recycleView);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(quizLevelMatrixAdapter);

        btnOk.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.show();
        alertDialog.setCancelable(false);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);

    }
    private void getPuzzleStatsForRes(){
        NetworkCallController controller = new NetworkCallController(this);
        controller.setListner(new NetworkResponseListner<QuizPuzzleStats>(){
            @Override
            public void onResponse(int requestCode, QuizPuzzleStats response) {
                Log.d("TAG", response+"");
                if (response != null) {
                    if (response.isSuccess()) {
                        String levelName = response.getData().getLevelName();
                        Picasso.get().load(response.getData().getLevelIcon()).placeholder(R.drawable.ic_level_common).into(mFragmentQuizCategoryBinding.awardIv);
                        /*if (levelName.equalsIgnoreCase("Basic")) {
                            mFragmentQuizCategoryBinding.awardIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_level_common));
                        } else if (levelName.equalsIgnoreCase("Intermediate")) {
                            mFragmentQuizCategoryBinding.awardIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_level_common));
                        } else if (levelName.equalsIgnoreCase("Expert")) {
                            mFragmentQuizCategoryBinding.awardIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_level_common));
                        }*/
                        mFragmentQuizCategoryBinding.levelTv.setTypeface(mFragmentQuizCategoryBinding.levelTv.getTypeface(), Typeface.BOLD);
                        mFragmentQuizCategoryBinding.pointsTv.setTypeface(mFragmentQuizCategoryBinding.pointsTv.getTypeface(), Typeface.BOLD);
                        mFragmentQuizCategoryBinding.levelTv.setText(" " + Objects.requireNonNull(response.getData()).getLevelNameDisplay());
                        mFragmentQuizCategoryBinding.pointsTv.setText(Objects.requireNonNull(response.getData()).getPoints()+"");
                    }
                }
            }

            @Override
            public void onFailure(int requestCode) {

            }
        });
        controller.getPuzzleStatsForRes(202122, resourceId, LocaleHelper.getLanguage(requireContext()));
    }

    private void getPuzzleLevel(){
        quizLevelDataList.clear();
        NetworkCallController controller = new NetworkCallController(this);
        controller.setListner(new NetworkResponseListner<QuizLevelModelBase>() {
            @Override
            public void onResponse(int requestCode, QuizLevelModelBase response) {
                if (response.isSuccess()){
                    for (int i=0; i<response.getData().size(); i++){
                        int id = response.getData().get(i).getId();
                        String levelName = response.getData().get(i).getLevelName();
                        String pointsInfo = response.getData().get(i).getPointsInfo();
                        quizLevelDataList.add(new QuizLevelData(id, levelName, pointsInfo));
                    }
                    new QuizLevelModelBase(response.isSuccess(), quizLevelDataList,
                            response.getErrorMessage(), response.getParam1(), response.getResponseMessage());
                }
                quizLevelMatrixAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int requestCode) {

            }
        });
        controller.getPuzzleLevel(202126, resourceId, LocaleHelper.getLanguage(requireContext()));
    }

    @Override
    public void onResume() {
        Log.d("TAG", "onResume Called");
        getQuizCategory();
        getPuzzleStatsForRes();
        getPuzzleLevel();
        super.onResume();
    }
}