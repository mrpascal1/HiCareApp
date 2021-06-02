//package com.ab.hicarerun.fragments;
//
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.databinding.DataBindingUtil;
//import androidx.fragment.app.DialogFragment;
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.ab.hicarerun.R;
//import com.ab.hicarerun.databinding.FragmentRenewalQrCodeDialogBinding;
//import com.ab.hicarerun.network.NetworkCallController;
//import com.ab.hicarerun.network.NetworkResponseListner;
//import com.ab.hicarerun.network.models.ModelQRCode.QRCode;
//import com.squareup.picasso.Picasso;
//
//
//public class RenewalQrCodeDialogFragment extends DialogFragment {
//FragmentRenewalQrCodeDialogBinding mFragmentRenewalQrCodeDialogBinding;
//
//    public RenewalQrCodeDialogFragment() {
//        // Required empty public constructor
//    }
//
//    // TODO: Rename and change types and number of parameters
//    public static RenewalQrCodeDialogFragment newInstance(String param1, String param2) {
//        RenewalQrCodeDialogFragment fragment = new RenewalQrCodeDialogFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        mFragmentRenewalQrCodeDialogBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_renewal_qr_code_dialog, container, false);
//        if (getDialog() != null && getDialog().getWindow() != null) {
//            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        }
//        return mFragmentRenewalQrCodeDialogBinding.getRoot();
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        getQRCode();
//    }
//
//    private void getQRCode() {
//        try {
//            if (getActivity() != null) {
//                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
//                LayoutInflater inflater = getLayoutInflater();
//                final View dialogView = inflater.inflate(R.layout.qr_code_dialog_layout, null);
//                dialogBuilder.setView(dialogView);
//                final AlertDialog alertDialog = dialogBuilder.create();
//                final TextView txtTitle = (TextView) dialogView.findViewById(R.id.txtTitle);
//                final TextView txtStatus = (TextView) dialogView.findViewById(R.id.txtPaymentStatus);
//                final ImageView imgClose = (ImageView) dialogView.findViewById(R.id.imgClose);
//                final ImageView imgCode = (ImageView) dialogView.findViewById(R.id.img_payscanner);
//                final LinearLayout lnrCheckStatus = (LinearLayout) dialogView.findViewById(R.id.lnrQrCode);
//
//                NetworkCallController controller = new NetworkCallController();
//                controller.setListner(new NetworkResponseListner<QRCode>() {
//
//                    @Override
//                    public void onResponse(int requestCode, QRCode response) {
//                        Picasso.get().load(response.getUrl()).into(imgCode);
////                            checkPaymentStatus(response.getOrderId());
////                            orderId = response.getOrderId();
//                    }
//
//                    @Override
//                    public void onFailure(int requestCode) {
//                    }
//                });
//                controller.getUPICode(QR_CODE_REQ, taskId, accountNo, orderNo, amount, source);
//
//                imgClose.setOnClickListener(view ->{
//                    alertDialog.dismiss();
//                });
//
//                lnrCheckStatus.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        checkPaymentStatus(response.getOrderId());
//                    }
//                });
//
//                alertDialog.show();
//                alertDialog.setCancelable(false);
//                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                alertDialog.setCanceledOnTouchOutside(false);
//            }
////            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}