package io.github.collins993.deliveryapp.models

import java.io.Serializable

data class DeliveryModel(
    var pickUpAddress: String = "",
    var pickUpDescription: String = "",
    var pickUpFirstName: String = "",
    var pickUpLastName: String = "",
    var pickUpPhoneNumber: String = "",
    var pickUpEmailAddress: String = "",
    var dropOffAddress: String = "",
    var dropOffFirstName: String = "",
    var dropOffLastName: String = "",
    var dropOffPhoneNumber: String = "",
    var dropOffEmailAddress: String = "",
    var scheduleOrder: String = "",
    var deliveryTime: String = "",
    var dateAndTime: String = "",
    var time: String = "",
    var userId: String = "",
    var deliveryStatus: String = "pending"
):Serializable
