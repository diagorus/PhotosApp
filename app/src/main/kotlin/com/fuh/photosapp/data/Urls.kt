package com.fuh.photosapp.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Urls(
        @SerializedName("raw")
        var raw: String? = null,
        @SerializedName("full")
        var full: String? = null,
        @SerializedName("regular")
        var regular: String? = null,
        @SerializedName("small")
        var small: String? = null,
        @SerializedName("thumb")
        var thumb: String? = null
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(raw)
        writeString(full)
        writeString(regular)
        writeString(small)
        writeString(thumb)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Urls> = object : Parcelable.Creator<Urls> {
            override fun createFromParcel(source: Parcel): Urls = Urls(source)
            override fun newArray(size: Int): Array<Urls?> = arrayOfNulls(size)
        }
    }
}
