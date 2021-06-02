package com.ab.hicarerun.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.ProductCartAdapter;
import com.ab.hicarerun.adapter.ProductUnitAdapter;
import com.ab.hicarerun.databinding.FragmentProductCartBinding;
import com.ab.hicarerun.handler.OnCartItemClickHandler;
import com.ab.hicarerun.network.models.ProductCartModel.ProductCart;
import com.ab.hicarerun.utils.AppUtils;

import io.realm.RealmResults;

import static com.ab.hicarerun.BaseApplication.getRealm;

/**
 * A simple {@link Fragment} subclass.
 */
public class Product_Cart_Fragment extends BaseFragment implements OnCartItemClickHandler {
    FragmentProductCartBinding mFragmentProductCartBinding;
    private ProductCartAdapter mAdapter;
    int sumDiscounted = 0;
    int sumSaved = 0;

    public Product_Cart_Fragment() {
        // Required empty public constructor
    }

    public static Product_Cart_Fragment newInstance() {
        Bundle args = new Bundle();
        Product_Cart_Fragment fragment = new Product_Cart_Fragment();
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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentProductCartBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_cart, container, false);

        return mFragmentProductCartBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Cart");
        mAdapter = new ProductCartAdapter(getActivity());
        mFragmentProductCartBinding.recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFragmentProductCartBinding.recycleView.setHasFixedSize(true);
        mFragmentProductCartBinding.recycleView.setAdapter(mAdapter);
        mAdapter.setOnCartItemClickHandler(this);
        mFragmentProductCartBinding.lnrCart.setVisibility(View.GONE);
        getCartList();
        showCheckOutDetails();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    private void getCartList() {
        try {
            getActivity().invalidateOptionsMenu();
            RealmResults<ProductCart> mCartData = getRealm().where(ProductCart.class).findAll();
            if (mCartData != null && mCartData.size() > 0) {
                mAdapter.setData(mCartData);
                mAdapter.notifyDataSetChanged();
            }else {
                mFragmentProductCartBinding.lnrCart.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCheckOutDetails() {
        try {
            RealmResults<ProductCart> mCartData = getRealm().where(ProductCart.class).findAll();
            if (mCartData != null && mCartData.size() > 0) {
                int sumDiscounted = 0;
                int sumSaved = 0;
                for (int i = 0; i < mCartData.size(); i++) {
                    sumDiscounted += mCartData.get(i).getDiscountedAmount();
                    sumSaved += mCartData.get(i).getDiscount();
                }
                mFragmentProductCartBinding.txtSaved.setText("Saved " + "\u20B9" + " " + String.valueOf(sumSaved));
                mFragmentProductCartBinding.txtDiscountedAmount.setText("\u20B9" + " " + String.valueOf(sumDiscounted));
                mFragmentProductCartBinding.txtItems.setText(String.valueOf(mCartData.size()) + " Items");
                mFragmentProductCartBinding.lnrCart.setVisibility(View.VISIBLE);
                mFragmentProductCartBinding.lnrCheckout.setOnClickListener(v -> {
                    Toast.makeText(getActivity(), "Great", Toast.LENGTH_SHORT).show();
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDeleteCartClicked(int position) {
        RealmResults<ProductCart> results = getRealm().where(ProductCart.class).findAll();
        getRealm().beginTransaction();
//        results.remove(position);
        results.deleteFromRealm(position);
        getRealm().commitTransaction();
        mAdapter.remove(position);
        getCartList();
    }

    @Override
    public void onAddQuantityClicked(int position) {
        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            sumDiscounted += mAdapter.getItem(i).getDiscountedAmount();
            sumSaved += mAdapter.getItem(i).getDiscount();
        }
        mFragmentProductCartBinding.txtItems.setText(String.valueOf(mAdapter.getItemCount()) + " Items");
        mFragmentProductCartBinding.txtSaved.setText("Saved " + "\u20B9" + " " + String.valueOf(sumSaved));
        mFragmentProductCartBinding.txtDiscountedAmount.setText("\u20B9" + " " + String.valueOf(sumDiscounted));
    }

    @Override
    public void onSubstractQuantityClicked(int position) {
        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            sumDiscounted += mAdapter.getItem(i).getDiscountedAmount();
            sumSaved += mAdapter.getItem(i).getDiscount();
        }
        mFragmentProductCartBinding.txtItems.setText(String.valueOf(mAdapter.getItemCount()) + " Items");
        mFragmentProductCartBinding.txtSaved.setText("Saved " + "\u20B9" + " " + String.valueOf(sumSaved));
        mFragmentProductCartBinding.txtDiscountedAmount.setText("\u20B9" + " " + String.valueOf(sumDiscounted));
    }
}
