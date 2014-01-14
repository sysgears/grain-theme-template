package com.example.site.deploy

import com.sysgears.grain.taglib.Site

/**
 * Provides deploying of the generated site to GitHub Pages service.
 */
class GHPagesDeployer {

    /**
     * Site reference, provides access to site configuration.
     */
    private final Site site

    public GHPagesDeployer(Site site) {
        this.site = site
    }

    /**
     * Deploys generated site.
     */
    def deploy = {
        def cacheDir = site.cache_dir
        def destinationDir = site.destination_dir
        def ghPagesUrl = site.gh_pages_url

        if (!ghPagesUrl) {
            System.err.println('Couldn\'t upload to GitHub Pages, please specify your GitHub repo url first')
            return
        }

        def workingBranch = ghPagesUrl.toString().endsWith('.github.io.git') ? 'master' : 'gh-pages'
        def cacheDeployDir = "$cacheDir/gh-deploy"

        def ant = new AntBuilder()

        def git = { List args ->
            ant.exec(executable: 'git', dir: cacheDeployDir) {
                args.collect { arg(value: it) }
            }
        }

        ant.sequential {
            ant.delete(dir: cacheDeployDir, failonerror: false)
            ant.mkdir(dir: cacheDeployDir)
            git(['init'])
            git(['remote', 'add', '-t', workingBranch, '-f', 'origin', ghPagesUrl])
            git(['checkout', workingBranch])
            ant.delete(includeEmptyDirs: true) {
                fileset(dir: cacheDeployDir) {
                    exclude(name: '.git')
                }
            }
            ant.copy(todir: cacheDeployDir) {
                fileset(dir: destinationDir)
            }
            git(['add', '.'])
            git(['commit', '-m', 'Updated site'])
            git(['push', 'origin', "$workingBranch:$workingBranch"])
            ant.delete(dir:cacheDeployDir)
        }
    }
}
