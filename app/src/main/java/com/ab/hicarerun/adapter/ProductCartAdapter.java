package com.ab.hicarerun.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.LayoutProductCartAdapterBinding;
import com.ab.hicarerun.databinding.ProductUnitsAdapterBinding;
import com.ab.hicarerun.handler.OnCartItemClickHandler;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.models.ProductCartModel.ProductCart;
import com.ab.hicarerun.network.models.ProductModel.ServicePlanUnits;
import com.ab.hicarerun.viewmodel.TaskViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.ab.hicarerun.BaseApplication.getRealm;

/**
 * Created by Arjun Bhatt on 6/3/2020.
 */
public class ProductCartAdapter extends RecyclerView.Adapter<ProductCartAdapter.ViewHolder> {

    private OnListItemClickHandler onItemClickHandler;
    private OnCartItemClickHandler onCartItemClickHandler;
    private final Context mContext;
    private List<ProductCart> items = null;

    public ProductCartAdapter(Context context) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mContext = context;
    }

    @NotNull
    @Override
    public ProductCartAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutProductCartAdapterBinding mLayoutProductCartAdapterBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.layout_product_cart_adapter, parent, false);
        return new ProductCartAdapter.ViewHolder(mLayoutProductCartAdapterBinding);
    }


    @Override
    public void onBindViewHolder(@NotNull ProductCartAdapter.ViewHolder holder, final int position) {
        try {
            holder.mLayoutProductCartAdapterBinding.txtActual.setPaintFlags(holder.mLayoutProductCartAdapterBinding.txtActual.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.mLayoutProductCartAdapterBinding.txtPlanTitle.setText(items.get(position).getTitle());
            holder.mLayoutProductCartAdapterBinding.txtActual.setText("\u20B9" + " " + String.valueOf(items.get(position).getActualAmount()));
            holder.mLayoutProductCartAdapterBinding.txtDiscounted.setText("\u20B9" + " " + String.valueOf(items.get(position).getDiscountedAmount()));
            holder.mLayoutProductCartAdapterBinding.txtUnit.setText(items.get(position).getUnit());
            holder.mLayoutProductCartAdapterBinding.txtQuantity.setText(items.get(position).getQuantity());
            holder.mLayoutProductCartAdapterBinding.txtSaved.setText("You Save : " + "\u20B9" + " " + items.get(position).getDiscount());
            holder.mLayoutProductCartAdapterBinding.imgAdd.setOnClickListener(v -> {
                int quantity = Integer.parseInt(holder.mLayoutProductCartAdapterBinding.txtQuantity.getText().toString());
                quantity++;
                holder.mLayoutProductCartAdapterBinding.txtQuantity.setText(String.valueOf(quantity));

                ProductCart cart = new ProductCart();
                if (cart.isValid()) {

                    final Double actualPrice = items.get(position).getActualAmount();
                    final Double discountedPrice = items.get(position).getDiscountedAmount();
                    final Double discount = items.get(position).getDiscount();
                    Double actualTotal = (actualPrice * quantity);
                    Double discountedTotal = (discountedPrice * quantity);
                    Double TotalDiscount = (discount * quantity);


                    cart.setId(items.get(position).getId());
                    cart.setSpCode(items.get(position).getSpCode());
                    cart.setActualAmount(actualTotal);
                    cart.setDiscountedAmount(discountedTotal);
                    cart.setQuantity(String.valueOf(quantity));
                    cart.setUnit(items.get(position).getUnit());
                    cart.setDiscount(TotalDiscount);
                    cart.setTitle(items.get(position).getTitle());
                    cart.setImgURL(items.get(position).getImgURL());

                    getRealm().beginTransaction();
                    getRealm().copyToRealmOrUpdate(cart);
                    getRealm().commitTransaction();

                    notifyDataSetChanged();
                }

                onCartItemClickHandler.onAddQuantityClicked(position);
            });
            holder.mLayoutProductCartAdapterBinding.imgSubstract.setOnClickListener(v -> {
                int quantity = Integer.parseInt(holder.mLayoutProductCartAdapterBinding.txtQuantity.getText().toString());
                quantity--;
                if (quantity < 1) {
                    quantity = 1;
                }
                holder.mLayoutProductCartAdapterBinding.txtQuantity.setText(String.valueOf(quantity));

                ProductCart cart = new ProductCart();
                if (cart.isValid()) {
                    final Double actualPrice = items.get(position).getActualAmount();
                    final Double discountedPrice = items.get(position).getDiscountedAmount();
                    final Double discount = items.get(position).getDiscount();

                    Double actualTotal = (actualPrice / quantity);
                    Double discountedTotal = (discountedPrice / quantity);
                    Double TotalDiscount = (discount / quantity);

                    cart.setId(items.get(position).getId());
                    cart.setSpCode(items.get(position).getSpCode());
                    cart.setActualAmount(actualTotal);
                    cart.setDiscountedAmount(discountedTotal);
                    cart.setQuantity(holder.mLayoutProductCartAdapterBinding.txtQuantity.getText().toString());
                    cart.setUnit(items.get(position).getUnit());
                    cart.setDiscount(TotalDiscount);
                    cart.setTitle(items.get(position).getTitle());
                    cart.setImgURL(items.get(position).getImgURL());

                    getRealm().beginTransaction();
                    getRealm().copyToRealmOrUpdate(cart);
                    getRealm().commitTransaction();

                    notifyDataSetChanged();
                }


                onCartItemClickHandler.onSubstractQuantityClicked(position);
            });
            holder.mLayoutProductCartAdapterBinding.imgRemove.setOnClickListener(v -> onCartItemClickHandler.onDeleteCartClicked(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnCartItemClickHandler(OnCartItemClickHandler onCartItemClickHandler) {
        this.onCartItemClickHandler = onCartItemClickHandler;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ProductCart getItem(int position) {
        return items.get(position);
    }

    public void setData(List<ProductCart> data) {
        items.clear();
        items.addAll(data);
    }

    public void removeAll() {
        items.removeAll(items);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        items.remove(items.get(position));
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LayoutProductCartAdapterBinding mLayoutProductCartAdapterBinding;

        public ViewHolder(LayoutProductCartAdapterBinding mLayoutProductCartAdapterBinding) {
            super(mLayoutProductCartAdapterBinding.getRoot());
            this.mLayoutProductCartAdapterBinding = mLayoutProductCartAdapterBinding;
        }
    }
}
