package com.flowery.flowerygateway.repository

import com.flowery.flowerygateway.dto.tempMember
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface tempMemberRepository : CrudRepository<tempMember, UUID> {
    fun findByName(name: String) : tempMember?
    fun updatePassword(id: UUID, newPassword: String)

}