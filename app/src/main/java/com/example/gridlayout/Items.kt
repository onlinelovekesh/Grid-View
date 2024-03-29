package com.example.gridlayout

class Items {
    var itemUid: String? = null
    var itemImageUri: String? = null
    var itemName: String? = null
    var itemPrice: String? = null
    var itemDescription:String? = null
    var itemQuantity: String? = null
    var cartItemPriceAndQuantity: String? = null
    var cartItemTotal: String? = null
    var isFavorite: String? = null

    constructor() {}

    constructor(
        itemUid1: String, itemImageUri1: String, itemName1: String, itemPrice1: String,
        itemDescription1: String, itemQuantity1: String?, cartItemPriceAndQuantity1:String?,cartItemTotal1:String?,
        isFavorite1: String?)
    {
        this.itemUid = itemUid1
        this.itemImageUri = itemImageUri1
        this.itemName = itemName1
        this.itemPrice = itemPrice1
        this.itemDescription = itemDescription1
        this.itemQuantity = itemQuantity1
        this.cartItemPriceAndQuantity = cartItemPriceAndQuantity1
        this.cartItemTotal = cartItemTotal1
        this.isFavorite = isFavorite1
    }
}