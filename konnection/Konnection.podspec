Pod::Spec.new do |spec|
    spec.name                     = 'Konnection'
    spec.version                  = '1.1.6'
    spec.homepage                 = 'https://github.com/TM-Apps/konnection'
    spec.source                   = { :http=> ''}
    spec.authors                  = ''
    spec.license                  = ''
    spec.summary                  = 'A Kotlin Multiplatform library for Network Connection data.'
    spec.vendored_frameworks      = "build/cocoapods/framework/#{spec.name}.framework"
    spec.libraries                = 'c++'
    spec.ios.deployment_target = '12.4'
                
    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':konnection',
        'PRODUCT_MODULE_NAME' => 'Konnection',
    }
                
    spec.script_phases = [
        {
            :name => 'Build Konnection library',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                if [ "YES" = "$COCOAPODS_SKIP_KOTLIN_BUILD" ]; then
                  echo "Skipping Gradle build task invocation due to COCOAPODS_SKIP_KOTLIN_BUILD environment variable set to \"YES\""
                  exit 0
                fi
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/../gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:syncFramework \
                    -Pkotlin.native.cocoapods.platform=$PLATFORM_NAME \
                    -Pkotlin.native.cocoapods.archs="$ARCHS" \
                    -Pkotlin.native.cocoapods.configuration="$CONFIGURATION"
            SCRIPT
        }
    ]
                
end