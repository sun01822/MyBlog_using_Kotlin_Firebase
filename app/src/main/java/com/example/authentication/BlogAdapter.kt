package com.example.authentication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class BlogAdapter(private val dataList: ArrayList<PostModel>):RecyclerView.Adapter<BlogAdapter.BlogViewHolder>(){
    private var mOnClickListener: OnClickListener? = null
    private var mLikeListener: OnClickListener? = null
    private var mLoveListener: OnClickListener? = null
    private var mUnlikeListener: OnClickListener? = null
    class BlogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val profileImage : CircleImageView = itemView.findViewById(R.id.profileImage)
        val profileName : TextView = itemView.findViewById(R.id.profileName)
        val postTime : TextView = itemView.findViewById(R.id.postTime)
        val postDescription : TextView = itemView.findViewById(R.id.postDescription)
        val postImage : ImageView = itemView.findViewById(R.id.postImage)
        val postLikes : TextView = itemView.findViewById(R.id.postLikes)
        val postLoves : TextView = itemView.findViewById(R.id.postLoves)
        val postUnlikes:  TextView = itemView.findViewById(R.id.postUnlikes)
        val likeButton : ImageView = itemView.findViewById(R.id.likeButton)
        val loveButton : ImageView = itemView.findViewById(R.id.loveButton)
        val unlikeButton : ImageView = itemView.findViewById(R.id.unlikeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.custom_show_blog_layout, parent, false)
        return BlogViewHolder(item)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        val singleData = dataList[position]
        Glide.with(holder.profileImage.context).load(singleData.profileImage).into(holder.profileImage)
        holder.profileName.text = singleData.profileName
        holder.postTime.text = singleData.postTime
        val temp = singleData.postDescription
        if(temp?.length!! > 70) {
            val shortString = temp?.let { Math.min(it.length, 70) }?.let { temp.substring(0, it) }
            holder.postDescription.text = "${shortString}" + "   see more"
        }
        else {
            holder.postDescription.text = temp.toString()
        }
        Glide.with(holder.postImage.context).load(singleData.postImage).into(holder.postImage)
        holder.postLikes.text = singleData.postLikes.toString()
        holder.postLoves.text = singleData.postLoves.toString()
        holder.postUnlikes.text = singleData.postUnlikes.toString()

        holder.itemView.setOnClickListener{
            if(mOnClickListener!=null){
                mOnClickListener!!.onClick(singleData)
            }
        }
        holder.likeButton.setOnClickListener{
            if(mLikeListener!=null){
                mLikeListener!!.onLikeClick(singleData)
                holder.likeButton.setImageResource(R.drawable.fill_like_icon)
                holder.loveButton.setImageResource(R.drawable.love_icon)
                holder.unlikeButton.setImageResource(R.drawable.unlike_icon)
            }
        }

        holder.loveButton.setOnClickListener {
            if(mLoveListener!=null) {
                mLikeListener!!.onLoveClick(singleData)
                holder.likeButton.setImageResource(R.drawable.like_icon)
                holder.loveButton.setImageResource(R.drawable.fill_love_icon)
                holder.unlikeButton.setImageResource(R.drawable.unlike_icon)
            }
        }

        holder.unlikeButton.setOnClickListener {
            if(mUnlikeListener!=null) {
                mUnlikeListener!!.onUnlikeClick(singleData)
                holder.likeButton.setImageResource(R.drawable.like_icon)
                holder.loveButton.setImageResource(R.drawable.love_icon)
                holder.unlikeButton.setImageResource(R.drawable.fill_unlike_icon)
            }
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.mOnClickListener = onClickListener
        this.mLikeListener = onClickListener
        this.mLoveListener = onClickListener
        this.mUnlikeListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(post: PostModel)
        fun onLikeClick(post: PostModel)
        fun onLoveClick(post: PostModel)
        fun onUnlikeClick(post: PostModel)

    }

}