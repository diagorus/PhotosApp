package com.fuh.photosapp.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Photo(
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("width")
        var width: Int? = null,
        @SerializedName("height")
        var height: Int? = null,
        @SerializedName("color")
        var color: String? = null,
        @SerializedName("urls")
        var urls: Urls? = null
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readParcelable<Urls>(Urls::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeValue(width)
        writeValue(height)
        writeString(color)
        writeParcelable(urls, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Photo> = object : Parcelable.Creator<Photo> {
            override fun createFromParcel(source: Parcel): Photo = Photo(source)
            override fun newArray(size: Int): Array<Photo?> = arrayOfNulls(size)
        }
    }
}
