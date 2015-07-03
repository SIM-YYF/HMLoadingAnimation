package com.hm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LeaFactory {

	public static final int DEFAULT_LEAF_FLOAT_CYCLE_TIME = 3000;
	public static final int DEFAULT_LEAF_ROTATE_CYCLE_TIME = 2000;
	//叶子飘动一个周期
	private long leaf_float_cycle_time;
	//叶子旋转一个周期
	private  long leaf_rotate_cycle_time;
	
	/**
	 * 生成叶子
	 * @return
	 */
	private Leaf getLeaf(){
	
		
		Leaf  leaf = new Leaf();
		Random random = new Random();
		int random_num = random.nextInt(3);
		AmplitudeType  type = AmplitudeType.SMALL;
		switch (random_num) {
		case 0:
			type = AmplitudeType.SMALL;
			break;
		case 1:
			type = AmplitudeType.SMALL;
			break;
		case 2:
			type = AmplitudeType.SMALL;
			break;
		default:
			break;
		}
		leaf.setType(type);

		//随机旋转角度
		leaf.setRotateAngle(random.nextInt(360));
		//随机旋转方向 (0代表顺时针，1代表逆时针)
		leaf.setRotateDirection(random.nextInt(2));
		//飘动的起始时间(交错感觉)
		leaf_float_cycle_time = leaf_float_cycle_time <=0 ? DEFAULT_LEAF_FLOAT_CYCLE_TIME : leaf_float_cycle_time;
		leaf.setStartTime(System.currentTimeMillis() + random.nextInt((int)leaf_float_cycle_time * 2));
		
		return leaf;
	}
	
	/**
	 * 生成叶子集合
	 * @return
	 */
	public List<Leaf> getLeafs(int leaf_number){
		List<Leaf>  leafs = new ArrayList<Leaf>();
		for(int i = 0; i < leaf_number; i++){
			leafs.add(getLeaf());
		}
		return leafs;
	}

	public long getLeaf_float_cycle_time() {
		return leaf_float_cycle_time;
	}

	public void setLeaf_float_cycle_time(long leaf_float_cycle_time) {
		this.leaf_float_cycle_time = leaf_float_cycle_time;
	}

	public long getLeaf_rotate_cycle_time() {
		return leaf_rotate_cycle_time;
	}

	public void setLeaf_rotate_cycle_time(long leaf_rotate_cycle_time) {
		this.leaf_rotate_cycle_time = leaf_rotate_cycle_time;
	}
	
	
	

	
	
}
