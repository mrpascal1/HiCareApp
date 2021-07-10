package com.ab.hicarerun;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.ab.hicarerun.utils.CustomBottomNavigation;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

/**
 * Created by arjun on 03/05/19.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;

    @Override
    protected void attachBaseContext(Context context) {
        try {
            super.attachBaseContext(ViewPumpContextWrapper.wrap(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void addFragment(BaseFragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment, tag).commit();
    }

    protected void replaceFragment(BaseFragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment, tag)
                .addToBackStack(tag)
                .commitAllowingStateLoss();
    }

    protected void viewFragment(BaseFragment fragment, String name, CustomBottomNavigation customNavigation) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment, name);
        final int count = getSupportFragmentManager().getBackStackEntryCount();
        // 2. If the fragment is **not** "home type", save it to the stack
        if (name.equals("INCENTIVE")) {
            getSupportFragmentManager().beginTransaction().addToBackStack(name);
        }
        // Commit !
        getSupportFragmentManager().beginTransaction().commit();
        // 3. After the commit, if the fragment is not an "home type" the back stack is changed, triggering the
        // OnBackStackChanged callback
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                // If the stack decreases it means I clicked the back button
                if (getSupportFragmentManager().getBackStackEntryCount() <= count) {
                    // pop all the fragment and remove the listener
                    getSupportFragmentManager().popBackStack("INCENTIVE", POP_BACK_STACK_INCLUSIVE);
                    getSupportFragmentManager().removeOnBackStackChangedListener(this);
                    // set the home button selected
                    customNavigation.getMenu().getItem(0).setChecked(true);
                }
            }
        });
    }

    public void showProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            return;
        }
        Log.i("ProgressDialog", "showProgressDialog");
        try {
            mProgressDialog = new ProgressDialog(this, R.style.TransparentProgressDialog);
            mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            if (!mProgressDialog.isShowing()) {
                return;
            }
            Log.i("ProgressDialog", "dismissProgressDialog");
            try {
                mProgressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
