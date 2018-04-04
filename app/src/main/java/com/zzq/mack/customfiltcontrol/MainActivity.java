package com.zzq.mack.customfiltcontrol;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zzq.mack.customfiltcontrol.model.FiltModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:xqt
 * Email:zzq1573@gmail.com
 * Date:2018/3/31 0031 11:24
 * Description:
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //构建数据  大多数清空 筛选条件都是在本地写死的，只是需要与服务器对应，所以我们需要用id
        String[] titles = getArray(R.array.fit_titles);
        String[][] tabs = new String[][]{
                getArray(R.array.fit_sex_tabs)
                ,getArray(R.array.fit_age_tabs)
                ,getArray(R.array.fit_money_tabs)
        };
        final List<FiltModel> listData = new ArrayList<>();
        for (int i = 0; i < titles.length; i++){
            FiltModel model = new FiltModel();
            model.setTypeName(titles[i]);
            model.setType(i);//筛选类型可以自己定义
            List<FiltModel.TableMode> tabsList = new ArrayList<>();
            for(int j = 0; j < tabs[i].length; j++){
                FiltModel.TableMode mod = new FiltModel.TableMode();
                mod.id = j;//id可以自己定义 看服务器需要什么
                mod.name = tabs[i][j];
                tabsList.add(mod);
            }
            model.setTabs(tabsList);
            listData.add(model);
            //默认选中第一项
            model.setTab(model.getTabs().get(0));
        }
        //数据组装稍微有点麻烦哈，如果您更好的方式请联系我，我学习学习。
        //直接看结果吧
        findViewById(R.id.tvTitleBarRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FiltPopuWindow.Builder(MainActivity.this).setColumnCount(3)//设置列数，测试2.3.4.5没问题
                        .setDataSource(listData)
                        .setColorBg(R.color.color_f8f8f8)
                        //所有的属性设置必须在build之前，不然无效
                        .build()
                        .createPop()
                        .showAsDropDown(findViewById(R.id.titleBar));
                //我这里头方便这样写了，pop对象可以拿出来存放，不需要每次都去创建
            }
        });


        //获取到您选择的tab

//        for (FiltModel model : listData){
//            switch (model.getType()){
//                case 0:
//                    Log.e("TAG","您选择了id=" + model.getTab().id + ",name="+model.getTab().name);
//                    break;
//                case 1:
//                    Log.e("TAG","您选择了id=" + model.getTab().id + ",name="+model.getTab().name);
//                    break;
//                case 2:
//                    Log.e("TAG","您选择了id=" + model.getTab().id + ",name="+model.getTab().name);
//                    break;
//            }
//        }

    }

    private String[] getArray(int id){
        return getResources().getStringArray(id);
    }
}
