package com.hm;

public class Leaf {
	
	//���Ʋ��ֵ�λ��
	private float x;
	private float y;
	
	//����Ҷ��Ʈ�����
	private AmplitudeType  type;
	//��ת�Ƕ�
	private  int rotateAngle;
	//��ת���� 0����˳ʱ�룬1������ʱ��
	private int rotateDirection;
	//��ʼʱ��
	private long startTime;
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public AmplitudeType getType() {
		return type;
	}
	public void setType(AmplitudeType type) {
		this.type = type;
	}
	public int getRotateAngle() {
		return rotateAngle;
	}
	public void setRotateAngle(int rotateAngle) {
		this.rotateAngle = rotateAngle;
	}
	public int getRotateDirection() {
		return rotateDirection;
	}
	public void setRotateDirection(int rotateDirection) {
		this.rotateDirection = rotateDirection;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
	
	
	
}
