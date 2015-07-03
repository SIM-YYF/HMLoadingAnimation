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
	 * view ����
	 */
	Rect  view_rect;
	
	/**
	 * �������߿����
	 */
	Rect  progress_frame_rect;
	
	/**
	 * ��������ɫ�������
	 */
	RectF  progress_white_rectf;
	/**
	 * ��������ɫ�������
	 */
	RectF progress_orange_rectf;
	/**
	 * �����������������
	 */
	RectF arc_rectf;
	
	/**
	 * view �Ŀ��
	 */
	int total_width = 0;
	/**
	 * view �ĸ߶�
	 */
	int total_height = 0;
	/**
	 * ���Ȱ뾶
	 */
	int arc_radius = 0;
	
	/**
	 * �������߿���
	 */
	int progress_frame_out_width;
	/**
	 * �������߿�߶�
	 */
	int progress_frame_out_height;
	
	/**
	 * Ҷ��ͼƬ�Ŀ��
	 */
	int leaf_out_width;
	/**
	 * Ҷ��ͼƬ�ĸ߶�
	 */
	int leaf_out_height;
	
	private static final int PROGRESS_LEFT_TOP_BOTTOM_MARGIN = 10;
	private static final int PROGRESS_RIGHT_MARGIN = 25;
	 // ����ɫ
    private static final int WHITE_COLOR = 0xfffde399;
    // ��ɫ
    private static final int ORANGE_COLOR = 0xffffa800;
    // ���������ܽ���
    private static final int TOTAL_PROGRESS = 100;
	/**
	 * ���ƻ��ƽ�����left��top��bottom�߾�
	 */
	int progress_left_top_bottom_margin = 10;
	/**
	 * ���ƻ��ƽ�����right�߾�
	 */
	int progress_right_margin = 25;
	/**
	 * ��ǰ���ڵĻ��ƵĽ�������λ��
	 */
	int current_progress_position = 0;
	/**
	 * ��ǰ�������Ŀ��
	 */
	int current_progress_width;
	/**
	 * ��ǰ����������
	 */
	int mProgress = 0;
	
	/**
	 * �������Ͻ�����
	 */
	int radian_right_x_location = 0;
	
	/**
	 * �������߿�
	 */
	Bitmap progress_frame_bitmap;
	/**
	 * Ҷ��ͼƬ
	 */
	Bitmap leaf_bitmap;
	
	Paint  bitmapPaint;
	Paint  whitePaint;
	Paint  orangePaint;
	float density;
	
	/**
	 * Ҷ�Ӽ���
	 */
	List<Leaf> leafs;
	LeaFactory  factory;
	
	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//��ʼ��������view�е�left top right bottom ��marginֵ
		initProgressMargin(context);
		// ��ʼ����
		initPaint();
		//��ʼͼƬ
		initBitmap();
		
		//����Ҷ��
		factory = new LeaFactory();
		leafs = factory.getLeafs(6);
		
		
	}

	/**
	 * ��ʼ����
	 * 1.�������߿�ͼƬ�Ļ���
	 * 2.���ư�ɫ����Ļ���
	 * 3.������ɫ����Ļ���
	 */
	private void initPaint() {
		bitmapPaint = new Paint();
		bitmapPaint.setDither(true);//������
		bitmapPaint.setAntiAlias(true);//�����
		
		whitePaint = new Paint();
		whitePaint.setAntiAlias(true);
		whitePaint.setColor(WHITE_COLOR);
		
		orangePaint = new Paint();
		orangePaint.setAntiAlias(true);
		orangePaint.setColor(ORANGE_COLOR);
	}

	/**
	 * ��ʼ��������view�е�left top right bottom ��marginֵ
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
	 * ��ʼͼƬ
	 */
	private void initBitmap(){
		
		//��һ�ַ�ʽ
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
		
		
		//�ڶ��ַ�ʽ
//		progress_frame_bitmap= ((BitmapDrawable)getResources().getDrawable(R.drawable.leaf_kuang)).getBitmap();
//		progress_frame_out_width = progress_frame_bitmap.getWidth();
//		progress_frame_out_height = progress_frame_bitmap.getHeight();
		
		
		
		
	}
	
	/**
	 * ��Ⱦview�е�����
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawProgressAndLeafs(canvas, leafs);
		canvas.drawBitmap(progress_frame_bitmap, progress_frame_rect, view_rect, bitmapPaint);
		postInvalidate();//ע�⣺Invalidate  ��  postInvalidate ������
	}
	
	/**
	 * ��view��С�����ı��Ժ󣬴����÷���
	 *
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		//view �Ŀ��
		total_width = w;
		//view�ĸ߶�
		total_height = h;
		
		//��ǰ�������Ŀ��
		current_progress_width = progress_frame_out_width - progress_left_top_bottom_margin - progress_right_margin;
		//���㻡�ȵİ뾶
		arc_radius =  (progress_frame_out_width -  2 *progress_left_top_bottom_margin) / 2;
				
		//��ʼ����view. ע�⣺���ﲻ��RectF 
		view_rect = new Rect(0, 0, total_width, total_height);
		//��ʼ�������߿���� ע�⣺���ﲻ��RectF 
		progress_frame_rect = new Rect(0, 0, progress_frame_out_width, progress_frame_out_height);
		
		//��ʼ��������ɫ�������(�����������򲿷�)
		progress_white_rectf = new RectF(
				progress_left_top_bottom_margin + arc_radius + current_progress_position, 
				progress_left_top_bottom_margin, 
				progress_frame_out_width - progress_right_margin, //���ﲻһ��
				progress_frame_out_height - progress_left_top_bottom_margin //���ﲻһ��
				);
		
		
		
				
		//��ʼ��������ɫ�������(�������������򲿷�)
		progress_orange_rectf = new RectF(
				progress_left_top_bottom_margin + arc_radius, 
				progress_left_top_bottom_margin, 
				current_progress_position,                                                                                                                                                                   
				progress_frame_out_height - progress_left_top_bottom_margin //���ﲻһ��
				);
		
		
		//���Ⱦ���
		arc_rectf = new RectF(
				progress_left_top_bottom_margin, 
				progress_left_top_bottom_margin, 
				progress_left_top_bottom_margin + 2 * arc_radius, 
				progress_frame_out_height - progress_left_top_bottom_margin
				);
		
	}
	
	/**
	 * ���ƽ�������Ҷ��
	 * @param leafs2  Ҷ�Ӽ���
	 */
	private void drawProgressAndLeafs(Canvas  canvas, List<Leaf> leafs2) {
		
		//1.���ý������ܽ���
		if(mProgress == TOTAL_PROGRESS) mProgress = 0;
		
		//2.���㵱ǰ����������λ��
		// 2-1���ѽ�������ʵ�ʿ�Ȱ����������ܽ���100������ƽ�����䣬�ֳ�100��.���ÿһ�ݵĽ���λ��
		// 2-2:���ݵ�ǰ���������� * ÿһ�ݵĽ�����λ�ã��Ϳ��Եõ���ǰ����������λ��
		current_progress_position = current_progress_width / TOTAL_PROGRESS  * mProgress;
		
		//3.���ƽ�������Ҷ��
		
		if(current_progress_position < arc_radius){//��ǰ������λ��  < ���Ȱ뾶
			//1.���ư�ɫ����Ļ���
				canvas.drawArc(arc_rectf, 90f, 180f, false, whitePaint);
			//2.���ư�ɫ����ľ���
				//��������left���� = left_margin + ���Ȱ뾶   (��ʵ����ȥ�����Ȳ���)
				progress_white_rectf.left = progress_left_top_bottom_margin + arc_radius;
				canvas.drawRect(progress_white_rectf, whitePaint);
			//3.����Ҷ��
				drawLeafs(canvas);
			//4.������ɫ����Ļ���
				//�Ƕ� �ȣ��Ի���Ϊ��λ������ 0 �ܦȡܦ�
				double valuebyarc = Math.acos((arc_radius - current_progress_position) / arc_radius);
				//�Ի���Ϊ��λ��õĽǶȴ�����ȵĽǶȣ��ԶȺ���
				int angrad = (int) Math.toDegrees(valuebyarc);
				 // ��ʼ��λ��
	            int startAngle = 180 - angrad;
	            // ɨ���ĽǶ�
	            int sweepAngle = 2 * angrad;
	            
				canvas.drawArc(progress_orange_rectf, startAngle, sweepAngle, false, orangePaint);
		}else{
			//1.���ư�ɫ����ľ���
			progress_white_rectf.left = progress_left_top_bottom_margin + arc_radius;
			canvas.drawRect(progress_white_rectf, whitePaint);
			//2.����Ҷ��
			drawLeafs(canvas);
			//3.������ɫ����Ļ���
			canvas.drawArc(arc_rectf, 90, 180, false, whitePaint);
			//4.������ɫ����ľ���
			progress_orange_rectf.left = progress_left_top_bottom_margin + arc_radius;//ȥ�����Ȳ���
			progress_orange_rectf.right = current_progress_position;//��ǰ�������Ľ���λ��
			canvas.drawRect(progress_orange_rectf, orangePaint);
		}
		
		
		
	}

	/**
	 * ����Ҷ��
	 * @param canvas
	 */
	private void drawLeafs(Canvas canvas) {
		// TODO Auto-generated method stub
		//��ʼҶ����תһ������ʱ��
		if(factory.getLeaf_rotate_cycle_time() <= 0){
			factory.setLeaf_rotate_cycle_time(factory.DEFAULT_LEAF_ROTATE_CYCLE_TIME);
		}
		//��ʼҶ��Ʈ��һ������ʱ��
		if(factory.getLeaf_float_cycle_time() <= 0){
			factory.setLeaf_float_cycle_time(factory.DEFAULT_LEAF_FLOAT_CYCLE_TIME);
		}
		
		long currentTime = System.currentTimeMillis();
		
		Leaf leaf;
		for(int i = 0 ; i < leafs.size(); i++){
			leaf = leafs.get(i);
			if(currentTime > leaf.getStartTime() && leaf.getStartTime() != 0){
				//1.����Ҷ�ӵ����ͺ͵�ǰʱ��ó�Ҷ�ӵģ�x��y��
				getLeafLocation(leaf, currentTime);
                canvas.save();//ע������
                
                //2.����Ҷ�ӵ��ƶ�����ת
                Matrix matrix = new Matrix();
                
					float transX = progress_left_top_bottom_margin + leaf.getX();
					float transY = progress_left_top_bottom_margin + leaf.getY();
					//����Ҷ�ӽ����ƶ�
					matrix.postTranslate(transX, transX);
                
                //����Ҷ�ӽ�����ת
	                // ͨ��ʱ�������ת�Ƕȣ������ֱ��ͨ���޸�LEAF_ROTATE_TIME����Ҷ����ת����
	                float rotateFraction = ((currentTime - leaf.getStartTime()) % factory.getLeaf_rotate_cycle_time()) / (float) factory.getLeaf_rotate_cycle_time();
	                int angle = (int) (rotateFraction * 360);
	                // ����Ҷ����ת����ȷ��Ҷ����ת�Ƕ�
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
		 long intervalTime = currentTime - leaf.getStartTime();// �������3��
	        if (intervalTime < 0) {
	            return;
	        } else if (intervalTime > factory.getLeaf_float_cycle_time()) {//����2��
	            leaf.setStartTime(System.currentTimeMillis() + new Random().nextInt((int) factory.getLeaf_float_cycle_time()));
	        }

	        float fraction = (float) intervalTime / factory.getLeaf_float_cycle_time();
	        leaf.setX((int) (current_progress_width - current_progress_width * fraction));
	        leaf.setY(getLocationY(leaf));
	}


    // ͨ��Ҷ����Ϣ��ȡ��ǰҶ�ӵ�Yֵ
    private int getLocationY(Leaf leaf) {
        // y = A(wx+Q)+h
        //���ȸ���Ч���������ȷ���� ���ߺ�������׼��������Ϊ��y = A(wx+Q)+h������wӰ�����ڣ�AӰ����� ������T�� 2 * Math.PI/w;
       // ����Ч�����Կ��������ڴ���Ϊ�ܽ��ȳ��ȣ�����ȷ��w��(float) ((float) 2 * Math.PI /mProgressWidth)��

        // �е������С
         int mMiddleAmplitude = 13;
        // �����
         int mAmplitudeDisparity = 5;
        
        float w = (float) ((float) 2 * Math.PI / current_progress_width);
        float a = mMiddleAmplitude;
        switch (leaf.getType()) {
            case SMALL:
                // С��� �� �е���� �� �����
                a = mMiddleAmplitude - mAmplitudeDisparity;
                break;
            case MIDDLE:
                a = mMiddleAmplitude;
                break;
            case BIG:
                // ����� �� �е���� + �����
                a = mMiddleAmplitude + mAmplitudeDisparity;
                break;
            default:
                break;
        }
        return (int) (a * Math.sin(w * leaf.getX())) + arc_radius * 2 / 3;
    }
	
	
    /**
     * ���ý���
     * 
     * @param progress
     */
    public void setProgress(int progress) {
        this.mProgress = progress;
        postInvalidate();
    }
	
}
