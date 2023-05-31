package com.kapitalletter.wardoffice.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kapitalletter.wardoffice.R
import com.kapitalletter.wardoffice.datamodel.ReviewData

class ReviewAdapter(
    private val dataset: ReviewData
): RecyclerView.Adapter<ReviewAdapter.ItemViewHolder>() {

    init {
        notifyDataSetChanged()
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val reviewText: TextView = view.findViewById(R.id.text_review)
        val nickname: TextView = view.findViewById(R.id.text_nickname)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset.docId[position]
        holder.reviewText.text = item.reviewContext
        holder.nickname.text = item.reviewerNickname
    }

    override fun getItemCount(): Int {
        return dataset.docId.size
    }
}