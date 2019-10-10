package app.zero_one.attendance.model

data class CustomCalender(
    var day: String,
    var hour: String="",
    var isLeave: Boolean=true
)