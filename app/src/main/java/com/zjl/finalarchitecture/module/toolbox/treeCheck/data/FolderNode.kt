package com.zjl.finalarchitecture.module.toolbox.treeCheck.data

sealed class FolderNode(
    open val id: Long,
    open val folderUri: String,
    open val folderName: String,
    open var selected: Boolean = false,
    /**
     * 文件夹状态
     * 1：全选或全不选 | -1：部分选择
     */
    open var folderStatus: Int = 1,
    open var haveChildren: Boolean = true
){

    data class RootFolderNode(
        override val id: Long,
        override val folderUri: String,
        override val folderName: String,
        override var selected: Boolean = false,
        override var folderStatus: Int = 1,
        override var haveChildren: Boolean = true
    ): FolderNode(id, folderUri, folderName, selected, folderStatus){

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is RootFolderNode) return false
            if (!super.equals(other)) return false

            if (folderUri != other.folderUri) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + folderUri.hashCode()
            return result
        }
    }

    data class ChildFolderNode(
        override val id: Long,
        override val folderUri: String,
        override val folderName: String,
        override var selected: Boolean = false,
        override var folderStatus: Int = 1,
        override var haveChildren: Boolean = true
    ): FolderNode(id, folderUri, folderName, selected, folderStatus){

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ChildFolderNode) return false

            if (folderUri != other.folderUri) return false

            return true
        }

        override fun hashCode(): Int {
            return folderUri.hashCode()
        }
    }


}
