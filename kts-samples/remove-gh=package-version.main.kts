@file:DependsOn("io.ktor:ktor-client-cio-jvm:2.2.2")
@file:DependsOn("io.ktor:ktor-serialization-gson-jvm:2.2.2")
@file:DependsOn("io.ktor:ktor-client-content-negotiation-jvm:2.2.2")
@file:DependsOn("io.ktor:ktor-client-auth-jvm:2.2.2")
@file:DependsOn("com.github.ajalt.clikt:clikt-jvm:3.5.1")

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.URLProtocol
import io.ktor.serialization.gson.*
import kotlinx.coroutines.runBlocking

class RemoveGhPackageVersion : CliktCommand(
    help = "Lists or deletes all github packages associated with a given version"
) {
    private val username by option("--username", "-u", envvar = "GH_USERNAME").required()
    private val token by option("--token", "-t", envvar = "GH_TOKEN").required()
    private val organisation by option("--organisation", "-o", envvar = "GH_ORGANISATION")
    private val delete by option(
        "--delete", "-d",
        help = "If set, deletes all found packages, otherwise just lists them."
    ).flag()

    private val repository by argument(help = "GitHub repository name to look up")
    private val version by argument(help = "Version to filter by")

    private val prefix by lazy { organisation?.let { "/orgs/$it" } ?: "/user" }

    override fun run() = runBlocking {
        println("repository=$repository version=$version apiPrefix=$prefix")

        val packages = mutableListOf<GHPackage>().run {
            var i = 0
            while (true) {
                val page = ghClient.get("$prefix/packages") {
                    parameter("package_type", "maven")
                    parameter("page", "${++i}")
                }.body<List<GHPackage>>()
                if (page.isEmpty()) {
                    break
                } else {
                    addAll(page.filter { it.repository.name == repository })
                }
            }
            toList()
        }
        println("Found ${packages.size} packages")

        val versions = packages.map {
            it to mutableListOf<GHPackageVersion>().run {
                var i = 0
                while (true) {
                    val page =
                        ghClient.get("$prefix/packages/${it.package_type}/${it.name}/versions") {
                            parameter("package_type", "maven")
                            parameter("page", "${++i}")
                        }.body<List<GHPackageVersion>>()
                    if (page.isEmpty()) {
                        break
                    } else {
                        addAll(page)
                    }
                }
                toList()
            }
        }

        if(versions.isEmpty()) println("Found no packages for version=$version")

        versions.forEach { (pkg, ver) ->
            if (ver.size > 1) {
                ver.firstOrNull { it.name == version }?.let {
                    deleteOrLog(pkg, it, false)
                }
            } else if (ver.any { it.name == version }) {
                deleteOrLog(pkg, ver.first(), true)
            }
        }
    }

    val ghClient = HttpClient(CIO) {
        install(Auth) {
            basic {
                credentials {
                    BasicAuthCredentials(
                        username = this@RemoveGHPackageVersion.username,
                        password = this@RemoveGHPackageVersion.token,
                    )
                }
                sendWithoutRequest { true }
            }
        }
        install(ContentNegotiation) { gson() }
        expectSuccess = true
        followRedirects = true
        defaultRequest {
            host = "api.github.com"
            url {
                protocol = URLProtocol.HTTPS
            }
            header("Accept", "application/vnd.github.v3+json")
        }
    }

    private suspend fun deleteOrLog(pkg: GHPackage, version: GHPackageVersion, last: Boolean) {
        if (!delete) {
            println("Found ${pkg.name}@${version.name}")
        } else {
            print("Deleting ${pkg.name}@${version.name}")
            if (last) {
                ghClient.delete("$prefix/packages/${pkg.package_type}/${pkg.name}")
            } else {
                ghClient.delete("$prefix/packages/${pkg.package_type}/${pkg.name}/versions/${version.id}")
            }
            println(" | Deleted!")
        }
    }
}

data class GHRepository(
    val id: String,
    val node_id: String,
    val name: String,
    val full_name: String
)

data class GHPackage(
    val id: String,
    val name: String,
    val package_type: String,
    val repository: GHRepository
)

data class GHPackageVersion(val id: String, val name: String)

RemoveGHPackageVersion().main(args)