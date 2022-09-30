package com.psr.modskey.models

import android.os.Parcel
import android.os.Parcelable
import com.psr.quizapp.models.User

data class Time (
    val data: String=""





) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!


    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(data)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}