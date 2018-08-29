package com.cg.quickcircle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class CircleImageView extends View {
	
    private float mAngle;

	private int mHeight;

	private int mNumber;

	private int mRedRadius;

	private int mWidth;
	
	private int position = 0;

	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		mHeight = h;
		mWidth = w;
	}
	
	public float getAngle() {
		return mAngle;
	}
	
	public void setAngle(float angle) {
		this.mAngle = angle;
	}
	
	public int getNumber() {
		return this.mNumber;
	}
	
	public void setNmuber(int number) {
		this.mNumber = number;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
    public int getPosition() {
        return position;
    } 

}
