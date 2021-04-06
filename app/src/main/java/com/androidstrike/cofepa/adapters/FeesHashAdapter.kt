package com.androidstrike.cofepa.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidstrike.cofepa.R
import com.androidstrike.cofepa.utils.Common
import com.androidstrike.cofepa.utils.Common.feeToPayHash
import kotlinx.android.synthetic.main.custom_hash_rv_layout.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FeesHashAdapter: RecyclerView.Adapter<FeesHashAdapter.HashFeesViewHolder>() {

    val modelList: HashMap<String, String> = HashMap()

    inner class HashFeesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HashFeesViewHolder {
        return HashFeesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_hash_rv_layout, parent, false))
    }

    override fun onBindViewHolder(holder: HashFeesViewHolder, position: Int) {
        val hashKeyArray = ArrayList(feeToPayHash.keys)
        val hashValueArray = ArrayList(feeToPayHash.values)

        val locale: Locale = Locale("en", "NG")
        val fmt: NumberFormat = NumberFormat.getCurrencyInstance(locale)


        holder.itemView.txt_fee_rv_key.text = hashKeyArray[position]
        holder.itemView.txt_fee_rv_value.append(fmt.format(hashValueArray[position]).toString())
                    Log.d("EQUA", "onBindViewHolderHash: $hashKeyArray")

    }

    override fun getItemCount(): Int {
        return feeToPayHash.size
    }
}