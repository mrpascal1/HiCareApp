package com.ab.hicarerun.utils;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

/**
 * Created by Arjun Bhatt on 7/15/2020.
 */
public class ProgressBarDrawable extends Drawable {

    private int parts;

    private Paint paint;
    private int fillColor;
    private int emptyColor;
    private int cutOffWidth;
    private int separatorColor;

    public ProgressBarDrawable(int parts, int fillColor, int emptyColor, int separatorColor) {
        this.parts = parts;
        this.fillColor = fillColor;
        this.emptyColor = emptyColor;
        this.separatorColor = separatorColor;

        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected boolean onLevelChange(int level) {
        invalidateSelf();
        return true;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

        // Calculate values
        Rect bounds = getBounds();
        float actualWidth = bounds.width();
        float actualHeight = bounds.height();


        //width with dividers + segment width
        int fullBlockWidth = (int) (actualWidth / this.parts);
        //ToDo: to change the width of segment change this line
        int segmentWidth = (int) (fullBlockWidth * 0.96f);
//        int dividerWidth =fullBlockWidth-segmentWidth;

        cutOffWidth = (int) (getLevel() * actualWidth / 10000);

        //Draw separator as background
        RectF fullBox = new RectF(0, 0, actualWidth, actualHeight);
        this.paint.setColor(this.separatorColor);
        canvas.drawRect(fullBox, this.paint);

        //start drawing lines as segmented bars
        int startX = 0;
        for (int i = 0; i < this.parts; i++) {
            int endX = startX + segmentWidth;

            //in ideal condition this would be the rectangle
            RectF part = new RectF(startX, 0, endX, actualHeight);

            //if the segment is below level the paint color should be fill color
            if ((startX + segmentWidth) <= cutOffWidth) {

                this.paint.setColor(this.fillColor);
                canvas.drawRect(part, this.paint);

            }
            //if the segment is started below the level but ends above the level than we need to create 2 different rectangle
            else if (startX < cutOffWidth) {

                RectF part1 = new RectF(startX, 0, cutOffWidth, actualHeight);
                this.paint.setColor(this.fillColor);
                canvas.drawRect(part1, this.paint);

                RectF part2 = new RectF(cutOffWidth, 0, startX + segmentWidth, actualHeight);
                this.paint.setColor(this.emptyColor);
                canvas.drawRect(part2, this.paint);

            }

            //if the segment is above level the paint color should be empty color
            else {
                this.paint.setColor(this.emptyColor);
                canvas.drawRect(part, this.paint);
            }

            //update the startX to start the new segment with the gap of divider and segment width
            startX += fullBlockWidth;
        }

    }


    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}