package com.example.authentication

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        val firestore = FirebaseFirestore.getInstance()
        val singleData = dataList[position]
        val documentId = singleData.id
        val profileEmail = singleData.profileEmail
        val reactChecker = singleData.reactChecker
        Glide.with(holder.profileImage.context).load(singleData.profileImage).into(holder.profileImage)
        holder.profileName.text = singleData.profileName
        holder.postTime.text = singleData.postTime
        val temp = singleData.postDescription
        if(temp?.length!! > 70) {
            val shortString = temp.length.coerceAtMost(70).let { temp.substring(0, it) }
            holder.postDescription.text = "$shortString   see more"
        }
        else {
            holder.postDescription.text = temp.toString()
        }
        Glide.with(holder.postImage.context).load(singleData.postImage).into(holder.postImage)
        firestore.collection("$profileEmail").document("$documentId").get().addOnSuccessListener {
            if(it.exists()){
                holder.postLikes.text = it.getString("postLikes")
                holder.postLoves.text = it.getString("postLoves")
                holder.postUnlikes.text = it.getString("postUnlikes")
                val reaction = it.getString("reactChecker")
                if(reaction == "1"){
                    holder.likeButton.setImageResource(R.drawable.fill_like_icon)
                    holder.loveButton.setImageResource(R.drawable.love_icon)
                    holder.unlikeButton.setImageResource(R.drawable.unlike_icon)
                }
                else if(reaction == "2"){
                    holder.likeButton.setImageResource(R.drawable.like_icon)
                    holder.loveButton.setImageResource(R.drawable.fill_love_icon)
                    holder.unlikeButton.setImageResource(R.drawable.unlike_icon)
                }
                else if(reaction == "3"){
                    holder.likeButton.setImageResource(R.drawable.like_icon)
                    holder.loveButton.setImageResource(R.drawable.love_icon)
                    holder.unlikeButton.setImageResource(R.drawable.fill_unlike_icon)
                }
            }
        }.addOnFailureListener {

        }

        holder.itemView.setOnClickListener{
            if(mOnClickListener!=null){
                mOnClickListener!!.onClick(singleData, documentId.toString())
            }
        }
        holder.likeButton.setOnClickListener{
            if(mLikeListener!=null){
                mLikeListener!!.onLikeClick(singleData, documentId.toString())
                holder.likeButton.setImageResource(R.drawable.fill_like_icon)
                holder.loveButton.setImageResource(R.drawable.love_icon)
                holder.unlikeButton.setImageResource(R.drawable.unlike_icon)
                var  likes = singleData.postLikes?.toInt()
                likes = likes!! + 1
                holder.postLikes.text = likes.toString()
                holder.postLoves.text = singleData.postLoves.toString()
                holder.postUnlikes.text = singleData.postUnlikes.toString()
            }
        }

        holder.loveButton.setOnClickListener {
            if(mLoveListener!=null) {
                mLikeListener!!.onLoveClick(singleData, documentId.toString())
                holder.likeButton.setImageResource(R.drawable.like_icon)
                holder.loveButton.setImageResource(R.drawable.fill_love_icon)
                holder.unlikeButton.setImageResource(R.drawable.unlike_icon)
                var  loves = singleData.postLoves?.toInt()
                loves = loves!! + 1
                holder.postLoves.text = loves.toString()
                holder.postLikes.text = singleData.postLikes.toString()
                holder.postUnlikes.text = singleData.postUnlikes.toString()
            }
        }

        holder.unlikeButton.setOnClickListener {
            if(mUnlikeListener!=null) {
                mUnlikeListener!!.onUnlikeClick(singleData, documentId.toString())
                holder.likeButton.setImageResource(R.drawable.like_icon)
                holder.loveButton.setImageResource(R.drawable.love_icon)
                holder.unlikeButton.setImageResource(R.drawable.fill_unlike_icon)
                var  unlikes = singleData.postUnlikes?.toInt()
                unlikes = unlikes!! + 1
                holder.postUnlikes.text = unlikes.toString()
                holder.postLikes.text = singleData.postLikes.toString()
                holder.postLoves.text = singleData.postLoves.toString()
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
        fun onClick(post: PostModel, documentId : String)
        fun onLikeClick(post: PostModel, documentId : String)
        fun onLoveClick(post: PostModel, documentId: String)
        fun onUnlikeClick(post: PostModel, documentId: String)

    }

}