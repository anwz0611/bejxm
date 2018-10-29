package com.zyinfo.brj.utils;



import com.zyinfo.brj.bean.addressBookInfoBeans;

import java.util.Comparator;


/**
 * 
 *
 *
 */
public class PinyinComparator implements Comparator<addressBookInfoBeans.WaterBookDetailListBean> {
	@Override
	public int compare(addressBookInfoBeans.WaterBookDetailListBean waterBookDetailListBean, addressBookInfoBeans.WaterBookDetailListBean t1) {
		return 0;
	}

//	public int compare(addressBookInfoBeans.WaterBookDetailListBean o1, addressBookInfoBeans.WaterBookDetailListBean o2) {
////		if (o1.getSortLetters().equals("@")
////				|| o2.getSortLetters().equals("#")) {
////			return -1;
////		} else if (o1.getSortLetters().equals("#")
////				|| o2.getSortLetters().equals("@")) {
////			return 1;
////		} else {
////			return o1.getSortLetters().compareTo(o2.getSortLetters());
////		}
//	}

}
