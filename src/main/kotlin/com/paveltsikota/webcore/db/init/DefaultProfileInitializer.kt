package com.paveltsikota.webcore.db.init

import com.paveltsikota.webcore.db.adapter.ProfileAdapter
import com.paveltsikota.webcore.db.service.ProfileService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class DefaultProfileInitializer(
    private val profileService: ProfileService
): ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        val defaultProfile = ProfileAdapter.makeDefaultProfileEntity()

        if (!profileService.isAlreadyExist(defaultProfile)) {
            val result = with(defaultProfile) {
                profileService.add(title, description, cfg, true)
            }

            if (result.success) {
                println("✅ Created the default profile")
            } else {
                println("❌ Error of creation the default profile")
            }
        } else {
            println("ℹ️ The default profile already exists")
        }
    }

}
