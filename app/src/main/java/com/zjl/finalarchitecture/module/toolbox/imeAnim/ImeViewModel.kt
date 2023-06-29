package com.zjl.finalarchitecture.module.toolbox.imeAnim

import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.module.toolbox.imeAnim.data.MessageData
import com.zjl.finalarchitecture.module.toolbox.imeAnim.data.MessageData.MESSAGE_TYPE_TEXT_OTHER
import com.zjl.finalarchitecture.module.toolbox.imeAnim.data.MessageData.MESSAGE_TYPE_TEXT_SELF
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * @author Xiaoc
 * @since  2023-06-29
 *
 * IME数据ViewModel
 **/
class ImeViewModel: BaseViewModel() {

    private val _messageList = MutableStateFlow(mutableListOf<MessageData>())
    val messageList: StateFlow<List<MessageData>> = _messageList

    init {
        // 模拟数据
        _messageList.value = buildList<MessageData> {
            add(
                MessageData().apply {
                    id = "1"
                    message = "简称SMS，是用户通过手机或电信终端直接发送或接收的方式的数字信息"
                    type = MESSAGE_TYPE_TEXT_SELF
                }
            )
            add(
                MessageData().apply {
                    id = "2"
                    message = "中国电信、中国移动、中国联通联合发布《5G消息白皮书》,我国基础短信业务"
                    type = MESSAGE_TYPE_TEXT_OTHER
                }
            )
            add(
                MessageData().apply {
                    id = "3"
                    message = "当世界上第一条短信在英国沃尔丰的GSM网络"
                    type = MESSAGE_TYPE_TEXT_SELF
                }
            )
            add(
                MessageData().apply {
                    id = "4"
                    message = "世人把1973年的第一部手机的问世归功于当年的摩托罗拉"
                    type = MESSAGE_TYPE_TEXT_OTHER
                }
            )
        }.toMutableList()
    }
}