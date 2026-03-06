package com.paveltsikota.webcore.db.service.impl

import com.paveltsikota.webcore.db.entity.ProfileEntity
import com.paveltsikota.webcore.db.entity.SourcesEntity
import com.paveltsikota.webcore.db.service.FilesService
import com.paveltsikota.webcore.db.service.GroupsService
import com.paveltsikota.webcore.db.service.HashesService
import com.paveltsikota.webcore.db.service.JobTaskService
import com.paveltsikota.webcore.db.service.ProfileService
import com.paveltsikota.webcore.db.service.SourcesService
import com.paveltsikota.webcore.db.service.WorkService
import com.paveltsikota.webcore.hash.calculator.HashCalculatorBuilder
import com.paveltsikota.webcore.utils.enums.HashType
import org.springframework.stereotype.Service
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.isRegularFile

@Service
class WorkServiceImpl(
    private val fileService: FilesService,
    private val groupService: GroupsService,
    private val hashesService: HashesService,
    private val profileService: ProfileService,
    private val sourcesService: SourcesService,
    private val jobTaskService: JobTaskService,
): WorkService {

    override fun addFilesFromDirectory(profileId: Long, sourcesId: List<Long>) {
        val profileResult = profileService.getById(profileId)
        if (!profileResult.success) return


        val sourcesResult = sourcesService.getSources(profileId = profileId, page = null, pageSize = null)

        if (!sourcesResult.success) return

        val dirs = (sourcesResult.obj as List<SourcesEntity>).map { Path(it.path) }.toList()

        val hashType = (profileResult.obj as ProfileEntity).cfg.getOrDefault("hashType", HashType.XXHASH64) as HashType
        val hashCalc = HashCalculatorBuilder().saveBuild(hashType)


        dirs.forEach {
            Files.walk(it).filter { it.isRegularFile() }.forEach {
//                val fileEntity = FilesEntity(
//                    profile = profileId,
//                    size = it.fileSize()
//
//                )                )
//                val hash = hashCalc.calculate(it)
                // TODO
            }
        }

    }

    override fun makeGroups(profileId: Long) {
        TODO("Not yet implemented")
    }

    override fun calculateHashes(profileId: Long) {
        TODO("Not yet implemented")
    }
}