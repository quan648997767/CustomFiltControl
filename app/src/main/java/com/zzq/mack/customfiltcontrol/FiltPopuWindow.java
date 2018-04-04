package com.zzq.mack.customfiltcontrol;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zzq.mack.customfiltcontrol.model.FiltModel;

import java.util.List;

/**
 * Author:xqt
 * Email:zzq1573@gmail.com
 * Date:2018/3/31 0031 11:24
 * Description:筛选弹框  版权所有转载请注明出处
 */
public class FiltPopuWindow extends PopupWindow{
    public FiltPopuWindow(Context context,View view){
        //这里可以修改popupwindow的宽高
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(view);
        initViews();
    }
    private void initViews() {
        setAnimationStyle(R.style.popwin_anim_style);
        //setBackgroundDrawable(new ColorDrawable(0x00000000));
        setFocusable(true);
        setOutsideTouchable(true);
    }


    public static class Builder {
        private Context context;
        private List<FiltModel> listData;
        private int columnCount;
        private GridLayout rootGridLayout;
        private LinearLayout contextll;
        //背景颜色
        private int colorBg = Color.parseColor("#F8F8F8");
        private int titleTextSize = 14;//SP
        private int tabTextSize = 14;//SP
        private int titleTextColor = Color.parseColor("#333333");//标题字体颜色
        private int tabTextColor = R.color.fit_item_textcolor;//选项字体颜色
        private int tabBgDrawable = R.drawable.item_lable_bg_shape;//选项背景颜色
        //当前加载的行数
        private int row = -1;
        private FiltPopuWindow mFiltPopuWindow;

        public Builder(Context context) {
            this.context = context;
        }
        /**
         * 设置数据源
         * @return
         */
        public Builder setDataSource(List<FiltModel> listData) {
            this.listData = listData;
            return this;
        }

        public Builder setColumnCount(int columnCount){
            this.columnCount = columnCount;
            return this;
        }

        public Builder setColorBg(int color){
            colorBg = context.getResources().getColor(color);
            return this;
        }

        public Builder setTitleTextSize(int titleTextSize) {
            this.titleTextSize = titleTextSize;
            return this;
        }

        public Builder setTabTextSize(int tabTextSize) {
            this.tabTextSize = tabTextSize;
            return this;
        }

        public Builder setTitleTextColor(int titleTextColor) {
            this.titleTextColor = titleTextColor;
            return this;
        }

        public Builder setTabTextColor(int tabTextColor) {
            this.tabTextColor = tabTextColor;
            return this;
        }

        public Builder setTabBgDrawable(int tabBgDrawable) {
            this.tabBgDrawable = tabBgDrawable;
            return this;
        }

        public Builder build(){
            newItemLayout(getRowCount(),columnCount);
            for (int i = 0; i < listData.size(); i++){
                ++row;
                TextView view = new TextView(context);
                view.setText(listData.get(i).getTypeName());
                view.setTextColor(titleTextColor);
                view.setTextSize(titleTextSize);
                //配置列 第一个参数是起始列标 第二个参数是占几列 title（筛选类型）应该占满整行，so -> 总列数
                GridLayout.Spec columnSpec = GridLayout.spec(0,columnCount);
                //配置行 第一个参数是起始行标  起始行+起始列就是一个确定的位置
                GridLayout.Spec rowSpec = GridLayout.spec(row);
                //将Spec传入GridLayout.LayoutParams并设置宽高为0或者WRAP_CONTENT，必须设置宽高，否则视图异常
                GridLayout.LayoutParams lp = new GridLayout.LayoutParams(rowSpec, columnSpec);
                lp.width = GridLayout.LayoutParams.WRAP_CONTENT;
                lp.height = GridLayout.LayoutParams.WRAP_CONTENT;
                lp.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                lp.bottomMargin = context.getResources().getDimensionPixelSize(R.dimen.dp_8);
                rootGridLayout.addView(view,lp);
                //添加选项
                addTabs(listData.get(i),i);
            }

            return this;
        }

