package com.techies.plugin

import com.android.annotations.NonNull
import com.android.annotations.Nullable
import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.techies.plugin.utils.Cell
import com.techies.plugin.utils.InjectConfig
import com.techies.plugin.utils.InjectManager
import com.techies.plugin.utils.MethodCell
import groovy.io.FileType
import groovy.json.JsonSlurper
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.gradle.api.Project

public class PointTransform extends Transform {
    private HashSet<String> mines = [] //需要埋点的文件的数据
    private Project project

    public PointTransform(Project project) {
        this.project = project
    }

    @Override
    String getName() {
        return PointPlugin.NAME
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<QualifiedContent.Scope> getScopes() {
        return TransformManager.PROJECT_ONLY
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(
            @NonNull Context context,
            @NonNull Collection<TransformInput> inputs,
            @NonNull Collection<TransformInput> referencedInputs,
            @Nullable TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {

        initInjectMethod()

        InjectConfig.AgentClassName = project.techiesConfig.agentName
        HashSet<String> targetPaths = project.techiesConfig.targetPaths
        HashSet<String> targetClass = project.techiesConfig.targetClass
        if (targetPaths != null) {
            this.mines.addAll(targetPaths)
        }

        if (targetClass != null) {
            this.mines.addAll(targetClass)
        }

        handleInputsFile(inputs, outputProvider, context)
    }


    private ArrayList<TransformInput> handleInputsFile(Collection<TransformInput> inputs, outputProvider, context) {
        inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput directoryInput ->
                //dest == Users/wudi/Desktop/Demo11/app/build/intermediates/transforms/Techies/release/0
                File dest = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY);
                //Users/wudi/Desktop/Demo11/app/build/intermediates/javac/release/compileReleaseJavaWithJavac/classes
                File dir = directoryInput.file
                if (dir) {
                    HashMap<String, File> newFileMap = new HashMap<>()
                    dir.traverse(type: FileType.FILES, nameFilter: ~/.*\.class/) {
                        File classFile ->
                            //dir 源文件目录   classFile 源文件File
                            File newFile = buryMine(dir, classFile, context.getTemporaryDir())
                            if (newFile != null) {
                                //将更改后的文件放到map中
                                newFileMap.put(classFile.absolutePath.replace(dir.absolutePath, ""), newFile)
                            }
                    }
                    //将源文件/javac/release/compileReleaseJavaWithJavac/classes复制到/intermediates/transforms/Techies/release/0
                    FileUtils.copyDirectory(directoryInput.file, dest)
                    newFileMap.entrySet().each {
                        Map.Entry<String, File> en ->
                            File target = new File(dest.absolutePath + en.getKey())
                            if (target.exists()) {
                                target.delete()
                            }
                            FileUtils.copyFile(en.getValue(), target) //将修改后的文件放到/intermediates/transforms/Techies/release/0
                            en.getValue().delete()
                    }
                } else {
                    println "请注意出现严重错误,马上要爆炸...5...4...3...2.."
                }
            }
        }
    }

    /**
     *
     * @param className 相对路径的类名 如com.ime.MainActivity
     * @return
     */
    private boolean needModifyClass(String className) {
        if (mines != null) {
            Iterator<String> iterator = mines.iterator()
            while (iterator.hasNext()) {
                String mine = iterator.next()
                if (className.contains(mine)) {
                    return (!className.contains("R\$") && !className.endsWith("R") && !className.endsWith("BuildConfig"))
                }
            }
        }
        return false
    }

    private String pathToClassname(String entryName) {
        entryName.replace(File.separator, ".").replace(".class", "")
    }

    //dir 源文件目录   classFile 源文件File
    private File buryMine(File dir, File classFile, File tempDir) {
        File modified
        try {
            String className = pathToClassname(classFile.absolutePath.replace(dir.absolutePath + File.separator, ""))
            byte[] sourceClassBytes = IOUtils.toByteArray(new FileInputStream(classFile))
            if (needModifyClass(className)) {
                byte[] modifiedClassBytes = InjectManager.injectClasses(sourceClassBytes)
                if (modifiedClassBytes) {
                    modified = new File(tempDir, className.replace('.', '') + '.class')
                    if (modified.exists()) {
                        modified.delete()
                    }
                    modified.createNewFile()
                    new FileOutputStream(modified).write(modifiedClassBytes)
                }
            }
        } catch (Exception e) {
        }
        return modified

    }

    private void initInjectMethod() {
        initInjectIFMethod()
        initInjectFAMethod()
    }

    private void initInjectIFMethod() {
        String injectEvents = project.techiesConfig.injectIFEvents
        def jsonSlurper = new JsonSlurper()
        def map = jsonSlurper.parseText(injectEvents)
        Cell cell = new Cell(map)

        cell.events.each {
            InjectConfig.InterfaceMethods.put(it.name+it.desc, new MethodCell(
                    it.name, it.desc, it.parent, it.agentName, it.agentDesc, it.paramsStart,
                    it.paramsCount, it.opcodes
            ))
        }
    }

    private void initInjectFAMethod() {
        String injectEvents = project.techiesConfig.injectFAEvents
        def jsonSlurper = new JsonSlurper()
        def map = jsonSlurper.parseText(injectEvents)
        Cell cell = new Cell(map)

        cell.events.each {
            InjectConfig.FragmentActivityMethods.put(it.name+it.desc, new MethodCell(
                    it.name, it.desc, it.parent, it.agentName, it.agentDesc, it.paramsStart,
                    it.paramsCount, it.opcodes
            ))
        }
    }
}
