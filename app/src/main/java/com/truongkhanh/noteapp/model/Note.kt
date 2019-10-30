package com.truongkhanh.noteapp.model

import android.os.Parcel
import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "title")
    var title: String?,
    @ColumnInfo(name = "content")
    var content: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(content)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }

        val diffCallback = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(
                oldItem: Note,
                newItem: Note
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Note, newItem: Note) =
                oldItem.content == newItem.content && oldItem.title == newItem.title
        }
    }
}