package com.techies.plugin.utils

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor

public class InjectManager {

    static byte[] injectClasses(byte[] srcByteCode) {
        byte[] classBytesCode = null
        try {
            classBytesCode = inject(srcByteCode)
            return classBytesCode
        } catch (Exception e) {
            e.printStackTrace()
        }
        if (classBytesCode == null) {
            classBytesCode = srcByteCode
        }
        return classBytesCode
    }


    private static byte[] inject(byte[] srcClass) throws IOException {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS)
        ClassVisitor methodFilterCV = new InjectClassVisitor(classWriter)
        ClassReader cr = new ClassReader(srcClass)
        cr.accept(methodFilterCV, ClassReader.SKIP_DEBUG)
        return classWriter.toByteArray()
    }

    static boolean instanceOfFragment(String superName) {
        return superName.equals('android/app/Fragment') || superName.equals('android/support/v4/app/Fragment')
    }

    static boolean instanceOfActivity(String superName) {
        return false
    }

    static void visitMethodWithLoadedParams(String className, MethodVisitor methodVisitor, int opcode, String owner, String methodName, String methodDesc, int start, int count, List<Integer> paramOpcodes) {

        String[] names = className.split("/")
        String[] name = names[names.length - 1].split('\\$')
        methodVisitor.visitLdcInsn(name[0]) //设置第一个参数, 不需要注入 手动设置

        for (int i = start; i < start + count; i++) {
            //设置的是原方法里面的参数的位置以及opcode, 对应到要注入方法的参数. 就是怎么将原方法的参数传到注入方法里面.详见:bytecode
            methodVisitor.visitVarInsn(paramOpcodes[i - start], i)
        }
        //注入新的方法
        methodVisitor.visitMethodInsn(opcode, owner, methodName, methodDesc, false)
    }

}
