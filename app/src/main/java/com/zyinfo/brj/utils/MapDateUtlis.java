/**
 * 
 */
package com.zyinfo.brj.utils;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* Description:
*
*/
public class MapDateUtlis {
	private static int yuntuNum=20;
	private static int lDaNum=48;
	public static List<String> getLDUrlDate() {
		List<String> yunTU=new ArrayList<String>();
		String top1="http://pi.weather.com.cn/i/product/pic/l/sevp_aoc_rdcp_sldas_ebref_anwc_l88_pi_";
		Date date=new Date();
		DateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		String time=simpleDateFormat.format(date);
		String[] times=time.split("-");
		int d=Integer.parseInt(times[2]);
		int h=Integer.parseInt(times[3]);
		int m=Integer.parseInt(times[4]);
//		if (m>54) {
//			yunTU.add(top1+times[0]+times[1]+times[2]+times[3]+"48"+"00001.jpg");
//		}
//		if (m>48) {
//			yunTU.add(top1+times[0]+times[1]+times[2]+times[3]+"42"+"00001.jpg");
//		}
//		if (m>42) {
//			yunTU.add(top1+times[0]+times[1]+times[2]+times[3]+"36"+"00001.jpg");
//		}
//		if (m>36) {
//			yunTU.add(top1+times[0]+times[1]+times[2]+times[3]+"30"+"00001.jpg");
//		}
//		if (m>30) {
//			yunTU.add(top1+times[0]+times[1]+times[2]+times[3]+"24"+"00001.jpg");
//		}
//		if (m>24) {
//			yunTU.add(top1+times[0]+times[1]+times[2]+times[3]+"18"+"00001.jpg");
//		}
//		if (m>18) {
//			yunTU.add(top1+times[0]+times[1]+times[2]+times[3]+"12"+"00001.jpg");
//		}
//		if (m>12) {
//			yunTU.add(top1+times[0]+times[1]+times[2]+times[3]+"00"+"00001.jpg");
//		}

		while (yunTU.size()<lDaNum) {
			String htine;
			for (int j = h-1; j >0; j--) {
				if (j>17) {
					htine=String.valueOf(j-8);
				}else {
					if (j<8) {
						times[2]=String.valueOf(d-1);
						htine=String.valueOf(24-(8-j));
					}else {
						htine="0"+String.valueOf(j-8);
					}
					
				}
				yunTU.add(top1+times[0]+times[1]+times[2]+htine+"54"+"00001.png");
				yunTU.add(top1+times[0]+times[1]+times[2]+htine+"48"+"00001.png");
				yunTU.add(top1+times[0]+times[1]+times[2]+htine+"42"+"00001.png");
				yunTU.add(top1+times[0]+times[1]+times[2]+htine+"36"+"00001.png");
				yunTU.add(top1+times[0]+times[1]+times[2]+htine+"30"+"00001.png");
				yunTU.add(top1+times[0]+times[1]+times[2]+htine+"24"+"00001.png");
				yunTU.add(top1+times[0]+times[1]+times[2]+htine+"18"+"00001.png");
				yunTU.add(top1+times[0]+times[1]+times[2]+htine+"12"+"00001.png");
				yunTU.add(top1+times[0]+times[1]+times[2]+htine+"06"+"00001.png");
				yunTU.add(top1+times[0]+times[1]+times[2]+htine+"00"+"00001.png");
				//48 张跳出循环
				if (yunTU.size()>lDaNum) {
					break ;
				}
			}
			/*
			 * 不够48张继续获取
			 */	
			if (yunTU.size()<lDaNum) {
				times[2]=String.valueOf(d-1);//前一天的值
				while (yunTU.size()<lDaNum) {
					for (int i = 24; i >0; i--) {
						if (i>17) {
							htine=String.valueOf(i-8);
						}else {
							if (i<8) {
								times[2]=String.valueOf(d-2);
								htine=String.valueOf(24-(8-i));
							}else {
								htine="0"+String.valueOf(i-8);
							}
							
						}
						/*
						 * j小时时候的云图url
						 */
						yunTU.add(top1+times[0]+times[1]+times[2]+htine+"54"+"00001.png");
						yunTU.add(top1+times[0]+times[1]+times[2]+htine+"48"+"00001.png");
						yunTU.add(top1+times[0]+times[1]+times[2]+htine+"42"+"00001.png");
						yunTU.add(top1+times[0]+times[1]+times[2]+htine+"36"+"00001.png");
						yunTU.add(top1+times[0]+times[1]+times[2]+htine+"30"+"00001.png");
						yunTU.add(top1+times[0]+times[1]+times[2]+htine+"24"+"00001.png");
						yunTU.add(top1+times[0]+times[1]+times[2]+htine+"18"+"00001.png");
						yunTU.add(top1+times[0]+times[1]+times[2]+htine+"12"+"00001.png");
						yunTU.add(top1+times[0]+times[1]+times[2]+htine+"06"+"00001.png");
						yunTU.add(top1+times[0]+times[1]+times[2]+htine+"00"+"00001.png");
						if (yunTU.size()>lDaNum) {
							break ;
						}
					}
				}
			}
		}
			
		return yunTU;
	}
	public static List<String> getLDDate() {
		List<String> yunTU=new ArrayList<String>();
		Date date=new Date();
		DateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		String time=simpleDateFormat.format(date);
		String[] times=time.split("-");
		int d=Integer.parseInt(times[2]);
		int h=Integer.parseInt(times[3]);
		int m=Integer.parseInt(times[4]);
//		if (m>54) {
//			yunTU.add(top1+times[0]+times[1]+times[2]+times[3]+"48"+"00001.jpg");
//		}
//		if (m>48) {
//			yunTU.add(top1+times[0]+times[1]+times[2]+times[3]+"42"+"00001.jpg");
//		}
//		if (m>42) {
//			yunTU.add(top1+times[0]+times[1]+times[2]+times[3]+"36"+"00001.jpg");
//		}
//		if (m>36) {
//			yunTU.add(top1+times[0]+times[1]+times[2]+times[3]+"30"+"00001.jpg");
//		}
//		if (m>30) {
//			yunTU.add(top1+times[0]+times[1]+times[2]+times[3]+"24"+"00001.jpg");
//		}
//		if (m>24) {
//			yunTU.add(top1+times[0]+times[1]+times[2]+times[3]+"18"+"00001.jpg");
//		}
//		if (m>18) {
//			yunTU.add(top1+times[0]+times[1]+times[2]+times[3]+"12"+"00001.jpg");
//		}
//		if (m>12) {
//			yunTU.add(top1+times[0]+times[1]+times[2]+times[3]+"00"+"00001.jpg");
//		}

		while (yunTU.size()<lDaNum) {
			String htine;
			for (int j = h-1; j >0; j--) {
				if (j>9) {
					htine=String.valueOf(j);
				}else {
					htine="0"+String.valueOf(j);
				}
				yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"54");
				yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"48");
				yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"42");
				yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"36");
				yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"30");
				yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"24");
				yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"18");
				yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"12");
				yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"06");
				yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"00");
				//48 张跳出循环
				if (yunTU.size()>lDaNum) {
					break ;
				}
			}
			/*
			 * 不够48张继续获取
			 */	
			if (yunTU.size()<lDaNum) {
				times[2]=String.valueOf(d-1);//前一天的值
				while (yunTU.size()<lDaNum) {
					for (int i = 24; i >0; i--) {
						if (i>9) {
							htine=String.valueOf(i);
						}else {
							htine="0"+String.valueOf(i);
						}
						/*
						 * j小时时候的云图url
						 */
						yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"54");
						yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"48");
						yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"42");
						yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"36");
						yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"30");
						yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"24");
						yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"18");
						yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"12");
						yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"06");
						yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"00");
						if (yunTU.size()>lDaNum) {
							break ;
						}
					}
				}
			}
		}
			
		return yunTU;
	}
	/*
	 * 计算出云图urlList
	 */
	public static List<String> getYunTuUrlDate() {
		List<String> yunTU=new ArrayList<String>();
		String top="http://pi.weather.com.cn/i/product/pic/l/sevp_nsmc_wxcl_asc_e99_achn_lno_py_";
		Date date=new Date();
		DateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		String time=simpleDateFormat.format(date);
		String[] times=time.split("-");
		int d=Integer.parseInt(times[2]);
		int h=Integer.parseInt(times[3]);
		int m=Integer.parseInt(times[4]);
		
//		if (m>30) {
//			yunTU.add(top+times[0]+times[1]+times[2]+times[3]+"15"+"00001.jpg");
//		}
//		if (m>50) {
//			yunTU.add(top+times[0]+times[1]+times[2]+times[3]+"45"+"00000.jpg");
//		}
		while (yunTU.size()<yuntuNum) {
			String htine;
			for (int j = h-1; j >0; j--) {
				if (j>17) {
					htine=String.valueOf(j-8);
				}else {
					if (j<8) {
						times[2]=String.valueOf(d-1);
						htine=String.valueOf(24-(8-j));
					}else {
						htine="0"+String.valueOf(j-8);
					}
					
				}
				
				if (j==1||j==2||j==3) {
					 continue; 
				}
//					yunTU.add(top+times[0]+times[1]+times[2]+htine+"00"+".jpg");
					yunTU.add(top+times[0]+times[1]+times[2]+htine+"45"+"00000.jpg");
//					yunTU.add(top+times[0]+times[1]+times[2]+htine+"30"+".jpg");
					yunTU.add(top+times[0]+times[1]+times[2]+htine+"15"+"00000.jpg");
				//48 张跳出循环
				if (yunTU.size()>yuntuNum) {
					break ;
				}
			}
			/*
			 * 不够48张继续获取
			 */	
			if (yunTU.size()<yuntuNum) {
				times[2]=String.valueOf(d-1);//前一天的值
				while (yunTU.size()<yuntuNum) {
					for (int i = 24; i >0; i--) {
						if (i>17) {
							htine=String.valueOf(i-8);
						}else {
							if (i<8) {
								times[2]=String.valueOf(d-2);
								htine=String.valueOf(24-(8-i));
							}else {
								htine="0"+String.valueOf(i-8);
							}
							
						}
						/*
						 * j小时时候的云图url
						 */
//						yunTU.add(top+times[0]+times[1]+times[2]+htine+"00"+".jpg");
						yunTU.add(top+times[0]+times[1]+times[2]+htine+"45"+"00000.jpg");
//						yunTU.add(top+times[0]+times[1]+times[2]+htine+"30"+".jpg");
						yunTU.add(top+times[0]+times[1]+times[2]+htine+"15"+"00000.jpg");
						if (yunTU.size()>yuntuNum) {
							break ;
						}
					}
				}
			}
		}
			
		return yunTU;
	}
	/*
	 * 计算出云图urlList
	 */
	public static List<String> getYunTuDate() {
		List<String> yunTU=new ArrayList<String>();
		Date date=new Date();
		DateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		String time=simpleDateFormat.format(date);
		String[] times=time.split("-");
		int d=Integer.parseInt(times[2]);
		int h=Integer.parseInt(times[3]);
		int m=Integer.parseInt(times[4]);
		
//		if (m>30) {
//			yunTU.add(top+times[0]+times[1]+times[2]+times[3]+"15"+"00001.jpg");
//		}
//		if (m>50) {
//			yunTU.add(top+times[0]+times[1]+times[2]+times[3]+"45"+"00000.jpg");
//		}
		while (yunTU.size()<yuntuNum) {
			String htine;
			for (int j = h-1; j >0; j--) {
//				if (j>9) {
					htine=String.valueOf(j);
//				}else {
//					htine="0"+String.valueOf(j);
//					
//				}
				if (j==1||j==2||j==3) {
					 continue; 
				}
//				yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"00");
				yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"45");
//				yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"30");
				yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"15");
				//48 张跳出循环
				if (yunTU.size()>yuntuNum) {
					break ;
				}
			}
			/*
			 * 不够48张继续获取
			 */	
			if (yunTU.size()<yuntuNum) {
				times[2]=String.valueOf(d-1);//前一天的值
				while (yunTU.size()<yuntuNum) {
					for (int i = 24; i >0; i--) {
//						if (i>9) {
							htine=String.valueOf(i);
//						}else {
//							htine="0"+String.valueOf(i);
//						}
						/*
						 * j小时时候的云图url
						 */
//						yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"00");
						yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"45");
//						yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"30");
						yunTU.add(times[0]+"/"+times[1]+"/"+times[2]+" "+htine+":"+"15");
						if (yunTU.size()>yuntuNum) {
							break ;
						}
					}
				}
			}
		}
			
		return yunTU;
	}
	
}