        private void newItemLayout(int rowCount,int columnCount){
            contextll = new LinearLayout(context);
            contextll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            contextll.setBackgroundColor(context.getResources().getColor(R.color.color_33000000));
            contextll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mFiltPopuWindow != null){
                        mFiltPopuWindow.dismiss();
                        //点击外部消失
                    }
                }
            });
            rootGridLayout = new GridLayout(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rootGridLayout.setOrientation(GridLayout.HORIZONTAL);
            rootGridLayout.setRowCount(rowCount);
            rootGridLayout.setColumnCount(columnCount);
            rootGridLayout.setBackgroundColor(colorBg);
            rootGridLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            int pandd = context.getResources().getDimensionPixelSize(R.dimen.dp_10);
            lp.weight = 1;
            rootGridLayout.setPadding(pandd,pandd,pandd,pandd);
            contextll.addView(rootGridLayout,lp);
        }

        /**
         * 添加选项
         * @param model
         */
        private void addTabs(final FiltModel model, final int titleIndex){
            List<FiltModel.TableMode> tabs = model.getTabs();
            for (int i = 0; i < tabs.size(); i++){
                if (i % columnCount == 0){
                    row ++;
                }
                final FiltModel.TableMode tab = tabs.get(i);
                final TextView lable = new TextView(context);
                lable.setTextColor(context.getResources().getColorStateList(tabTextColor));
                lable.setBackgroundDrawable(context.getResources().getDrawable(tabBgDrawable));
                lable.setSingleLine(true);
                lable.setGravity(Gravity.CENTER);
                lable.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                //这里可以自行修改tab框框的大小
                int panddT = context.getResources().getDimensionPixelSize(R.dimen.dp_2);
                int panddL = context.getResources().getDimensionPixelSize(R.dimen.dp_8);
                lable.setPadding(panddL,panddT,panddL,panddT);
                lable.setTextSize(tabTextSize);
                rootGridLayout.addView(lable,getItemLayoutParams(i,row));
                lable.setText(tab.name);
                if (tabs.get(i) == model.getTab()){
                    lable.setSelected(true);
                }
                lable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //lable.setSelected(true);
                        if (tab != model.getTab()){
                            //清空上次选中
                            rootGridLayout.getChildAt(getIndex(model,titleIndex)).setSelected(false);
                            //设置当前选中
                            model.setTab(tab);
                            lable.setSelected(true);
                        }
                    }
                });
            }
        }

        private GridLayout.LayoutParams getItemLayoutParams(int i,int row){
            //使用Spec定义子控件的位置和比重
            GridLayout.Spec rowSpec = GridLayout.spec(row,1f);
            GridLayout.Spec columnSpec = GridLayout.spec(i%columnCount,1f);
            //将Spec传入GridLayout.LayoutParams并设置宽高为0，必须设置宽高，否则视图异常
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams(rowSpec, columnSpec);
            lp.width = 0;
            lp.height = GridLayout.LayoutParams.WRAP_CONTENT;
            lp.bottomMargin = context.getResources().getDimensionPixelSize(R.dimen.dp_8);
            if(i % columnCount == 0) {//最左边
                lp.leftMargin = context.getResources().getDimensionPixelSize(R.dimen.dp_10);
                lp.rightMargin = context.getResources().getDimensionPixelSize(R.dimen.dp_20);
            }else if((i + 1) % columnCount == 0){//最右边
                lp.rightMargin = context.getResources().getDimensionPixelSize(R.dimen.dp_10);
            }else {//中间
                lp.rightMargin = context.getResources().getDimensionPixelSize(R.dimen.dp_20);
            }
            return lp;
        }

        /**
         * 获取当前选中tab的 在整个GridLayout的索引
         * @return
         */
        private int getIndex(FiltModel model,int titleIndex){
            int index = 0;
            for (int i = 0; i < titleIndex; i++){
                //计算当前类型之前的元素所占的个数 title算一个
                index += listData.get(i).getTabs().size() + 1;
            }
            //加上当前 title下的索引
            FiltModel.TableMode tableModel = model.getTab();
            index += model.getTabs().indexOf(tableModel) + 1;
            return index;
        }

        /**
         * 计算行数
         * @return
         */
        private int getRowCount(){
            int row = 0;
            for (FiltModel model : listData){
                //计算当前类型之前的元素所占的个数 title算一个
                row ++;
                int size = model.getTabs().size();
                row += (size / columnCount) + (size % columnCount > 0 ? 1 : 0) ;
            }
            return row;
        }
        public FiltPopuWindow createPop(){
            if (listData == null || listData.size() == 0){
                try {
                    throw new Exception("没有筛选条件");
                } catch (Exception e) {
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                return null;
            }
            mFiltPopuWindow = new FiltPopuWindow(context,contextll);
            return mFiltPopuWindow;
        }

    }
}
