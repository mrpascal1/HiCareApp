package com.ab.hicarerun.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.HicareProductAdapter;
import com.ab.hicarerun.databinding.FragmentHicareProductsBinding;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.productcartmodel.ProductCart;
import com.ab.hicarerun.network.models.productmodel.ProductData;

import java.util.List;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class HicareProductsFragment extends BaseFragment {
    FragmentHicareProductsBinding mFragmentHicareProductsBinding;
    private HicareProductAdapter mAdapter;
    private static final int PRODUCT_REQ = 1000;
    private int sumActual = 0;


    public HicareProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle oldInstanceState)
    {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    public static HicareProductsFragment newInstance() {
        Bundle args = new Bundle();
        HicareProductsFragment fragment = new HicareProductsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentHicareProductsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_hicare_products, container, false);
        getActivity().setTitle(getString(R.string.hicare_products));
        getActivity().invalidateOptionsMenu();
        return mFragmentHicareProductsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentHicareProductsBinding.recycleView.setHasFixedSize(true);
        mFragmentHicareProductsBinding.recycleView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter = new HicareProductAdapter(getActivity());
        mFragmentHicareProductsBinding.recycleView.setAdapter(mAdapter);
        mFragmentHicareProductsBinding.lnrCart.setVisibility(View.GONE);
        getProducts();
        showViewCart();
    }

    private void showViewCart() {
        try {
            RealmResults<ProductCart> mCartData = getRealm().where(ProductCart.class).findAll();
            int sumDiscounted = 0;
            int sumSaved = 0;
            if (mCartData != null && mCartData.size() > 0) {
                for (int i = 0; i < mCartData.size(); i++) {
                    sumDiscounted += mCartData.get(i).getDiscountedAmount();
                    sumSaved += mCartData.get(i).getDiscount();
                }
                mFragmentHicareProductsBinding.txtSaved.setText("Saved " + "\u20B9" + " " + String.valueOf(sumSaved));
                mFragmentHicareProductsBinding.txtDiscountedAmount.setText("\u20B9" + " " + String.valueOf(sumDiscounted));
                mFragmentHicareProductsBinding.txtItems.setText(String.valueOf(mCartData.size()) + " Items");
                mFragmentHicareProductsBinding.lnrCart.setVisibility(View.VISIBLE);
                mFragmentHicareProductsBinding.lnrViewCart.setOnClickListener(v -> {
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getProducts() {
        try {
            NetworkCallController controller = new NetworkCallController(this);
            controller.setListner(new NetworkResponseListner<List<ProductData>>() {
                @Override
                public void onResponse(int requestCode, List<ProductData> items) {
                    if (items != null && items.size() > 0) {
                        mAdapter.setData(items);
                        mAdapter.notifyDataSetChanged();
                    }
                    mAdapter.setOnItemClickHandler(position -> {
                        replaceFragment(ProductDetailFragment.newInstance(items.get(position)), "HicareProductFragment-ProductDetailFragment");
                    });
                }

                @Override
                public void onFailure(int requestCode) {

                }
            });
            controller.getProducts(PRODUCT_REQ);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
