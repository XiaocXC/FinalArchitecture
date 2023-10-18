package com.zjl.finalarchitecture.test
import com.google.gson.annotations.SerializedName


/**
 * @author: 周健力
 * @e-mail: zhoujl@eetrust.com
 * @time: 2023/8/15
 * @version: 1.0
 * @description: TODO
 */
data class ComServBean(
    @SerializedName("_j_data_")
    val jData: String?,
    @SerializedName("msg_id")
    val msgId: Long?,
    @SerializedName("n_content")
    val nContent: String?,
    @SerializedName("n_extras")
    val nExtras: NExtras?,
    @SerializedName("n_title")
    val nTitle: String?,
    @SerializedName("rom_type")
    val romType: Int?,
    @SerializedName("show_type")
    val showType: Int?
)

data class NExtras(
    @SerializedName("key2")
    val key2: String?
)