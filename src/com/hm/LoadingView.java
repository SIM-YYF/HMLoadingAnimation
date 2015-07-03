package com.hm;

import java.util.List;
import java.util.Random;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class LoadingView extends View{

	/**
	 * view 矩形
	 */
	Rect  view_rect;
	
	/**
	 * 进度条边框矩形
	 */
	Rect  progress_frame_rect;
	
	/**
	 * 进度条白色区域矩形
	 */
	RectF  progress_white_rectf;
	/**
	 * 进度条橘色区域矩形
	 */
	RectF progress_orange_rectf;
	/**
	 * 进度条弧度区域矩形
	 */
	RectF arc_rectf;
	
	/**
	 * view 的宽度
	 */
	int total_width = 0;
	/**
	 * view 的高度
	 */
	int total_height = 0;
	/**
	 * 弧度半径
	 */
	int arc_radius = 0;
	
	/**
	 * 进度条边框宽度
	 */
	int progress_frame_out_width;
	/**
	 * 进度条边框高度
	 */
	int progress_frame_out_height;
	
	/**
	 * 叶子图片的宽度
	 */
	int leaf_out_width;
	/**
	 * 叶子图片的高度
	 */
	int leaf_out_height;
	
	private static final int PROGRESS_LEFT_TOP_BOTTOM_MARGIN = 10;
	private static final int PROGRESS_RIGHT_MARGIN = 25;
	 // 淡白色
    private static final int WHITE_COLOR = 0xfffde399;
    // 橙色
    private static final int ORANGE_COLOR = 0xffffa800;
    // 进度条的总进度
    private static final int TOTAL_PROGRESS = 100;
	/**
	 * 控制绘制进度条left，top，bottom边距
	 */
	int progress_left_top_bottom_margin = 10;
	/**
	 * 控制绘制进度条right边距
	 */
	int progress_right_margin = 25;
	/**
	 * 当前所在的绘制的进度条的位置
	 */
	int current_progress_position = 0;
	/**
	 * 当前进度条的宽度
	 */
	int current_progress_width;
	/**
	 * 当前进度条进度
	 */
	int mProgress = 0;
	
	/**
	 * 弧度右上角坐标
	 */
	int radian_right_x_location = 0;
	
	/**
	 * 进度条边框
	 */
	Bitmap progress_frame_bitmap;
	/**
	 * 叶子图片
	 */
	Bitmap leaf_bitmap;
	
	Paint  bitmapPaint;
	Paint  whitePaint;
	Paint  orangePaint;
	float density;
	
	/**
	 * 叶子集合
	 */
	List<Leaf> leafs;
	LeaFactory  factory;
	
	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//初始进度条在view中的left top right bottom 的margin值
		initProgressMargin(context);
		// 初始画笔
		initPaint();
		//初始图片
		initBitmap();
		
		//产生叶子
		factory = new LeaFactory();
		leafs = factory.getLeafs(6);
		
		
	}

	/**
	 * 初始画笔
	 * 1.进度条边框图片的画笔
	 * 2.绘制白色区域的画笔
	 * 3.绘制橘色区域的画笔
	 */
	private void initPaint() {
		bitmapPaint = new Paint();
		bitmapPaint.setDither(true);//防抖动
		bitmapPaint.setAntiAlias(true);//抗锯齿
		
		whitePaint = new Paint();
		whitePaint.setAntiAlias(true);
		whitePaint.setColor(WHITE_COLOR);
		
		orangePaint = new Paint();
		orangePaint.setAntiAlias(true);
		orangePaint.setColor(ORANGE_COLOR);
	}

	/**
	 * 初始进度条在view中的left top right bottom 的margin值
	 * @param context
	 */
	private void initProgressMargin(Context context) {
		DisplayMetrics  metric = context.getResources().getDisplayMetrics();
		WindowManager  wm = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
		Display  display = wm.getDefaultDisplay();
		density = metric.density;
		display.getMetrics(metric);
		
		progress_left_top_bottom_margin = (int) (PROGRESS_LEFT_TOP_BOTTOM_MARGIN * metric.density);
		progress_right_margin = (int)(PROGRESS_RIGHT_MARGIN * metric.density);
	}
	
	/**
	 * 初始图片
	 */
	private void initBitmap(){
		
		//第一种方式
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = false;
		opts.inScaled = false;
		opts.inDither = true;
		opts.inSampleSize = 1;
		opts.inPreferredConfig = Config.ARGB_8888;
		
		progress_frame_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.leaf_kuang, opts);
		progress_frame_out_width = progress_frame_bitmap.getWidth();
		progress_frame_out_height = progress_frame_bitmap.getHeight();
		
		leaf_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.leaf, opts);
		leaf_out_width = leaf_bitmap.getWidth();
		leaf_out_height = leaf_bitmap.getHeight();
		
		
		//第二种方式
