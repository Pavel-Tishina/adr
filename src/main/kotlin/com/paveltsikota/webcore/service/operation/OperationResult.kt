package com.paveltsikota.webcore.service.operation

abstract class OperationResult(
    success : Boolean = false,
    errors: ArrayList<String> = ArrayList(),
    warnings: ArrayList<String> = ArrayList()
) {
    val success = success
    private val errors = errors
    private val warnings = warnings
    abstract val obj: Any?
    abstract val result: Any

    fun getError(): String {
        return errors.joinToString("\n")
    }

    fun getWarnings(): String {
        return warnings.joinToString("\n")
    }

    fun hasErrorDetails(): Boolean {
        return errors.any { it.isNotBlank() }
    }

    fun hasWarningsDetails(): Boolean {
        return warnings.any { it.isNotBlank() }
    }

    fun hasDetails(): Boolean {
        return hasWarningsDetails() || hasErrorDetails()
    }

    fun addError(error: String) {
        errors.add(error)
    }

    fun addError(errors: List<String>) {
        this.errors.addAll(errors)
    }

    fun addWarning(warning: String) {
        warnings.add(warning)
    }

    fun addWarning(warnings: List<String>) {
        this.warnings.addAll(warnings)
    }

}