package com.hm;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
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
	
	private static final int PROGRESS_LEFT_TOP_BOTTOM_MARGIN = 12;
	private static final int PROGRESS_RIGHT_MARGIN = 25;
    // 叶子飘动一个周期所花的时间
    private static final long LEAF_FLOAT_TIME = 3000;
    // 叶子旋转一周需要的时间
    private static final long LEAF_ROTATE_TIME = 2000;
    
	 // 淡白色
    private static final int WHITE_COLOR = 0xfffde399;
    // 橙色
    private static final int ORANGE_COLOR = 0xffffa800;
    
    // 进度条的总进度
    private static final int TOTAL_PROGRESS = 100;
    // 中等振幅大小
    private static final int MIDDLE_AMPLITUDE = 13;
    // 不同类型之间的振幅差距
    private static final int AMPLITUDE_DISPARITY = 5;
    
    
    /**
     * 叶子飘动一个周期所花的时间
     */
    private long mLeafFloatTime = LEAF_FLOAT_TIME;
    /**
     *  叶子旋转一周需要的时间
      */
    private long mLeafRotateTime = LEAF_ROTATE_TIME;
    
    // 中等振幅大小
    private int mMiddleAmplitude = MIDDLE_AMPLITUDE;
    // 振幅差
    private int mAmplitudeDisparity = AMPLITUDE_DISPARITY;
    
    
	/**
	 * 控制绘制进度条left，top，bottom边距
	 */
	int progress_left_top_bottom_margin = 0;
	/**
	 * 控制绘制进度条right边距
	 */
	int progress_right_margin = 0;
	/**
	 * 当前所在的绘制的进度条的位置
	 */
	int current_progress_position = 0;
	/**
	 * 进度条的宽度
	 */
	int progress_width;
	/**
	 * 当前进度条进度
	 */
	int mProgress = 0;
	
	// arc的右上角的x坐标，也是矩形x坐标的起始点
    private int mArcRightLocation;
	
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
	LeafFactory  factory;
	
	 // 用于控制随机增加的时间不抱团
    private int mAddTime;
	
	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//初始进度条在view中的left top right bottom 的margin值
		initProgressMargin(context);
		// 初始画笔
		initPaint();
		//初始图片
		initBitmap();
		
		//产生叶子
		factory = new LeafFactory();
		leafs = factory.generateLeafs();
		
		
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
		display.getMetrics(metric);
		density = metric.density;
		
		progress_left_top_bottom_margin = (int) (PROGRESS_LEFT_TOP_BOTTOM_MARGIN * density);
		progress_right_margin = (int)(PROGRESS_RIGHT_MARGIN * density);
	}
	
	/**
	 * 初始图片
	 */
	private void initBitmap(){
		
		//第一种方式
//		BitmapFactory.Options opts = new BitmapFactory.Options();
//		opts.inJustDecodeBounds = false;
//		opts.inScaled = false;
//		opts.inDither = false;
//		opts.inSampleSize = 1;
//		opts.inPreferredConfig = Config.ARGB_8888;
//		
//		progress_frame_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.leaf_kuang, opts);
//		progress_frame_out_width = progress_frame_bitmap.getWidth();
//		progress_frame_out_height = progress_frame_bitmap.getHeight();
		
		leaf_bitmap = ((BitmapDrawable)(getResources().getDrawable(R.drawable.leaf))).getBitmap();
		leaf_out_width = leaf_bitmap.getWidth();
		leaf_out_height = leaf_bitmap.getHeight();
		
		
		//第二种方式
		progress_frame_bitmap= ((BitmapDrawable)getResources().getDrawable(R.drawable.leaf_kuang)).getBitmap();
		progress_frame_out_width = progress_frame_bitmap.getWidth();
		progress_frame_out_height = progress_frame_bitmap.getHeight();
		
		
		
		
	}
	
	/**
	 * 渲染view中的内容
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawProgressAndLeafs(canvas);
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
		progress_width = total_width - progress_left_top_bottom_margin - progress_right_margin;
		//计算弧度的半径
		arc_radius =  (total_height -  2 * progress_left_top_bottom_margin) / 2;
				
		//初始矩形view. 注意：这里不是RectF 
		view_rect = new Rect(0, 0, total_width, total_height);
		//初始进度条边框矩形 注意：这里不是RectF 
		progress_frame_rect = new Rect(0, 0, progress_frame_out_width, progress_frame_out_height);
		
		//初始进度条白色区域矩形(包含弧度区域部分)
		progress_white_rectf = new RectF(
				progress_left_top_bottom_margin + current_progress_position, 
				progress_left_top_bottom_margin, 
				total_width - progress_right_margin, //这里不一样
				total_height - progress_left_top_bottom_margin //这里不一样
				);
		
		
		
				
		//初始进度条橘色区域矩形(不包含弧度区域部分)
		progress_orange_rectf = new RectF(
				progress_left_top_bottom_margin + arc_radius, 
				progress_left_top_bottom_margin, 
				current_progress_position,                                                                                                                                                                   
				total_height - progress_left_top_bottom_margin //这里不一样
				);
		
		
		//弧度矩形
		arc_rectf = new RectF(
				progress_left_top_bottom_margin, 
				progress_left_top_bottom_margin, 
				progress_left_top_bottom_margin + 2 * arc_radius, 
				total_height - progress_left_top_bottom_margin
				);
		
		//初始弧形的右上角的x坐标，也是矩形x坐标的起始点
        mArcRightLocation = progress_left_top_bottom_margin + arc_radius;
        
	}
	
	/**
	 * 绘制进度条和叶子
	 * @param leafs2  叶子集合
	 */
	private void drawProgressAndLeafs(Canvas  canvas) {
		
		 if (mProgress >= TOTAL_PROGRESS) {
	            mProgress = 0;
	        }
	        // mProgressWidth为进度条的宽度，根据当前进度算出进度条的位置
	        //mProgressWidth 在onSizeChanage()方法进行初始化
	        current_progress_position = progress_width * mProgress / TOTAL_PROGRESS;
	        // 即当前位置在图中所示1范围内
	        //当前进度条的位置小于弧形半径
	        if (current_progress_position < arc_radius) {
	            // 1.绘制白色ARC，绘制orange ARC
	            // 2.绘制白色矩形

	            // 1.绘制白色ARC
	            canvas.drawArc(
	                    arc_rectf,//RectF
	                    90,//startAngle //起始角度
	                    180,//sweepAngle //顺时针扫过的角度 (圆心角角度，360为圆，180为半圆)
	                    false,//useCenter
	                    whitePaint //Paint
	            );

	            // 2.绘制白色矩形
	            progress_white_rectf.left = mArcRightLocation;
	            canvas.drawRect(progress_white_rectf, whitePaint);

	            // 绘制叶子
	            drawLeafs(canvas);

	            // 3.绘制棕色 ARC
	            // 单边角度
	            //Math.acos(double d)返回余弦值为指定数字的角度
	            //Matn.toDegress(double d) 转换以弧度为单位测得的角度大致相等的角度，以度衡量.  弧度转换为角度
	            int angle = (int) Math.toDegrees(Math.acos((arc_radius - current_progress_position) / (float) arc_radius));
	            // 起始的位置
	            int startAngle = 180 - angle;
	            // 扫过的角度
	            int sweepAngle = 2 * angle;
	            canvas.drawArc(arc_rectf, startAngle, sweepAngle, false, orangePaint);
	        } else {

	            // 1.绘制white RECT
	            // 2.绘制Orange ARC
	            // 3.绘制orange RECT
	            // 这个层级进行绘制能让叶子感觉是融入棕色进度条中
	            // 1.绘制white RECT
	        	progress_white_rectf.left = current_progress_position;
	            canvas.drawRect(progress_white_rectf, whitePaint);
	            // 绘制叶子
	            drawLeafs(canvas);
	            // 2.绘制Orange ARC
	            canvas.drawArc(arc_rectf, 90, 180, false, orangePaint);
	            // 3.绘制orange RECT
	            progress_orange_rectf.left = mArcRightLocation;
	            progress_orange_rectf.right = current_progress_position;
	            canvas.drawRect(progress_orange_rectf, orangePaint);

	        }

		
		
	}

    /**
     * 绘制叶子
     * 
     * @param canvas
     */
    private void drawLeafs(Canvas canvas) {
    	//初始叶子旋转一周所需时间
        mLeafRotateTime = mLeafRotateTime <= 0 ? LEAF_ROTATE_TIME : mLeafRotateTime;

        long currentTime = System.currentTimeMillis();

        //遍历随机生成的叶子
        for (int i = 0; i < leafs.size(); i++) {
            Leaf leaf = leafs.get(i);
            if (currentTime > leaf.startTime && leaf.startTime != 0) {
                //绘制叶子－－根据叶子的类型和当前时间得出叶子的（x，y）
                getLeafLocation(leaf, currentTime);
                
                canvas.save();
                
                //通过Matrix控制叶子移动和旋转
                Matrix matrix = new Matrix();
                float transX = progress_left_top_bottom_margin + leaf.x;
                float transY = progress_left_top_bottom_margin + leaf.y;
                //进行叶子移动
                matrix.postTranslate(transX, transY);
                
                //控制叶子旋转
                // 通过时间关联旋转角度，则可以直接通过修改LEAF_ROTATE_TIME调节叶子旋转快慢
                // 
                float rotateFraction = ((currentTime - leaf.startTime) % mLeafRotateTime) / (float) mLeafRotateTime;
                int angle = (int) (rotateFraction * 360);
                // 根据叶子旋转方向确定叶子旋转角度
                int rotate = leaf.rotateDirection == 0 ? angle + leaf.rotateAngle : -angle + leaf.rotateAngle;
                //进行叶子旋转
                matrix.postRotate(
                        rotate,
                        transX + leaf_out_width / 2,
                        transY + leaf_out_height/ 2

                );

                canvas.drawBitmap(leaf_bitmap, matrix, bitmapPaint);

                canvas.restore();

            } else {
                continue;
            }
        }
    }
    
    private void getLeafLocation(Leaf leaf, long currentTime) {
        long intervalTime = currentTime - leaf.startTime;// 相差秒
        if (intervalTime < 0) {//则还没有出现进度条中
            return;
        } else if (intervalTime > mLeafFloatTime) {//大于飘动一周的时间，重新计算当前叶子的开始的时间。当前时间 + 飘动的一个周期
            leaf.startTime = System.currentTimeMillis() + new Random().nextInt((int) mLeafFloatTime);
        }

        //计算叶子在进度条中所在的位置
        //1.计算当前叶子在一个周期内飘动过的时间比率：  T = (当前时间 - 叶子开始时间 ) / 叶子飘动的一周的时间
        //2.计算在进度条上飘过的距离 W = 进度条的宽度  * T(叶子一周期内飘过的时间比率)
        //3.计算叶子在X轴飘过的坐标：X = 进度条的宽度 - W(叶子在进度条飘过的距离)
        //4.计算叶子在Y轴飘过的坐标：y=Asin（ωx+φ）+h
        float fraction = (float) intervalTime / mLeafFloatTime;
        leaf.x = (int) (progress_width - progress_width * fraction);
        leaf.y = getLocationY(leaf);
    }

    // 通过叶子信息获取当前叶子的Y值
    private int getLocationY(Leaf leaf) {
        //y=Asin（ωx+φ）+h
    	//各常数值对函数图像的影响：
    	//A：决定峰值（即纵向拉伸压缩的倍数）- 振幅
    	//φ（初相位）：决定波形与X轴位置关系或横向移动距离
    	//ω：决定周期（最小正周期T=2π/|ω|）
    	//h：表示波形在Y轴的位置关系或纵向移动距离
    	
        float w = (float) ((float) 2 * Math.PI / progress_width);
        float a = mMiddleAmplitude;
        switch (leaf.type) {
            case LITTLE:
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
        return (int) (a * Math.sin(w * leaf.x)) +  arc_radius * 2 / 3;
    }

    
    private enum StartType {
        LITTLE, MIDDLE, BIG
    }
    
    private class Leaf {

        // 在绘制部分的位置
        float x, y;
        // 控制叶子飘动的幅度
        StartType type;
        // 旋转角度
        int rotateAngle;
        // 旋转方向--0代表顺时针，1代表逆时针
        int rotateDirection;
        // 起始时间(ms)
        long startTime;
    }
    /**
     * 叶子工厂，用于生成叶子对象
     */
    private class LeafFactory {
        private static final int MAX_LEAFS = 8;
        Random random = new Random();

        // 生成一个叶子信息
        public Leaf generateLeaf() {
            Leaf leaf = new Leaf();
            int randomType = random.nextInt(3);
            // 随时类型－ 叶子飘浮的随机振幅
            StartType type = StartType.MIDDLE;
            switch (randomType) {
                case 0:
                    break;
                case 1:
                    type = StartType.LITTLE;
                    break;
                case 2:
                    type = StartType.BIG;
                    break;
                default:
                    break;
            }
            leaf.type = type;
            // 随机起始的旋转角度
            leaf.rotateAngle = random.nextInt(360);
            // 随机旋转方向（顺时针或逆时针）
            leaf.rotateDirection = random.nextInt(2);
            // 为了产生交错的感觉，让开始的时间有一定的随机性
            mLeafFloatTime = mLeafFloatTime <= 0 ? LEAF_FLOAT_TIME : mLeafFloatTime;
            mAddTime += random.nextInt((int) (mLeafFloatTime * 2));
            leaf.startTime = System.currentTimeMillis() + mAddTime;
            return leaf;
        }
        
     // 根据最大叶子数产生叶子信息
        public List<Leaf> generateLeafs() {
            return generateLeafs(MAX_LEAFS);
        }

        // 根据传入的叶子数量产生叶子信息
        public List<Leaf> generateLeafs(int leafSize) {
            List<Leaf> leafs = new LinkedList<Leaf>();
            for (int i = 0; i < leafSize; i++) {
                leafs.add(generateLeaf());
            }
            return leafs;
        }
        
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
