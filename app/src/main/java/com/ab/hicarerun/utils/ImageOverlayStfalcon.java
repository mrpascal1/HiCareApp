package com.ab.hicarerun.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.hicarerun.R;
import com.squareup.picasso.Picasso;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.loader.ImageLoader;

import java.util.ArrayList;

public class ImageOverlayStfalcon extends RelativeLayout {

    private StfalconImageViewer<String> imageViewer;

    String[] arrayList;
    String imageLoader;

    private String sharingText;

    public ImageOverlayStfalcon(Context context, String[] arrayList) {
        super(context);
        this.arrayList = arrayList;
        init();
    }

    public ImageOverlayStfalcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public ImageOverlayStfalcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    public void setShareText(String text) {
        this.sharingText = text;
    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_image_overlay_stfalcon, this);
        imageViewer =  new StfalconImageViewer.Builder<String>(getContext(), arrayList, (imageView, image) -> {
            try {
                Picasso.get().load(image).into(imageView);
            }catch (Exception e){

            }
        }).withStartPosition(0)
                .withOverlayView(view)
                .show();
        view.findViewById(R.id.closeIv).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewer.close();
            }
        });
    }
}
