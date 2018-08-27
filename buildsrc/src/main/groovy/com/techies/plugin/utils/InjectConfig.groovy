package com.techies.plugin.utils

import jdk.internal.org.objectweb.asm.Opcodes

/**
 * Created by zhangdan on 17/3/3.
 */

public class InjectConfig {

    /**
     * 必须是全路径形式，不是点分形式的包名
     */
    public static String AgentClassName = ''

    /**
     * interface中的方法
     */
    public static HashMap<String, MethodCell> InterfaceMethods = new HashMap<>()
    static {
        InterfaceMethods.put('onClick(Landroid/view/View;)V', new MethodCell(
                'onClick',
                '(Landroid/view/View;)V',
                'android/view/View$OnClickListener',
                'onClick',
                '(Ljava/lang/String;Landroid/view/View;)V', //两个参数
                1, //第二个参数
                1, //共需要多少个注入参数
                [Opcodes.ALOAD]))
//        InterfaceMethods.put('onItemClick(Landroid/widget/AdapterView;Landroid/view/View;IJ)V', new MethodCell(
//                'onItemClick',
//                '(Landroid/widget/AdapterView;Landroid/view/View;IJ)V',
//                'android/widget/AdapterView$OnItemClickListener',
//                'onItemClick',
//                '(Ljava/lang/String;Landroid/widget/AdapterView;Landroid/view/View;IJ)V',
//                1, 4,
//                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD, Opcodes.LLOAD]))
    }

    /**
     * Fragment中的方法
     */
    public static HashMap<String, MethodCell> FragmentActivityMethods = new HashMap<>()
    static {
//        FragmentActivityMethods.put('onResume()V', new MethodCell(
//                'onResume',
//                '()V',
//                '',// parent省略，均为 android/app/Fragment 或 android/support/v4/app/Fragment
//                'onFragmentResume',
//                '(Ljava/lang/String;)V',
//                1, 0,
//                []))
//        FragmentActivityMethods.put('onPause()V', new MethodCell(
//                'onPause',
//                '()V',
//                '',
//                'onFragmentPause',
//                '(Ljava/lang/Object;)V',
//                0, 1,
//                [Opcodes.ALOAD]))
//        FragmentActivityMethods.put('setUserVisibleHint(Z)V', new MethodCell(
//                'setUserVisibleHint',
//                '(Z)V',
//                '',// parent省略，均为 android/app/Fragment 或 android/support/v4/app/Fragment
//                'setFragmentUserVisibleHint',
//                '(Ljava/lang/Object;Z)V',
//                0, 2,
//                [Opcodes.ALOAD, Opcodes.ILOAD]))
//        FragmentActivityMethods.put('onHiddenChanged(Z)V', new MethodCell(
//                'onHiddenChanged',
//                '(Z)V',
//                '',
//                'onFragmentHiddenChanged',
//                '(Ljava/lang/String;Z)V',
//                1, 1,
//                [Opcodes.ILOAD]))
    }
}
