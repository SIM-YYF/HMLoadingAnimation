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
	
	private static final int PROGRESS_LEFT_TOP_BOTTOM_MARGIN = 12;
	private static final int PROGRESS_RIGHT_MARGIN = 25;
    // Ҷ��Ʈ��һ������������ʱ��
    private static final long LEAF_FLOAT_TIME = 3000;
    // Ҷ����תһ����Ҫ��ʱ��
    private static final long LEAF_ROTATE_TIME = 2000;
    
	 // ����ɫ
    private static final int WHITE_COLOR = 0xfffde399;
    // ��ɫ
    private static final int ORANGE_COLOR = 0xffffa800;
    
    // ���������ܽ���
    private static final int TOTAL_PROGRESS = 100;
    // �е������С
    private static final int MIDDLE_AMPLITUDE = 13;
    // ��ͬ����֮���������
    private static final int AMPLITUDE_DISPARITY = 5;
    
    
    /**
     * Ҷ��Ʈ��һ������������ʱ��
     */
    private long mLeafFloatTime = LEAF_FLOAT_TIME;
    /**
     *  Ҷ����תһ����Ҫ��ʱ��
      */
    private long mLeafRotateTime = LEAF_ROTATE_TIME;
    
    // �е������С
    private int mMiddleAmplitude = MIDDLE_AMPLITUDE;
    // �����
    private int mAmplitudeDisparity = AMPLITUDE_DISPARITY;
    
    
	/**
	 * ���ƻ��ƽ�����left��top��bottom�߾�
	 */
	int progress_left_top_bottom_margin = 0;
	/**
	 * ���ƻ��ƽ�����right�߾�
	 */
	int progress_right_margin = 0;
	/**
	 * ��ǰ���ڵĻ��ƵĽ�������λ��
	 */
	int current_progress_position = 0;
	/**
	 * �������Ŀ��
	 */
	int progress_width;
	/**
	 * ��ǰ����������
	 */
	int mProgress = 0;
	
	// arc�����Ͻǵ�x���꣬Ҳ�Ǿ���x�������ʼ��
    private int mArcRightLocation;
	
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
	LeafFactory  factory;
	
	 // ���ڿ���������ӵ�ʱ�䲻����
    private int mAddTime;
	
	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//��ʼ��������view�е�left top right bottom ��marginֵ
		initProgressMargin(context);
		// ��ʼ����
		initPaint();
		//��ʼͼƬ
		initBitmap();
		
		//����Ҷ��
		factory = new LeafFactory();
		leafs = factory.generateLeafs();
		
		
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
		display.getMetrics(metric);
		density = metric.density;
		
		progress_left_top_bottom_margin = (int) (PROGRESS_LEFT_TOP_BOTTOM_MARGIN * density);
		progress_right_margin = (int)(PROGRESS_RIGHT_MARGIN * density);
	}
	
	/**
	 * ��ʼͼƬ
	 */
	private void initBitmap(){
		
		//��һ�ַ�ʽ
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
		
		
		//�ڶ��ַ�ʽ
		progress_frame_bitmap= ((BitmapDrawable)getResources().getDrawable(R.drawable.leaf_kuang)).getBitmap();
		progress_frame_out_width = progress_frame_bitmap.getWidth();
		progress_frame_out_height = progress_frame_bitmap.getHeight();
		
		
		
		
	}
	
	/**
	 * ��Ⱦview�е�����
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawProgressAndLeafs(canvas);
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
		progress_width = total_width - progress_left_top_bottom_margin - progress_right_margin;
		//���㻡�ȵİ뾶
		arc_radius =  (total_height -  2 * progress_left_top_bottom_margin) / 2;
				
		//��ʼ����view. ע�⣺���ﲻ��RectF 
		view_rect = new Rect(0, 0, total_width, total_height);
		//��ʼ�������߿���� ע�⣺���ﲻ��RectF 
		progress_frame_rect = new Rect(0, 0, progress_frame_out_width, progress_frame_out_height);
		
		//��ʼ��������ɫ�������(�����������򲿷�)
		progress_white_rectf = new RectF(
				progress_left_top_bottom_margin + current_progress_position, 
				progress_left_top_bottom_margin, 
				total_width - progress_right_margin, //���ﲻһ��
				total_height - progress_left_top_bottom_margin //���ﲻһ��
				);
		
		
		
				
		//��ʼ��������ɫ�������(�������������򲿷�)
		progress_orange_rectf = new RectF(
				progress_left_top_bottom_margin + arc_radius, 
				progress_left_top_bottom_margin, 
				current_progress_position,                                                                                                                                                                   
				total_height - progress_left_top_bottom_margin //���ﲻһ��
				);
		
		
		//���Ⱦ���
		arc_rectf = new RectF(
				progress_left_top_bottom_margin, 
				progress_left_top_bottom_margin, 
				progress_left_top_bottom_margin + 2 * arc_radius, 
				total_height - progress_left_top_bottom_margin
				);
		
		//��ʼ���ε����Ͻǵ�x���꣬Ҳ�Ǿ���x�������ʼ��
        mArcRightLocation = progress_left_top_bottom_margin + arc_radius;
        
	}
	
	/**
	 * ���ƽ�������Ҷ��
	 * @param leafs2  Ҷ�Ӽ���
	 */
	private void drawProgressAndLeafs(Canvas  canvas) {
		
		 if (mProgress >= TOTAL_PROGRESS) {
	            mProgress = 0;
	        }
	        // mProgressWidthΪ�������Ŀ�ȣ����ݵ�ǰ���������������λ��
	        //mProgressWidth ��onSizeChanage()�������г�ʼ��
	        current_progress_position = progress_width * mProgress / TOTAL_PROGRESS;
	        // ����ǰλ����ͼ����ʾ1��Χ��
	        //��ǰ��������λ��С�ڻ��ΰ뾶
	        if (current_progress_position < arc_radius) {
	            // 1.���ư�ɫARC������orange ARC
	            // 2.���ư�ɫ����

	            // 1.���ư�ɫARC
	            canvas.drawArc(
	                    arc_rectf,//RectF
	                    90,//startAngle //��ʼ�Ƕ�
	                    180,//sweepAngle //˳ʱ��ɨ���ĽǶ� (Բ�ĽǽǶȣ�360ΪԲ��180Ϊ��Բ)
	                    false,//useCenter
	                    whitePaint //Paint
	            );

	            // 2.���ư�ɫ����
	            progress_white_rectf.left = mArcRightLocation;
	            canvas.drawRect(progress_white_rectf, whitePaint);

	            // ����Ҷ��
	            drawLeafs(canvas);

	            // 3.������ɫ ARC
	            // ���߽Ƕ�
	            //Math.acos(double d)��������ֵΪָ�����ֵĽǶ�
	            //Matn.toDegress(double d) ת���Ի���Ϊ��λ��õĽǶȴ�����ȵĽǶȣ��ԶȺ���.  ����ת��Ϊ�Ƕ�
	            int angle = (int) Math.toDegrees(Math.acos((arc_radius - current_progress_position) / (float) arc_radius));
	            // ��ʼ��λ��
	            int startAngle = 180 - angle;
	            // ɨ���ĽǶ�
	            int sweepAngle = 2 * angle;
	            canvas.drawArc(arc_rectf, startAngle, sweepAngle, false, orangePaint);
	        } else {

	            // 1.����white RECT
	            // 2.����Orange ARC
	            // 3.����orange RECT
	            // ����㼶���л�������Ҷ�Ӹо���������ɫ��������
	            // 1.����white RECT
	        	progress_white_rectf.left = current_progress_position;
	            canvas.drawRect(progress_white_rectf, whitePaint);
	            // ����Ҷ��
	            drawLeafs(canvas);
	            // 2.����Orange ARC
	            canvas.drawArc(arc_rectf, 90, 180, false, orangePaint);
	            // 3.����orange RECT
	            progress_orange_rectf.left = mArcRightLocation;
	            progress_orange_rectf.right = current_progress_position;
	            canvas.drawRect(progress_orange_rectf, orangePaint);

	        }

		
		
	}

    /**
     * ����Ҷ��
     * 
     * @param canvas
     */
    private void drawLeafs(Canvas canvas) {
    	//��ʼҶ����תһ������ʱ��
        mLeafRotateTime = mLeafRotateTime <= 0 ? LEAF_ROTATE_TIME : mLeafRotateTime;

        long currentTime = System.currentTimeMillis();

        //����������ɵ�Ҷ��
        for (int i = 0; i < leafs.size(); i++) {
            Leaf leaf = leafs.get(i);
            if (currentTime > leaf.startTime && leaf.startTime != 0) {
                //����Ҷ�ӣ�������Ҷ�ӵ����ͺ͵�ǰʱ��ó�Ҷ�ӵģ�x��y��
                getLeafLocation(leaf, currentTime);
                
                canvas.save();
                
                //ͨ��Matrix����Ҷ���ƶ�����ת
                Matrix matrix = new Matrix();
                float transX = progress_left_top_bottom_margin + leaf.x;
                float transY = progress_left_top_bottom_margin + leaf.y;
                //����Ҷ���ƶ�
                matrix.postTranslate(transX, transY);
                
                //����Ҷ����ת
                // ͨ��ʱ�������ת�Ƕȣ������ֱ��ͨ���޸�LEAF_ROTATE_TIME����Ҷ����ת����
                // 
                float rotateFraction = ((currentTime - leaf.startTime) % mLeafRotateTime) / (float) mLeafRotateTime;
                int angle = (int) (rotateFraction * 360);
                // ����Ҷ����ת����ȷ��Ҷ����ת�Ƕ�
                int rotate = leaf.rotateDirection == 0 ? angle + leaf.rotateAngle : -angle + leaf.rotateAngle;
                //����Ҷ����ת
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
        long intervalTime = currentTime - leaf.startTime;// �����
        if (intervalTime < 0) {//��û�г��ֽ�������
            return;
        } else if (intervalTime > mLeafFloatTime) {//����Ʈ��һ�ܵ�ʱ�䣬���¼��㵱ǰҶ�ӵĿ�ʼ��ʱ�䡣��ǰʱ�� + Ʈ����һ������
            leaf.startTime = System.currentTimeMillis() + new Random().nextInt((int) mLeafFloatTime);
        }

        //����Ҷ���ڽ����������ڵ�λ��
        //1.���㵱ǰҶ����һ��������Ʈ������ʱ����ʣ�  T = (��ǰʱ�� - Ҷ�ӿ�ʼʱ�� ) / Ҷ��Ʈ����һ�ܵ�ʱ��
        //2.�����ڽ�������Ʈ���ľ��� W = �������Ŀ��  * T(Ҷ��һ������Ʈ����ʱ�����)
        //3.����Ҷ����X��Ʈ�������꣺X = �������Ŀ�� - W(Ҷ���ڽ�����Ʈ���ľ���)
        //4.����Ҷ����Y��Ʈ�������꣺y=Asin����x+�գ�+h
        float fraction = (float) intervalTime / mLeafFloatTime;
        leaf.x = (int) (progress_width - progress_width * fraction);
        leaf.y = getLocationY(leaf);
    }

    // ͨ��Ҷ����Ϣ��ȡ��ǰҶ�ӵ�Yֵ
    private int getLocationY(Leaf leaf) {
        //y=Asin����x+�գ�+h
    	//������ֵ�Ժ���ͼ���Ӱ�죺
    	//A��������ֵ������������ѹ���ı�����- ���
    	//�գ�����λ��������������X��λ�ù�ϵ������ƶ�����
    	//�أ��������ڣ���С������T=2��/|��|��
    	//h����ʾ������Y���λ�ù�ϵ�������ƶ�����
    	
        float w = (float) ((float) 2 * Math.PI / progress_width);
        float a = mMiddleAmplitude;
        switch (leaf.type) {
            case LITTLE:
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
        return (int) (a * Math.sin(w * leaf.x)) +  arc_radius * 2 / 3;
    }

    
    private enum StartType {
        LITTLE, MIDDLE, BIG
    }
    
    private class Leaf {

        // �ڻ��Ʋ��ֵ�λ��
        float x, y;
        // ����Ҷ��Ʈ���ķ���
        StartType type;
        // ��ת�Ƕ�
        int rotateAngle;
        // ��ת����--0����˳ʱ�룬1������ʱ��
        int rotateDirection;
        // ��ʼʱ��(ms)
        long startTime;
    }
    /**
     * Ҷ�ӹ�������������Ҷ�Ӷ���
     */
    private class LeafFactory {
        private static final int MAX_LEAFS = 8;
        Random random = new Random();

        // ����һ��Ҷ����Ϣ
        public Leaf generateLeaf() {
            Leaf leaf = new Leaf();
            int randomType = random.nextInt(3);
            // ��ʱ���ͣ� Ҷ��Ʈ����������
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
            // �����ʼ����ת�Ƕ�
            leaf.rotateAngle = random.nextInt(360);
            // �����ת����˳ʱ�����ʱ�룩
            leaf.rotateDirection = random.nextInt(2);
            // Ϊ�˲�������ĸо����ÿ�ʼ��ʱ����һ���������
            mLeafFloatTime = mLeafFloatTime <= 0 ? LEAF_FLOAT_TIME : mLeafFloatTime;
            mAddTime += random.nextInt((int) (mLeafFloatTime * 2));
            leaf.startTime = System.currentTimeMillis() + mAddTime;
            return leaf;
        }
        
     // �������Ҷ��������Ҷ����Ϣ
        public List<Leaf> generateLeafs() {
            return generateLeafs(MAX_LEAFS);
        }

        // ���ݴ����Ҷ����������Ҷ����Ϣ
        public List<Leaf> generateLeafs(int leafSize) {
            List<Leaf> leafs = new LinkedList<Leaf>();
            for (int i = 0; i < leafSize; i++) {
                leafs.add(generateLeaf());
            }
            return leafs;
        }
        
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
