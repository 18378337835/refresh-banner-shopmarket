package com.example.administrator.school_design.view.my_scroll;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义的布局，用来管理三个子控件，其中一个是下拉头，一个是包含内容的pullableView（可以是实现Pullable接口的的任何View），
 * 还有一个上拉头，更多详解见博客http://blog.csdn.net/zhongkejingwang/article/details/38868463
 * 
 * @author 陈靖
 */
public class PullToRefresh0 extends RelativeLayout {

	public static final int INIT = 0;
	// 正在刷新
	public static final int REFRESHING = 2;

	// 正在加载
	public static final int LOADING = 4;
	// 当前状态
	private int state = INIT;

	// 按下Y坐标，上一个事件点Y坐标
	private float downY, lastY;

	// 下拉的距离。注意：pullDownY和pullUpY不可能同时不为0
	public float pullDownY = 0;
	// 上拉的距离
	private float pullUpY = 0;

	// 释放刷新的距离
	private float refreshDist = 200;
	// 释放加载的距离
	private float loadmoreDist = 200;

	private MyTimer timer;
	// 回滚速度
	public float MOVE_SPEED = 8;
	// 第一次执行布局
	private boolean isLayout = false;
	// 在刷新过程中滑动操作
	private boolean isTouch = false;
	// 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
	private float radio = 2;

	private View refreshView;

	private View loadmoreView;

	private View pullableView;
	// 过滤多点触碰
	private int mEvents;
	// 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
	private boolean canPullDown = true;
	private boolean canPullUp = true;

	private Context mContext;

	/**
	 * 执行自动回滚的handler
	 */
	Handler updateHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// 回弹速度随下拉距离moveDeltaY增大而增大
			MOVE_SPEED = (float) (1 + 5 * Math.tan(Math.PI / 2
					/ getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
			if (!isTouch) {
				// 正在刷新，且没有往上推的话则悬停，显示"正在刷新..."
				if (state == REFRESHING && pullDownY <= refreshDist) {
					pullDownY = refreshDist;
					timer.cancel();
				} else if (state == LOADING && -pullUpY <= loadmoreDist) {
					pullUpY = -loadmoreDist;
					timer.cancel();
				}

			}
			if (pullDownY > 0)
				pullDownY -= MOVE_SPEED;
			else if (pullUpY < 0)
				pullUpY += MOVE_SPEED;
			
			if (pullDownY < 0) {
				// 已完成回弹
				pullDownY = 0;
				timer.cancel();
				requestLayout();
			}
			if (pullUpY > 0) {
				// 已完成回弹
				pullUpY = 0;
				timer.cancel();
				requestLayout();
			}

			requestLayout();
			// 没有拖拉或者回弹完成
			if (pullDownY + Math.abs(pullUpY) == 0)
				timer.cancel();
		}

	};
	public PullToRefresh0(Context context) {
		super(context);
		initView(context);
	}

