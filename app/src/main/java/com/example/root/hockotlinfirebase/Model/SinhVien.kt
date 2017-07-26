package com.example.root.hockotlinfirebase.Model

/**
 * Created by root on 7/26/17.
 */
class SinhVien{
    public var name = ""
    public var age = 0
    //Default data of Firebase
    constructor()

    constructor(name: String, age: Int) {
        this.name = name
        this.age = age
    }


}