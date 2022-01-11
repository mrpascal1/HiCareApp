package com.ab.hicarerun.activities;

import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ActivityHicareProductsBinding;
import com.ab.hicarerun.fragments.HicareProductsFragment;
import com.ab.hicarerun.fragments.Product_Cart_Fragment;
import com.ab.hicarerun.network.models.productcartmodel.ProductCart;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.LocaleHelper;

import java.util.Objects;

import io.realm.RealmResults;

import static com.ab.hicarerun.BaseApplication.getRealm;

public class HicareProductsActivity extends BaseActivity {
    ActivityHicareProductsBinding mActivityHicareProductsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityHicareProductsBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_hicare_products);
        addFragment(HicareProductsFragment.newInstance(), "HicareProductsActivity - HicareProductsFragment");
        this.setSupportActionBar(mActivityHicareProductsBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, LocaleHelper.getLanguage(base)));
    }

    public void onBackPressed() {
        try {
            getBack();
            super.onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getBack() {
        int fragment = getSupportFragmentManager().getBackStackEntryCount();
        Log.e("fragments", String.valueOf(fragment));
        if (fragment < 1) {
            finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*   MenuInflater inflater =*/
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        MenuItem itemCart = menu.findItem(R.id.action_cart);
        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
        RealmResults<ProductCart> results = getRealm().where(ProductCart.class).findAll();
        if (results != null && results.size() > 0) {
            AppUtils.setBadgeCount(this, icon, String.valueOf(results.size()));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                getBack();
                break;

            case R.id.action_cart:
//                startActivity(new Intent(this, ProductCartActivity.class));
                replaceFragment(Product_Cart_Fragment.newInstance(), "HicareProductActivity-ProductCartFragment");
                return true;

        }

        return true;
    }
}
