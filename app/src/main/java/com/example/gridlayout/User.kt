package com.example.gridlayout

class User {
    var uid: String? = null
    var name: String? = null
    var email: String? = null
    var pass: String? = null
    //var profileImageUri: String? = null

    constructor() {}

    constructor(uid1: String, name1: String, email1: String, pass1: String)
    {//, profileImgUri1: String){
        this.uid = uid1
        this.name = name1
        this.email = email1
        this.pass = pass1
        //  this.profileImageUri = profileImgUri1
    }
}