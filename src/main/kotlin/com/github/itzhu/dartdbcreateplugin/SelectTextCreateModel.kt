package com.github.itzhu.dartdbcreateplugin

import com.github.itzhu.dartdbcreateplugin.tablecreator.Creator
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.ui.Messages
import java.io.File
import kotlin.concurrent.thread


/**根据dart文件生成数据库配置文件*/
class SelectTextCreateModel : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        println("start")
        //工程环境
        val project = e.getData(PlatformDataKeys.PROJECT)
        println("path:project?.basePath:${project?.basePath}")

        //操作的文件
        val psiFile = e.getData(PlatformDataKeys.PSI_FILE)
        //文件路径
        val dartFilePath = psiFile?.virtualFile?.path
        val checkFileOk = (dartFilePath?.endsWith(".dart") == true || dartFilePath?.endsWith(".txt") == true)
        if (!checkFileOk) {
            Messages.showMessageDialog(project, "${psiFile?.name}不是一个dart或txt文件，无法生成!", "错误提示", null)
            return
        }
        LogUtil.log("start==========")
        Creator.start(project?.basePath!!, dartFilePath!!)
        LogUtil.log("finish==========")
        Messages.showMessageDialog(project, "文件已生成，请到/lib/dbcreate/table下查看", "成功", null)
    }


}