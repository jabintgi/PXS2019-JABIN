package app.zero_one.attendance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.zero_one.attendance.R
import app.zero_one.attendance.model.CustomCalender
import kotlinx.android.synthetic.main.row_day.view.*

class CalenderAdapter(
    private val context: Context,
    private val mData: ArrayList<CustomCalender>
) :
    RecyclerView.Adapter<CalenderAdapter.CalenderHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalenderHolder {
        return CalenderHolder(LayoutInflater.from(context).inflate(R.layout.row_day, parent, false))
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: CalenderHolder, position: Int) {
        holder.day.text = mData[position].day
        var hr = mData[position].hour
        if (hr.equals("0.0")) hr = ""
        holder.hour.text = hr
    }

    class CalenderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val day: TextView = itemView.day
        val hour: TextView = itemView.hour
    }
}