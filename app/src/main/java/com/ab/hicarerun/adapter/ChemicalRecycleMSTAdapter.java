package com.ab.hicarerun.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.handler.OnAddChemicalClickHandler;
import com.ab.hicarerun.handler.OnAddJobCardClickHandler;
import com.ab.hicarerun.handler.OnRecentTaskClickHandler;
import com.ab.hicarerun.network.models.ChemicalModel.Chemicals;
import com.ab.hicarerun.network.models.GeneralModel.GeneralData;

import java.util.HashMap;
import java.util.List;

import io.realm.RealmResults;

import static com.ab.hicarerun.BaseApplication.getRealm;

/**
 * Created by Arjun Bhatt on 1/3/2020.
 */
public class ChemicalRecycleMSTAdapter extends BaseExpandableListAdapter {
    private OnAddChemicalClickHandler onItemClickHandler;
    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<Chemicals>> expandableListDetail;
    private ExpandableListView mExpandableListView;
    private OnEditTextChanged onEditTextChanged;
    private Boolean isVerified = false;


    public ChemicalRecycleMSTAdapter(Context context, List<String> expandableListTitle,
                                     HashMap<String, List<Chemicals>> expandableListDetail, ExpandableListView expandableListView/*, OnEditTextChanged onEditTextChanged*/) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        this.mExpandableListView = expandableListView;
        this.onEditTextChanged = onEditTextChanged;
//        for (int i = 0; i < expandableListTitle.size(); i++) {
//            for (int j = 0; j < expandableListDetail.get(expandableListTitle.get(i)).size(); j++) {
//                mapActual.put(j, "");
//            }
//        }
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        return convertView;
    }

    public void setOnItemClickHandler(OnAddChemicalClickHandler onItemClickHandler) {
        this.onItemClickHandler = onItemClickHandler;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.layout_chemical_header, null);
        }
        TextView listTitleTextView = convertView
                .findViewById(R.id.txtHeader);
        ImageView imgArrow = convertView.findViewById(R.id.imgArrow);
        LinearLayout lnrHeader = convertView.findViewById(R.id.lnrHeader);

        LinearLayout cardArea = convertView.findViewById(R.id.cardService);
        listTitleTextView.setText(listTitle);
        imgArrow.setOnClickListener(view -> onItemClickHandler.onAddChemicalClicked(listPosition));

//        cardArea.setOnClickListener(view -> {
//            if (imgArrow.getTag() != null && imgArrow.getTag().toString().equals("180")) {
//                ObjectAnimator anim = ObjectAnimator.ofFloat(imgArrow, "rotation", 180, 0);
//                anim.setDuration(300);
//                anim.start();
//                imgArrow.setTag("");
//                mExpandableListView.expandGroup(listPosition);
//                lnrHeader.setVisibility(View.VISIBLE);
//
//            } else {
//                ObjectAnimator anim = ObjectAnimator.ofFloat(imgArrow, "rotation", 0, 180);
//                anim.setDuration(300);
//                anim.start();
//                imgArrow.setTag(180 + "");
//                mExpandableListView.collapseGroup(listPosition);
//                lnrHeader.setVisibility(View.GONE);
//            }
//        });

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    public interface OnEditTextChanged {
        void onTextChanged(int position, String charSeq);
    }

    class ViewHolder {
        EditText edtCurrent;
        TextView txtName, txtConsumption;
    }
}