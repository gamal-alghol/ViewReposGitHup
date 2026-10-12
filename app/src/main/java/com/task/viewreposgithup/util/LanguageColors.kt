package com.task.viewreposgithup.util

import androidx.compose.ui.graphics.Color
import com.task.viewreposgithup.ui.theme.JavaColor
import com.task.viewreposgithup.ui.theme.KotlinColor
import com.task.viewreposgithup.ui.theme.SwiftColor
import com.task.viewreposgithup.ui.theme.TypeScriptColor


val supportedLanguages: List<String> = listOf(
    "Kotlin", "Java", "Swift", "TypeScript", "JavaScript",
    "Python", "C++", "C#", "Ruby", "Go", "Rust", "HTML", "CSS", "Shell"
).sorted()


object LanguageColors {
    fun getColorForLanguage(language: String?): Color {
        return when (language) {
            "Kotlin" -> KotlinColor
            "Java" -> JavaColor
            "Swift" -> SwiftColor
            "TypeScript" -> TypeScriptColor
            "JavaScript" -> com.task.viewreposgithup.ui.theme.JavaScriptColor
            "Python" -> com.task.viewreposgithup.ui.theme.PythonColor
            "C++" -> com.task.viewreposgithup.ui.theme.CppColor
            "C#" -> com.task.viewreposgithup.ui.theme.CSharpColor
            "Ruby" -> com.task.viewreposgithup.ui.theme.RubyColor
            "Go" -> com.task.viewreposgithup.ui.theme.GoColor
            "Rust" -> com.task.viewreposgithup.ui.theme.RustColor
            "HTML" ->com.task.viewreposgithup.ui.theme. HtmlColor
            "CSS" -> com.task.viewreposgithup.ui.theme.CssColor
            "Shell" -> com.task.viewreposgithup.ui.theme.ShellColor
            else ->com.task.viewreposgithup.ui.theme. DefaultLanguageColor
        }
    }
}