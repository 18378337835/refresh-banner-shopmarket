package com.example.administrator.school_design.diy_item;

/**
 * Created by Administrator on 2018/6/3.
 */

/**
* 封装的PopupWidow的工具类
 * @version 1.0
 * @des -调用很简单，直接new完传递参数，实现方法，通过builder调用方法，可以自己扩展
*/

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.school_design.R;

public abstract class PopUtils {
          private Context mContext;
            private int mlayoutResId;
           private int mHeight;
           private int mwidth;
          private ClickListener mCallback;

        /**
   * The constructor of MyPopupWindow
   *
     * @param context     Interface to global information about an application environment
     * @param layoutResId The resource id of the layout embedded inside the PopupWindow
     * @param width       the width of the PopupWindow
    * @param height      the height of the PopupWindow
     * @param view        a parent view to get the android.view.View.getWindowToken() token from
     * @param gravity     the gravity which controls the placement of the popup window
     * @param x           the PopupWindow's x location offset
    * @param y           the PopupWindow's y location offset
     */
            public PopUtils(Context context, int layoutResId, int width, int height, View view, int gravity, int x, int y, ClickListener callback) {
               this.mContext = context;
              this.mlayoutResId = layoutResId;
                this.mwidth = width;
               this.mHeight = height;

           setCallBack(callback);
         //       this.mCallback =callback;
               PopBuilder builder = PopBuilder.createPopupWindow(context, layoutResId, width, height, view, gravity, x, y, mCallback);
           }

           public static class PopBuilder {
           private ClickListener mCallback;
             private static PopupWindow window;
              private SparseArray<View> mViews;
               private View mItem;

                private PopBuilder(Context context, View view, ClickListener callback) {
                      this.mViews = new SparseArray<>();
                     this.mItem = view;
                      this.mCallback = callback;
                 }

               public static PopBuilder createPopupWindow(final Context context, int layoutResId, int width, int height, View parent, int gravity, int x, int y, ClickListener callback) {
                        // 利用layoutInflater获得View
                        LayoutInflater inflater = (LayoutInflater) ((Activity) context)
                               .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                      View view = inflater.inflate(layoutResId, null);

                       PopBuilder builder = new PopBuilder(context, view, callback);

                        window = new PopupWindow(view, width, height);

                        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
                       window.setFocusable(true);
                   window.setTouchable(true);

                        // 设置触摸外面时消失
                      window.setOutsideTouchable(true);
                     window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                   window.setAnimationStyle(R.style.AnimationPreview);
                     // 监听PopupWindow关闭，如果为关闭状态则设置为空
                       window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                                     window = null;

                                    // 主界面完全显示
                                      WindowManager.LayoutParams params = ((Activity) context).getWindow().getAttributes();
                                      params.alpha = 1.0f;
                                      ((Activity) context).getWindow().setAttributes(params);
                                   }
            });

                        // 实例化一个ColorDrawable颜色为透明，不设置为半透明是因为带圆角
                        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(android.R.color.transparent));
                     window.setBackgroundDrawable(dw);

                          window.showAtLocation(parent, gravity, x, y);


                     // 主界面变暗
                       WindowManager.LayoutParams params = ((Activity) context).getWindow().getAttributes();
                   params.alpha = 0.8f;
                       ((Activity) context).getWindow().setAttributes(params);
                       //点击事件回调
                      if (window != null) {
                              callback.setUplistener(builder);
                          }

                        return builder;

               }
      /**
        * 得到视图
         *
       * @param id 控件资源id
       * @param <T> 类型
         *
        * @return T
          */
            public <T extends View> T getView(int id) {
                       T t = (T) mViews.get(id);
                       if (t == null) {
                             t = (T) mItem.findViewById(id);
                              mViews.put(id, t);
                           }
                      return t;
                  }
       /**
        * 使窗口消失
         * @return
       */
              public PopBuilder dismiss() {
                      if (window != null) {
                          window.dismiss();
                         }
                        return this;
                 }
        /**
       * 设置是否可见
       *
       * @param id         控件id
         * @param visibility 是否可见
        *
       * @return PopBuilder
        */
          public PopBuilder setVisibility(int id, int visibility) {
                       getView(id).setVisibility(visibility);
                      return this;
              }

                /**
         * 设置图片资源
        *
        * @param id          控件id
       * @param drawableRes drawable资源id
        *
       * @return PopBuilder
         */
              public PopBuilder setImageResource(int id, int drawableRes) {
                      View view = getView(id);
                       if (view instanceof ImageView) {
                              ((ImageView) view).setImageResource(drawableRes);
                          } else {
                               view.setBackgroundResource(drawableRes);
                           }
                       return this;
                   }

           /**
       * 设置文本
        *
        * @param id   控件id
         * @param text 文本内容
       *
        * @return PopBuilder
        */
           public PopBuilder setText(int id, CharSequence text) {
                       View view = getView(id);
                       if (view instanceof TextView) {
                               ((TextView) view).setText(text);
                           }
                       return this;
                }
   }
  /**
     * 用于回调的接口
   */
     public interface ClickListener {
       void setUplistener(PopBuilder builder);
   }

   /**
    * 设置回调对象
    * @param callBack 回调对象
  */
            private void setCallBack(ClickListener callBack) {
              this.mCallback = callBack;
          }
}

