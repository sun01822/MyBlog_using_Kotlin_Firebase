package com.example.authentication

data class PostModel(
    val profileImage : String?=null,
    val profileName : String?=null,
    val postTime : String?=null,
    val postDescription : String?=null,
    val postImage : String?=null,
    val postLikes : Int?=null,
    val postLoves : Int?=null,
    val postUnlikes : Int?=null,
)
