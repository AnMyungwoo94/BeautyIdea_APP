package com.example.shoppingmall_app.contentList

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoppingmall_app.R
import com.example.shoppingmall_app.utils.FBAuth
import com.example.shoppingmall_app.utils.FBRef

class BookmarkRvAdapter(
    val context: Context,
    val items: ArrayList<ContentModel>,
    val keyList: ArrayList<String>,
    val bookmarkIdList: MutableList<String>
) : RecyclerView.Adapter<BookmarkRvAdapter.Viewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkRvAdapter.Viewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.content_rv_item, parent, false)
        return Viewholder(v)
    }

    override fun onBindViewHolder(holder: BookmarkRvAdapter.Viewholder, position: Int) {

        holder.bindItems(items[position], keyList[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item: ContentModel, key: String) {
            itemView.setOnClickListener {
                val intent = Intent(context, ContentShowActivity::class.java)
                intent.putExtra("uri", item.webUrl)
                itemView.context.startActivity(intent)
            }

            val ContentTitle = itemView.findViewById<TextView>(R.id.textArea)
            val imageViewArea = itemView.findViewById<ImageView>(R.id.imageArea)
            val bookmarkArea = itemView.findViewById<ImageView>(R.id.bookmarkArea)
            //content List와 bookmark List의 값이 같은게 있다면 검은색으로 변경
            if (bookmarkIdList.contains(key)) {
                bookmarkArea.setImageResource(R.drawable.bookmark_color)
            } else {
                bookmarkArea.setImageResource(R.drawable.bookmark_white)
            }

            ContentTitle.text = item.title
            Glide.with(context).load(item.imageUrl).into(imageViewArea)
        }
    }
}