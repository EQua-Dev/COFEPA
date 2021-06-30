package com.androidstrike.cofepa.adapters

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidstrike.cofepa.R
import com.androidstrike.cofepa.interfaces.IRecyclerItemClickListener

class VerifyHolder (itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener  {

    var txtSemester: TextView
    var txtTotalDue: TextView
    var txtTotalPaid: TextView
    var txtTotalOwing: TextView
    var txtCleared: TextView
    var btnVerify: Button

    lateinit var iRecyclerItemClickListener: IRecyclerItemClickListener

    fun setClick(iRecyclerItemClickListener: IRecyclerItemClickListener){
        this.iRecyclerItemClickListener = iRecyclerItemClickListener
    }

    init {
        txtSemester = itemView.findViewById(R.id.txt_semester) as TextView
        txtTotalDue = itemView.findViewById(R.id.txt_total_due) as TextView
        txtTotalPaid = itemView.findViewById(R.id.txt_total_paid) as TextView
        txtTotalOwing = itemView.findViewById(R.id.txt_total_owing) as TextView
        txtCleared = itemView.findViewById(R.id.txt_cleared) as TextView
        btnVerify = itemView.findViewById(R.id.btn_verify) as Button
    }

    override fun onClick(v: View?) {
        iRecyclerItemClickListener.onItemClickListener(v!!, adapterPosition)
    }
}