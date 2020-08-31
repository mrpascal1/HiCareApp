package com.ab.hicarerun.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.HomeActivity;
import com.ab.hicarerun.activities.OnSiteAccountDetailsActivity;
import com.ab.hicarerun.activities.OnSiteTaskActivity;
import com.ab.hicarerun.adapter.OnSiteAccountAdapter;
import com.ab.hicarerun.databinding.FragmentOnsiteAccountBinding;
import com.ab.hicarerun.handler.OnAccountOnsiteClickHandler;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteAccounts;
import com.ab.hicarerun.utils.AppUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnSiteAccountFragment extends BaseFragment implements OnAccountOnsiteClickHandler {
    FragmentOnsiteAccountBinding fragmentOnsiteAccountBinding;
    private static final int ACCOUNT_REQ = 1000;
    private OnSiteAccountAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    private Integer pageNumber = 1;
    List<OnSiteAccounts> items = null;


    public OnSiteAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle oldInstanceState)
    {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    public static OnSiteAccountFragment newInstance() {
        Bundle args = new Bundle();
        OnSiteAccountFragment fragment = new OnSiteAccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentOnsiteAccountBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_onsite_account, container, false);
//        fragmentOnsiteAccountBinding.setHandler(this);
        getActivity().setTitle(getResources().getString(R.string.tool_onsite_account));
        return fragmentOnsiteAccountBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        setViewPagerView();
        fragmentOnsiteAccountBinding.swipeRefreshLayout.setOnRefreshListener(
                this::getOnSiteAccounts);
        fragmentOnsiteAccountBinding.recycleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        fragmentOnsiteAccountBinding.recycleView.setLayoutManager(layoutManager);
        fragmentOnsiteAccountBinding.swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_dark, android.R.color.holo_blue_light,
                android.R.color.holo_red_dark, android.R.color.holo_red_light,
                android.R.color.holo_green_dark, android.R.color.holo_green_light,
                android.R.color.holo_red_dark, android.R.color.holo_red_light);
        mAdapter = new OnSiteAccountAdapter(getActivity());
        mAdapter.setOnItemClickHandler(this);
        fragmentOnsiteAccountBinding.recycleView.setAdapter(mAdapter);
        getOnSiteAccounts();
    }

    private void getOnSiteAccounts() {
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
                            items = (List<OnSiteAccounts>) data;
                            fragmentOnsiteAccountBinding.swipeRefreshLayout.setRefreshing(false);
                            if (items != null) {
                                if (pageNumber == 1 && items.size() > 0) {
                                    mAdapter.setData(items);
                                    mAdapter.notifyDataSetChanged();
                                    fragmentOnsiteAccountBinding.emptyTask.setVisibility(View.GONE);
                                } else if (items.size() > 0) {
                                    mAdapter.addData(items);
                                    mAdapter.notifyDataSetChanged();
                                    fragmentOnsiteAccountBinding.emptyTask.setVisibility(View.GONE);

                                } else {
                                    fragmentOnsiteAccountBinding.emptyTask.setVisibility(View.VISIBLE);
                                    pageNumber--;
                                }
                            }
                        }

                        @Override
                        public void onFailure(int requestCode) {

                        }
                    });
                    controller.getOnSiteAccounts(ACCOUNT_REQ, userId);
                    mAdapter.setOnItemClickHandler(position -> startActivity(new Intent(getActivity(), OnSiteAccountDetailsActivity.class).putExtra(OnSiteAccountDetailsActivity.ARG_ACCOUNT, items.get(position).getAccount())));

                }
            }
        } catch (Exception e) {
            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                assert mLoginRealmModels.get(0) != null;
                String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "getGroomingDetails", lineNo, userName, DeviceName);
            }
        }
    }


    @Override
    public void onPrimaryMobileClicked(int position) {
        if (items.get(position).getAccount().getMobileC() != null) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + items.get(position).getAccount().getMobileC()));
            startActivity(callIntent);
        } else {
            AppUtils.showOkActionAlertBox(getActivity(), "Mobile number is unavaible.", (dialogInterface, i) -> dialogInterface.cancel());
        }

    }

    @Override
    public void onAlternateMobileClicked(int position) {
        if (items.get(position).getAccount().getAlternateMobileC() != null) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + items.get(position).getAccount().getAlternateMobileC()));
            startActivity(callIntent);
        } else {
            AppUtils.showOkActionAlertBox(getActivity(), "Alt. Mobile number is unavaible.", (dialogInterface, i) -> dialogInterface.cancel());
        }
    }

    @Override
    public void onTelePhoneClicked(int position) {
        if (items.get(position).getAccount().getPhone() != null) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + items.get(position).getAccount().getPhone()));
            startActivity(callIntent);
        } else {
            AppUtils.showOkActionAlertBox(getActivity(), "Phone number is unavaible.", (dialogInterface, i) -> dialogInterface.cancel());
        }
    }

    @Override
    public void onTrackLocationIconClicked(int position) {
        try {
            if (getActivity() != null) {
                if (((OnSiteTaskActivity) getActivity()).getmLocation() != null) {
                    if (mAdapter.getItem(position).getLongitude() != null) {
                        double latitude = mAdapter.getItem(position).getLatitude();
                        double longitude = mAdapter.getItem(position).getLongitude();
                        String uri = "http://maps.google.com/maps?f=d&hl=en&saddr=" + ((OnSiteTaskActivity)getActivity()).getmLocation().getLatitude() + "," + ((OnSiteTaskActivity)getActivity()).getmLocation().getLongitude() + "&daddr=" + latitude + "," + longitude;
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(Intent.createChooser(intent, "HiCare Run"));
                    } else {
                        Toast.makeText(getActivity(), " On-Site location not found!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "On-Site location not found!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "onTrackLocationIconClicked", lineNo, userName, DeviceName);
            }
        }
    }
}
