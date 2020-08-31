package com.ab.hicarerun.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.ExpandableRecentAdapter;
import com.ab.hicarerun.adapter.ViewActivityAdapter;
import com.ab.hicarerun.databinding.FragmentRecentOnsiteTaskBinding;
import com.ab.hicarerun.handler.OnRecentTaskClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.OnSiteModel.Account;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteHead;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteRecent;
import com.ab.hicarerun.network.models.OnSiteModel.SaveAccountAreaResponse;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecentOnsiteTaskFragment extends BaseFragment implements OnRecentTaskClickHandler {
    FragmentRecentOnsiteTaskBinding mFragmentRecentOnsiteTaskBinding;
    private static final String ARG_ACCOUNT = "ARG_ACCOUNT";
    private static final int RECENT_TASKS_REQ = 1000;
    private static final int DELETE_TASKS_REQ = 2000;
    private Account model;
    private Integer pageNumber = 1;
    //    private OnSiteRecentAdapter mAdapter;
//    private RecentListAdapter mAdapter;
    private ViewActivityAdapter viewAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<OnSiteHead> items = null;
    List<OnSiteRecent> SubItems = null;

    ExpandableRecentAdapter mRecentAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<OnSiteRecent>> expandableListDetail;

    public RecentOnsiteTaskFragment() {
        // Required empty public constructor
    }

    public static RecentOnsiteTaskFragment newInstance(Account model) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_ACCOUNT, model);
        RecentOnsiteTaskFragment fragment = new RecentOnsiteTaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle oldInstanceState)
    {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            model = getArguments().getParcelable(ARG_ACCOUNT);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getRecentTasks();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentRecentOnsiteTaskBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recent_onsite_task, container, false);
        getActivity().setTitle(getResources().getString(R.string.tool_activities));
        return mFragmentRecentOnsiteTaskBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentRecentOnsiteTaskBinding.swipeRefreshLayout.setOnRefreshListener(
                this::getRecentTasks);
        mFragmentRecentOnsiteTaskBinding.swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_dark, android.R.color.holo_blue_light,
                android.R.color.holo_red_dark, android.R.color.holo_red_light,
                android.R.color.holo_green_dark, android.R.color.holo_green_light,
                android.R.color.holo_red_dark, android.R.color.holo_red_light);
//        mFragmentRecentOnsiteTaskBinding.recycleView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(getActivity());
//        mFragmentRecentOnsiteTaskBinding.recycleView.setLayoutManager(layoutManager);
        getRecentTasks();
        mFragmentRecentOnsiteTaskBinding.swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
            getRecentTasks();
    }


    private void getRecentTasks() {
        try {
            if (getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        BaseApplication.getRealm().where(LoginResponse.class).findAll();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                    assert LoginRealmModels.get(0) != null;
                    String userId = LoginRealmModels.get(0).getUserID();
                    NetworkCallController controller = new NetworkCallController(this);
                    controller.setListner(new NetworkResponseListner() {
                        @Override
                        public void onResponse(int requestCode, Object data) {
                            items = (List<OnSiteHead>) data;
                            mFragmentRecentOnsiteTaskBinding.swipeRefreshLayout.setRefreshing(false);
                            expandableListDetail = new HashMap<>();
                            expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
                            if (items.size() > 0) {
                                mFragmentRecentOnsiteTaskBinding.emptyTask.setVisibility(View.GONE);
//                                mAdapter = new RecentListAdapter(items, getActivity());
                                for (int i = 0; i < items.size(); i++) {
                                    expandableListTitle.add(items.get(i).getHead());
                                    expandableListDetail.put(items.get(i).getHead(), items.get(i).getData());
                                    SubItems = expandableListDetail.get(expandableListTitle);
                                }
                                mRecentAdapter = new ExpandableRecentAdapter(getActivity(), expandableListTitle, expandableListDetail, mFragmentRecentOnsiteTaskBinding.expandableListView);
                                mFragmentRecentOnsiteTaskBinding.expandableListView.setAdapter(mRecentAdapter);
                                mRecentAdapter.setOnItemClickHandler(RecentOnsiteTaskFragment.this);
                                for (int i = 0; i < mFragmentRecentOnsiteTaskBinding.expandableListView.getExpandableListAdapter().getGroupCount(); i++) {
                                    mFragmentRecentOnsiteTaskBinding.expandableListView.expandGroup(i);
                                }

                            } else {
                                mFragmentRecentOnsiteTaskBinding.emptyTask.setVisibility(View.VISIBLE);
                                mFragmentRecentOnsiteTaskBinding.swipeRefreshLayout.setRefreshing(false);
                            }
                        }

                        @Override
                        public void onFailure(int requestCode) {
                            mFragmentRecentOnsiteTaskBinding.swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                    controller.getRecentAccountAreaActivity(RECENT_TASKS_REQ, model.getId(), userId, true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDeleteItemClicked(final int parent, final int child) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getResources().getString(R.string.delete_activity));
            builder.setMessage(getResources().getString(R.string.are_you_sure_you_want_to_delete_activity))
                    .setNegativeButton(getResources().getString(R.string.no), (dialog, id) -> dialog.cancel())
                    .setPositiveButton(getResources().getString(R.string.yes), (dialog, id) -> {
                        NetworkCallController controller = new NetworkCallController(RecentOnsiteTaskFragment.this);
                        controller.setListner(new NetworkResponseListner() {
                            @Override
                            public void onResponse(int requestCode, Object data) {
                                SaveAccountAreaResponse response = (SaveAccountAreaResponse) data;
                                if (response.getSuccess()) {
                                    dialog.dismiss();
                                    Toasty.success(getActivity(), getResources().getString(R.string.task_deleted_successfully)).show();
                                    getRecentTasks();
                                }
                            }

                            @Override
                            public void onFailure(int requestCode) {
                                dialog.dismiss();
                            }
                        });
                        controller.getDeleteOnSiteTasks(DELETE_TASKS_REQ, expandableListDetail.get(expandableListTitle.get(parent)).get(child).getId());
                    });
            AlertDialog alertdialog = builder.create();
            alertdialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onViewItemClicked(int parent, int child) {
        viewTaskDetailsDialog(parent, child);
    }

    private void viewTaskDetailsDialog(int parent, int child) {
        try {
            LayoutInflater li = LayoutInflater.from(getActivity());
            View promptsView = li.inflate(R.layout.layout_add_activity_dialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setView(promptsView);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCancelable(false);
            final RecyclerView recyclerView =
                    (RecyclerView) promptsView.findViewById(R.id.recycleView);
            final Button btnDone =
                    (Button) promptsView.findViewById(R.id.btnDone);
            final Button btnCancel =
                    (Button) promptsView.findViewById(R.id.btnCancel);
            final TextView txtTitle =
                    (TextView) promptsView.findViewById(R.id.txtTitle);

            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            viewAdapter = new ViewActivityAdapter(getActivity(), expandableListDetail.get(expandableListTitle.get(parent)).get(child).getActivityDetail());
            txtTitle.setText(expandableListDetail.get(expandableListTitle.get(parent)).get(child).getAreaSubType()+" Activity");
            recyclerView.setAdapter(viewAdapter);
            btnDone.setVisibility(View.GONE);
            btnCancel.setText(getResources().getString(R.string.ok));
            btnCancel.setOnClickListener(view -> alertDialog.cancel());
            alertDialog.setIcon(R.mipmap.logo);
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(int parent, int child) {

    }
}
