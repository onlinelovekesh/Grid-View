package com.example.gridlayout

class Items {
    var itemUid: String? = null
    var itemImageUri: String? = null
    var itemName: String? = null
    var itemPrice: String? = null
    var itemDescription:String? = null
    var itemQuantity: String? = null

    constructor() {}

    constructor(
        itemUid1: String, itemImageUri1: String, itemName1: String, itemPrice1: String,
        itemDescription1: String, itemQuantity1: String?)
    {
        this.itemUid = itemUid1
        this.itemImageUri = itemImageUri1
        this.itemName = itemName1
        this.itemPrice = itemPrice1
        this.itemDescription = itemDescription1
        this.itemQuantity = itemQuantity1
    }
}