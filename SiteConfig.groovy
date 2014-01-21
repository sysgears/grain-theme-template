import com.example.site.ResourceMapper
import com.example.site.deploy.GHPagesDeployer
import com.example.site.taglib.ThemeTagLib

environments {
    dev {
        log.info 'Development environment is used'
        jetty_port = 4000
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

// Default features configuration.
features {
    compass = 'auto'             // 'none', 'ruby', 'jruby', 'shell', 'auto'
    highlight = 'pygments'           // 'none', 'pygments'
    // pygments = 'auto'         // 'none', 'python', 'jython', 'shell', 'auto'
    // cache_highlight = 'true'  // default to 'true'
}

// Resource mapper and tag libs.
resource_mapper = new ResourceMapper(site).map
tag_libs = [ThemeTagLib]

// Locale and datetime format.
Locale.setDefault(Locale.US)
datetime_format = 'yyyy-MM-dd HH:mm'

// Site directories.
base_dir = System.getProperty('user.dir')
cache_dir = "${base_dir}/.cache"
content_dir = "${base_dir}/content"
theme_dir = "${base_dir}/theme"
source_dir = [content_dir, theme_dir, "${cache_dir}/compass"]
include_dir = "${theme_dir}/includes"
layout_dir = "${theme_dir}/layouts"
destination_dir = "${base_dir}/target"

excludes = ['/sass/.*', '/src/.*', '/target/.*']

//Embedded code configuration
code_enabled_files = ['html', 'md', 'markdown', 'xml', 'css', 'rst', 'adoc', 'asciidoc']
code_allowed_files = ['txt', 'js', 'rb']

// Deployment settings.
s3_bucket = '' // your S3 bucket name
deploy_s3 = "s3cmd sync --acl-public --reduced-redundancy ${destination_dir}/ s3://${s3_bucket}/"

gh_pages_url = '' // path to GitHub repository in format git@github.com:{username}/{repo}.git
deploy = new GHPagesDeployer(site).deploy

// Custom commands-line commands.
commands = [
/**
 * Creates new page.
 *
 * location - relative path to the new page or to the directory to save a new page to, should start with the /, i.e.
 *            /pages/page.md. If contains file extension, command will attempt to create file as specified in this
 *            variable, otherwise command will create index.markdown file in the specified directory
 * pageTitle - new page title
 */
'create-page': { String location, String pageTitle ->
        def ext = new File(location).extension
        def file = ext ? file = new File(content_dir, location) : new File(content_dir + location, 'index.markdown')
        file.parentFile.mkdirs()
        file.exists() || file.write("""---
layout: default
title: "${pageTitle}"
published: true
---
""")}
]