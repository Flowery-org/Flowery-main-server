package com.flowery.flowerygateway.service

import com.flowery.flowerygateway.dto.tempMember
import com.flowery.flowerygateway.repository.tempMemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service("memberService")
class tempMemberService(private val db: tempMemberRepository){
    fun findByName(name: String) : tempMember? = db.findByName(name)
    fun updatePassword(id: UUID, newPassword: String) : Boolean {
        db.updatePassword(id, newPassword)
        val updatedMember = db.findByIdOrNull(id)
        if (updatedMember == null || newPassword != updatedMember.password) {
            return false
        } else return true
    }
    fun deleteById(id: UUID): Boolean {
        db.deleteById(id)
        if (db.findByIdOrNull(id) == null) {
            return true
        } else return false
    }
}