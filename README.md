# EthankLib
本项目是自己一些项目中用过的lib组合而成的,也有一些github 上的项目经过本地化适配或者优化后上传的
项目引用:
##compile 'cn.com.ethank.ethanklib:coyoteLib:1.0.0'

##compile 'cn.com.ethank.ethanklib:hotelviewpagerlibrary:1.0.0'

##compile 'cn.com.ethank.ethanklib:pull_to_refresh:1.0.0'

##compile 'cn.com.ethank.ethanklib:social_sdk_library_project:1.0.0'

##compile 'cn.com.ethank.ethanklib:wheel:1.0.0'

#下面介绍一下如何上传lib 到 jenter
    1.首先 打开https://bintray.com/网站注册一个账号或者直接用github账号登录
    2.在"Owned Repositories"下面点加号创建一个叫maven的仓库
    3.在项目的buidle.gradle下的 dependencies中加:
    classpath 'com.github.dcendents:android-maven-gradle-plugin:1.5'
    classpath 'com.jfrog.bintray.gradle:gradle-bintray-plugin:1.2'
    4.在要上传的lib中的build.gradle中的最外面的后面加一下代码
        要把项目中相关资料developer,url,coyoteLib等信息改成自己的
    apply plugin: 'com.github.dcendents.android-maven'
    apply plugin: 'com.jfrog.bintray'
    def siteUrl = 'https://github.com/gaoxuefeng/EthankLib'
    def gitUrl = 'https://github.com/gaoxuefeng/EthankLib.git'
    version = "1.0.0"
    install {
        repositories.mavenInstaller {
            // This generates POM.xml with proper parameters
            pom {
                project {
                    packaging 'aar'
                    // Add your description here
                    name 'a lib for Android Project' //项目描述,填什么都可以
                    url siteUrl
                    // Set your license
                    licenses {
                        license {
                            name 'The Apache Software License, Version 2.0'
                            url 'http://www.apache.org/licenses/LICENSE-2.0.txt'
                        }
                    }
                    developers {
                        developer {
                            id 'gaoxuefeng'    //填写的一些基本信息
                            name 'Gao Xuefeng'
                            email '107820194@qq.com'
                        }
                    }
                    scm {
                        connection gitUrl
                        developerConnection gitUrl
                        url siteUrl
                    }
                }
            }
        }
    }
    task sourcesJar(type: Jar) {
        from android.sourceSets.main.java.srcDirs
        classifier = 'sources'
    }
    task javadoc(type: Javadoc) {
        source = android.sourceSets.main.java.srcDirs
        classpath += project.files(android.getBootClasspath().join(File.pathSeparator))
    }
    task javadocJar(type: Jar, dependsOn: javadoc) {
        classifier = 'javadoc'
        from javadoc.destinationDir
    }
    artifacts {
    //    archives javadocJar //正常时不需要注释掉的报错时注释掉,
        archives sourcesJar
    }
    Properties properties = new Properties()
    properties.load(project.rootProject.file('local.properties').newDataInputStream())
    bintray {
        user = properties.getProperty("bintray.user")
        key = properties.getProperty("bintray.apikey")
        configurations = ['archives']
        pkg {
            repo = "maven"
            name = "coyoteLib"    //发布到JCenter上的项目名字
            websiteUrl = siteUrl
            vcsUrl = gitUrl
            licenses = ["Apache-2.0"]
            publish = true
        }
    }

    最后在项目的local.properties中 添加name和key注意:name是bintray上小写的一个
    #your bintray user name
    bintray.user=gaoxuefeng
    #your bintray api key
    bintray.apikey=fffffffffffffffwewswopooooppppp
    代码部分完成
    terminal中 输入指令gradlew bintrayUpload输入enter 最后看到 success表示上传完成,就可以在 https://bintray.com/中看到你的项目了
    打开项目点击 Add to Jcenter 随便输入内容 点击 send就可以把aar传到jcenter了,只需要等待审核就ok了



