package com.ab.hicarerun.fragments;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.ProductUnitAdapter;
import com.ab.hicarerun.databinding.FragmentProductDetailBinding;
import com.ab.hicarerun.network.models.ProductCartModel.ProductCart;
import com.ab.hicarerun.network.models.ProductModel.ProductData;
import com.ab.hicarerun.utils.AppUtils;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class
ProductDetailFragment extends BaseFragment {
    FragmentProductDetailBinding mFragmentProductDetailBinding;
    private ProductData mProductData;
    private static final String ARG_PRODUCT = "ARG_PRODUCT";
    private ProductUnitAdapter mAdapter;
    private int selectedPos = 0;
    private int quantity = 1;
    LayerDrawable icon;


    public ProductDetailFragment() {
        // Required empty public constructor
    }

    public static ProductDetailFragment newInstance(ProductData productData) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_PRODUCT, productData);
        ProductDetailFragment fragment = new ProductDetailFragment();
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
            mProductData = getArguments().getParcelable(ARG_PRODUCT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentProductDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_detail, container, false);
        getActivity().setTitle(getString(R.string.details));
        getActivity().invalidateOptionsMenu();
        return mFragmentProductDetailBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new ProductUnitAdapter(getActivity());
        mFragmentProductDetailBinding.recycleView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mFragmentProductDetailBinding.recycleView.setHasFixedSize(true);
        mFragmentProductDetailBinding.recycleView.setAdapter(mAdapter);
        setProductDetails();
    }

    private void setProductDetails() {
        try {
            mFragmentProductDetailBinding.txtTitle.setText(mProductData.getPlanName());
            mFragmentProductDetailBinding.txtDescription.setText(mProductData.getServiceDescription());
            if (mProductData.getServicePlanUnits() != null && mProductData.getServicePlanUnits().size() > 0) {
                mAdapter.setData(mProductData.getServicePlanUnits());
                mAdapter.notifyDataSetChanged();
                mFragmentProductDetailBinding.txtActualAmount.setPaintFlags(mFragmentProductDetailBinding.txtActualAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                mFragmentProductDetailBinding.txtActualAmount.setText("\u20B9" + " " + mProductData.getServicePlanUnits().get(selectedPos).getPrice());
                mFragmentProductDetailBinding.txtDiscount.setText("You Save : " + "\u20B9" + " " + mProductData.getServicePlanUnits().get(selectedPos).getTotalDiscount());
                mFragmentProductDetailBinding.txtDiscountedAmount.setText("\u20B9" + " " + mProductData.getServicePlanUnits().get(selectedPos).getDiscountedPrice());
                mFragmentProductDetailBinding.imgAdd.setOnClickListener(v -> {
                    quantity++;
                    mFragmentProductDetailBinding.txtQuantity.setText(String.valueOf(quantity));
                });
                mFragmentProductDetailBinding.imgSubstract.setOnClickListener(v -> {
                    quantity--;
                    if (quantity < 1)
                        quantity = 1;
                    mFragmentProductDetailBinding.txtQuantity.setText(String.valueOf(quantity));
                });
                mAdapter.setOnItemClickHandler(position -> {
                    selectedPos = position;
                    setProductDetails();
                });

                mFragmentProductDetailBinding.btnAddToCart.setOnClickListener(v -> {
                    if (mFragmentProductDetailBinding.btnAddToCart.getText().toString().equals("Add to Cart")) {
                        addProducts();
                    } else {
//                        startActivity(new Intent(getActivity(), ProductCartActivity.class));
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater = getActivity().getMenuInflater();
//        MenuItem itemCart = menu.findItem(R.id.action_cart);
//        icon = (LayerDrawable) itemCart.getIcon();
//
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    private void addProducts() {
        try {
            int qty = Integer.parseInt(mFragmentProductDetailBinding.txtQuantity.getText().toString());
            ProductCart cart = new ProductCart();
            cart.setId(mProductData.getId());
            cart.setSpCode(mProductData.getSpCode());
            cart.setActualAmount((Double.parseDouble(mProductData.getServicePlanUnits().get(selectedPos).getPrice())*qty));
            cart.setDiscountedAmount((Double.parseDouble(mProductData.getServicePlanUnits().get(selectedPos).getDiscountedPrice())*qty));
            cart.setQuantity(mFragmentProductDetailBinding.txtQuantity.getText().toString().trim());
            cart.setUnit(mProductData.getServicePlanUnits().get(selectedPos).getUnit());
            cart.setDiscount((Double.parseDouble(mProductData.getServicePlanUnits().get(selectedPos).getTotalDiscount())*qty));
            cart.setTitle(mProductData.getPlanName());
            cart.setImgURL(mProductData.getImageUrl());

            getRealm().beginTransaction();
            getRealm().copyToRealm(cart);
            getRealm().commitTransaction();

            mFragmentProductDetailBinding.btnAddToCart.setText("View Cart");

            getActivity().invalidateOptionsMenu();
//            RealmResults<ProductCart> results = getRealm().where(ProductCart.class).findAll();
//            if (results != null && results.size() > 0) {
//                AppUtils.setBadgeCount(getActivity(), icon, String.valueOf(results.size()));
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
