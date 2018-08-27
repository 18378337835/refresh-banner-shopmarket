package com.example.administrator.school_design.view.my_scroll;

public interface Pullable {
	/**
	 * 判断是否可以下拉，如果不需要下拉功能可以直接return false
	 * 
	 * @return
	 */
	boolean canPullDown();

	/**
	 * 判断是否可以上拉，如果不需要上拉功能可以直接return false
	 * 
	 * @return
	 */
	boolean canPullUp();
}
