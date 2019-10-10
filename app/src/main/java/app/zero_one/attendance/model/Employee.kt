package app.zero_one.attendance.model

data class Employee(val emp_id: String, val name: String) {
    override fun toString(): String {
        return name
    }
}