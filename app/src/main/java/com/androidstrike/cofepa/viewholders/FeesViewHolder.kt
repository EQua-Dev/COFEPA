package com.androidstrike.cofepa.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidstrike.cofepa.R
import kotlinx.android.synthetic.main.custom_items_to_pay.view.*

class FeesViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {

    var txt_item_to_pay: TextView = itemView.findViewById(R.id.item_to_pay) as TextView
    var txt_amount_to_pay: TextView = itemView.findViewById(R.id.amount_to_pay) as TextView

}