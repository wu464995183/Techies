package ime.demo11;

import android.view.View;
import android.widget.Toast;

/**
 * 埋点注入代码
 * 统一管理所有view的点击事件, 涉及到怎么知道你点击的是哪个view, 经查询大部分人用的是扫描ViewTree通过view的层级结构来
 * 做view的唯一标识, 但是这样有一个弊端: 让你的view的位置变化了 势必会影响viewtree的结构从而影响了view的标识, 那么就得
 * 在更改界面的同时去更改埋点的配置文件,由于我们的app UI 改动频繁加上并不是每个人都记得遵循这个规定,所以这个方法不太适合
 * <p>
 * 经过思考结合我们自己的app, 决定使用view的id和当前界面的名字来做标识. 这么做也是有很多弊端 比如说这个id必须唯一(可以通过规定每个开发人员
 * 都已自己的模块来命名来避免).
 * <p>
 * 综上每中方法都有自己劣势,找到合适自己的才重要 ☺ .
 */
public class PluginAgent {
    public static void onClick(View view) {
        String idName = view.getResources().getResourceEntryName(view.getId());
        if (idName.equals("clickB")) {
            Toast.makeText(view.getContext(), "埋点B", Toast.LENGTH_SHORT).show();
        } else if (idName.equals("clickT")) {
            Toast.makeText(view.getContext(), "埋点T", Toast.LENGTH_SHORT).show();
        }
    }

    public static void onClick(String className, View view) {
        String[] names = className.split("/");
        Toast.makeText(view.getContext(), className, Toast.LENGTH_SHORT).show();
    }

    public static void onFragmentHiddenChanged(String fragment, boolean hidden) {
    }
    public static void onFragmentResume(String fragment) {
    }
}