	public PullToRefresh0(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public PullToRefresh0(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context) {
		mContext = context;
		timer = new MyTimer(updateHandler);

	}

	private void hide() {
		timer.schedule(1);
	}


	private void releasePull() {
		canPullDown = true;
		canPullUp = true;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev){
		switch (ev.getActionMasked()){
		case MotionEvent.ACTION_DOWN:
			downY = ev.getY();
			lastY = downY;
			timer.cancel();
			mEvents = 0;
			releasePull();
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_POINTER_UP:
			// 过滤多点触碰
			mEvents = -1;
			break;
		case MotionEvent.ACTION_MOVE:
			if (mEvents == 0) {
				if (pullDownY > 0 || (((Pullable) pullableView).canPullDown()
								&& canPullDown && state != LOADING)) {
					// 可以下拉，正在加载时不能下拉
					// 对实际滑动距离做缩小，造成用力拉的感觉
					pullDownY = pullDownY + (ev.getY() - lastY) / radio;
					if (pullDownY < 0) {
						pullDownY = 0;
						canPullDown = false;
						canPullUp = true;
					}
					if (pullDownY > getMeasuredHeight()){
						pullDownY = getMeasuredHeight();
					}
					if (state == REFRESHING) {
						// 正在刷新的时候触摸移动
						isTouch = true;
					}
				} else if (pullUpY < 0
						|| (((Pullable) pullableView).canPullUp() && canPullUp && state != REFRESHING)) {
					// 可以上拉，正在刷新时不能上拉
					pullUpY = pullUpY + (ev.getY() - lastY) / radio;
					
					if (pullUpY > 0) {
						pullUpY = 0;
						canPullDown = true;
						canPullUp = false;
					}
					if (pullUpY < -getMeasuredHeight()){
						pullUpY = -getMeasuredHeight();
					}
					if (state == LOADING) {
						// 正在加载的时候触摸移动
						isTouch = true;
					}
				} else{
					releasePull();
				}
			} else{
				mEvents = 0;
			}
			
			lastY = ev.getY();
			// 根据下拉距离改变比例
			radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight()
					* (pullDownY + Math.abs(pullUpY))));
			if (pullDownY > 0 || pullUpY < 0){
				requestLayout();
			}
			if (pullDownY > 0) {


			} else if (pullUpY < 0) {
				// 下面是判断上拉加载的，同上，注意pullUpY是负值

				// 上拉操作

			}
			// 因为刷新和加载操作不能同时进行，所以pullDownY和pullUpY不会同时不为0，因此这里用(pullDownY +
			// Math.abs(pullUpY))就可以不对当前状态作区分了
			if ((pullDownY + Math.abs(pullUpY)) > 8) {
				// 防止下拉过程中误触发长按事件和点击事件
				ev.setAction(MotionEvent.ACTION_CANCEL);
			}
			break;
		case MotionEvent.ACTION_UP:

			hide();
		default:
			break;
		}
		// 事件分发交给父类
		super.dispatchTouchEvent(ev);
		return true;
	}


	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		Log.d("Test", "Test");
		if (!isLayout) {
			// 这里是第一次进来的时候做一些初始化
			refreshView = getChildAt(0);
			pullableView = getChildAt(1);
			loadmoreView = getChildAt(2);
			isLayout = true;

			refreshDist = ((ViewGroup) refreshView).getChildAt(0).getMeasuredHeight();
			loadmoreDist = ((ViewGroup) loadmoreView).getChildAt(0).getMeasuredHeight();
		}
		// 改变子控件的布局，这里直接用(pullDownY + pullUpY)作为偏移量，这样就可以不对当前状态作区分
		refreshView.layout(0, (int) (pullDownY + pullUpY) - refreshView.getMeasuredHeight(),
				refreshView.getMeasuredWidth(), (int) (pullDownY + pullUpY));
		pullableView.layout(0, (int) (pullDownY + pullUpY), pullableView.getMeasuredWidth(), 
				(int) (pullDownY + pullUpY) + pullableView.getMeasuredHeight());
		loadmoreView.layout(0, (int) (pullDownY + pullUpY) + pullableView.getMeasuredHeight(),
				loadmoreView.getMeasuredWidth(), (int) (pullDownY + pullUpY) 
				+ pullableView.getMeasuredHeight() + loadmoreView.getMeasuredHeight());
	}

	class MyTimer {
		private Handler handler;
		private Timer timer;
		private MyTask mTask;

		public MyTimer(Handler handler) {
			this.handler = handler;
			timer = new Timer();
		}

		public void schedule(long period) {
			if (mTask != null) {
				mTask.cancel();
				mTask = null;
			}
			mTask = new MyTask(handler);
			timer.schedule(mTask, 0, period);
		}

		public void cancel() {
			if (mTask != null) {
				mTask.cancel();
				mTask = null;
			}
		}

		class MyTask extends TimerTask {
			private Handler handler;

			public MyTask(Handler handler) {
				this.handler = handler;
			}

			@Override
			public void run() {
				handler.obtainMessage().sendToTarget();
			}
		}
	}



}
