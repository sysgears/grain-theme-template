import com.example.site.ResourceMapper
import com.example.site.deploy.GHPagesDeployer
import com.example.site.taglib.ThemeTagLib

environments {
    dev {
        log.info 'Development environment is used'
        url = "http://localhost:${jetty_port}"
        show_unpublished = true
    }
    prod {
        log.info 'Production environment is used'
        url = '' // site URL, for example http://www.example.com
        show_unpublished = false
        features {
            minify_xml = false
            minify_html = false
            minify_js = false
            minify_css = false
        }
    }
    cmd {
        features {
            compass = 'none'
            highlight = 'none'
        }
    }
}

// Features configuration.
features {
    compass = 'auto'             // 'none', 'ruby', 'jruby', 'shell', 'auto'
    highlight = 'pygments'       // 'none', 'pygments'
    // pygments = 'auto'         // 'none', 'python', 'jython', 'shell', 'auto'
    // cache_highlight = 'true'  // default to 'true'
}

// Resource mapper and tag libs.
resource_mapper = new ResourceMapper(site).map
tag_libs = [ThemeTagLib]

// Deployment settings.
s3_bucket = '' // your S3 bucket name
deploy_s3 = "s3cmd sync --acl-public --reduced-redundancy ${destination_dir}/ s3://${s3_bucket}/"

gh_pages_url = '' // path to GitHub repository in format git@github.com:{username}/{repo}.git
deploy = new GHPagesDeployer(site).deploy

// Custom commands-line commands.
commands = [
/*
 * Creates new page.
 *
 * location - relative path to the new page, should start with the /, i.e. /pages/index.html.
 * pageTitle - new page title
 */
'create-page': { String location, String pageTitle ->
        file = new File(content_dir, location)
        file.parentFile.mkdirs()
        file.exists() || file.write("""---
layout: default
title: "${pageTitle}"
published: true
---
""")}
]