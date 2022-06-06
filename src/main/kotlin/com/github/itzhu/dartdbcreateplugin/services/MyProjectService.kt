package com.github.itzhu.dartdbcreateplugin.services

import com.intellij.openapi.project.Project
import com.github.itzhu.dartdbcreateplugin.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