//		progress_frame_bitmap= ((BitmapDrawable)getResources().getDrawable(R.drawable.leaf_kuang)).getBitmap();
//		progress_frame_out_width = progress_frame_bitmap.getWidth();
//		progress_frame_out_height = progress_frame_bitmap.getHeight();
		
		
		
		
	}
	
	/**
	 * 渲染view中的内容
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawProgressAndLeafs(canvas, leafs);
		canvas.drawBitmap(progress_frame_bitmap, progress_frame_rect, view_rect, bitmapPaint);
		postInvalidate();//注意：Invalidate  和  postInvalidate 的区别
	}
	
	/**
	 * 当view大小发生改变以后，触发该方法
	 *
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		//view 的宽度
		total_width = w;
		//view的高度
		total_height = h;
		
		//当前进度条的宽度
		current_progress_width = progress_frame_out_width - progress_left_top_bottom_margin - progress_right_margin;
		//计算弧度的半径
		arc_radius =  (progress_frame_out_width -  2 *progress_left_top_bottom_margin) / 2;
				
		//初始矩形view. 注意：这里不是RectF 
		view_rect = new Rect(0, 0, total_width, total_height);
		//初始进度条边框矩形 注意：这里不是RectF 
		progress_frame_rect = new Rect(0, 0, progress_frame_out_width, progress_frame_out_height);
		
		//初始进度条白色区域矩形(包含弧度区域部分)
		progress_white_rectf = new RectF(
				progress_left_top_bottom_margin + arc_radius + current_progress_position, 
				progress_left_top_bottom_margin, 
				progress_frame_out_width - progress_right_margin, //这里不一样
				progress_frame_out_height - progress_left_top_bottom_margin //这里不一样
				);
		
		
		
				
		//初始进度条橘色区域矩形(不包含弧度区域部分)
		progress_orange_rectf = new RectF(
				progress_left_top_bottom_margin + arc_radius, 
				progress_left_top_bottom_margin, 
				current_progress_position,                                                                                                                                                                   
				progress_frame_out_height - progress_left_top_bottom_margin //这里不一样
				);
		
		
		//弧度矩形
		arc_rectf = new RectF(
				progress_left_top_bottom_margin, 
				progress_left_top_bottom_margin, 
				progress_left_top_bottom_margin + 2 * arc_radius, 
				progress_frame_out_height - progress_left_top_bottom_margin
				);
		
	}
	
	/**
	 * 绘制进度条和叶子
	 * @param leafs2  叶子集合
	 */
	private void drawProgressAndLeafs(Canvas  canvas, List<Leaf> leafs2) {
		
		//1.重置进度条总进度
		if(mProgress == TOTAL_PROGRESS) mProgress = 0;
		
		//2.计算当前进度条进度位置
		// 2-1：把进度条的实际宽度按进度条的总进度100，进行平均分配，分成100份.获得每一份的进度位置
		// 2-2:根据当前进度条进度 * 每一份的进度条位置，就可以得到当前进度条所在位置
		current_progress_position = current_progress_width / TOTAL_PROGRESS  * mProgress;
		
		//3.绘制进度条和叶子
		
		if(current_progress_position < arc_radius){//当前进度条位置  < 弧度半径
			//1.绘制白色区域的弧度
				canvas.drawArc(arc_rectf, 90f, 180f, false, whitePaint);
			//2.绘制白色区域的矩形
				//矩形区域left坐标 = left_margin + 弧度半径   (其实就是去除弧度部分)
				progress_white_rectf.left = progress_left_top_bottom_margin + arc_radius;
				canvas.drawRect(progress_white_rectf, whitePaint);
			//3.绘制叶子
				drawLeafs(canvas);
			//4.绘制橘色区域的弧度
				//角度 θ，以弧度为单位，满足 0 ≤θ≤π
				double valuebyarc = Math.acos((arc_radius - current_progress_position) / arc_radius);
				//以弧度为单位测得的角度大致相等的角度，以度衡量
				int angrad = (int) Math.toDegrees(valuebyarc);
				 // 起始的位置
	            int startAngle = 180 - angrad;
	            // 扫过的角度
	            int sweepAngle = 2 * angrad;
	            
				canvas.drawArc(progress_orange_rectf, startAngle, sweepAngle, false, orangePaint);
		}else{
			//1.绘制白色区域的矩形
			progress_white_rectf.left = progress_left_top_bottom_margin + arc_radius;
			canvas.drawRect(progress_white_rectf, whitePaint);
			//2.绘制叶子
			drawLeafs(canvas);
			//3.绘制橘色区域的弧度
			canvas.drawArc(arc_rectf, 90, 180, false, whitePaint);
			//4.绘制橘色区域的矩形
			progress_orange_rectf.left = progress_left_top_bottom_margin + arc_radius;//去除弧度部分
			progress_orange_rectf.right = current_progress_position;//当前进度条的进度位置
			canvas.drawRect(progress_orange_rectf, orangePaint);
		}
		
		
		
	}

	/**
	 * 绘制叶子
	 * @param canvas
	 */
	private void drawLeafs(Canvas canvas) {
		// TODO Auto-generated method stub
		//初始叶子旋转一周所需时间
		if(factory.getLeaf_rotate_cycle_time() <= 0){
			factory.setLeaf_rotate_cycle_time(factory.DEFAULT_LEAF_ROTATE_CYCLE_TIME);
		}
		//初始叶子飘动一周所需时间
		if(factory.getLeaf_float_cycle_time() <= 0){
			factory.setLeaf_float_cycle_time(factory.DEFAULT_LEAF_FLOAT_CYCLE_TIME);
		}
		
		long currentTime = System.currentTimeMillis();
		
		Leaf leaf;
		for(int i = 0 ; i < leafs.size(); i++){
			leaf = leafs.get(i);
			if(currentTime > leaf.getStartTime() && leaf.getStartTime() != 0){
				//1.根据叶子的类型和当前时间得出叶子的（x，y）
				getLeafLocation(leaf, currentTime);
                canvas.save();//注意这里
                
                //2.控制叶子的移动和旋转
                Matrix matrix = new Matrix();
                
					float transX = progress_left_top_bottom_margin + leaf.getX();
					float transY = progress_left_top_bottom_margin + leaf.getY();
					//控制叶子进行移动
					matrix.postTranslate(transX, transX);
                
                //控制叶子进行旋转
	                // 通过时间关联旋转角度，则可以直接通过修改LEAF_ROTATE_TIME调节叶子旋转快慢
	                float rotateFraction = ((currentTime - leaf.getStartTime()) % factory.getLeaf_rotate_cycle_time()) / (float) factory.getLeaf_rotate_cycle_time();
	                int angle = (int) (rotateFraction * 360);
	                // 根据叶子旋转方向确定叶子旋转角度
	                int rotate = leaf.getRotateDirection() == 0 ? angle + leaf.getRotateAngle() : -angle + leaf.getRotateAngle();
	                
	                matrix.postRotate(
	                        rotate,
	                        transX + leaf_out_width / 2,
	                        transY + leaf_out_height / 2
	
	                );
	                
                canvas.drawBitmap(leaf_bitmap, matrix, bitmapPaint);
                canvas.restore();
                
			}else{
				continue;
			}
		}
		
		
	}

	private void getLeafLocation(Leaf leaf, long currentTime) {
		 long intervalTime = currentTime - leaf.getStartTime();// 假如相差3秒
	        if (intervalTime < 0) {
	            return;
	        } else if (intervalTime > factory.getLeaf_float_cycle_time()) {//大于2秒
	            leaf.setStartTime(System.currentTimeMillis() + new Random().nextInt((int) factory.getLeaf_float_cycle_time()));
	        }

	        float fraction = (float) intervalTime / factory.getLeaf_float_cycle_time();
	        leaf.setX((int) (current_progress_width - current_progress_width * fraction));
	        leaf.setY(getLocationY(leaf));
	}


    // 通过叶子信息获取当前叶子的Y值
    private int getLocationY(Leaf leaf) {
        // y = A(wx+Q)+h
        //首先根据效果情况基本确定出 曲线函数，标准函数方程为：y = A(wx+Q)+h，其中w影响周期，A影响振幅 ，周期T＝ 2 * Math.PI/w;
       // 根据效果可以看出，周期大致为总进度长度，所以确定w＝(float) ((float) 2 * Math.PI /mProgressWidth)；

        // 中等振幅大小
         int mMiddleAmplitude = 13;
        // 振幅差
         int mAmplitudeDisparity = 5;
        
        float w = (float) ((float) 2 * Math.PI / current_progress_width);
        float a = mMiddleAmplitude;
        switch (leaf.getType()) {
            case SMALL:
                // 小振幅 ＝ 中等振幅 － 振幅差
                a = mMiddleAmplitude - mAmplitudeDisparity;
                break;
            case MIDDLE:
                a = mMiddleAmplitude;
                break;
            case BIG:
                // 大振幅 ＝ 中等振幅 + 振幅差
                a = mMiddleAmplitude + mAmplitudeDisparity;
                break;
            default:
                break;
        }
        return (int) (a * Math.sin(w * leaf.getX())) + arc_radius * 2 / 3;
    }
	
	
    /**
     * 设置进度
     * 
     * @param progress
     */
    public void setProgress(int progress) {
        this.mProgress = progress;
        postInvalidate();
    }
	
}
