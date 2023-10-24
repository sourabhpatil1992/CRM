package com.venter.crm.models

data class UserHierarchyData(
    val userList: List<UserDataList>,
    val userRole: List<UserRole>
)

data class UserDataList(
    val id: Int,
    val user_name: String,
    val user_type: String
)

data class UserRole(
    val sba: Int,
    val tl: Int,
    val floor_manager: Int,
    val sales_manger: Int

)