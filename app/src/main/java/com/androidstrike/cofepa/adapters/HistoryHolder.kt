package com.androidstrike.cofepa.adapters

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidstrike.cofepa.R
import com.androidstrike.cofepa.interfaces.IRecyclerItemClickListener

class HistoryHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener  {

    var txtHistorySemester: TextView
    var txtHistoryTotalDue: TextView
    var txtHistoryTotalPaid: TextView
    var btnHistory: Button

    lateinit var iRecyclerItemClickListener: IRecyclerItemClickListener

    fun setClick(iRecyclerItemClickListener: IRecyclerItemClickListener){
        this.iRecyclerItemClickListener = iRecyclerItemClickListener
    }

    init {
        txtHistorySemester = itemView.findViewById(R.id.txt_history_semester) as TextView
        txtHistoryTotalDue = itemView.findViewById(R.id.txt_history_total_due) as TextView
        txtHistoryTotalPaid = itemView.findViewById(R.id.txt_history_total_paid) as TextView
        btnHistory = itemView.findViewById(R.id.btn_history) as Button
    }

    override fun onClick(v: View?) {
        iRecyclerItemClickListener.onItemClickListener(v!!, adapterPosition)
    }
}