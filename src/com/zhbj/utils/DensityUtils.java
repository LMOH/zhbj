package com.zhbj.utils;

import android.content.Context;

/**设备密度运算工具类
 * @author PC-LMOH
 *
 */
public class DensityUtils {

	/**dp转px
	 * @param context
	 * @param dip
	 * @return
	 */
	public static int dipTopx(Context context,int dip){
		float density = context.getResources().getDisplayMetrics().density;
		int px = (int) (dip*density+0.5);				//+0.5解决四舍五入的问题
		return px;
	}
	/**px转dp
	 * @param context
	 * @param px
	 * @return
	 */
	public static float pxTodip(Context context,int px){
		float density = context.getResources().getDisplayMetrics().density;
		float dp = px/density;
		return dp;
	}
}
