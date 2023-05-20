package com.zjl.finalarchitecture.module.toolbox.treeCheck.data

import com.zjl.finalarchitecture.module.toolbox.treeCheck.helper.TreeSelectorHelper

sealed class FolderNode(
    open val id: Long,
    open val folderUri: String,
    open val folderName: String,
    open var selected: Int = TreeSelectorHelper.NODE_UNCHECKED,
    open var haveChildren: Boolean = true,
    open var addAnim: Boolean = true
){

    data class RootFolderNode(
        override val id: Long,
        override val folderUri: String,
        override val folderName: String,
        override var selected: Int = TreeSelectorHelper.NODE_UNCHECKED,
        override var haveChildren: Boolean = true,
        override var addAnim: Boolean = true
    ): FolderNode(id, folderUri, folderName, selected){

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
        override var selected: Int = TreeSelectorHelper.NODE_UNCHECKED,
        override var haveChildren: Boolean = true,
        override var addAnim: Boolean = true
    ): FolderNode(id, folderUri, folderName, selected){

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
