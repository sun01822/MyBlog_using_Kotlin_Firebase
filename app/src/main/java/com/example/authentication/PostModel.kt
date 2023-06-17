package com.example.authentication

data class PostModel(
    val id: String?=null,
    var profileImage : String?=null,
    var profileName : String?=null,
    val postTime : String?=null,
    val postDescription : String?=null,
    val postImage : String?=null,
    val postLikes : String?=null,
    val postLoves : String?=null,
    val postUnlikes : String?=null,
)
