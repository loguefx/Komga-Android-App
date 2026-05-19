package com.komga.android.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LibraryDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("root") val root: String = "",
    @SerializedName("importComicInfoBook") val importComicInfoBook: Boolean = false,
    @SerializedName("importComicInfoSeries") val importComicInfoSeries: Boolean = false,
    @SerializedName("importComicInfoCollection") val importComicInfoCollection: Boolean = false,
    @SerializedName("importComicInfoReadList") val importComicInfoReadList: Boolean = false,
    @SerializedName("importComicInfoSeriesAppendVolume") val importComicInfoSeriesAppendVolume: Boolean = false,
    @SerializedName("importEpubBook") val importEpubBook: Boolean = false,
    @SerializedName("importEpubSeries") val importEpubSeries: Boolean = false,
    @SerializedName("importMylarSeries") val importMylarSeries: Boolean = false,
    @SerializedName("importLocalArtwork") val importLocalArtwork: Boolean = false,
    @SerializedName("importBarcodeIsbn") val importBarcodeIsbn: Boolean = false,
    @SerializedName("scanForceModifiedTime") val scanForceModifiedTime: Boolean = false,
    @SerializedName("scanDeep") val scanDeep: Boolean = false,
    @SerializedName("repairExtensions") val repairExtensions: Boolean = false,
    @SerializedName("convertToCbz") val convertToCbz: Boolean = false,
    @SerializedName("emptyTrashAfterScan") val emptyTrashAfterScan: Boolean = false,
    @SerializedName("seriesCover") val seriesCover: String = "FIRST",
    @SerializedName("hashFiles") val hashFiles: Boolean = false,
    @SerializedName("hashPages") val hashPages: Boolean = false,
    @SerializedName("analyzeDimensions") val analyzeDimensions: Boolean = false,
    @SerializedName("unavailableDate") val unavailableDate: String? = null,
    @SerializedName("createdDate") val createdDate: String = "",
    @SerializedName("lastModifiedDate") val lastModifiedDate: String = "",
    @SerializedName("lastScan") val lastScan: String? = null,
    @SerializedName("scanStatus") val scanStatus: String = ""
)
