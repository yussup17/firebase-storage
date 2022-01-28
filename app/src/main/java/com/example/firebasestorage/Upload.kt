package com.example.firebasestorage

class Upload {
    var name: String = ""
    var image_url:String = ""
    var key:String = ""
    constructor(name: String, image_url: String, key: String){

        if (name.trim() == "") {
            this.name = "No name"
        }
        this.name = name
        this.image_url = image_url
        this.key = key
    }

    constructor(){}

}