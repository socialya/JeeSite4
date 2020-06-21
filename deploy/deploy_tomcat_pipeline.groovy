stage('同步源码') {
    node('master'){
        git([url: 'git@github.com:princeqjzh/JeeSite4.git', branch: 'master'])
    }
}

stage('maven编译打包') {
    node('master'){
        sh ". ~/.bash_profile"

        //定义mvn环境
        def mvnHome = tool 'maven-3.6.0_master'
        def jdkHome = tool 'jdk1.8_master'
        env.PATH = "${mvnHome}/bin:${env.PATH}"
        env.PATH = "${jdkHome}/bin:${env.PATH}"
        sh '''
        export pwd=`pwd`
        export os_type=`uname`
        cd web/src/main/resources/config
        if [[ "${os_type}" == "Darwin" ]]; then
            sed -i "" "s/mysql_ip/${mysql_ip}/g" application.yml
            sed -i "" "s/mysql_port/${mysql_port}/g" application.yml
            sed -i "" "s/mysql_user/${mysql_user}/g" application.yml
            sed -i "" "s/mysql_pwd/${mysql_pwd}/g" application.yml
        else
            sed -i "s/mysql_ip/${mysql_ip}/g" application.yml
            sed -i "s/mysql_port/${mysql_port}/g" application.yml
            sed -i "s/mysql_user/${mysql_user}/g" application.yml
            sed -i "s/mysql_pwd/${mysql_pwd}/g" application.yml
        fi
        cd $pwd/web
        mvn clean package spring-boot:repackage -Dmaven.test.skip=true -U
        '''
        
    }
}