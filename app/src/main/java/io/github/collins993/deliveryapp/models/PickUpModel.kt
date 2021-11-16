package io.github.collins993.deliveryapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class PickUpModel(
    var address: String,
    var description: String,
    var firstName: String,
    var lastName: String,
    var phoneNumber: String,
    var emailAddress: String,
): Parcelable
