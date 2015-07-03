package com.hm;

public class Leaf {
	
	//绘制部分的位置
	private float x;
	private float y;
	
	//控制叶子飘动振幅
	private AmplitudeType  type;
	//旋转角度
	private  int rotateAngle;
	//旋转方向 0代表顺时针，1代表逆时针
	private int rotateDirection;
	//起始时间
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
